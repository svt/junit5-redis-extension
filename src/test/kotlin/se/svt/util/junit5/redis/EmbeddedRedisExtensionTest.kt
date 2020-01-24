package se.svt.util.junit5.redis

import me.alexpanov.net.FreePortFinder
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import redis.clients.jedis.Jedis
import java.net.URI

class EmbeddedRedisExtensionTest {

    @Nested
    inner class BeforeAll {

        private val embeddedRedisExtension = EmbeddedRedisExtension()

        @AfterEach
        fun tearDown() {
            embeddedRedisExtension.afterAll(null)
        }

        @Test
        fun `Redis uri is set in system property`() {
            embeddedRedisExtension.beforeAll(null)
            val redisUri = assertRedisUriSet()

            assertThat(redisUri)
                    .hasScheme("redis")
                    .hasHost("localhost")
        }

        @Test
        fun `Port from system property is used if exists`() {
            val port = FreePortFinder.findFreeLocalPort()
            System.setProperty(REDIS_URI_PROPERTY, "redis://localhost:$port")
            embeddedRedisExtension.beforeAll(null)

            val redisUri = assertRedisUriSet()

            assertThat(redisUri)
                    .hasScheme("redis")
                    .hasHost("localhost")
                    .hasPort(port)
        }

        @Test
        fun `Redis instance is started`() {
            embeddedRedisExtension.beforeAll(null)
            val redisUri = assertRedisUriSet()
            pingRedis(redisUri)
        }
    }

    @Nested
    inner class BeforeAllForceRandomPort {

        private val embeddedRedisExtension = EmbeddedRedisExtension(true)

        @AfterEach
        fun tearDown() {
            embeddedRedisExtension.afterAll(null)
        }

        @Test
        fun `Port from system property is ignored`() {
            val port = FreePortFinder.findFreeLocalPort()
            System.setProperty(REDIS_URI_PROPERTY, "redis://localhost:$port")
            embeddedRedisExtension.beforeAll(null)

            val redisUri = assertRedisUriSet()

            assertThat(redisUri)
                    .hasScheme("redis")
                    .hasHost("localhost")

            assertThat(redisUri.port).isNotEqualTo(port)
        }

        @Test
        fun `Redis instance is started`() {
            embeddedRedisExtension.beforeAll(null)
            val redisUri = assertRedisUriSet()
            pingRedis(redisUri)
        }
    }

    @Nested
    inner class AfterAll {

        private val embeddedRedisExtension = EmbeddedRedisExtension()

        @Test
        fun `Redis is shut down`() {
            embeddedRedisExtension.beforeAll(null)
            embeddedRedisExtension.afterAll(null)
            assertThat(embeddedRedisExtension.redisServer.isActive)
                    .isFalse()
        }
    }

    private fun pingRedis(redisUri: URI) {
        Jedis(redisUri.host, redisUri.port).use { jedis ->
            jedis.ping()
        }
    }

    private fun assertRedisUriSet(): URI {
        val redisUriStr = System.getProperty("redis.uri")

        assertThat(redisUriStr)
                .isNotNull()

        return URI.create(redisUriStr)
    }
}