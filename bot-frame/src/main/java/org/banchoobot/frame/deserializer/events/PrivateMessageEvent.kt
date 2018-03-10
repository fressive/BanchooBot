package org.banchoobot.frame.deserializer.events

/**
 * 群组消息事件
 *
 * @param message 信息
 * @param userID 发送者 QQ
 * @param subType 子类型
 */
class PrivateMessageEvent(
        override val message: String,
        override val userID: Long,
        val subType: String = "friend"
) : MessageEvent(message, "private", userID)