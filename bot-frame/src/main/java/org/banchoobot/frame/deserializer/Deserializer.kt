package org.banchoobot.frame.deserializer

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONException
import com.alibaba.fastjson.JSONObject
import org.banchoobot.frame.deserializer.events.Event
import org.banchoobot.frame.deserializer.events.GroupMessageEvent
import org.banchoobot.frame.deserializer.events.PrivateMessageEvent
import org.banchoobot.frame.deserializer.exceptions.FieldNotFoundException

/**
 * 上报数据解析
 *
 * @param src 原 JSON 数据
 */
class Deserializer(src: String) {
    private lateinit var jsonObject: JSONObject

    init {
        try {
            this.jsonObject = JSON.parseObject(src)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    /**
     * 解析数据
     *
     * @return 解析后的事件，若事件未解析成功则返回基事件，postType 为 null
     */
    fun deserialize(): Event {
        val obj = this.jsonObject
        var event = Event("null")

        if (obj.containsKey("post_type")) {
            if (obj.containsKey("message_type")) {
                // 消息事件
                event = when (obj.getString("message_type")) {
                    "group" -> GroupMessageEvent(obj.getString("message"), obj.getLong("user_id"), obj.getLong("group_id"), obj.getString("sub_type"))
                    "private" -> PrivateMessageEvent(obj.getString("message"), obj.getLong("user_id"), obj.getString("sub_type"))
                    else -> throw FieldNotFoundException("message_type")
                }
            }
            // TODO: 其他事件未处理

        } else {
            throw FieldNotFoundException("post_type")
        }

        return event
    }


}