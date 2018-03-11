package org.banchoobot.frame.deserializer.exceptions

/**
 * 字段未知异常
 *
 * @see org.banchoobot.frame.deserializer.Deserializer
 */
class UnknownFieldException constructor(fieldName: String, field: String):
        Exception("$field ($fieldName) is unknown.")