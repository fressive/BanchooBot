package org.banchoobot.globals

/**
 * 屙屎 Api
 */
enum class OsuApis(url: String) {

    /**
     * 获取用户数据
     */
    GET_USER("/api/get_user");

    private val ROOT_URL = "http://osu.ppy.sh"

    var url: String = url
        get() { return ROOT_URL + field }
}