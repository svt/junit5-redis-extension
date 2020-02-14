package se.svt.util.junit5.redis

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class RedisPortResolverTest {

    @BeforeEach
    fun setUp() {
        System.clearProperty(REDIS_PORT_PROPERTY)
    }

    @AfterEach
    fun tearDown() {
        System.clearProperty(REDIS_PORT_PROPERTY)
    }

    @Test
    fun `returns null if system property not set`() {
        assertThat(findPortFromSystemProperty()).isNull()
    }

    @Test
    fun `returns null if system property is invalid ie not redis uri to localhost`() {
        System.setProperty(REDIS_PORT_PROPERTY, "abd123")
        assertThat(findPortFromSystemProperty()).isNull()
    }

    @Test
    fun `returns port from system property if valid port set`() {
        val port = 1234
        System.setProperty(REDIS_PORT_PROPERTY, port.toString())
        assertThat(findPortFromSystemProperty()).isEqualTo(port)
    }
}
