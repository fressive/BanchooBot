package org.banchoobot.extensions

import java.text.DecimalFormat

/**
 * Number 类扩展
 */
object NumberExtensions {
    fun Number.round(): Double
        = DecimalFormat("#.00").format(this).toDouble()
}