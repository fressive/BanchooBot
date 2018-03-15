package org.banchoobot.functions

import org.banchoobot.BanchooBot
import org.banchoobot.BotBootstrapper
import org.banchoobot.functions.annotations.ScheduleFunction
import org.quartz.Job
import org.quartz.JobExecutionContext
import java.util.logging.Logger

/**
 * 自动保存配置
 */
@ScheduleFunction(name = "AutoSaveConfig", cron = "*/5 * * * * ?")
class AutoSaveConfig : Job {
    private val LOGGER = Logger.getLogger(AutoSaveConfig::class.java.name)

    override fun execute(ctx: JobExecutionContext?) {
        BotBootstrapper.bot ?: return
        val bot = BotBootstrapper.bot as BanchooBot

        bot.saveConfig()

    }
}