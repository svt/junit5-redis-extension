// SPDX-FileCopyrightText: 2018 Sveriges Television AB
//
// SPDX-License-Identifier: Apache-2.0

package se.svt.oss.junit5.redis

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import uk.org.webcompere.systemstubs.SystemStubs.restoreSystemProperties
import uk.org.webcompere.systemstubs.jupiter.SystemStubsExtension
import uk.org.webcompere.systemstubs.properties.SystemProperties

@ExtendWith(
    SystemStubsExtension::class
)
class PropertiesResolverTest {

    @Test
    fun `returns null if system property not set`(prop: SystemProperties) {
        prop.execute {
            System.clearProperty(REDIS_SERVER_PROPERTY)
            System.clearProperty(REDIS_PORT_PROPERTY)
            assertThat(findPortFromSystemProperty()).isNull()
            assertThat(findRedisPathFromSystemProperty()).isNull()
        }
    }

    @Test
    fun `returns null if system property is invalid ie not redis uri to localhost`(prop: SystemProperties) {
        restoreSystemProperties {
            prop.set(REDIS_PORT_PROPERTY, "abd123")
            assertThat(findPortFromSystemProperty()).isNull()
        }
    }

    @Test
    fun `returns port from system property if valid port set`(prop: SystemProperties) {
        val port = 1234
        prop.set(REDIS_PORT_PROPERTY, port.toString()).execute {
            assertThat(findPortFromSystemProperty()).isEqualTo(port)
        }
    }

    @Test
    fun `returns null if server path exists but is not a file`(prop: SystemProperties) {
        prop.set(REDIS_SERVER_PROPERTY, "abd123").execute {
            assertThat(findRedisPathFromSystemProperty()).isNull()
        }
    }

    @Test
    fun `returns path from redis server property if a valid file path set`(prop: SystemProperties) {
        val path = createTempFile().absolutePath
        prop.set(REDIS_SERVER_PROPERTY, path).execute {
            assertThat(findRedisPathFromSystemProperty()).isEqualTo(path)
        }
    }
}
