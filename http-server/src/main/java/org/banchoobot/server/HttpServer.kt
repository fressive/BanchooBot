package org.banchoobot.server

import com.sun.net.httpserver.HttpServer
import org.banchoobot.server.entities.Response
import org.banchoobot.server.globals.HttpMethods
import java.net.InetSocketAddress

/**
 * Http 服务器
 */
class HttpServer {
    private lateinit var server: HttpServer
    private lateinit var baseHandler: BaseHandler

    /**
     * 创建 HttpServer 实例
     *
     * @param host 监听地址
     * @param port 监听端口
     * @return self
     */
    fun create(host: String = "127.0.0.1", port: Int = 8060): org.banchoobot.server.HttpServer {
        this.server = HttpServer.create(InetSocketAddress(host, port), 0)
        this.baseHandler = BaseHandler()
        return this
    }

    /**
     * 添加 Http 路径映射
     *
     * @param path 路径
     * @param handler 处理器
     *
     * @return self
     */
    fun route(path: String,
              handler: (HttpRequest) -> Response,
              methods: List<HttpMethods> = listOf(HttpMethods.GET, HttpMethods.POST)
    ): org.banchoobot.server.HttpServer {
        // this.server.createContext(path, handler)
        this.baseHandler.addRoute(path, handler, methods)
        return this
    }

    /**
     * 启动监听
     *
     * @return self
     */
    fun listen(): org.banchoobot.server.HttpServer {
        this.baseHandler.getRoutePaths().forEach { this@HttpServer.server.createContext(it, this.baseHandler) }
        // 为每个路径映射到基处理器上

        this.server.start() // 服务器，启动！
        return this
    }

}
