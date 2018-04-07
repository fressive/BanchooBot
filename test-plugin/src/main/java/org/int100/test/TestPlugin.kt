package org.int100.test

import org.banchoobot.frame.deserializer.events.Message
import org.banchoobot.sdk.Plugin
import org.banchoobot.sdk.annotations.CommandFunction
import org.banchoobot.sdk.entities.LoggerConfig
import org.banchoobot.sdk.interfaces.ICommandFunction
import org.banchoobot.sdk.utils.Logger

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