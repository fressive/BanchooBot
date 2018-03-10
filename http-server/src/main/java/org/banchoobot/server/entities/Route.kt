package org.banchoobot.server.entities

import org.banchoobot.server.HttpRequest
import org.banchoobot.server.globals.HttpMethods

/**
 * 路由配置
 *
 * @param path 路径
 * @param handler 处理器
 * @param methods 支持的访问方法
 */
data class Route(
        val path: String,
        val handler: (HttpRequest) -> Response,
        val methods: List<HttpMethods>
)