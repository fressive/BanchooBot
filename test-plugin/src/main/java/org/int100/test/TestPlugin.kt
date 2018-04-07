package org.int100.test

import org.banchoobot.frame.deserializer.events.Message
import org.banchoobot.functions.Plugin
import org.banchoobot.functions.annotations.CommandFunction
import org.banchoobot.functions.entities.LoggerConfig
import org.banchoobot.functions.interfaces.ICommandFunction
import org.banchoobot.functions.utils.Logger

/**
 * A test plugin for Banchoobot.
 *
 * @author int100
 */

class TestPlugin : Plugin(
        "TestPlugin",
        "int100",
        "A test plugin."
) {
    companion object {
        val logger: Logger = Logger(TestPlugin::class.java, LoggerConfig(TestPlugin::class.java, isWritingToFile = true))
    }

    @CommandFunction(["test"])
    class Test : ICommandFunction {
        override fun onCommand(event: Message) {
            event.reply("Test OK.")
            logger.verb("test")
        }
    }

}