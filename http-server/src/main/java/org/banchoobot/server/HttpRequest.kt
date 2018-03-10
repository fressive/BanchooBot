package org.banchoobot.server

import com.sun.net.httpserver.Headers
import com.sun.net.httpserver.HttpExchange
import org.banchoobot.server.globals.HttpMethods

/**
 * 对 Http 请求的封装
 *
 * @see com.sun.net.httpserver.HttpExchange
 */
class HttpRequest(src: HttpExchange) {

    private val method: HttpMethods = HttpMethods.valueOf(src.requestMethod.toUpperCase())
    private val headers: Headers = src.requestHeaders
    private val body: String = src.requestBody.reader(charset("UTF-8")).readText()

    fun getRequestBodyText(): String
        = this.body

}
