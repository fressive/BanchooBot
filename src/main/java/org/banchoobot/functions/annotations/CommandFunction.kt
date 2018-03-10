package org.banchoobot.functions.annotations

/**
 * 命令功能
 */
@Target(AnnotationTarget.CLASS)
annotation class CommandFunction (
        val command: String,
        val allowedMethods: Array<AllowedMethods> = [(AllowedMethods.PRIVATE), (AllowedMethods.GROUP), (AllowedMethods.DISCUSS)]
)