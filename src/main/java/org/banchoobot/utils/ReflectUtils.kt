package org.banchoobot.utils

import org.banchoobot.functions.Function
import org.banchoobot.functions.entities.EFunction
import org.reflections.Reflections
import org.reflections.scanners.SubTypesScanner
import org.reflections.util.ClasspathHelper
import org.reflections.util.ConfigurationBuilder
import java.io.File
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
    inline fun <reified T> getImplementClassesFromPackage(`package`: String): Set<Class<out T>> {
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
     * @param packages 除了 org.banchoobot 包外要扫描的包列表
     * @param filter 过滤器
     *
     * @return 所有方法
     */
    inline fun <reified A : Annotation, reified I> getFunctions(packages: Collection<String> = emptySet(), filter: (A) -> Boolean = { true }): Set<EFunction<A, I>> {
        val set = mutableSetOf<EFunction<A, I>>()
        getImplementClasses<I>().forEach { c ->
            val ats = c.getAnnotationsByType(A::class.java)
            if (ats.count() < 1) return@forEach
            if (filter.invoke(ats[0]))
                set.add(EFunction(ats[0], c))
        }
        packages.forEach {
            getImplementClassesFromPackage<I>(it).forEach f@{ c ->
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
    fun getJars(path: String) = File(path).list({ _, name -> name.toLowerCase().endsWith(".jar") })

    /**
     * 获取插件
     *
     * @return 所有符合条件的插件的包
     */
    fun getPlugins(): Set<String> {
        val plugins = getJars("plugins")

        val packageSet = mutableSetOf<String>()

        plugins.forEach {
            val name = "plugins/$it"
            pluginClassLoader.addJar(File(name).toURI().toURL())

            val jar = JarFile(File(name))

            val `class` = pluginClassLoader.loadClass(ManifestParser.parse(jar.getInputStream(jar.getJarEntry("META-INF/MANIFEST.MF")).reader().readText())["Load-Class"])

            if (`class`.newInstance() !is Function) {
                return@forEach
            }

            pluginClassLoader.addJar(ClasspathHelper.forClass(`class`, pluginClassLoader))

            packageSet.add(`class`.`package`.name)

            jar.close()
        }
        return packageSet
    }

}
