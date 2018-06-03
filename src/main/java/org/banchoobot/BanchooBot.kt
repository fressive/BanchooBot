package org.banchoobot

import com.alibaba.fastjson.JSONArray
import org.apache.ibatis.io.Resources
import org.apache.ibatis.session.SqlSessionFactoryBuilder
import org.banchoobot.frame.qq.Bot
import org.banchoobot.frame.qq.configs.BotConfig
import org.banchoobot.frame.qq.deserializer.events.Event
import org.banchoobot.frame.qq.deserializer.events.GroupMessage
import org.banchoobot.frame.qq.deserializer.events.Message
import org.banchoobot.frame.qq.utils.BotUtils
import org.banchoobot.sdk.annotations.*
import org.banchoobot.sdk.entities.EFunction
import org.banchoobot.sdk.entities.LoggerConfig
import org.banchoobot.sdk.interfaces.ICommandFunction
import org.banchoobot.sdk.interfaces.IEventFunction
import org.banchoobot.sdk.interfaces.IMessageFunction
import org.banchoobot.sdk.entities.EPlugin
import org.banchoobot.sdk.utils.Logger
import org.banchoobot.utils.ConfigUtils
import org.banchoobot.utils.ReflectUtils
import org.quartz.*
import org.quartz.impl.StdSchedulerFactory
import java.io.File
import java.lang.reflect.InvocationTargetException
import java.util.*
import java.util.concurrent.CopyOnWriteArrayList

/**
 * BanchooBot's Body
 */
class BanchooBot(@Volatile var config: BotConfig) : Bot(config) {
    private val LOGGER = Logger(BanchooBot::class.java, LoggerConfig(BanchooBot::class.java, isWritingToFile = true))

    companion object {

        val commandFunctions: CopyOnWriteArrayList<EFunction<CommandFunction, ICommandFunction>> = CopyOnWriteArrayList(ReflectUtils.getFunctions{ !it.disabled })
            @Synchronized get() { return field }

        val messageFunctions: CopyOnWriteArrayList<EFunction<MessageFunction, IMessageFunction>> = CopyOnWriteArrayList(ReflectUtils.getFunctions { !it.disabled })
            @Synchronized get() { return field }

        val eventFunctions: CopyOnWriteArrayList<EFunction<EventFunction, IEventFunction>> = CopyOnWriteArrayList(ReflectUtils.getFunctions{ !it.disabled })
            @Synchronized get() { return field }

        val scheduleFunctions: MutableList<EFunction<ScheduleFunction, Job>> = ReflectUtils.getFunctions<ScheduleFunction, Job> { !it.disabled }.toMutableList()
            @Synchronized get() { return field }

        val plugins: CopyOnWriteArrayList<EPlugin> by lazy {
            val pluginFile = File("plugins")
            if (!pluginFile.exists()) {
                pluginFile.mkdirs()
                return@lazy CopyOnWriteArrayList(setOf<EPlugin>())
            }
            CopyOnWriteArrayList(ReflectUtils.getPlugins())
        }

        val scheduler: Scheduler = StdSchedulerFactory().scheduler

        val names: MutableList<String> = mutableListOf()

    }

    private val dbProperties = Properties().apply {
        this["driver"] = "com.mysql.jdbc.Driver"
        this["url"] = "jdbc:mysql://localhost:3306/banchoobot?useUnicode=true&characterEncoding=UTF-8"
        this["username"] = config.anotherConfigs.getOrDefault("db_username", "root")
        this["password"] = config.anotherConfigs.getOrDefault("db_password", "root")
    }

    val dbSessionFactory = SqlSessionFactoryBuilder().build(Resources.getResourceAsStream("mybatis-config.xml"), dbProperties)!!

    override fun onMessage(message: Message) {

        Thread {
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

                                    val p = when (message) { is GroupMessage -> getUserPermission(message.userID, message.groupID); else -> getUserPermission(message.userID)
                                    }

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
            } catch (ex: InvocationTargetException) {
                val e = ex.cause ?: ex
                e.printStackTrace()
                message.reply("[${e::class.java.name}] ${e.message}\nStackTrace:\n${e.stackTrace.filter { it.className.contains("org.banchoobot") }.joinToString { "\n$it" }.trimStart('\n').trimEnd('\n')}")
            } catch (ex: Exception) {
                val e = ex.cause ?: ex
                e.printStackTrace()
                message.reply("[${e::class.java.name}] ${e.message}\nStackTrace:\n${e.stackTrace.filter { it.className.contains("org.banchoobot") }.joinToString { "\n$it" }.trimStart('\n').trimEnd('\n')}")
            } catch (ex: Error) {
                val e = ex.cause ?: ex
                e.printStackTrace()
                message.reply("[${e::class.java.name}] ${e.message}\nStackTrace:\n${e.stackTrace.filter { it.className.contains("org.banchoobot") }.joinToString { "\n$it" }.trimStart('\n').trimEnd('\n')}")
            }
        }.start()
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
        scheduleFunctions.forEach {
            val jobDetail = JobBuilder.newJob(it.clazz).withIdentity(it.annotation.name, "BaseGroup").build()
            val trigger = TriggerBuilder.newTrigger().withIdentity("${it.annotation.name}_trigger", "BaseGroup")
                    .startNow().withSchedule<CronTrigger>(CronScheduleBuilder.cronSchedule(it.annotation.cron)).build()
            scheduler.scheduleJob(jobDetail, trigger)
            names.add(it.annotation.name)
        }

        LOGGER.info("已启动 BanchooBot。\n")

        LOGGER.info("Plugins: ${plugins.joinToString { it.toString() } }")
        LOGGER.info("CommandFunctions: ${commandFunctions.joinToString { it.clazz.simpleName } }")
        LOGGER.info("MessageFunctions: ${messageFunctions.joinToString { it.clazz.simpleName } }")
        LOGGER.info("EventFunctions: ${eventFunctions.joinToString { it.clazz.simpleName } }")
        LOGGER.info("ScheduleFunctions: ${names.joinToString { it } }\n")

        plugins.forEach {
            commandFunctions.addAll(it.commandFunctions)
            messageFunctions.addAll(it.messageFunctions)
            eventFunctions.addAll(it.eventFunctions)
            it.schedulers.forEach {
                @Suppress("UNCHECKED_CAST")
                val jobDetail = JobBuilder.newJob(it.clazz as Class<Job>).withIdentity(it.annotation.name, "BaseGroup").build()
                val trigger = TriggerBuilder.newTrigger().withIdentity("${it.annotation.name}_trigger", "BaseGroup")
                        .startNow().withSchedule<CronTrigger>(CronScheduleBuilder.cronSchedule(it.annotation.cron)).build()
                scheduler.scheduleJob(jobDetail, trigger)
                names.add(it.annotation.name)
            }
        }

        scheduler.start()
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