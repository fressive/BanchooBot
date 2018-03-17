package org.banchoobot.functions

import org.banchoobot.BanchooBot
import org.banchoobot.BotBootstrapper
import org.banchoobot.frame.configs.BotConfig
import org.banchoobot.functions.annotations.ScheduleFunction
import org.banchoobot.utils.ConfigUtils
import org.quartz.Job
import org.quartz.JobExecutionContext
import java.util.logging.Logger

/**
 * 自动保存配置
 */
@ScheduleFunction(name = "AutoSaveConfig", cron = "*/5 * * * * ?")
class AutoSaveConfig : Job {
    companion object {
        var lastConfig: BotConfig? = null
    }

    private val LOGGER = Logger.getLogger(AutoSaveConfig::class.java.name)

    override fun execute(ctx: JobExecutionContext?) {
        BotBootstrapper.bot ?: return
        val bot = BotBootstrapper.bot as BanchooBot

        if (lastConfig == null) {
            lastConfig = bot.config
            return
        }

        val newCfg = ConfigUtils.readConfig()

        if (newCfg != AutoSaveConfig.lastConfig) {
            LOGGER.info("配置文件被更改，读取新配置中\n")
            bot.config = newCfg
        } else if (bot.config != AutoSaveConfig.lastConfig) {
            LOGGER.info("程序内配置被更改，保存新配置中\n")
            bot.saveConfig()
        }

        AutoSaveConfig.lastConfig = bot.config

    }
}