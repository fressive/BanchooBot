package org.banchoobot.tests

import org.banchoobot.frame.deserializer.events.Message
import org.banchoobot.functions.annotations.MessageFunction
import org.banchoobot.functions.annotations.ScheduleFunction
import org.banchoobot.functions.interfaces.IMessageFunction
import org.quartz.Job
import org.quartz.JobExecutionContext

/**
 * 测试定时任务
 */
@MessageFunction
@ScheduleFunction("TestScheduleFunction", "*/2 * * * * ?")
class TestScheduleFunction : Job, IMessageFunction {
    override fun execute(p0: JobExecutionContext?) {
        println("stupid code")
    }

    override fun onMessage(event: Message) {
        // test done
    }

}