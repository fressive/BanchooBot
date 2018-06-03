package org.banchoobot.frame.qq.globals

/**
 * 插件的 Api 地址
 */
enum class PostApis(val url: String) {
    SEND_PRIVATE_MESSAGE("/send_private_msg"),
    SEND_PRIVATE_MESSAGE_ASYNC("/send_private_msg_async"),
    SEND_GROUP_MESSAGE("/send_group_msg"),
    SEND_GROUP_MESSAGE_ASYNC("/send_group_msg_async"),
    SEND_DISCUSS_MESSAGE("/send_discuss_msg"),
    SEND_DISCUSS_MESSAGE_ASYNC("/send_discuss_msg_async"),
    DELETE_MESSAGE("/delete_msg"),
    SEND_LIKE("/send_like"),
    GROUP_KICK("/set_group_kick"),
    GROUP_BAN("/set_group_ban"),
    GET_GROUP_MEMBER_INFO("/get_group_member_info")
}
