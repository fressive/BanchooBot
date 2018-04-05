package org.banchoobot.functions.schedulers

import org.banchoobot.BotBootstrapper
import org.banchoobot.functions.annotations.ScheduleFunction
import org.banchoobot.utils.ReflectUtils
import org.quartz.*
import org.quartz.Scheduler
import org.quartz.impl.StdSchedulerFactory

/**
 * 定时任务
 */
object Scheduler {
    private val scheduler: Scheduler = StdSchedulerFactory().scheduler
    val names: MutableList<String> = mutableListOf()

    fun init() {
        ReflectUtils.getFunctions<ScheduleFunction, Job>(BotBootstrapper.bot!!.pluginPackages, { !it.disabled }).forEach {
            val jobDetail = JobBuilder.newJob(it.clazz).withIdentity(it.annotation.name, "BaseGroup").build()
            val trigger = TriggerBuilder.newTrigger().withIdentity("${it.annotation.name}_trigger", "BaseGroup")
                    .startNow().withSchedule<CronTrigger>(CronScheduleBuilder.cronSchedule(it.annotation.cron)).build()
            scheduler.scheduleJob(jobDetail, trigger)
            names.add(it.annotation.name)
        }

        scheduler.start()
    }
}