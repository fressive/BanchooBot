package org.int100.test

import org.banchoobot.frame.deserializer.events.Message
import org.banchoobot.functions.Function
import org.banchoobot.functions.annotations.CommandFunction
import org.banchoobot.functions.interfaces.ICommandFunction

/**
 * A test plugin for Banchoobot.
 *
 * @author int100
 */

class TestPlugin : Function(
        "TestPlugin",
        "int100",
        "A test plugin."
) {
    @CommandFunction(["test"])
    class Test : ICommandFunction {
        override fun onCommand(event: Message) {
            event.reply("Test OK.")
        }
    }

}