package org.banchoobot.frame.utils

import com.alibaba.fastjson.JSON
import org.banchoobot.frame.configs.PublicConfig
import org.banchoobot.frame.globals.PostApis

/**
 * Bot 工具类
 */
object BotUtils {

    /**
     * 发送私聊消息
     *
     * @param qq 接受者的QQ号
     * @param text 要发送的信息
     * @param isEscape 是否不转义
     * @param isAsync 是否使用异步发送
     *
     * @return 消息ID
     */
    fun sendPrivateMessage(qq: Long,
                           text: String,
                           isEscape: Boolean = false,
                           isAsync: Boolean = true): Long {
        val url = "${PublicConfig.apiUrl}${ if(isAsync) PostApis.SEND_PRIVATE_MESSAGE_ASYNC.url else PostApis.SEND_PRIVATE_MESSAGE.url }"
        val json = JSON.toJSONString(mapOf("user_id" to qq, "message" to text, "auto_escape" to isEscape))
        return JSON.parseObject(HttpUtils.post(url, json)?.body()?.string()).getLong("message_id")
    }

    /**
     * 发送群聊
     *
     * @param qq 接受群的群号
     * @param text 要发送的信息
     * @param isEscape 是否不转义
     * @param isAsync 是否使用异步发送
     *
     * @return 消息ID
     */
    fun sendGroupMessage(qq: Long,
                           text: String,
                           isEscape: Boolean = false,
                           isAsync: Boolean = true): Long {
        val url = "${PublicConfig.apiUrl}${ if(isAsync) PostApis.SEND_GROUP_MESSAGE_ASYNC.url else PostApis.SEND_GROUP_MESSAGE.url }"
        val json = JSON.toJSONString(mapOf("group_id" to qq, "message" to text, "auto_escape" to isEscape))
        return JSON.parseObject(HttpUtils.post(url, json)?.body()?.string()).getLong("message_id")
    }

    /**
     * 发送讨论组信息
     *
     * @param qq 接受的讨论组的 ID
     * @param text 要发送的信息
     * @param isEscape 是否不转义
     * @param isAsync 是否使用异步发送
     *
     * @return 消息ID
     */
    fun sendDiscussMessage(qq: Long,
                         text: String,
                         isEscape: Boolean = false,
                         isAsync: Boolean = true): Long {
        val url = "${PublicConfig.apiUrl}${ if(isAsync) PostApis.SEND_DISCUSS_MESSAGE_ASYNC.url else PostApis.SEND_DISCUSS_MESSAGE.url }"
        val json = JSON.toJSONString(mapOf("discuss_id" to qq, "message" to text, "auto_escape" to isEscape))
        return JSON.parseObject(HttpUtils.post(url, json)?.body()?.string()).getLong("message_id")
    }

    /**
     * 获取图片的 CQ 码（使用url）
     *
     * @param url 图片Url
     * @param isCached 是否缓存图片
     *
     * @return CQ码
     */
    fun getImageWithUrl(url: String, isCached: Boolean = true): String
            = "[CQ:image,${if (!isCached) "cache=0," else ""}file=$url]"

    /**
     * 获取图片的 CQ 码（使用 Base64）
     *
     * @param base64 base64
     */
    fun getImageWithBase64(base64: String): String
            = "[CQ:image,file=base64://$base64]"
}