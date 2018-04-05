package org.banchoobot.utils

import java.net.URL
import java.net.URLClassLoader

/**
 * Shit class loader
 */
class MyClassLoader : URLClassLoader {
    constructor(urls: Array<URL>) : super(urls)
    constructor(urls: Array<URL>, parent: ClassLoader?) : super(urls, parent)

    fun addJar(path: URL) = this.addURL(path)
}