package org.banchoobot.frame.qq.deserializer.events

import org.banchoobot.frame.qq.utils.BotUtils

/**
 * 群组消息事件
 *
 * @param message 信息
 * @param messageID 信息ID
 * @param userID 发送者 QQ
 * @param subType 子类型
 * @param time 时间
 */
class PrivateMessage(
        override val message: String,
        override val messageID: Long,
        override val userID: Long,
        val subType: String,
        override val time: Long
) : Message(message, messageID, "private", userID, time) {
    override fun reply(msg: String, isEscape: Boolean, isAsync: Boolean)
            = BotUtils.sendPrivateMessage(userID, msg, isEscape, isAsync)
}