package org.banchoobot.frame.qq.deserializer.events

import org.banchoobot.frame.qq.deserializer.events.entities.UploadFile

/**
 * 群组文件上传事件
 *
 * @param groupID 群号
 * @param userID 发送者 QQ
 * @param file 文件
 * @param time 时间
 */
class FileUploadEvent(
        val groupID: Long,
        val userID: Long,
        val file: UploadFile,
        override val time: Long
) : Event("group_upload", time)