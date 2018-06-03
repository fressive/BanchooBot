package org.banchoobot.functions

import org.banchoobot.frame.qq.deserializer.events.Message
import org.banchoobot.sdk.annotations.CommandFunction
import org.banchoobot.sdk.interfaces.ICommandFunction

/**
 * 帮助
 */
@CommandFunction(command = ["help"])
class Help : ICommandFunction {
    override val usage: String
        get() = "Nothing for usage."

    override fun onCommand(event: Message) {
        event.reply("https://github.com/1004121460/BanchooBot")
    }
}
