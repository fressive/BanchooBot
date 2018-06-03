package org.banchoobot.sdk.utils.configurator

import java.io.File
import java.nio.file.Paths

/**
 * 简单的配置管理器
 */
open class Configurator (
        private val configName: String,
        private val format: ConfigSavingFormat = ConfigSavingFormat.Json,
        configPath: String = Paths.get("configs", configName + format.suffix).toString(),
        defaultConfig: Map<String, Any> = mapOf()
){
    private val configs: MutableMap<String, Any> = mutableMapOf()
    private val configFile = File(configPath)

    init {
        this.copyConfig(defaultConfig)
        this.load()
        this.save()
    }

    /**
     * 复制 [map] 到现有配置中
     */
    private fun copyConfig(map: Map<String, Any>): Unit
        = this.configs.let { it.clear(); it.putAll(map) }

    /**
     * 获取配置项
     */
    fun get(key: String, default: Any = ""): Any
        = configs.getOrDefault(key, default)

    /**
     * 设置配置项
     */
    fun set(key: String, value: Any): Unit
        = configs.apply { this[key] = value }.let { save() }

    /**
     * 保存配置文件
     */
    fun save()
        = configFile
            .apply { if(!this.parentFile.exists()) this.parentFile.mkdirs(); this.createNewFile(); this.writeText(format.serialize(configs)) }

    /**
     * 加载配置文件
     */
    fun load()
        = configFile
            .apply { if (!this.exists()) save(); configs.let { configs.putAll(format.deserialize(this.readText())) } }

}