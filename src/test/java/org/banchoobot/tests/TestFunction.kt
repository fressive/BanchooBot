package org.banchoobot.tests

import org.banchoobot.frame.deserializer.events.Event
import org.banchoobot.frame.deserializer.events.FileUploadEvent
import org.banchoobot.frame.deserializer.events.Message
import org.banchoobot.functions.annotations.AllowedEvents
import org.banchoobot.functions.annotations.CommandFunction
import org.banchoobot.functions.annotations.EventFunction
import org.banchoobot.functions.interfaces.ICommandFunction
import org.banchoobot.functions.interfaces.IEventFunction

/**
 * Test function
 */
@CommandFunction(command = "upload", disabled = true)
@EventFunction(allowedEvent = AllowedEvents.GROUP_UPLOAD, disabled = true)
class TestFunction : IEventFunction, ICommandFunction {

    override fun onCommand(event: Message) {
        println("Test OK")
    }

    override fun onEvent(event: Event) {
        println("Test2 OK")
        if (event is FileUploadEvent)
            println("${event.userID} 在 ${event.groupID} 中上传了 ${event.file.name}")
    }

}