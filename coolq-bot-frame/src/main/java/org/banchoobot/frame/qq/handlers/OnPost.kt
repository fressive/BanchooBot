package org.banchoobot.frame.qq.handlers

import org.banchoobot.frame.qq.Bot
import org.banchoobot.frame.qq.deserializer.Deserializer
import org.banchoobot.frame.qq.deserializer.events.Event
import org.banchoobot.frame.qq.deserializer.events.Message
import org.banchoobot.frame.qq.deserializer.exceptions.FieldNotFoundException
import org.banchoobot.server.HttpRequest
import org.banchoobot.server.entities.Response
import java.util.logging.Logger

/**
 * 上报处理
 */
object OnPost {
    private val LOGGER = Logger.getLogger("PostServer")

    fun onPost(req: HttpRequest, bot: Bot): Response {
        // LOGGER.info("上报服务器接收到请求。\n")

        val text = req.getRequestBodyText()
        if (text != "") {
            try {
                val event = Deserializer(text).deserialize()

                when (event) {
                    is Message -> bot.onMessage(event) // 调用 bot 的 onMessage 方法
                    is Event -> bot.onEvent(event)
                    else -> throw Exception("Unknown event")
                }

                // return Response(JSON.toJSONString(response))
                return Response("{}")

            } catch (e: FieldNotFoundException) {
                LOGGER.severe("无法解析上报数据：${req.getRequestBodyText().trim('\n')}\n")
            }
        }

        return Response("Reserved")
    }
}