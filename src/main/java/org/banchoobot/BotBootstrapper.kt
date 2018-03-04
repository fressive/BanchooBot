package org.banchoobot

import org.banchoobot.server.ReportServer

/**
 * BanchooBot bootstrapper
 */

object BotBootstrapper {

    @JvmStatic
    fun main(args: Array<String>) {
        val server = ReportServer("127.0.0.1")
        server.listen()

    }
}