package org.banchoobot.frame.qq.deserializer.exceptions

/**
 * 字段未找到异常
 *
 * @see org.banchoobot.frame.qq.deserializer.Deserializer
 */
class FieldNotFoundException constructor(fieldName: String):
        Exception("$fieldName field not found.")