package org.banchoobot.server

import com.sun.net.httpserver.HttpHandler
import com.sun.net.httpserver.HttpServer
import org.banchoobot.server.handlers.MessageHandler
import java.net.InetSocketAddress

/**
 * 信息上报 Http 服务器
 */

class ReportServer(private val host: String, private val port: Int = 8060) {
    private lateinit var server: HttpServer

    fun listen() {
        this.server = HttpServer.create(InetSocketAddress(host, port), 0)
        // 创建 http 服务
        this.server.createContext("/", MessageHandler())
        // 添加映射
        this.server.start()
        // 服务器，启动！
    }

}
