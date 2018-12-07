package se.svt.util.junit5.redis

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import redis.clients.jedis.Jedis
import java.net.URI

class EmbeddedRedisExtensionTest {

    private val embeddedRedisExtension = EmbeddedRedisExtension()

    @Nested
    inner class BeforeAll {

        @BeforeEach
        fun setUp() {
            embeddedRedisExtension.beforeAll(null)
        }

        @AfterEach
        fun tearDown() {
            embeddedRedisExtension.afterAll(null)
        }

        @Test
        fun `Redis uri is set in system property`() {
            val redisUri = assertRedisUriSet()

            assertThat(redisUri)
                    .hasScheme("redis")
                    .hasHost("localhost")
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

        @Test
        fun `Redis is shut down`() {
            embeddedRedisExtension.beforeAll(null)
            embeddedRedisExtension.afterAll(null)
            assertThat(embeddedRedisExtension.redisServer.isActive)
                    .isFalse()
        }
    }

    private fun pingRedis(redisUri: URI) {
        var jedis: Jedis? = null
        try {
            jedis = Jedis(redisUri.host, redisUri.port)
            jedis.ping()
        } finally {
            jedis?.close()
        }
    }

    private fun assertRedisUriSet(): URI {
        val redisUriStr = System.getProperty("redis.uri")

        assertThat(redisUriStr)
                .isNotNull()

        return URI.create(redisUriStr)
    }
}