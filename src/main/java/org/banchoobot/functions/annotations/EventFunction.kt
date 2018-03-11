package org.banchoobot.functions.annotations

/**
 * 消息功能
 */
@Target(AnnotationTarget.CLASS)
annotation class EventFunction (
        val allowedEvent: AllowedEvents,
        val disabled: Boolean = false
)