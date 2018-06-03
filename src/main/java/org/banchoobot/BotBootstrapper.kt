package org.banchoobot

import org.banchoobot.utils.ConfigUtils

/**
 * Hello, Banchoobot!
 */
object BotBootstrapper {

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

    var bot: BanchooBot? = null


    @JvmStatic
    fun main(args: Array<String>) {
        println(MOTD.trimMargin("."))

        val config = ConfigUtils.readConfig()
        bot = BanchooBot(config)
        bot!!.start()
    }
}