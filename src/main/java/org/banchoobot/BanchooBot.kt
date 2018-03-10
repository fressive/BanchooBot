package org.banchoobot

import org.banchoobot.frame.Bot
import org.banchoobot.frame.configs.BotConfig
import org.banchoobot.frame.deserializer.events.MessageEvent
import org.banchoobot.frame.entities.MessageReply
import org.banchoobot.frame.handlers.OnPost
import org.banchoobot.functions.annotations.AllowedMethods
import org.banchoobot.functions.annotations.CommandFunction
import org.banchoobot.functions.annotations.MessageFunction
import org.banchoobot.utils.ReflectUtils
import java.util.logging.Logger

/**
 * BanchooBot
 */
class BanchooBot(private val config: BotConfig) : Bot(config) {
    private val LOGGER = Logger.getLogger(this::class.java.simpleName)

    private val commandFunctions = ReflectUtils.getCommandFunctions()
    private val messageFunctions = ReflectUtils.getMessageFunctions()

    override fun onMessage(message: MessageEvent): MessageReply {
        val type = AllowedMethods.valueOf(message.messageType.toUpperCase())

        LOGGER.info("接收到消息事件：${message.javaClass.simpleName}\n[$type]( ${message.userID} ) ${message.message}\n")
        messageFunctions.forEach { c ->
            c.methods
                    .filter { it.name == "onMessage" }
                    .forEach {
                        if (c.getAnnotationsByType(MessageFunction::class.java)[0].allowedMethods.contains(type))
                            return@onMessage it.invoke(c.newInstance(), message) as MessageReply
                    }
        }

        commandFunctions.forEach { c ->
            c.methods
                    .filter { it.name == "onCommand" }
                    .forEach {
                        val ant = c.getAnnotationsByType(CommandFunction::class.java)[0]
                        if (ant.allowedMethods.contains(type)) {
                            val cmd = message.message.split(" ")[0].substring(config.anotherConfigs["prefix"].toString().length)
                            if (cmd == ant.command)
                                return@onMessage it.invoke(c.newInstance(), message) as MessageReply
                        }
                    }
        }

        return MessageReply()
    }
}