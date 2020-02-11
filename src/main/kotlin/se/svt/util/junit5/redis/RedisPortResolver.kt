package se.svt.util.junit5.redis

private val redisUriRegex = "redis://localhost:([0-9]+)".toRegex()

fun findPortFromSystemProperty(): Int? {
    val uri = System.getProperty(REDIS_URI_PROPERTY) ?: return null
    return redisUriRegex.matchEntire(uri)?.groups?.get(1)?.value?.toInt()
}
