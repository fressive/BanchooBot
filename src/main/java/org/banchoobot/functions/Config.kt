package org.banchoobot.functions

import org.banchoobot.BotBootstrapper
import org.banchoobot.frame.deserializer.events.Message
import org.banchoobot.functions.annotations.CommandFunction
import org.banchoobot.functions.annotations.UserPermissions
import org.banchoobot.functions.interfaces.ICommandFunction

/**
 * 配置操作
 */
class Config {

    @CommandFunction(command = ["setconf", "setconfig", "sc"], needPermission = UserPermissions.BOT_ADMIN)
    class SetConfig : ICommandFunction {
        override fun onCommand(event: Message) {
            val msg = event.message.split(" ")
            BotBootstrapper.bot?.config?.anotherConfigs?.put(msg[1], msg[2])
            BotBootstrapper.bot?.saveConfig()
            event.reply("Set config ('${msg[1]}' : '${msg[2]}').")
        }
    }

    @CommandFunction(command = ["delconf", "delconfig", "dc"], needPermission = UserPermissions.BOT_ADMIN)
    class DelConfig : ICommandFunction {
        override fun onCommand(event: Message) {
            val msg = event.message.split(" ")
            BotBootstrapper.bot?.config?.anotherConfigs?.remove(msg[1])
            BotBootstrapper.bot?.saveConfig()
            event.reply("Remove config ('${msg[1]}').")
        }
    }
}