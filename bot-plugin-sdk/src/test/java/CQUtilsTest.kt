import org.banchoobot.sdk.utils.CQUtils
import org.junit.Test

class CQUtilsTest {
    @Test
    fun test() {
        CQUtils.getImageFromCQCode("aaaaa[CQ:image,file=B00E0DDCC18B107598188FC6EB922B80.jpg,url=https://gchat.qpic.cn/gchatpic_new/2037246484/641236878-2826080140-B00E0DDCC18B107598188FC6EB922B80/0?vuin=3318691441&amp;term=2]").forEach {
            print("file: ${it.file}, url: ${it.url}")
        }
    }
}