package org.banchoobot.frame.globals

/**
 * 插件的 Api 地址
 */
enum class PostApis(val url: String) {
    SEND_PRIVATE_MESSAGE("/send_private_msg"),
    SEND_PRIVATE_MESSAGE_ASYNC("/send_private_msg_async"),
    SEND_GROUP_MESSAGE("/send_group_msg"),
    SEND_GROUP_MESSAGE_ASYNC("/send_group_msg_async"),
    SEND_DISCUSS_MESSAGE("/send_discuss_msg"),
    SEND_DISCUSS_MESSAGE_ASYNC("/send_discuss_msg_async")
}
