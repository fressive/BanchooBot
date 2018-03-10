package org.banchoobot.server

import com.sun.net.httpserver.HttpExchange
import com.sun.net.httpserver.HttpHandler
import org.banchoobot.server.extensions.HttpExchangeExtensions.writeText
import org.banchoobot.server.entities.Response
import org.banchoobot.server.entities.Route
import org.banchoobot.server.globals.HttpMethods
import org.banchoobot.server.extensions.HttpExchangeExtensions.throwError

/**
 * 基处理器
 */
class BaseHandler : HttpHandler {
    private val routes: MutableMap<String, Route> = mutableMapOf()

    override fun handle(ctx: HttpExchange?) {
        ctx ?: return

        // 遍历路由配置
        routes.forEach { path, route ->
            if (ctx.requestURI.rawPath == path) {
                // 如果路径符合

                if (route.methods.contains(HttpMethods.valueOf(ctx.requestMethod.toUpperCase()))) {
                    // 如果请求方式符合

                    val rep = route.handler(HttpRequest(ctx))

                    ctx.sendResponseHeaders(rep.code, 0)
                    ctx.writeText(rep.text)
                    ctx.responseHeaders.putAll(rep.headers)

                } else {
                    ctx.throwError(405)
                }

                ctx.responseBody.flush()
                ctx.responseBody.close()
            }
        }
    }

    /**
     * 获取所有路由路径
     *
     * @return 路径
     */
    fun getRoutePaths(): Set<String>
            = this.routes.keys

    /**
     * 添加路由配置
     *
     * @see org.banchoobot.server.entities.Route
     */
    fun addRoute(path: String, handler: (HttpRequest) -> Response, methods: List<HttpMethods>)
            = this.routes.put(path, Route(path, handler, methods))

    fun addRoute(route: Route)
            = this.routes.put(route.path, route)

    /**
     * 删除路由配置
     *
     * @param path 路径
     */
    fun removeRoute(path: String)
            = this.routes.remove(path)
}