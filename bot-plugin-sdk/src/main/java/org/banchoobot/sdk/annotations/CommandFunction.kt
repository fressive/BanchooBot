package org.banchoobot.sdk.annotations

/**
 * 命令功能
 */
@Target(AnnotationTarget.CLASS)
annotation class CommandFunction (
        val command: Array<String>,
        val allowedMethods: Array<AllowedMethods> = [(AllowedMethods.PRIVATE), (AllowedMethods.GROUP), (AllowedMethods.DISCUSS)],
        val needPermission: UserPermissions = UserPermissions.NORMAL,
        val disabled: Boolean = false
)