package org.banchoobot.functions.entities

/**
 * 功能实体类
 */
data class EFunction<out A, out C>(
        val annotation: A,
        val clazz: Class<out C>
)