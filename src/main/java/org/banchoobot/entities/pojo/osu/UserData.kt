package org.banchoobot.entities.pojo.osu
import com.alibaba.fastjson.annotation.JSONField


/**
 * 用户数据 POJO for osu's api
 */
data class UserData(
		@JSONField(name = "user_id") val userId: String,
		@JSONField(name = "username") val username: String,
		@JSONField(name = "count300") val count300: String,
		@JSONField(name = "count100") val count100: String,
		@JSONField(name = "count50") val count50: String,
		@JSONField(name = "playcount") val playcount: String,
		@JSONField(name = "ranked_score") val rankedScore: String,
		@JSONField(name = "total_score") val totalScore: String,
		@JSONField(name = "pp_rank") val ppRank: String,
		@JSONField(name = "level") val level: String,
		@JSONField(name = "pp_raw") val ppRaw: String,
		@JSONField(name = "accuracy") val accuracy: String,
		@JSONField(name = "count_rank_ss") val countRankSs: String,
		@JSONField(name = "count_rank_ssh") val countRankSsh: String,
		@JSONField(name = "count_rank_s") val countRankS: String,
		@JSONField(name = "count_rank_sh") val countRankSh: String,
		@JSONField(name = "count_rank_a") val countRankA: String,
		@JSONField(name = "country") val country: String,
		@JSONField(name = "pp_country_rank") val ppCountryRank: String,
		@JSONField(name = "events") val events: List<Event>
)

data class Event(
		@JSONField(name = "display_html") val displayHtml: String,
		@JSONField(name = "beatmap_id") val beatmapId: String,
		@JSONField(name = "beatmapset_id") val beatmapsetId: String,
		@JSONField(name = "date") val date: String,
		@JSONField(name = "epicfactor") val epicfactor: String
)