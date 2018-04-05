package org.banchoobot.utils

import com.alibaba.fastjson.JSON
import org.banchoobot.frame.configs.BotConfig
import java.io.File
import java.io.FileReader
import java.io.FileWriter

/**
 * 配置读取工具类
 */
object ConfigUtils {

    /**
     * 读取配置。
     *
     * @param path 路径
     * @return 读取的配置
     */
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

    /**
     * 保存配置。
     *
     * @param config 配置
     * @param path 路径
     */
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