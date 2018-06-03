package org.banchoobot.utils

import org.banchoobot.sdk.Plugin
import org.banchoobot.sdk.annotations.CommandFunction
import org.banchoobot.sdk.annotations.EventFunction
import org.banchoobot.sdk.annotations.MessageFunction
import org.banchoobot.sdk.annotations.ScheduleFunction
import org.banchoobot.sdk.entities.EFunction
import org.banchoobot.sdk.entities.EPlugin
import org.banchoobot.sdk.interfaces.ICommandFunction
import org.banchoobot.sdk.interfaces.IEventFunction
import org.banchoobot.sdk.interfaces.IMessageFunction
import org.quartz.Job
import org.reflections.Reflections
import org.reflections.scanners.SubTypesScanner
import org.reflections.util.ClasspathHelper
import org.reflections.util.ConfigurationBuilder
import java.io.File
import java.lang.NullPointerException
import java.util.jar.JarFile

/**
 * 反射工具类
 */
object ReflectUtils {
    val pluginClassLoader: MyClassLoader by lazy { MyClassLoader(arrayOf(), ClassLoader.getSystemClassLoader()) }

    /**
     *  在 org.banchoobot 包下获取实现自 [T] 类型的所有类
     *
     *  @return 所有实现的类
     */
    inline fun <reified T> getImplementClasses(): Set<Class<out T>> {
        val reflections = Reflections(ConfigurationBuilder()
                .setUrls(ClasspathHelper.forPackage("org.banchoobot"))
                .addClassLoader(pluginClassLoader)
                .addScanners(SubTypesScanner()))

        return reflections.getSubTypesOf(T::class.java)
    }

    /**
     * 在 [package] 包下获取实现自 [T] 类型的所有类
     *
     * @param `package` 要获取的包
     *
     * @return 所有实现的类
     */
    inline fun <reified T> getImplementClasses(`package`: String): Set<Class<out T>> {
        val reflections = Reflections(ConfigurationBuilder()
                .setUrls(ClasspathHelper.forPackage(`package`, pluginClassLoader))
                .addClassLoader(pluginClassLoader)
                .addScanners(SubTypesScanner()))

        return reflections.getSubTypesOf(T::class.java)
    }

    /**
     * 获取方法
     *
     * @param A 方法注解
     * @param I 方法接口
     * @param filter 过滤器
     *
     * @return 所有方法
     */
    inline fun <reified A : Annotation, reified I> getFunctions(filter: (A) -> Boolean = { true }): Set<EFunction<A, I>> {
        val set = mutableSetOf<EFunction<A, I>>()
        getImplementClasses<I>("org.banchoobot").forEach f@{ c ->
            val ats = c.getAnnotationsByType(A::class.java)
            if (ats.count() < 1) return@f
            if (filter.invoke(ats[0]))
                set.add(EFunction(ats[0], c))
        }


        return set
    }

    /**
     * 获取方法
     *
     * @param A 方法注解
     * @param I 方法接口
     * @param packages 要扫描的包列表
     * @param filter 过滤器
     *
     * @return 所有方法
     */
    inline fun <reified A : Annotation, reified I> getFunctions(packages: Collection<String> = emptySet(), filter: (A) -> Boolean = { true }): Set<EFunction<A, I>> {
        val set = mutableSetOf<EFunction<A, I>>()
        packages.forEach {
            getImplementClasses<I>(it).forEach f@{ c ->
                val ats = c.getAnnotationsByType(A::class.java)
                if (ats.count() < 1) return@f
                if (filter.invoke(ats[0]))
                    set.add(EFunction(ats[0], c))
            }
        }

        return set
    }

    /**
     * 获取 [path] 路径下的所有 jar 包
     */
    private fun getJars(path: String) = File(path).list({ _, name -> name.toLowerCase().endsWith(".jar") })

    /**
     * 获取插件
     *
     * @return 所有符合条件的插件
     */
    fun getPlugins(): Set<EPlugin> {
        val plugins = getJars("plugins")

        val eplugins = mutableSetOf<EPlugin>()

        plugins.forEach {
            this.pluginClassLoader.addJar(File("plugins/$it").toURI().toURL())
            val jar = JarFile(File("plugins/$it"))


            @Suppress("UNCHECKED_CAST")

            try {
                val `class` = (this.pluginClassLoader.loadClass(
                        ManifestParser.parse(
                                jar.getInputStream(jar.getJarEntry("META-INF/MANIFEST.MF"))
                                        .reader().readText()
                        )["Load-Class"])) as? Class<Plugin> ?: return@forEach
                // if (`class`.isAssignableFrom(Plugin::class.java)) return@forEach
                this.pluginClassLoader.addJar(ClasspathHelper.forClass(`class`, this.pluginClassLoader))

                val cfs = getFunctions<CommandFunction, ICommandFunction>(listOf(`class`.`package`.name))
                val mfs = getFunctions<MessageFunction, IMessageFunction>(listOf(`class`.`package`.name))
                val efs = getFunctions<EventFunction, IEventFunction>(listOf(`class`.`package`.name))
                val ss = getFunctions<ScheduleFunction, Job>(listOf(`class`.`package`.name))

                eplugins.add(EPlugin(`class`, cfs, mfs, efs, ss))
                `class`.newInstance().onInit()

                jar.close()
            } catch (e: NullPointerException) {
                return@forEach
            }

        }
        return eplugins
    }

}
