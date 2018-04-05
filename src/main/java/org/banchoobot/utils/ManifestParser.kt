package org.banchoobot.utils

/**
 * jar 包的 MANIFEST.MF 文件转换
 */
object ManifestParser {

    /**
     * 转换 MANIFEST.MF 文件
     *
     * @param text 文本
     * @return 配置文件
     */
    fun parse(text: String): Map<String, String> {
        val map = mutableMapOf<String, String>()

        text.split("\n".toRegex()).forEach{
            val ps = it.trimEnd().split(": ", limit = 2)
            if (ps.size < 2) return@forEach
            map[ps[0]] = ps[1]
        }
        return map
    }
}