package org.banchoobot.functions

import com.alibaba.fastjson.JSON
import org.banchoobot.frame.deserializer.events.Message
import org.banchoobot.frame.utils.BotUtils
import org.banchoobot.frame.utils.HttpUtils
import org.banchoobot.sdk.annotations.CommandFunction
import org.banchoobot.sdk.interfaces.ICommandFunction

/**
 * Sorry，有钱真的了不起
 */
@CommandFunction(command = ["sorry"])
class Sorry : ICommandFunction {
    override fun onCommand(event: Message) {
        val msg = event.message.split(" ")
        val map = mutableMapOf<String, String>()
        msg.forEachIndexed { i, s ->
            if (i == 0) return@forEachIndexed
            map[(i - 1).toString()] = s
        }

        event.reply(BotUtils.getImageWithUrl("https://sorry.xuty.tk/${HttpUtils.post("https://sorry.xuty.tk/api/sorry/make", body = JSON.toJSONString(map))?.body()?.string()}", isCached = true))

    }
}