package org.banchoobot.frame.qq.utils

import okhttp3.*
import java.net.URLEncoder

/**
 * Http 工具对象
 *
 * @author int100
 * Created by int100 on 2017/12/23
 */
object HttpUtils {

    private val HTTP_CLIENT: OkHttpClient = OkHttpClient()

    /**
     * 发送 GET 请求
     *
     * @param url 请求地址
     * @param params 请求参数 默认为 null
     *
     * @return response， 当发生异常时返回 null
     */
    fun get(url: String, params: Map<String, Any>? = null): Response? {
        return try {
            val req: Request = Request.Builder()
                    .get()
                    .url(buildUrl(url, params))
                    .build()

            HTTP_CLIENT
                    .newCall(req)
                    .execute()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * 发送 Post 请求
     *
     * @param url 请求地址
     * @param body 请求体
     * @param type Content-Type
     *
     * @return response， 当发生异常时返回 null
     */
    fun post(url: String, body: String = "", type: MediaType = MediaType.parse("application/json")!!): Response? {
        return try {
            val req: Request = Request.Builder()
                    .post(RequestBody.create(type, body))
                    .url(url)
                    .build()

            HTTP_CLIENT
                    .newCall(req)
                    .execute()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }


    /**
     * 解析参数，生成url
     *
     * @param url 请求地址
     * @param params 请求参数 默认为 null
     *
     * @return 格式化后的 url
     */
    private fun buildUrl(url: String, params: Map<String, Any>? = null): String {
        params ?: return url

        var realUrl = "$url?"

        params.forEach { k, v ->
            realUrl += "$k=$v&"
        }

        return realUrl.trimEnd('&')
    }

}