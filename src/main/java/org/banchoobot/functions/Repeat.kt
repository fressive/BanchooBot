package org.banchoobot.functions

import org.banchoobot.frame.deserializer.events.MessageEvent
import org.banchoobot.frame.entities.MessageReply
import org.banchoobot.functions.annotations.AllowedMethods
import org.banchoobot.functions.annotations.MessageFunction
import org.banchoobot.functions.interfaces.IMessageFunction

/**
 * 复读机天下无敌
 */
//@MessageFunction(allowedMethods = [AllowedMethods.PRIVATE])
class Repeat : IMessageFunction {
    override fun onMessage(event: MessageEvent): MessageReply {
        return MessageReply(event.message)
    }
}
