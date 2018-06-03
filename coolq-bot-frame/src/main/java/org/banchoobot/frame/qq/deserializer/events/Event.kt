package org.banchoobot.frame.qq.deserializer.events

/**
 * 所有事件类型的基类
 *
 * @param eventType 事件类型
 */
abstract class Event(
        open val eventType: String,
        override val time: Long
) : PostEvent("event", time)