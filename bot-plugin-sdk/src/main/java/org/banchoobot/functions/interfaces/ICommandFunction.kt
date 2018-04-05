package org.banchoobot.functions.interfaces

import org.banchoobot.frame.deserializer.events.Message

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
}