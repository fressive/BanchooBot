package org.banchoobot.functions.entities

/**
 * 功能实体类
 */
data class EFunction<out A, C>(
        val annotation: A,
        val clazz: Class<C>
)