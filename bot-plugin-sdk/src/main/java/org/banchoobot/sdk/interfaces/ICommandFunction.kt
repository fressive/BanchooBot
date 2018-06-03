package org.banchoobot.sdk.interfaces

import org.banchoobot.frame.qq.deserializer.events.Message

/**
 * 命令功能接口
 */
interface ICommandFunction {

    /**
     * 当接收到特定的命令时调用
     *
     * @param event 消息事件
     */
    fun onCommand(event: Message)

    /**
     * 使用方法字符串，推荐格式：
     *
     * <plugin>.<function>.usage: <message>
     */
    val usage: String
        get() = "Nothing for usage"
}