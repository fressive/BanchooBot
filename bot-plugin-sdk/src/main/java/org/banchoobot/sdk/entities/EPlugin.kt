package org.banchoobot.sdk.entities

import org.banchoobot.sdk.Plugin
import org.banchoobot.sdk.annotations.*
import org.banchoobot.sdk.interfaces.*

/**
 * 插件实体类
 */
data class EPlugin (
        val plugin: Class<out Plugin>,
        val commandFunctions: Set<EFunction<CommandFunction, ICommandFunction>>,
        val messageFunctions: Set<EFunction<MessageFunction, IMessageFunction>>,
        val eventFunctions: Set<EFunction<EventFunction, IEventFunction>>,
        val schedulers: Set<EFunction<ScheduleFunction, *>>
) {
    override fun toString(): String {
        val functions = mutableSetOf<EFunction<*, *>>()
        functions.addAll(commandFunctions)
        functions.addAll(messageFunctions)
        functions.addAll(eventFunctions)
        functions.addAll(schedulers)

        return "${this.plugin.simpleName}[ ${functions.joinToString { it.clazz.simpleName }} ]"
    }
}