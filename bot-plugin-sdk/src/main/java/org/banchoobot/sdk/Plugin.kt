package org.banchoobot.sdk

import java.nio.file.Paths

/**
 * 插件基类，所有插件必须继承自此类
 *
 * @author int100
 */
abstract class Plugin (
        /** 名称 */
        val name: String,
        /** 作者 */
        val author: String,
        /** 描述 */
        val description: String = ""
) {

    /**
     * 插件路径，可存数据等文件。
     */
    val pluginDirectory : String get() { return Paths.get("plugins", name).toString() }

    override fun toString(): String
            = "$name by $author ($description)"

    /**
     * 插件被加载时触发的回调。
     */
    open fun onInit() { println("$this loaded.") }

    /**
     * 插件被启用时触发的回调。
     */
    open fun onEnable() {}

    /**
     * 插件被禁用时触发的回调。
     */
    open fun onDisable() {}
}
