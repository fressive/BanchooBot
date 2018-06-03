package org.banchoobot.functions

import org.banchoobot.BanchooBot
import org.banchoobot.frame.qq.deserializer.events.Message
import org.banchoobot.sdk.annotations.*
import org.banchoobot.sdk.entities.EFunction
import org.banchoobot.sdk.entities.EPlugin
import org.banchoobot.sdk.interfaces.ICommandFunction
import org.banchoobot.sdk.interfaces.IEventFunction
import org.banchoobot.sdk.interfaces.IMessageFunction

/**
 * 一些 Bot 管理功能
 *
 * @author int100
 */
class Bot {
    companion object {
        val dCommandFunctions: MutableMap<String, EFunction<CommandFunction, ICommandFunction>> = mutableMapOf()
        val dMessageFunctions: MutableMap<String, EFunction<MessageFunction, IMessageFunction>> = mutableMapOf()
        val dEventFunctions: MutableMap<String, EFunction<EventFunction, IEventFunction>> = mutableMapOf()
        val dPlugins: MutableMap<String, EPlugin> = mutableMapOf()
    }


    @CommandFunction(command = ["disable"], needPermission = UserPermissions.BOT_ADMIN)
    class DisableFunction : ICommandFunction {
        override val usage: String
            get() = "bot.disable.usage：\ndisable <type> <name>"

        override fun onCommand(event: Message) {
            val cmds = event.message.split(" ")

            if (cmds.size < 3) event.reply(usage).apply { return }

            when (cmds[1]) {
                "p", "plugin" -> BanchooBot.plugins.forEach {
                    if (it.plugin.simpleName.toLowerCase() == cmds[2].toLowerCase()) {
                        dPlugins[cmds[2].toLowerCase()] = it

                        BanchooBot.commandFunctions.removeAll(it.commandFunctions)
                        BanchooBot.eventFunctions.removeAll(it.eventFunctions)
                        BanchooBot.messageFunctions.removeAll(it.messageFunctions)
                        BanchooBot.plugins.remove(it)

                        dPlugins[cmds[2].toLowerCase()]!!.plugin.newInstance().onDisable()

                        event.reply("bot.disable: 已禁用插件 [${cmds[2]}] 。")
                        return
                    }
                }
                "c", "command" -> BanchooBot.commandFunctions.forEach {
                    if (it.clazz.simpleName.toLowerCase() == cmds[2].toLowerCase()) {
                        dCommandFunctions[cmds[2].toLowerCase()] = it
                        BanchooBot.commandFunctions.remove(it)

                        event.reply("bot.disable: 已禁用命令 [${cmds[2]}] 。")
                        return
                    }
                }
                "e", "event" -> BanchooBot.eventFunctions.forEach {
                    if (it.clazz.simpleName.toLowerCase() == cmds[2].toLowerCase()) {
                        dEventFunctions[cmds[2].toLowerCase()] = it
                        BanchooBot.eventFunctions.remove(it)

                        event.reply("bot.disable: 已禁用事件 [${cmds[2]}] 。")
                        return
                    }
                }
                "m", "message" -> BanchooBot.messageFunctions.forEach {
                    if (it.clazz.simpleName.toLowerCase() == cmds[2].toLowerCase()) {
                        dMessageFunctions[cmds[2].toLowerCase()] = it
                        BanchooBot.messageFunctions.remove(it)

                        event.reply("bot.disable: 已禁用文本事件 [${cmds[2]}] 。")
                        return
                    }
                }
                else -> event.reply("bot.disable.exception: 无效的参数值 [type]，应为 p/plugin/c/command/e/event/m/message")
            }


            event.reply("bot.disable.exception: 无法找到 [${cmds[2]}] 。")
        }
    }

    @CommandFunction(command = ["enable"], needPermission = UserPermissions.BOT_ADMIN)
    class EnableFunction : ICommandFunction {
        override val usage: String
            get() = "bot.enable.usage：\nenable <type> <name>"

        override fun onCommand(event: Message) {
            val cmds = event.message.split(" ")

            if (cmds.size < 3) event.reply(usage).apply { return }

            when (cmds[1]) {
                "p", "plugin" -> {
                    if (dPlugins.contains(cmds[2].toLowerCase())) {
                        BanchooBot.plugins.add(dPlugins[cmds[2].toLowerCase()])
                        BanchooBot.commandFunctions.addAll(dPlugins[cmds[2].toLowerCase()]!!.commandFunctions)
                        BanchooBot.eventFunctions.addAll(dPlugins[cmds[2].toLowerCase()]!!.eventFunctions)
                        BanchooBot.messageFunctions.addAll(dPlugins[cmds[2].toLowerCase()]!!.messageFunctions)

                        dPlugins[cmds[2].toLowerCase()]!!.plugin.newInstance().onEnable()

                        dPlugins.remove(cmds[2].toLowerCase())
                        event.reply("bot.enable: 已启用插件 [${cmds[2]}] 。")
                        return
                    }
                }
                "c", "command" -> {
                    if (dCommandFunctions.contains(cmds[2].toLowerCase())) {
                        BanchooBot.commandFunctions.add(dCommandFunctions[cmds[2].toLowerCase()])
                        dCommandFunctions.remove(cmds[2].toLowerCase())
                        event.reply("bot.enable: 已启用命令 [${cmds[2]}] 。")
                        return
                    }
                }
                "e", "event" -> {
                    if (dEventFunctions.contains(cmds[2].toLowerCase())) {
                        BanchooBot.eventFunctions.add(dEventFunctions[cmds[2].toLowerCase()]!!)
                        dEventFunctions.remove(cmds[2].toLowerCase())
                        event.reply("bot.enable: 已启用事件 [${cmds[2]}] 。")
                        return
                    }
                }
                "m", "message" -> {
                    if (dMessageFunctions.contains(cmds[2].toLowerCase())) {
                        BanchooBot.messageFunctions.add(dMessageFunctions[cmds[2].toLowerCase()])
                        dMessageFunctions.remove(cmds[2].toLowerCase())
                        event.reply("bot.enable: 已启用文本事件 [${cmds[2]}] 。")
                        return
                    }
                }
                else -> event.reply("bot.enable.exception: 无效的参数值 [type]，应为 p/plugin/c/command/e/event/m/message")
            }

            event.reply("bot.enable.exception: 无法找到 [${cmds[2]}] 。")
        }
    }
}
