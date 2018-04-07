package org.banchoobot.functions

import org.banchoobot.frame.deserializer.events.Message
import org.banchoobot.functions.annotations.CommandFunction
import org.banchoobot.functions.interfaces.ICommandFunction

/**
 * 帮助
 */
@CommandFunction(command = ["help"])
class Help : ICommandFunction {
    override fun onCommand(event: Message) {
        event.reply("https://github.com/1004121460/BanchooBot")
    }
}
