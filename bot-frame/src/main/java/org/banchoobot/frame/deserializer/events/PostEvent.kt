package org.banchoobot.frame.deserializer.events

/**
 * 所有上报事件的基类
 *
 * @param postType 事件类型
 * @param time 时间
 */
open class PostEvent(
        val postType: String = "event",
        open val time: Long = -1
)