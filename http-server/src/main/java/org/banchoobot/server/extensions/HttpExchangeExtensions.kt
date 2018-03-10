package org.banchoobot.server.extensions

import com.sun.net.httpserver.HttpExchange

/**
 * HttpExchange 类的扩展
 */
object HttpExchangeExtensions {

    /**
     * 丢你雷母的 Http Errors
     *
     * @param code Http 错误码
     */
    fun HttpExchange.throwError(code: Int) {
        this.sendResponseHeaders(code, 0)

        when (code) {
            404 -> writeText("404 NOT FOUND")
            405 -> writeText("405 METHOD NOT ALLOWED")
        }
    }

    /**
     * 将文本写到响应流上
     *
     * @param text 文本
     */
    fun HttpExchange.writeText(text: String) {
        val writer = this.responseBody.writer()
        writer.write(text)
        writer.close()
    }

}