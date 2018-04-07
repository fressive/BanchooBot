package org.banchoobot.sdk.annotations

/**
 * 消息功能
 */
@Target(AnnotationTarget.CLASS)
annotation class EventFunction (
        val allowedEvent: AllowedEvents,
        val needPermission: UserPermissions = UserPermissions.NORMAL,
        val disabled: Boolean = false
)