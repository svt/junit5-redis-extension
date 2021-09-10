// SPDX-FileCopyrightText: 2018 Sveriges Television AB
//
// SPDX-License-Identifier: Apache-2.0

package se.svt.oss.junit5.redis

import me.alexpanov.net.FreePortFinder
import org.junit.jupiter.api.extension.BeforeAllCallback
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.api.extension.ExtensionContext.Namespace
import redis.embedded.RedisServer
import java.io.File

const val REDIS_URI_PROPERTY = "redis.uri"
const val REDIS_PORT_PROPERTY = "embedded-redis.port"
const val REDIS_SERVER_PROPERTY = "REDIS_SERVER"

class EmbeddedRedisExtension(private val reusePort: Boolean = false) : BeforeAllCallback {
    override fun beforeAll(context: ExtensionContext) {
        val port = port()

        System.setProperty(REDIS_PORT_PROPERTY, port.toString())

        val redisServer = redisServer(port)

        redisServer.start()

        val wrapper = RedisWrapper(redisServer)

        context.getStore(Namespace.create(EmbeddedRedisExtension::class.java))
            .put("redis", wrapper)
        Runtime.getRuntime().addShutdownHook(
            Thread(
                Runnable {
                    wrapper.close()
                }
            )
        )
    }

    private fun port() = if (!reusePort) FreePortFinder.findFreeLocalPort()
    else findPortFromSystemProperty() ?: FreePortFinder.findFreeLocalPort()

    private fun redisServer(port: Int): RedisServer {
        return when (val redisPath = findRedisPathFromSystemProperty()) {
            null -> RedisServer(port)
            else -> RedisServer(File(redisPath), port)
        }
    }

    class RedisWrapper(private val redis: RedisServer) : ExtensionContext.Store.CloseableResource {
        override fun close() {
            if (redis.isActive) {
                redis.stop()
            }
        }
    }
}
