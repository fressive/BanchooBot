package org.banchoobot.frame

import org.banchoobot.frame.configs.BotConfig
import org.banchoobot.frame.deserializer.events.MessageEvent
import org.banchoobot.frame.entities.MessageReply
import org.banchoobot.frame.handlers.OnPost
import org.banchoobot.server.HttpServer

/**
 * Bot
 *
 * @param config bot 配置
 */
abstract class Bot(private val config: BotConfig) {
    private lateinit var server: HttpServer

    /**
     * 当收到消息时的回调
     *
     * @param message 消息事件
     * @return 回复信息
     */
    abstract fun onMessage(message: MessageEvent): MessageReply

    /**
     * 启动 bot
     */
    fun start() {
        this.server = HttpServer()
                .create(config.host, config.port)
                .route("/", {
                    OnPost.onPost(it, this@Bot)
                })
                .listen()
    }

}