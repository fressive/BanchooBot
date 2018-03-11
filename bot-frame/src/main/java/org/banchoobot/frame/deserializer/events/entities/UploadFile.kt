package org.banchoobot.frame.deserializer.events.entities

/**
 * 文件文件 POJO
 *
 * @param id 文件ID
 * @param name 文件名称
 * @param size 文件大小（字节数）
 * @param busid 未知
 * @see org.banchoobot.frame.deserializer.events.FileUploadEvent
 */
data class UploadFile(
        val id: String,
        val name: String,
        val size: Long,
        val busid: Int
)