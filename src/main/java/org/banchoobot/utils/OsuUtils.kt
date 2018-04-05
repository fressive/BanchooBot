package org.banchoobot.utils

import com.alibaba.fastjson.JSON
import org.banchoobot.BotBootstrapper
import org.banchoobot.entities.pojo.mothership.QQUserData
import org.banchoobot.entities.pojo.mothership.UserInfo
import org.banchoobot.entities.pojo.osu.RecentData
import org.banchoobot.entities.pojo.osu.UserData
import org.banchoobot.extensions.StringExtensions.mformat
import org.banchoobot.frame.utils.HttpUtils
import org.banchoobot.globals.GameModes
import org.banchoobot.globals.MotherShipApi
import org.banchoobot.globals.OsuApis
import java.text.SimpleDateFormat
import java.util.*

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
     * 获取用户数据
     *
     * @param name 用户名或 UID
     * @param mode 模式
     *
     * @return 用户数据
     */
    fun getUserData(name: String, mode: GameModes = GameModes.STD): UserData?
            = JSON.parseArray(HttpUtils.get(OsuApis.GET_USER.url,
            mapOf("k" to (BotBootstrapper.bot?.config?.anotherConfigs?.get("osu_api_key")?:throw Exception("osu_api_key is undefined")), "u" to name, "m" to mode))?.body()?.string(), UserData::class.java)
            .let { return if (it.size == 0) null else it[0] }

    /**
     * 获取用户 UID
     *
     * @param name 用户名或 UID
     *
     * @return UID，若用户不存在返回 -1
     */
    fun getUserID(name: String): Int
            = getUserData(name)?.userId ?: -1

    /**
     * 获取用户名称
     *
     * @param name 用户名或 UID
     *
     * @return 用户名，若用户不存在返回空字符串
     */
    fun getUsername(name: String): String
            = getUserData(name)?.username.orEmpty()

    /**
     * 获取用户最近游玩数据
     *
     * @param name 用户名或 UID
     * @param mode 模式
     * @param limit 记录数
     *
     * @return 数据
     */
    fun getUserRecent(name: String, mode: GameModes = GameModes.STD, limit: Int = 100): List<RecentData>
            = JSON.parseArray(HttpUtils.get(OsuApis.GET_USER_RECENT.url,
            mapOf("k" to (BotBootstrapper.bot?.config?.anotherConfigs?.get("osu_api_key")?:throw Exception("osu_api_key is undefined")), "u" to name, "m" to mode.ordinal, "l" to limit))?.body()?.string(), RecentData::class.java)

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
         * 获取用户数据
         *
         * @param userID 玩家UID
         * @param mode 模式
         * @param date 记录日期
         * @param limit 记录数
         *
         * @return data
         */
        fun getUserData(userID: Int,
                        mode: GameModes = GameModes.STD,
                        date: String = SimpleDateFormat("yyyyMMdd").format(Date(System.currentTimeMillis() - 1000 * 60 * 60 * 24)),
                        limit: Int = 1): UserInfo
                = JSON.parseObject(HttpUtils.get(MotherShipApi.GET_USER_INFO.url.mformat(userID.toString()), mapOf("mode" to mode.ordinal, "start" to date, "limit" to limit))?.body()?.string(), UserInfo::class.java)

        /**
         * 获取最近一条数据
         *
         * @param userID 玩家UID
         * @param mode 模式
         *
         * @return data
         */
        fun getUserNearestData(userID: Int,
                               mode: GameModes = GameModes.STD): UserInfo
                = JSON.parseObject(HttpUtils.get(MotherShipApi.GET_USER_INFO_NEAREST.url.mformat(userID.toString()), mapOf("mode" to mode.ordinal))?.body()?.string(), UserInfo::class.java)

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