package org.banchoobot

import org.banchoobot.frame.Bot
import org.banchoobot.frame.configs.BotConfig
import org.banchoobot.frame.deserializer.events.Event
import org.banchoobot.frame.deserializer.events.Message
import org.banchoobot.functions.annotations.*
import org.banchoobot.functions.entities.EFunction
import org.banchoobot.functions.interfaces.ICommandFunction
import org.banchoobot.functions.interfaces.IEventFunction
import org.banchoobot.functions.interfaces.IMessageFunction
import org.banchoobot.utils.ReflectUtils
import java.util.logging.Logger

/**
 * BanchooBot
 */
class BanchooBot(private val config: BotConfig) : Bot(config) {
    private val LOGGER = Logger.getLogger(this::class.java.simpleName)

    val commandFunctions: Set<EFunction<CommandFunction, ICommandFunction>>
            = ReflectUtils.getFunctions({ !it.disabled })

    val messageFunctions: Set<EFunction<MessageFunction, IMessageFunction>>
            = ReflectUtils.getFunctions({ !it.disabled })

    val eventFunctions: Set<EFunction<EventFunction, IEventFunction>>
            = ReflectUtils.getFunctions({ !it.disabled })

    override fun onMessage(message: Message) {
        val type = AllowedMethods.valueOf(message.messageType.toUpperCase())

        LOGGER.info("接收到消息事件：${message.javaClass.simpleName}\n[$type](${message.userID}): ${message.message}\n")
        messageFunctions.forEach { m ->
            m.clazz.methods
                    .filter { it.name == "onMessage" }
                    .forEach {
                        if (m.annotation.allowedMethods.contains(type))
                            it.invoke(m.clazz.newInstance(), message)
                    }
        }

        commandFunctions.forEach { m ->
            m.clazz.methods
                    .filter { it.name == "onCommand" }
                    .forEach {
                        val ant = m.annotation
                        if (ant.allowedMethods.contains(type)) {
                            val cmd = message.message.split(" ")[0].substring(config.anotherConfigs["prefix"].toString().length)
                            if (cmd == ant.command)
                                it.invoke(m.clazz.newInstance(), message)
                        }
                    }
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

}