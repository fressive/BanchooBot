package org.banchoobot.sdk.utils

import org.banchoobot.sdk.entities.LoggerConfig
import java.io.File

/**
 * 一个简单的日志工具类
 */
class Logger(private val loggerName: String, config: LoggerConfig) {
    companion object {
        private val loggers = mutableMapOf<String, LoggerConfig>()
    }

    enum class LogLevel(val levelName: String) {
        VERB("Verb"),
        INFO("Info"),
        DEBUG("Debug"),
        WARNING("Warning"),
        ERROR("Error")
    }

    constructor(clazz: Class<*>, config: LoggerConfig) : this(clazz.simpleName, config)

    private fun format(message: String, level: LogLevel)
        = loggers[loggerName]!!.format
            .replace("#time", DateUtils.getFormatDate())
            .replace("#name", loggerName)
            .replace("#level", level.levelName)
            .replace("#thread", Thread.currentThread().name)
            .replace("#message", message)

    fun log(text: Any, level: LogLevel)
        = when (level) { LogLevel.ERROR -> error(text); LogLevel.WARNING -> warning(text); LogLevel.DEBUG -> debug(text); LogLevel.INFO -> info(text); LogLevel.VERB -> verb(text) }

    fun verb(message: Any) = format(message.toString(), LogLevel.VERB).apply { writeToFile(this); System.out.println(this) }

    fun info(message: Any) = format(message.toString(), LogLevel.INFO).apply { writeToFile(this); System.out.println(this) }

    fun debug(message: Any) = format(message.toString(), LogLevel.DEBUG).apply { writeToFile(this); System.out.println(this) }

    fun warning(message: Any) = format(message.toString(), LogLevel.WARNING).apply { writeToFile(this); System.err.println(this) }

    fun error(message: Any) = format(message.toString(), LogLevel.ERROR).apply { writeToFile(this); System.err.println(this) }


    private fun writeToFile(message: String) {
        val cfg = loggers[loggerName]
        if (!cfg!!.isWritingToFile) return

        val file = File(cfg.logFilePath)
        if (!file.isRooted) if (!file.parentFile.exists()) file.parentFile.mkdirs()
        if (!file.exists()) file.createNewFile()
        file.appendText(message + System.getProperty("line.separator"))
    }

    init {
        Logger.loggers[loggerName] = config

        if (config.createNewFile) {
            val file = File(config.logFilePath)
            if (file.exists()) {
                file.delete()
                file.createNewFile()
            }
        }
    }
}