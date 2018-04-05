package org.banchoobot.functions.interfaces

import org.banchoobot.frame.deserializer.events.Message

/**
 * 消息功能
 */
interface IMessageFunction {
    fun onMessage(event: Message)
}