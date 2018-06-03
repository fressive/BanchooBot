package org.banchoobot.frame.qq.deserializer.exceptions

/**
 * 字段未知异常
 *
 * @see org.banchoobot.frame.qq.deserializer.Deserializer
 */
class UnknownFieldException constructor(fieldName: String, field: String):
        Exception("$field ($fieldName) is unknown.")