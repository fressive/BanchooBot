package org.banchoobot.frame.deserializer.events

/**
 * 所有消息类型的基类
 *
 * @param message 信息
 * @param messageID 信息ID
 * @param messageType 信息类型
 * @param userID 发送者 QQ
 * @param time 时间
 */
abstract class Message (
        open val message: String,
        open val messageID: Long,
        val messageType: String,
        open val userID: Long,
        override val time: Long
) : PostEvent("message", time) {
    abstract fun reply(msg: String, isEscape: Boolean = false, isAsync: Boolean = true)
}