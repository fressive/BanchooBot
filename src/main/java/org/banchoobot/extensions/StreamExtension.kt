package org.banchoobot.extensions

import com.alibaba.fastjson.JSON
import java.io.OutputStream

/**
 * 操作流扩展
 */

object StreamExtension {

    /**
     * 将字符串写入流
     *
     * @param string 欲写入流的字符串
     * @param charset 编码
     */
    fun OutputStream.writeString(string: String, charset: String = "UTF-8") {
        this.write(string.toByteArray(charset(charset)))
    }

    /**
     * 将 Json 字符串写入流
     *
     * @param obj 对象，将会被序列化
     * @param charset 编码
     */
    fun OutputStream.writeJson(obj: Any, charset: String = "UTF-8") {
        this.writeString(JSON.toJSONString(obj), charset)
    }
}
