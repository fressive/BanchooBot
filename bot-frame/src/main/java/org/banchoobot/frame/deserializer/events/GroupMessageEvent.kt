package org.banchoobot.frame.deserializer.events

/**
 * 群组消息事件
 *
 * @param message 信息
 * @param userID 发送者 QQ
 * @param groupID 群号
 * @param subType 子类型
 */
class GroupMessageEvent(
        override val message: String,
        override val userID: Long,
        val groupID: Long,
        val subType: String = "friend"
) : MessageEvent(message, "group", userID)