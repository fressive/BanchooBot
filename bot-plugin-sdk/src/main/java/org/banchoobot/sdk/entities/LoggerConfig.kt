package org.banchoobot.sdk.entities

import java.io.File

/**
 * 日志类配置
 */
class LoggerConfig (
        val name: String,
        val format: String = "#time #name [#level]: #message",
        val isWritingToFile: Boolean,
        val logFilePath: String = "logs${File.separator}$name.log"
){
    constructor(clazz: Class<*>, format: String = "#time #name [#level]: #message", isWritingToFile: Boolean = false)
            : this(clazz.name,
                    format,
                    isWritingToFile,
                    "logs${File.separator}${clazz.simpleName}.log"
    )
}
