package org.banchoobot.tests

import org.banchoobot.frame.deserializer.events.PrivateMessageEvent
import org.banchoobot.functions.annotations.CommandFunction
import org.banchoobot.functions.interfaces.ICommandFunction
import org.banchoobot.utils.ReflectUtils
import org.junit.Test

/**
 * Unit test for ReflectUtils
 *
 * @see org.banchoobot.utils.ReflectUtils
 */
class ReflectUtilsTest {
    @Test
    fun getImplementClassesTest() {
        ReflectUtils.getImplementClasses(ICommandFunction::class.java).forEach { c ->
            val ats = c.getAnnotationsByType(CommandFunction::class.java)
            assert(ats.count() > 0)

            c.methods.forEach {
                if (it.name == "onCommand")
                    it.invoke(c.newInstance(), PrivateMessageEvent("!roll", 1004121460))
            }
            ats.forEach { println("${it.command} for ${c.simpleName}") }
        }
    }
}