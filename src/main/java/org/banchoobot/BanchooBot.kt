package org.banchoobot

import com.alibaba.fastjson.JSONArray
import org.banchoobot.frame.Bot
import org.banchoobot.frame.configs.BotConfig
import org.banchoobot.frame.deserializer.events.Event
import org.banchoobot.frame.deserializer.events.GroupMessage
import org.banchoobot.frame.deserializer.events.Message
import org.banchoobot.frame.utils.BotUtils
import org.banchoobot.functions.annotations.*
import org.banchoobot.functions.entities.EFunction
import org.banchoobot.functions.interfaces.ICommandFunction
import org.banchoobot.functions.interfaces.IEventFunction
import org.banchoobot.functions.interfaces.IMessageFunction
import org.banchoobot.utils.ConfigUtils
import org.banchoobot.utils.ReflectUtils
import java.lang.reflect.InvocationTargetException
import java.util.logging.Logger

/**
 * BanchooBot
 */
class BanchooBot(@Volatile var config: BotConfig) : Bot(config) {
    val LOGGER = Logger.getLogger(this::class.java.simpleName)

    val commandFunctions: Set<EFunction<CommandFunction, ICommandFunction>>
            = ReflectUtils.getFunctions({ !it.disabled })

    val messageFunctions: Set<EFunction<MessageFunction, IMessageFunction>>
            = ReflectUtils.getFunctions({ !it.disabled })

    val eventFunctions: Set<EFunction<EventFunction, IEventFunction>>
            = ReflectUtils.getFunctions({ !it.disabled })

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
                        .forEach {
                            val ant = m.annotation
                            if (ant.allowedMethods.contains(type)) {

                                val p = when (message) { is GroupMessage -> getUserPermission(message.userID, message.groupID); else -> getUserPermission(message.userID)
                                }

                                if (message.message.startsWith(config.anotherConfigs["prefix"].toString())) {
                                    val cmd = message.message.split(" ")[0].substring(config.anotherConfigs["prefix"].toString().length)
                                    if (ant.command.contains(cmd))
                                        if (p >= ant.needPermission)
                                            it.invoke(m.clazz.newInstance(), message)
                                        else
                                            message.reply("You need a higher permission to use this function.")
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

    fun saveConfig(path: String = "./config/bot.json") {
        ConfigUtils.saveConfig(this.config, path)
    }

}