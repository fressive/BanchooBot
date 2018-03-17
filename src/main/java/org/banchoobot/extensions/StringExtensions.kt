package org.banchoobot.extensions

import java.text.MessageFormat

/**
 * String 类扩展
 */
object StringExtensions {
    fun String.mformat(vararg params: Any): String {
        return MessageFormat.format(this, *params)
    }
}