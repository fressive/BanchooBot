package org.banchoobot.tests

import org.banchoobot.frame.utils.BotUtils
import org.junit.Test

/**
 * Unit test for BotUtils
 *
 * @see org.banchoobot.frame.utils.BotUtils
 */
class BotUtilsTest {
    @Test
    fun apiTest() {
        BotUtils.sendPrivateMessage(1004121460, "Hello World!")
        BotUtils.sendGroupMessage(583808847, "[CQ:image,file=base64://iVBORw0KGgoAAAANSUhEUgAAABQAAAAVCAIAAADJt1n/AAAAKElEQVQ4EWPk5+RmIBcwkasRpG9UM4mhNxpgowFGMARGEwnBIEJVAAAdBgBNAZf+QAAAAABJRU5ErkJggg==]")
    }
}