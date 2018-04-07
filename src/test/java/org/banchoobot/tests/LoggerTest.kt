package org.banchoobot.tests

import org.banchoobot.functions.entities.LoggerConfig
import org.banchoobot.functions.utils.Logger
import org.junit.Test

/**
 * Shit unit test
 */
class LoggerTest {
    @Test
    fun test() {
        val config = LoggerConfig(LoggerTest::class.java, isWritingToFile = true)
        val logger = Logger(LoggerTest::class.java, config)

        logger.log("Test", Logger.LogLevel.DEBUG)
        logger.verb("Test verb")
        logger.info("Test info")
        logger.debug("Test debug")
        logger.warning("Test warning")
        logger.error("Test error")
    }
}