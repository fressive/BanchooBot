package org.banchoobot.utils

import org.banchoobot.functions.annotations.CommandFunction
import org.banchoobot.functions.annotations.EventFunction
import org.banchoobot.functions.annotations.MessageFunction
import org.banchoobot.functions.entities.EFunction
import org.banchoobot.functions.interfaces.ICommandFunction
import org.banchoobot.functions.interfaces.IEventFunction
import org.banchoobot.functions.interfaces.IMessageFunction
import org.reflections.Reflections
import org.reflections.scanners.SubTypesScanner
import org.reflections.util.ClasspathHelper
import org.reflections.util.ConfigurationBuilder

/**
 * 反射工具类
 */
object ReflectUtils {
    fun getImplementClasses(interfaze: Class<*>): Set<Class<*>> {
        val reflections = Reflections(ConfigurationBuilder()
                .setUrls(ClasspathHelper.forPackage("org.banchoobot"))
                .addScanners(SubTypesScanner()))

        return reflections.getSubTypesOf(interfaze)
    }

    fun getCommandFunctions(): Set<EFunction<CommandFunction, *>> {
        val set = mutableSetOf<EFunction<CommandFunction, *>>()
        getImplementClasses(ICommandFunction::class.java).forEach { c ->
            val ats = c.getAnnotationsByType(CommandFunction::class.java)
            if (ats.count() < 1 || ats[0].disabled) return@forEach
            set.add(EFunction(ats[0], c))
        }

        return set
    }

    fun getMessageFunctions(): Set<EFunction<MessageFunction, *>> {
        val set = mutableSetOf<EFunction<MessageFunction, *>>()
        getImplementClasses(IMessageFunction::class.java).forEach { c ->
            val ats = c.getAnnotationsByType(MessageFunction::class.java)
            if (ats.count() < 1 || ats[0].disabled) return@forEach
            set.add(EFunction(ats[0], c))
        }

        return set
    }

    fun getEventFunctions(): Set<EFunction<EventFunction, *>> {
        val set = mutableSetOf<EFunction<EventFunction, *>>()
        getImplementClasses(IEventFunction::class.java).forEach { c ->
            val ats = c.getAnnotationsByType(EventFunction::class.java)
            if (ats.count() < 1 || ats[0].disabled) return@forEach
            set.add(EFunction(ats[0], c))
        }

        return set
    }
}