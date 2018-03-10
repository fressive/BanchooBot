package org.banchoobot.frame.configs

/**
 * Bot 配置
 *
 * @param host 上报服务器绑定的地址
 * @param port 上报服务器绑定的端口
 * @param apiUrl 插件服务器的 URL
 */
data class BotConfig(
        val host: String = "127.0.0.1",
        val port: Int = 8060,
        val apiUrl: String = "http://127.0.0.1:5700"
)