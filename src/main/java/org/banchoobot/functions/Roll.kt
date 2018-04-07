package org.banchoobot.functions

import org.banchoobot.frame.deserializer.events.Message
import org.banchoobot.functions.annotations.CommandFunction
import org.banchoobot.functions.interfaces.ICommandFunction
import java.util.*

/**
 * 瞎杰宝roll
 *
 * Usage:
 * roll                 返回 0 - 100 随机整数
 */
@CommandFunction(command = ["roll"])
class Roll : ICommandFunction {
    override fun onCommand(event: Message) {
        event.reply(Random().let { it.setSeed(System.currentTimeMillis().hashCode().toLong()); it.nextInt(100).toString() })
    }
}