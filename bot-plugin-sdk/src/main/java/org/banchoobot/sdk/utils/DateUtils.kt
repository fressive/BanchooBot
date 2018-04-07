package org.banchoobot.sdk.utils

import java.text.SimpleDateFormat
import java.util.*

/**
 * 日期工具类
 */
object DateUtils {
    val DEFAULT_FORMAT = SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
    val DATE_NOW get() = Date(System.currentTimeMillis())

    fun getFormatDate(): String
        = DEFAULT_FORMAT.format(DATE_NOW)

    fun getFormatDate(format: String = "yyyy-MM-dd hh:mm:ss"): String
        = SimpleDateFormat(format).format(DATE_NOW)
}