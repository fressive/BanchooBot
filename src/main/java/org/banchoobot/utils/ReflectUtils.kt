package org.banchoobot.utils

import org.banchoobot.functions.entities.EFunction
import org.reflections.Reflections
import org.reflections.scanners.SubTypesScanner
import org.reflections.util.ClasspathHelper
import org.reflections.util.ConfigurationBuilder

/**
 * 反射工具类
 */
object ReflectUtils {
    inline fun <reified T> getImplementClasses(): Set<Class<out T>> {
        val reflections = Reflections(ConfigurationBuilder()
                .setUrls(ClasspathHelper.forPackage("org.banchoobot"))
                .addScanners(SubTypesScanner()))

        return reflections.getSubTypesOf(T::class.java)
    }

    inline fun <reified A : Annotation, reified I> getFunctions(filter: (A) -> Boolean = { true }): Set<EFunction<A, I>> {
        val set = mutableSetOf<EFunction<A, I>>()
        getImplementClasses<I>().forEach { c ->
            val ats = c.getAnnotationsByType(A::class.java)
            if (ats.count() < 1) return@forEach
            if (filter.invoke(ats[0]))
                set.add(EFunction(ats[0], c))
        }

        return set
    }
}