package se.svt.util.junit5.redis

import me.alexpanov.net.FreePortFinder
import org.junit.jupiter.api.extension.AfterAllCallback
import org.junit.jupiter.api.extension.BeforeAllCallback
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.api.extension.ExtensionContext.Namespace
import redis.embedded.RedisServer

const val REDIS_URI_PROPERTY = "redis.uri"

class EmbeddedRedisExtension(private val forceRandomPort: Boolean = false) : BeforeAllCallback, AfterAllCallback {

    lateinit var redisServer: RedisServer

    override fun beforeAll(context: ExtensionContext?) {
        val port =
                if (forceRandomPort) FreePortFinder.findFreeLocalPort()
                else findPortFromSystemProperty() ?: FreePortFinder.findFreeLocalPort()
        System.setProperty(REDIS_URI_PROPERTY, "redis://localhost:$port")
        redisServer = RedisServer(port)
        redisServer.start()

        if (context != null) {
            val wrapper = RedisWrapper(redisServer)
            context.getStore(Namespace.create(EmbeddedRedisExtension::class.java))
                    .put("redis", wrapper)
            Runtime.getRuntime().addShutdownHook(Thread(Runnable {
                wrapper.close()
            }))
        }
    }

    override fun afterAll(context: ExtensionContext?) {
        redisServer.stop()
    }

    class RedisWrapper(private val redis: RedisServer) : ExtensionContext.Store.CloseableResource {
        override fun close() {
            if (redis.isActive) {
                redis.stop()
            }
        }
    }
}
