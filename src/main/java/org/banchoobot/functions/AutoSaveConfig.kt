package org.banchoobot.functions

import org.banchoobot.BanchooBot
import org.banchoobot.BotBootstrapper
import org.banchoobot.frame.configs.BotConfig
import org.banchoobot.sdk.annotations.ScheduleFunction
import org.banchoobot.sdk.entities.LoggerConfig
import org.banchoobot.sdk.utils.Logger
import org.banchoobot.utils.ConfigUtils
import org.quartz.Job
import org.quartz.JobExecutionContext

/**
 * 自动保存配置
 */
@ScheduleFunction(name = "AutoSaveConfig", cron = "*/5 * * * * ?")
class AutoSaveConfig : Job {
    companion object {
        var lastConfig: BotConfig? = null
    }

    private val LOGGER = Logger(AutoSaveConfig::class.java, LoggerConfig(AutoSaveConfig::class.java))

    override fun execute(ctx: JobExecutionContext?) {
        BotBootstrapper.bot ?: return
        val bot = BotBootstrapper.bot as BanchooBot

        if (lastConfig == null) {
            lastConfig = bot.config
            return
        }

        val newCfg = ConfigUtils.readConfig()

        if (newCfg != lastConfig) {
            LOGGER.info("配置文件被更改，读取新配置中\n")
            bot.config = newCfg
        } else if (bot.config != lastConfig) {
            LOGGER.info("程序内配置被更改，保存新配置中\n")
            bot.saveConfig()
        }

        lastConfig = bot.config

    }
}