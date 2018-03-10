package org.banchoobot.frame.deserializer.events

/**
 * 所有消息类型的基类
 *
 * @param message 信息
 * @param messageType 信息类型
 * @param userID 发送者 QQ
 */
open class MessageEvent (
        open val message: String,
        val messageType: String,
        open val userID: Long
) : Event("message")