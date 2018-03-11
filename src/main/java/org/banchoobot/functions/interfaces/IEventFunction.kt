package org.banchoobot.functions.interfaces

import org.banchoobot.frame.deserializer.events.Event

/**
 * 事件功能
 */
interface IEventFunction {
    fun onEvent(event: Event)
}