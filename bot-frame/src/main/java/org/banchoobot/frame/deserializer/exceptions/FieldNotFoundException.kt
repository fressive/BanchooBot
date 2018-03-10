package org.banchoobot.frame.deserializer.exceptions

/**
 * 字段未找到异常
 *
 * @see org.banchoobot.frame.deserializer.Deserializer
 */
class FieldNotFoundException constructor(fieldName: String):
        Exception("$fieldName field not found.")