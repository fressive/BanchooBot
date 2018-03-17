package org.banchoobot.functions

import org.banchoobot.frame.deserializer.events.Message
import org.banchoobot.functions.annotations.MessageFunction
import org.banchoobot.functions.interfaces.IMessageFunction

/**
 * 测试图片功能
 *
 * 原理：根据图片的 MD5 ，调用对应的方法
 */
@MessageFunction()
class TestImageFunction : IMessageFunction {
    override fun onMessage(event: Message) {
        if (event.message.contains("446730585AD2F553535F6B3009C54BB3"))
            event.reply("Test OK.")
    }
}