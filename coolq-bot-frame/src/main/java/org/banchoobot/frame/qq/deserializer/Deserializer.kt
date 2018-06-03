package org.banchoobot.frame.qq.deserializer

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONException
import com.alibaba.fastjson.JSONObject
import org.banchoobot.frame.qq.deserializer.events.*
import org.banchoobot.frame.qq.deserializer.events.entities.UploadFile
import org.banchoobot.frame.qq.deserializer.exceptions.FieldNotFoundException
import org.banchoobot.frame.qq.deserializer.exceptions.UnknownFieldException

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
    fun deserialize(): PostEvent {
        val obj = this.jsonObject
        var event = PostEvent("null")

        if (obj.containsKey("post_type")) {
            val time = obj.getLong("time")

            when (obj.getString("post_type")) {
                "message" -> {
                    val m = obj.getString("message")
                    val id = obj.getLong("message_id")
                    val uid = obj.getLong("user_id")

                    event = when (obj.getString("message_type")) {
                        "group"   -> GroupMessage(m, id, uid, obj.getLong("group_id"), obj.getString("sub_type"), time)
                        "private" -> PrivateMessage(m, id, uid, obj.getString("sub_type"), time)
                        "discuss" -> DiscussMessage(m, id, uid, obj.getLong("discuss_id"), time)
                             else -> throw UnknownFieldException("message_type", obj.getString("message_type"))
                    }
                }

                "event" -> {
                    val uid = obj.getLong("user_id")

                    event = when (obj.getString("event")) {
                        "group_upload" -> FileUploadEvent(obj.getLong("group_id"), uid, obj.getObject("file", UploadFile::class.java), time)
                        "group_admin" -> AdminChangeEvent(obj.getString("sub_type"), obj.getLong("group_id"), uid, time)
                        else -> throw UnknownFieldException("event", obj.getString("event"))
                    }
                }
            }

        }
        else {
            throw FieldNotFoundException("post_type")
        }

        return event
    }


}