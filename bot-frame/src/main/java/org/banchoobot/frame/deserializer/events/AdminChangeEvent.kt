package org.banchoobot.frame.deserializer.events

/**
 * 管理员变化事件
 *
 * @param subType 子类型
 * @param userID 管理员 QQ
 * @param groupID 群号
 * @param time 时间
 */
class AdminChangeEvent (
        val subType: String,
        val groupID: Long,
        val userID: Long,
        override val time: Long
) : Event("group_admin", time)