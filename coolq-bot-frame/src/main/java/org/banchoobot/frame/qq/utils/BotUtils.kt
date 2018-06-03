package org.banchoobot.frame.qq.utils

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONObject
import org.banchoobot.frame.qq.configs.PublicConfig
import org.banchoobot.frame.qq.globals.PostApis

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
                           isAsync: Boolean = true) {
        val url = "${PublicConfig.apiUrl}${ if(isAsync) PostApis.SEND_PRIVATE_MESSAGE_ASYNC.url else PostApis.SEND_PRIVATE_MESSAGE.url }"
        val json = JSON.toJSONString(mapOf("user_id" to qq, "message" to text, "auto_escape" to isEscape))
        HttpUtils.post(url, json)
    }

    /**
     * 发送群聊
     *
     * @param qq 接受群的群号
     * @param text 要发送的信息
     * @param isEscape 是否不转义
     * @param isAsync 是否使用异步发送
     */
    fun sendGroupMessage(qq: Long,
                         text: String,
                         isEscape: Boolean = false,
                         isAsync: Boolean = true) {
        val url = "${PublicConfig.apiUrl}${ if(isAsync) PostApis.SEND_GROUP_MESSAGE_ASYNC.url else PostApis.SEND_GROUP_MESSAGE.url }"
        val json = JSON.toJSONString(mapOf("group_id" to qq, "message" to text, "auto_escape" to isEscape))
        HttpUtils.post(url, json)
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
                           isAsync: Boolean = true) {
        val url = "${PublicConfig.apiUrl}${ if(isAsync) PostApis.SEND_DISCUSS_MESSAGE_ASYNC.url else PostApis.SEND_DISCUSS_MESSAGE.url }"
        val json = JSON.toJSONString(mapOf("discuss_id" to qq, "message" to text, "auto_escape" to isEscape))
        HttpUtils.post(url, json)
    }

    /**
     * 撤回消息
     *
     * @param msgID 消息ID
     */
    fun deleteMessage(msgID: Long) {
        val url = "${PublicConfig.apiUrl}${ PostApis.DELETE_MESSAGE }"
        val json = JSON.toJSONString(mapOf("message_id" to msgID))
        HttpUtils.post(url, json)
    }

    /**
     * 发送赞
     *
     * @param user 用户 QQ
     * @param times 发送赞的次数，每天最多 10 次
     */
    fun sendLike(user: Long,
                 times: Short = 1) {
        val url = "${PublicConfig.apiUrl}${ PostApis.SEND_LIKE }"
        val json = JSON.toJSONString(mapOf("user_id" to user, "times" to times))
        HttpUtils.post(url, json)
    }

    /**
     * 群组踢人
     *
     * @param group 群
     * @param user 用户
     * @param isRejectRequest 是否拒绝加群请求
     */
    fun groupKick(group: Long,
                  user: Long,
                  isRejectRequest: Boolean = false) {
        val url = "${PublicConfig.apiUrl}${ PostApis.GROUP_KICK }"
        val json = JSON.toJSONString(mapOf("group_id" to group, "user_id" to user, "reject_add_request" to isRejectRequest))
        HttpUtils.post(url, json)
    }
    /**
     * 获取群成员信息
     *
     * @param qq 接受的讨论组的 ID
     * @param group 群号
     * @param cache 是否缓存
     *
     * @return obj
     */
    fun getGroupMemberInfo(qq: Long,
                           group: Long,
                           cache: Boolean = false): JSONObject {
        val url = "${PublicConfig.apiUrl}${PostApis.GET_GROUP_MEMBER_INFO.url}"
        val json = JSON.toJSONString(mapOf("user_id" to qq, "group_id" to group, "no_cache" to cache))
        return JSON.parseObject(HttpUtils.post(url, json)?.body()?.string())
    }

    /**
     * 获取@的 CQ 码
     *
     * @param qq 被@的人的 QQ
     *
     * @return CQ码
     */
    fun getAt(qq: Long): String
            = "[CQ:at,qq=$qq]"

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
     *
     * @return CQ码
     */
    fun getImageWithBase64(base64: String): String
            = "[CQ:image,file=base64://$base64]"
}