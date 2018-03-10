package org.banchoobot.functions.interfaces

import org.banchoobot.frame.deserializer.events.MessageEvent
import org.banchoobot.frame.entities.MessageReply

/**
 * 消息功能
 */
interface IMessageFunction {
    fun onMessage(event: MessageEvent): MessageReply
}