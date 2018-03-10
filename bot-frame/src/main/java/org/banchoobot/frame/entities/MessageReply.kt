package org.banchoobot.frame.entities

import com.alibaba.fastjson.annotation.JSONField

/**
 * 上报消息响应数据
 *
 * @param reply 回复文本
 * @param autoEscape 是否转义
 * @param atSender 是否在开头 @ 发送者
 * @param delete 是否撤回消息
 * @param kick 是否踢人
 * @param ban 是否烟人
 */
data class MessageReply(
        @JSONField(name = "reply") val reply: String = "",
        @JSONField(name = "auto_escape") val autoEscape: Boolean = false,
        @JSONField(name = "at_sender") val atSender: Boolean = false,
        @JSONField(name = "delete") val delete: Boolean = false,
        @JSONField(name = "kick") val kick: Boolean = false,
        @JSONField(name = "ban") val ban: Boolean = false
)