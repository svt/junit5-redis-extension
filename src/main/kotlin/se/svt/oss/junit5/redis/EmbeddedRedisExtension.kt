// SPDX-FileCopyrightText: 2018 Sveriges Television AB
//
// SPDX-License-Identifier: Apache-2.0

package se.svt.oss.junit5.redis

import me.alexpanov.net.FreePortFinder
import org.junit.jupiter.api.extension.BeforeAllCallback
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.api.extension.ExtensionContext.Namespace
import redis.embedded.RedisServer

const val REDIS_URI_PROPERTY = "redis.uri"
const val REDIS_PORT_PROPERTY = "embedded-redis.port"

class EmbeddedRedisExtension(private val reusePort: Boolean = false) : BeforeAllCallback {

    lateinit var redisServer: RedisServer

    override fun beforeAll(context: ExtensionContext) {
        val port =
            if (!reusePort) FreePortFinder.findFreeLocalPort()
            else findPortFromSystemProperty() ?: FreePortFinder.findFreeLocalPort()
        System.setProperty(REDIS_PORT_PROPERTY, port.toString())
        redisServer = RedisServer(port)
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

    class RedisWrapper(private val redis: RedisServer) : ExtensionContext.Store.CloseableResource {
        override fun close() {
            if (redis.isActive) {
                redis.stop()
            }
        }
    }
}
