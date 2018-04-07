package org.banchoobot.functions

/**
 * 插件基类，所有插件必须继承自此类
 */
open class Plugin (val name: String,
                   val author: String,
                   val description: String) {

    override fun toString(): String
        = "$name by $author ($description)"
}
