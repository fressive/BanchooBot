package org.banchoobot.functions

import org.banchoobot.frame.deserializer.events.Message
import org.banchoobot.frame.utils.BotUtils
import org.banchoobot.functions.annotations.CommandFunction
import org.banchoobot.functions.annotations.MessageFunction
import org.banchoobot.functions.interfaces.ICommandFunction
import org.banchoobot.functions.interfaces.IMessageFunction
import org.banchoobot.utils.OsuUtils
import java.util.regex.Pattern

/**
 * 瞎几把查
 *
 * Usage:
 * stats             查询自己的数据
 * stats :mode       查询自己模式为 $mode 的数据
 * stats [name]      查询 $name 的数据
 * stats [name:mode] 查询 $name 模式为 $mode 的数据
 * istats            返回图片
 */
@Suppress("KDocUnresolvedReference")
@CommandFunction(command = ["s", "stat", "stats", "is", "istat", "istats"])
@MessageFunction()
class Stats : ICommandFunction, IMessageFunction {
    companion object {
        /**
         * 查找结果：
         * Group1: 名称
         * Group2: 模式
         */
        private val REGEX = Pattern.compile("""^(.+?)\s*(?:[:：](\S+))?${'$'}""").toRegex()

    }

    override fun onCommand(event: Message) {
        val command = event.message.split(" ")

        val params = if (command.size == 1) { "${OsuUtils.MotherShip.getUserBindData(event.userID).data.userId}:STD" } else { command[1] }

        val result = Stats.REGEX.find(params)
        val user = OsuUtils.getUserID(result?.groups?.get(1)?.value ?: "")
        val mode = OsuUtils.getModeFromString(result?.groups?.get(2)?.value?.trim(' ', '\n', '\r') ?: "STD")

        if (user == -1)
            event.reply("没有这个人，快丨！").let { return }

        if (command[0].substring(AutoSaveConfig.lastConfig?.anotherConfigs?.get("prefix")?.toString()?.length ?: 1).startsWith("i"))
            event.reply(BotUtils.getImageWithUrl(url = OsuUtils.MotherShip.getStatsImage(userID = user, mode = mode), isCached = false))
        else
            TODO("so lazy")
    }

    override fun onMessage(event: Message) {
        if (event.message != "~")
            return

        TODO("so lazy 二连")
    }
}