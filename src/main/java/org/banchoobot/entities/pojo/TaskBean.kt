package org.banchoobot.entities.pojo

/**
 * 任务 Bean
 */
data class TaskBean(
        val taskId: Int,
        val osuUid: Int,
        val qq: Long,
        val hintGroup: Long,
        val taskCode: String,
        val isComplete: Boolean
)