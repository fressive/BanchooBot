package org.banchoobot.server.handlers

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONException
import com.sun.net.httpserver.HttpExchange
import com.sun.net.httpserver.HttpHandler
import org.banchoobot.extensions.StreamExtension.writeJson
import java.util.logging.Logger

/**
 * 上报消息处理
 */

class MessageHandler : HttpHandler {
    private val LOGGER = Logger.getLogger(MessageHandler::class.java.name)

    override fun handle(exchange: HttpExchange?) {
        exchange ?: return

        // 过滤 favicon 的请求
        if ("favicon.ico" in exchange.requestURI.rawPath)
            return

        val repBody = exchange.responseBody

        exchange.responseHeaders.set("Content-type", "application/json")
        // 设置响应头的 Content-type

        if (exchange.requestMethod.toUpperCase() != "POST") {
            // 过滤非 POST 的请求
            exchange.sendResponseHeaders(405, 0)
            repBody.writeJson(mapOf("code" to 1000001, "msg" to "Method not allowed."))

        } else {

            val reqBody = exchange.requestBody.reader(charset("UTF-8")).readText()
            LOGGER.info(reqBody)

            if (reqBody == "") {
                // 过滤无参的请求
                exchange.sendResponseHeaders(412, 0)
                repBody.writeJson(mapOf("code" to 1000002, "msg" to "Request body cannot be null."))
            } else {
                try {
                    // 过滤请求体非 json 的请求
                    JSON.parse(reqBody)
                    exchange.sendResponseHeaders(200, 0)
                    repBody.writeJson(mapOf("code" to 200, "msg" to "SUCCESS"))
                } catch (e: JSONException) {
                    exchange.sendResponseHeaders(412, 0)
                    repBody.writeJson(mapOf("code" to 1000003, "msg" to "Cannot convert request body to json."))
                }
            }
        }


        this.LOGGER.info("Receive a http request from ${exchange.remoteAddress.hostString}\n${exchange.responseCode} - ${exchange.requestURI.rawPath}${ if (exchange.requestURI.rawQuery != null) "?${exchange.requestURI.rawQuery}" else "" }\n")
        // 劲爆一长串的 shit log

        repBody.close()

        TODO("处理上报的数据")
    }
}
