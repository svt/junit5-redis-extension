package se.svt.util.junit5.redis

import me.alexpanov.net.FreePortFinder
import org.junit.jupiter.api.extension.AfterAllCallback
import org.junit.jupiter.api.extension.BeforeAllCallback
import org.junit.jupiter.api.extension.ExtensionContext
import redis.embedded.RedisServer

class EmbeddedRedisExtension : BeforeAllCallback, AfterAllCallback {
    lateinit var redisServer: RedisServer

    override fun beforeAll(context: ExtensionContext?) {
        val port = FreePortFinder.findFreeLocalPort()
        System.setProperty("redis.uri", "redis://localhost:$port")
        redisServer = RedisServer(port)
        redisServer.start()
    }

    override fun afterAll(context: ExtensionContext?) {
        redisServer.stop()
    }
}