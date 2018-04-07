package org.banchoobot

import com.alibaba.fastjson.JSONArray
import org.apache.ibatis.io.Resources
import org.apache.ibatis.session.SqlSessionFactoryBuilder
import org.banchoobot.frame.Bot
import org.banchoobot.frame.configs.BotConfig
import org.banchoobot.frame.deserializer.events.Event
import org.banchoobot.frame.deserializer.events.GroupMessage
import org.banchoobot.frame.deserializer.events.Message
import org.banchoobot.frame.utils.BotUtils
import org.banchoobot.sdk.annotations.*
import org.banchoobot.sdk.entities.EFunction
import org.banchoobot.sdk.entities.LoggerConfig
import org.banchoobot.sdk.interfaces.ICommandFunction
import org.banchoobot.sdk.interfaces.IEventFunction
import org.banchoobot.sdk.interfaces.IMessageFunction
import org.banchoobot.functions.schedulers.Scheduler
import org.banchoobot.sdk.utils.Logger
import org.banchoobot.utils.ConfigUtils
import org.banchoobot.utils.ReflectUtils
import java.io.File
import java.lang.reflect.InvocationTargetException
import java.util.*

/**
 * BanchooBot's Body
 */
class BanchooBot(@Volatile var config: BotConfig) : Bot(config) {
    private val LOGGER = Logger(BanchooBot::class.java, LoggerConfig(BanchooBot::class.java, isWritingToFile = true))

    val pluginPackages: Set<String> by lazy {
        val pluginFile = File("plugins")
        if (!pluginFile.exists()) {
            pluginFile.mkdirs()
        }

        ReflectUtils.getPlugins()
    }

    private val commandFunctions: Set<EFunction<CommandFunction, ICommandFunction>> by lazy { ReflectUtils.getFunctions<CommandFunction, ICommandFunction>(pluginPackages, { !it.disabled }) }
    private val messageFunctions: Set<EFunction<MessageFunction, IMessageFunction>> by lazy { ReflectUtils.getFunctions<MessageFunction, IMessageFunction>(pluginPackages, { !it.disabled }) }
    private val eventFunctions: Set<EFunction<EventFunction, IEventFunction>> by lazy { ReflectUtils.getFunctions<EventFunction, IEventFunction>(pluginPackages, { !it.disabled })  }

    private val dbProperties = Properties().apply {
        this["driver"] = "com.mysql.jdbc.Driver"
        this["url"] = "jdbc:mysql://localhost:3306/banchoobot?useUnicode=true&characterEncoding=UTF-8"
        this["username"] = config.anotherConfigs.getOrDefault("db_username", "root")
        this["password"] = config.anotherConfigs.getOrDefault("db_password", "root")
    }

    val dbSessionFactory = SqlSessionFactoryBuilder().build(Resources.getResourceAsStream("mybatis-config.xml"), dbProperties)!!

    override fun onMessage(message: Message) {
        val type = AllowedMethods.valueOf(message.messageType.toUpperCase())

        try {
            LOGGER.info("接收到消息事件：${message.javaClass.simpleName}\n[$type](${message.userID}): ${message.message}\n")
            messageFunctions.forEach { m ->
                m.clazz.methods
                        .filter { it.name == "onMessage" }
                        .forEach {
                            val p = when (message) { is GroupMessage -> getUserPermission(message.userID, message.groupID); else -> getUserPermission(message.userID)
                            }

                            if (m.annotation.allowedMethods.contains(type))
                                if (p >= m.annotation.needPermission)
                                    it.invoke(m.clazz.newInstance(), message)
                        }
            }

            commandFunctions.forEach { m ->
                m.clazz.methods
                        .filter { it.name == "onCommand" }
                        .forEach { method ->
                            val ant = m.annotation
                            if (ant.allowedMethods.contains(type)) {

                                val p = when (message) { is GroupMessage -> getUserPermission(message.userID, message.groupID); else -> getUserPermission(message.userID) }


                                if (message.message.startsWith(config.anotherConfigs["prefix"].toString())) {
                                    ant.command.forEach {
                                        if (message.message.startsWith(config.anotherConfigs["prefix"].toString() + it)) {
                                            if (p >= ant.needPermission)
                                                method.invoke(m.clazz.newInstance(), message)
                                            else
                                                message.reply("You need a higher permission to use this function.")
                                        }
                                    }
                                }
                            }
                        }
            }

        } catch (e: InvocationTargetException) {
            e.cause?.printStackTrace()
            message.reply("[${e.cause!!::class.java.name}] ${e.cause?.message}\nStackTrace:\n${e.cause?.stackTrace?.filter { it.className.contains("org.banchoobot") }?.joinToString { "\n$it" }?.trimStart('\n')?.trimEnd('\n')}")
        } catch (e: Exception) {
            e.printStackTrace()
            message.reply("[${e::class.java.name}] ${e.message}\nStackTrace:\n${e.stackTrace.filter { it.className.contains("org.banchoobot") }.joinToString { "\n$it" }.trimStart('\n').trimEnd('\n')}")
        } catch (e: Error) {
            e.printStackTrace()
            message.reply("[${e::class.java.name}] ${e.message}\nStackTrace:\n${e.stackTrace.filter { it.className.contains("org.banchoobot") }.joinToString { "\n$it" }.trimStart('\n').trimEnd('\n')}")
        }
    }

    override fun onEvent(event: Event) {
        val type = AllowedEvents.valueOf(event.eventType.toUpperCase())
        LOGGER.info("接收到事件：${event.javaClass.simpleName} (${event.eventType})")

        eventFunctions.forEach { m ->
            m.clazz.methods
                    .filter { it.name == "onEvent" }
                    .forEach {
                        val ant = m.annotation
                        if (ant.allowedEvent == type) {
                            it.invoke(m.clazz.newInstance(), event)
                        }
                    }
        }
    }

    override fun start() {
        super.start()

        Scheduler.init()

        LOGGER.info("已启动 BanchooBot。\n")
        LOGGER.info("CommandFunctions: ${BotBootstrapper.bot!!.commandFunctions.joinToString { it.clazz.simpleName } }\n")
        LOGGER.info("MessageFunctions: ${BotBootstrapper.bot!!.messageFunctions.joinToString { it.clazz.simpleName } }\n")
        LOGGER.info("EventFunctions: ${BotBootstrapper.bot!!.eventFunctions.joinToString { it.clazz.simpleName } }\n")
        LOGGER.info("ScheduleFunctions: ${Scheduler.names.joinToString { it } }\n")


    }

    /**
     * 获取用户的权限。
     *
     * @return 用户的权限
     * @see UserPermissions
     */
    fun getUserPermission(qq: Long, group: Long = -1L): UserPermissions {
        val botAdmins = config.anotherConfigs["bot_admins"]

        try {
            if (botAdmins != null) {
                if (botAdmins is JSONArray) {
                    botAdmins.forEach {
                        if ((it as Int).toLong() == qq) return UserPermissions.BOT_ADMIN
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return if (group == -1L) {
            UserPermissions.NORMAL
        } else {
            val role = BotUtils.getGroupMemberInfo(qq, group).getJSONObject("data").getString("role")
            when (role) {
                "owner", "admin" -> UserPermissions.ADMIN
                else -> UserPermissions.NORMAL
            }
        }
    }

    /**
     * 保存当前配置文件。
     */
    fun saveConfig(path: String = "./config/bot.json") {
        ConfigUtils.saveConfig(this.config, path)
    }

}