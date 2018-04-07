package org.banchoobot.sdk.annotations

/**
 * 定时任务
 */
annotation class ScheduleFunction (
        val name: String,
        val cron: String,
        val disabled: Boolean = false
)