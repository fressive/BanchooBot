package org.banchoobot.functions.annotations

/**
 * 用户权限
 */
enum class UserPermissions {
    BLOCK, // 被封禁的用户
    NORMAL, // 普通用户
    ADMIN, // 群管理
    BOT_ADMIN // bot 主人
}