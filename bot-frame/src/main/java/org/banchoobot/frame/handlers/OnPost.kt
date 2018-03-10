package org.banchoobot.frame.handlers

import com.alibaba.fastjson.JSON
import org.banchoobot.frame.Bot
import org.banchoobot.frame.deserializer.Deserializer
import org.banchoobot.frame.deserializer.events.MessageEvent
import org.banchoobot.frame.deserializer.exceptions.FieldNotFoundException
import org.banchoobot.server.HttpRequest
import org.banchoobot.server.entities.Response
import java.util.logging.Logger

/**
 * 上报处理
 */
object OnPost {
    private val LOGGER = Logger.getLogger("PostServer")

    fun onPost(req: HttpRequest, bot: Bot): Response {
        LOGGER.info("上报服务器接收到请求。\n")

        val text = req.getRequestBodyText()
        if (text != "") {
            try {
                val event = Deserializer(text).deserialize()

                val response = when (event) {
                    is MessageEvent -> bot.onMessage(event) // 调用 bot 的 onMessage 方法
                    else -> throw Exception("Unknown event")
                }

                return Response(JSON.toJSONString(response))

            } catch (e: FieldNotFoundException) {
                LOGGER.severe("无法解析上报数据：${req.getRequestBodyText().trim('\n')}\n")
            }
        }

        return Response("Reserved")
    }
}