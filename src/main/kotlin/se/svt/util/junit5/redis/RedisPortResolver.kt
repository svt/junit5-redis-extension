package se.svt.util.junit5.redis

private val redisPortRegex = "([0-9]+)".toRegex()

fun findPortFromSystemProperty(): Int? {
    val uri = System.getProperty(REDIS_PORT_PROPERTY) ?: return null
    return redisPortRegex.matchEntire(uri)?.groups?.get(1)?.value?.toInt()
}
