// SPDX-FileCopyrightText: 2018 Sveriges Television AB
//
// SPDX-License-Identifier: Apache-2.0

package se.svt.oss.junit5.redis

import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import me.alexpanov.net.FreePortFinder
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtensionContext
import redis.clients.jedis.Jedis

class EmbeddedRedisExtensionTest {

    @Nested
    inner class BeforeAllReusePort {

        private val store = mockk<ExtensionContext.Store>()

        private val extensionContext = mockk<ExtensionContext>()

        private val redisSlot = slot<EmbeddedRedisExtension.RedisWrapper>()

        private val embeddedRedisExtension = EmbeddedRedisExtension(true)

        @BeforeEach
        fun setUp() {
            every { extensionContext.getStore(any()) } returns store
            every { store.put("redis", capture(redisSlot)) } just Runs
        }

        @AfterEach
        fun tearDown() {
            verify { store.put("redis", any<EmbeddedRedisExtension.RedisWrapper>()) }
            redisSlot.captured.close()
        }

        @Test
        fun `Redis port is set in system property`() {
            embeddedRedisExtension.beforeAll(extensionContext)
            assertRedisPortSet()
        }

        @Test
        fun `Port from system property is used if exists`() {
            val port = FreePortFinder.findFreeLocalPort()
            System.setProperty(REDIS_PORT_PROPERTY, port.toString())
            embeddedRedisExtension.beforeAll(extensionContext)

            val actualPort = assertRedisPortSet()

            assertThat(actualPort)
                .isEqualTo(port)
        }

        @Test
        fun `Redis instance is started`() {
            embeddedRedisExtension.beforeAll(extensionContext)
            val port = assertRedisPortSet()
            pingRedis(port)
        }
    }

    @Nested
    inner class BeforeAll {

        private val store = mockk<ExtensionContext.Store>()

        private val extensionContext = mockk<ExtensionContext>()

        private val redisSlot = slot<EmbeddedRedisExtension.RedisWrapper>()

        private val embeddedRedisExtension = EmbeddedRedisExtension()

        @BeforeEach
        fun setUp() {
            every { extensionContext.getStore(any()) } returns store
            every { store.put("redis", capture(redisSlot)) } just Runs
        }

        @AfterEach
        fun tearDown() {
            verify { store.put("redis", any<EmbeddedRedisExtension.RedisWrapper>()) }
            redisSlot.captured.close()
        }

        @Test
        fun `Port from system property is ignored`() {
            val port = FreePortFinder.findFreeLocalPort()
            System.setProperty(REDIS_PORT_PROPERTY, "$port")
            embeddedRedisExtension.beforeAll(extensionContext)

            val redisPort = assertRedisPortSet()

            assertThat(redisPort)
                .isNotEqualTo(port)
        }

        @Test
        fun `Redis instance is started`() {
            embeddedRedisExtension.beforeAll(extensionContext)
            val port = assertRedisPortSet()
            pingRedis(port)
        }
    }

    private fun pingRedis(port: Int) {
        Jedis("localhost", port).use { jedis ->
            jedis.ping()
        }
    }

    private fun assertRedisPortSet(): Int {
        val redisPortStr = System.getProperty(REDIS_PORT_PROPERTY)

        assertThat(redisPortStr)
            .isNotNull()
            .matches("[0-9]+")

        return redisPortStr.toInt()
    }
}
