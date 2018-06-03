package org.banchoobot.sdk.utils.configurator

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.serializer.SerializerFeature

/**
 * 配置保存格式接口
 */
abstract class ConfigSavingFormat (
        val suffix: String
){
    companion object {
        val Json : ConfigSavingFormat = object : ConfigSavingFormat(".json") {
            private val _map: Map<String, Any> = mutableMapOf()

            fun Map<String, Any>.getClass() = this::class.java

            override fun serialize(obj: Map<String, Any>): String
                = JSON.toJSONString(obj, SerializerFeature.PrettyFormat)

            override fun deserialize(code: String): Map<String, Any>
                = JSON.parseObject(code, _map.getClass())

        }
    }

    abstract fun serialize(obj: Map<String, Any>): String
    abstract fun deserialize(code: String): Map<String, Any>
}
