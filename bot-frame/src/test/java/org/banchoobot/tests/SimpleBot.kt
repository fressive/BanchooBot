package org.banchoobot.tests

import org.banchoobot.frame.Bot
import org.banchoobot.frame.configs.BotConfig
import org.banchoobot.frame.deserializer.events.MessageEvent
import org.banchoobot.frame.entities.MessageReply

/**
 * Test Bot
 */
object Bootstrapper {
    @JvmStatic
    fun main(args: Array<String>) {
        val bot = SimpleBot()
        bot.start()
    }
}

class SimpleBot : Bot(BotConfig()) {
    override fun onMessage(msg: MessageEvent): MessageReply {
        return MessageReply(msg.message)
        // 超级无敌服毒 bot
    }

}