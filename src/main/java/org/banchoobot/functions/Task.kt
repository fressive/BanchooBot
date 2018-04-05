package org.banchoobot.functions

import org.banchoobot.BotBootstrapper
import org.banchoobot.entities.pojo.TaskBean
import org.banchoobot.frame.deserializer.events.GroupMessage
import org.banchoobot.frame.deserializer.events.Message
import org.banchoobot.frame.utils.BotUtils
import org.banchoobot.functions.annotations.CommandFunction
import org.banchoobot.functions.annotations.ScheduleFunction
import org.banchoobot.functions.interfaces.ICommandFunction
import org.banchoobot.utils.OsuUtils
import org.quartz.Job
import org.quartz.JobExecutionContext
import javax.script.Invocable
import javax.script.ScriptEngineManager
import javax.script.ScriptException


/**
 * 任务系统，开发中
 */
class Task {
    companion object {
        val engineManager = ScriptEngineManager()
    }

    @CommandFunction(["addtask"])
    class AddTask : ICommandFunction {
        override fun onCommand(event: Message){
            val params = event.message.split('\n', ' ')

            if (params.size < 2) event.reply("本功能需要 2 个参数。").apply { return@onCommand }

            var codes = event.message.substring(params[0].length).trimStart().trimEnd()
            codes = Regex("&#(\\d*);").replace(codes, { it.groups[1]?.value?.toInt()?.toChar()?.toString().orEmpty() })

            try {
                val engine = engineManager.getEngineByName("javascript")
                engine.eval("""function verify(user, recents) {${"\n"}$codes${"\n"}}""")

                val uid = OsuUtils.MotherShip.getUserBindData(event.userID).data.userId

                if (engine is Invocable) {
                    val result = engine.invokeFunction("verify", OsuUtils.getUserData(uid.toString()), OsuUtils.getUserRecent(uid.toString())) as? Boolean
                    if (result == null) {
                        event.reply("${BotUtils.getAt(event.userID)}\n添加失败")
                        return
                    }
                }

                val hintGroup = (event as? GroupMessage)?.groupID ?: -1

                val task = TaskBean(-1, uid, event.userID, hintGroup, codes, false)
                val session = BotBootstrapper.bot!!.dbSessionFactory.openSession()
                session.insert("org.banchoobot.mappers.TaskMapper.insertTask", task).let { session.commit(); session.close() }

                event.reply("${BotUtils.getAt(event.userID)}\n任务添加成功！")

            } catch (e: ScriptException) {
                event.reply(e.message.orEmpty())
            }
        }
    }

    @ScheduleFunction(name = "CheckTasks", cron = "0 */1 * * * ?")
    class CheckTasks : Job {
        override fun execute(p0: JobExecutionContext?) {
            val session = BotBootstrapper.bot!!.dbSessionFactory.openSession()
            val tasks = session.selectList<TaskBean>("org.banchoobot.mappers.TaskMapper.selectUncompletedTasks")

            try {
                tasks.forEach {
                    val code = it.taskCode

                    val engine = engineManager.getEngineByName("javascript")
                    engine.eval("""function verify(user, recents) {${"\n"}$code${"\n"}}""")
                    // 使用 javascript 来判断时候执行完毕

                    val uid = OsuUtils.MotherShip.getUserBindData(it.qq).data.userId

                    if (engine is Invocable) {
                        val result = engine.invokeFunction("verify", OsuUtils.getUserData(uid.toString()), OsuUtils.getUserRecent(uid.toString())) as Boolean

                        if (result) {
                            session.update("org.banchoobot.mappers.TaskMapper.completeTask", it)
                            session.commit()

                            BotUtils.sendPrivateMessage(it.qq, "你的任务 #${it.taskId} 已完成！祝贺！")

                            if (it.hintGroup != -1L)
                                BotUtils.sendGroupMessage(it.hintGroup, "${BotUtils.getAt(it.qq)} 完成了任务 #${it.taskId} ！")

                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

            session.close()
        }
    }
}