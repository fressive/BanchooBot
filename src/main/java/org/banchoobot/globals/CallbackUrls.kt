package org.banchoobot.globals

/**
 * 回调 Url 枚举类
 */
enum class CallbackUrls(val url: String) {
    SEND_PRIVATE_MESSAGE("/send_private_msg"),
    SEND_PRIVATE_MESSAGE_ASYNC("/send_private_msg_async"),
    SEND_GROUP_MESSAGE("/send_group_msg"),
    SEND_GROUP_MESSAGE_ASYNC("/send_group_msg_async");

    override fun toString(): String {
        return this.url
    }
}