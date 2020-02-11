package se.svt.util.junit5.redis

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class RedisPortResolverTest {

    @Nested
    inner class FindPortFromSystemProperty {

        @AfterEach
        fun tearDown() {
            System.clearProperty(REDIS_URI_PROPERTY)
        }

        @Test
        fun `returns null if system property not set`() {
            assertThat(findPortFromSystemProperty()).isNull()
        }

        @Test
        fun `returns null if system property is invalid ie not redis uri to localhost`() {
            System.setProperty(REDIS_URI_PROPERTY, "redis://some-server:1234")
            assertThat(findPortFromSystemProperty()).isNull()
        }

        @Test
        fun `returns port from system property if valid redis uri to localhost`() {
            val port = 1234
            System.setProperty(REDIS_URI_PROPERTY, "redis://localhost:$port")
            assertThat(findPortFromSystemProperty()).isEqualTo(port)
        }
    }
}
