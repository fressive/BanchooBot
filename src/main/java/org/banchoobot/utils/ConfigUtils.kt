package org.banchoobot.utils

import com.alibaba.fastjson.JSON
import org.banchoobot.frame.configs.BotConfig
import java.io.File
import java.io.FileReader
import java.io.FileWriter

/**
 * 配置读取
 */
object ConfigUtils {
    fun readConfig(path: String = "./config/bot.json"): BotConfig {
        val file = File(path)
        if (!file.exists()) {
            file.parentFile.mkdirs()
            file.createNewFile()
            return BotConfig().apply { saveConfig(this) }
        } else if (FileReader(file).readText() == "") {
            return BotConfig().apply { saveConfig(this) }
        }

        return JSON.parseObject(FileReader(file).readText(), BotConfig::class.java)
    }

    fun saveConfig(config: BotConfig, path: String = "./config/bot.json") {
        val file = File(path)
        if (file.exists()) {
            FileWriter(file).apply { this.write(JSON.toJSONString(config, true)); this.flush(); this.close() }
            return
        }

        file.createNewFile()
        FileWriter(file).apply { this.write(JSON.toJSONString(config, true)); this.flush(); this.close() }
        return
    }
}