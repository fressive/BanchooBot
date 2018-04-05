package org.banchoobot.entities.pojo.mothership
import com.alibaba.fastjson.annotation.JSONField


/**
 * Bean for MotherShip's UserInfo API
 */

data class UserInfo(
		@JSONField(name = "code") val code: Int,
		@JSONField(name = "status") val status: String,
		@JSONField(name = "data") val data: List<UserInfoData>
)

data class UserInfoData(
		@JSONField(name = "mode") val mode: Int,
		@JSONField(name = "userId") val userId: Int,
		@JSONField(name = "count300") val count300: Int,
		@JSONField(name = "count100") val count100: Int,
		@JSONField(name = "count50") val count50: Int,
		@JSONField(name = "playcount") val playcount: Int,
		@JSONField(name = "accuracy") val accuracy: Double,
		@JSONField(name = "ppRaw") val ppRaw: Double,
		@JSONField(name = "rankedScore") val rankedScore: Long,
		@JSONField(name = "totalScore") val totalScore: Long,
		@JSONField(name = "level") val level: Double,
		@JSONField(name = "ppRank") val ppRank: Int,
		@JSONField(name = "countRankSs") val countRankSs: Int,
		@JSONField(name = "countRankSsh") val countRankSsh: Int,
		@JSONField(name = "countRankS") val countRankS: Int,
		@JSONField(name = "countRankSh") val countRankSh: Int,
		@JSONField(name = "countRankA") val countRankA: Int,
		@JSONField(name = "queryDate") val queryDate: QueryDate
)

data class QueryDate(
		@JSONField(name = "year") val year: Int,
		@JSONField(name = "month") val month: Int,
		@JSONField(name = "day") val day: Int
)