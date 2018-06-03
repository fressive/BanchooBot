package org.banchoobot.sdk.utils

import org.banchoobot.sdk.entities.cq.CQImage
import org.banchoobot.sdk.globals.Patterns

object CQUtils {
    private val CQImagePtn = Regex(Patterns.CQIMAGE_PATTERN)

    /**
     * 从 [code] 中提取图片，返回提取到的图片列表。
     */
    fun getImageFromCQCode(code: String): List<CQImage> {
        val result = CQImagePtn.findAll(code)

        val findResult = mutableListOf<CQImage>()
        result.forEach {
            if (it.groups.size < 6)
                return@forEach

            if (it.groups[1]!!.value == "image") {
                findResult.add(CQImage(it.value, it.groups[3]!!.value, it.groups[5]!!.value))
            } else {
                return@forEach
            }
        }

        return findResult
    }
}