package org.banchoobot

import org.banchoobot.frame.configs.BotConfig
import org.banchoobot.functions.schedulers.Scheduler
import java.util.logging.Logger

/**
 * 启动类
 */
object BotBootstrapper {
    private val LOGGER = Logger.getLogger(this::class.java.simpleName)

    private const val MOTD = """
                            .    ____                   _                 ____        _
                            .   | __ )  __ _ _ __   ___| |__   ___   ___ | __ )  ___ | |_
                            .   |  _ \ / _` | '_ \ / __| '_ \ / _ \ / _ \|  _ \ / _ \| __|
                            .   | |_) | (_| | | | | (__| | | | (_) | (_) | |_) | (_) | |_
                            .   |____/ \____|_| |_|\___|_| |_|\___/ \___/|____/ \___/ \__|
                            .          https://github.com/1004121460/BanchooBot
                            .          BanchooBot Project, All rights reserved.
                            .
                       """

    @JvmStatic
    fun main(args: Array<String>) {
        println(MOTD.trimMargin("."))


        val bot = BanchooBot(BotConfig(anotherConfigs = mutableMapOf("prefix" to "+")))
        bot.start()

        Scheduler.init()

        LOGGER.info("已启动 BanchooBot。\n")
        LOGGER.info("CommandFunctions: ${bot.commandFunctions.joinToString { it.clazz.simpleName } }\n")
        LOGGER.info("MessageFunctions: ${bot.messageFunctions.joinToString { it.clazz.simpleName } }\n")
        LOGGER.info("EventFunctions: ${bot.eventFunctions.joinToString { it.clazz.simpleName } }\n")
        LOGGER.info("ScheduleFunctions: ${Scheduler.names.joinToString { it } }\n")
    }
}