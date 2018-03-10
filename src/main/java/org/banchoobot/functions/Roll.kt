package org.banchoobot.functions

import org.banchoobot.frame.deserializer.events.MessageEvent
import org.banchoobot.frame.entities.MessageReply
import org.banchoobot.functions.annotations.CommandFunction
import org.banchoobot.functions.interfaces.ICommandFunction

/**
 * 瞎杰宝roll
 *
 * Usage:
 * roll                 返回 0 - 100 随机整数
 * roll {max}           返回 0 - {max} 随机整数
 * roll {min}-{max}     返回 {min} - {max} 随机整数
 * roll {list}          返回 {list} 内随机一个元素
 */
@CommandFunction(command = "roll")
class Roll : ICommandFunction {
    override fun onCommand(event: MessageEvent): MessageReply {
        println("Invoke success!!!")
        return MessageReply("Reserved.")
    }
}