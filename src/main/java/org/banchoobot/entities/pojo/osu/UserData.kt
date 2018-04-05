package org.banchoobot.entities.pojo.osu
import com.alibaba.fastjson.annotation.JSONField


/**
 * 用户数据 Bean for osu's api
 */
data class UserData(
		@JSONField(name = "user_id") val userId: Int,
		@JSONField(name = "username") val username: String,
		@JSONField(name = "count300") val count300: Int,
		@JSONField(name = "count100") val count100: Int,
		@JSONField(name = "count50") val count50: Int,
		@JSONField(name = "playcount") val playcount: Int,
		@JSONField(name = "ranked_score") val rankedScore: Long,
		@JSONField(name = "total_score") val totalScore: Long,
		@JSONField(name = "pp_rank") val ppRank: Int,
		@JSONField(name = "level") val level: Double,
		@JSONField(name = "pp_raw") val ppRaw: Double,
		@JSONField(name = "accuracy") val accuracy: Double,
		@JSONField(name = "count_rank_ss") val countRankSs: Int,
		@JSONField(name = "count_rank_ssh") val countRankSsh: Int,
		@JSONField(name = "count_rank_s") val countRankS: Int,
		@JSONField(name = "count_rank_sh") val countRankSh: Int,
		@JSONField(name = "count_rank_a") val countRankA: Int,
		@JSONField(name = "country") val country: String,
		@JSONField(name = "pp_country_rank") val ppCountryRank: Int,
		@JSONField(name = "events") val events: List<Event>
)

data class Event(
		@JSONField(name = "display_html") val displayHtml: String,
		@JSONField(name = "beatmap_id") val beatmapId: Int,
		@JSONField(name = "beatmapset_id") val beatmapsetId: Int,
		@JSONField(name = "date") val date: String,
		@JSONField(name = "epicfactor") val epicfactor: String
)