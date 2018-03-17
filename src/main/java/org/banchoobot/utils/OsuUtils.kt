package org.banchoobot.utils

import com.alibaba.fastjson.JSON
import org.banchoobot.entities.pojo.mothership.QQUserData
import org.banchoobot.entities.pojo.osu.UserData
import org.banchoobot.extensions.StringExtensions.mformat
import org.banchoobot.frame.configs.PublicConfig
import org.banchoobot.frame.utils.HttpUtils
import org.banchoobot.functions.AutoSaveConfig
import org.banchoobot.globals.GameModes
import org.banchoobot.globals.MotherShipApi
import org.banchoobot.globals.OsuApis

/**
 * 屙屎工具类
 */
object OsuUtils {

    /**
     * 从字符串中获取模式
     *
     * @param str 字符串
     *
     * @return 游戏模式，若未匹配到则返回 STD
     */
    fun getModeFromString(str: String)
            = when(str.toUpperCase()) { "0", "STD" -> GameModes.STD; "1", "TAIKO" -> GameModes.TAIKO; "2", "CTB" -> GameModes.CTB; "3", "MANIA" -> GameModes.MANIA; else -> GameModes.STD}

    /**
     * 获取用户 UID
     *
     * @param name 用户名或 UID
     *
     * @return UID，若用户不存在返回 -1
     */
    fun getUserID(name: String): Int
            = JSON.parseArray(HttpUtils.get(OsuApis.GET_USER.url,
                mapOf("k" to (AutoSaveConfig.lastConfig?.anotherConfigs?.get("osu_api_key")?:""), "u" to name))?.body()?.string(), UserData::class.java)
                .let { return if (it.size == 0) -1 else it[0].userId.toInt() }


    /**
     * 妈船的
     */
    object MotherShip {

        /**
         * 获取 Stat 图片
         *
         * @param userID 玩家UID
         * @param mode 模式
         *
         * @return 图片 Url
         */
        fun getStatsImage(userID: Int,
                          mode: GameModes = GameModes.STD): String
                = "${MotherShipApi.GET_USER_STATS_IMAGE.url.mformat(userID.toString())}?mode=${mode.ordinal}"

        /**
         * 获取用户绑定数据
         *
         * @param qq QQ号
         *
         * @return 实体类
         */
        fun getUserBindData(qq: Long): QQUserData
                = JSON.parseObject(HttpUtils.get(MotherShipApi.GET_USER_BIND_DATA.url.mformat(qq.toString()))?.body()?.string(), QQUserData::class.java)
    }
}