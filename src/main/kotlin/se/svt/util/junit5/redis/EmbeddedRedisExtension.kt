// SPDX-FileCopyrightText: 2018 Sveriges Television AB
//
// SPDX-License-Identifier: Apache-2.0

package se.svt.util.junit5.redis

import me.alexpanov.net.FreePortFinder
import org.junit.jupiter.api.extension.AfterAllCallback
import org.junit.jupiter.api.extension.BeforeAllCallback
import org.junit.jupiter.api.extension.ExtensionContext
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
    }

    override fun afterAll(context: ExtensionContext?) {
        redisServer.stop()
    }
}
