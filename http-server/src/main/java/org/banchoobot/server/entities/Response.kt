package org.banchoobot.server.entities

import com.sun.net.httpserver.Headers

/**
 * 响应实体类
 *
 * @param code http状态码
 * @param text 响应文本
 * @param headers 响应头
 */
data class Response(
        val code: Int,
        val text: String,
        val headers: Headers = Headers()
) {
    constructor(text: String) : this(200, text)

    /**
     * 将 Content-Type 设置为 application/json
     *
     * @return 设置后的 Response
     */
    fun json(): Response
        = this.apply { if(this.headers.containsKey("Content-Type")) this.headers["Content-Type"] = "application/json" else this.headers.add("Content-Type", "application/json") }
}