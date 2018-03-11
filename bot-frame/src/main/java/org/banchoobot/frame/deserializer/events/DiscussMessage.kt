package org.banchoobot.frame.deserializer.events

import org.banchoobot.frame.utils.BotUtils

/**
 * 讨论组消息事件
 *
 * @param message 信息
 * @param messageID 信息ID
 * @param userID 发送者 QQ
 * @param discussID 讨论组ID
 * @param time 时间
 */
class DiscussMessage(
        override val message: String,
        override val messageID: Long,
        override val userID: Long,
        val discussID: Long,
        override val time: Long
) : Message(message, messageID,"discuss", userID, time) {
    override fun reply(msg: String, isEscape: Boolean, isAsync: Boolean)
            = BotUtils.sendDiscussMessage(discussID, msg, isEscape, isAsync)
}