package org.banchoobot.entities.pojo.mothership
import com.alibaba.fastjson.annotation.JSONField

/**
 * QQ 绑定数据 Bean for MotherShip's API
 */
data class QQUserData(
		@JSONField(name = "code") val code: Int,
		@JSONField(name = "status") val status: String,
		@JSONField(name = "data") val data: Data
)

data class Data(
		@JSONField(name = "userId") val userId: Int,
		@JSONField(name = "role") val role: String,
		@JSONField(name = "qq") val qq: Long,
		@JSONField(name = "legacyUname") val legacyUname: String,
		@JSONField(name = "currentUname") val currentUname: String,
		@JSONField(name = "banned") val banned: Boolean,
		@JSONField(name = "mode") val mode: Int,
		@JSONField(name = "repeatCount") val repeatCount: Int,
		@JSONField(name = "speakingCount") val speakingCount: Int
)