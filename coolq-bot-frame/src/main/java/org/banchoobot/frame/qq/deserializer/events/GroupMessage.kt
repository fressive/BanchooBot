package org.banchoobot.frame.qq.deserializer.events

import org.banchoobot.frame.qq.utils.BotUtils

/**
 * 群组消息事件
 *
 * @param message 信息
 * @param userID 发送者 QQ
 * @param groupID 群号
 * @param subType 子类型
 * @param time 时间
 */
class GroupMessage(
        override val message: String,
        override val messageID: Long,
        override val userID: Long,
        val groupID: Long,
        val subType: String,
        override val time: Long
) : Message(message, messageID,"group", userID, time) {
    override fun reply(msg: String, isEscape: Boolean, isAsync: Boolean)
            = BotUtils.sendGroupMessage(groupID, msg, isEscape, isAsync)
}