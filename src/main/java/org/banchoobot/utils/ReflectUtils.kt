package org.banchoobot.utils

import org.banchoobot.frame.deserializer.events.PrivateMessageEvent
import org.banchoobot.functions.annotations.CommandFunction
import org.banchoobot.functions.annotations.MessageFunction
import org.banchoobot.functions.interfaces.ICommandFunction
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

    fun getCommandFunctions(): Set<Class<*>> {
        val set = mutableSetOf<Class<*>>()
        getImplementClasses(ICommandFunction::class.java).forEach { c ->
            val ats = c.getAnnotationsByType(CommandFunction::class.java)
            if (ats.count() < 1) {
                return@forEach
            }
            set.add(c)
        }

        return set
    }

    fun getMessageFunctions(): Set<Class<*>> {
        val set = mutableSetOf<Class<*>>()
        getImplementClasses(IMessageFunction::class.java).forEach { c ->
            val ats = c.getAnnotationsByType(MessageFunction::class.java)
            if (ats.count() < 1) {
                return@forEach
            }
            set.add(c)
        }

        return set
    }
}