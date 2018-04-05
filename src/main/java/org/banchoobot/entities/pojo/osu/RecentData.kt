package org.banchoobot.entities.pojo.osu
import com.alibaba.fastjson.annotation.JSONField


/**
 * 最近游玩历史的 JavaBean
 */

data class RecentData(
		@JSONField(name = "beatmap_id") val beatmapId: Int,
		@JSONField(name = "score") val score: Int,
		@JSONField(name = "maxcombo") val maxcombo: Int,
		@JSONField(name = "count50") val count50: Int,
		@JSONField(name = "count100") val count100: Int,
		@JSONField(name = "count300") val count300: Int,
		@JSONField(name = "countmiss") val countmiss: Int,
		@JSONField(name = "countkatu") val countkatu: Int,
		@JSONField(name = "countgeki") val countgeki: Int,
		@JSONField(name = "perfect") val perfect: Int,
		@JSONField(name = "enabled_mods") val enabledMods: Int,
		@JSONField(name = "user_id") val userId: Int,
		@JSONField(name = "date") val date: String,
		@JSONField(name = "rank") val rank: String
)