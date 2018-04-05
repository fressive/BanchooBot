package org.banchoobot.globals

/**
 * 妈船 Api
 */
enum class MotherShipApi(url: String) {

    /**
     * 获取最近一条记录
     *
     * @param {0} Uid
     */
    GET_USER_INFO_NEAREST("/userinfo/nearest/{0}"),

    /**
     * 获取一条记录
     *
     * @param {0} Uid
     */
    GET_USER_INFO("/userinfo/{0}"),

    /**
     * 获取玩家 stat 图片
     *
     * @param {0} Uid
     */
    GET_USER_STATS_IMAGE("/stat/{0}"),

    /**
     * 获取玩家绑定信息
     *
     * @param {0} QQ
     */
    GET_USER_BIND_DATA("/user/qq/{0}");


    private val ROOT_URL = "http://www.mothership.top:8080/api/v1"

    var url: String = url
        get() { return ROOT_URL + field }
}