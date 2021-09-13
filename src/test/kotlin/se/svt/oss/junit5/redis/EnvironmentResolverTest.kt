// SPDX-FileCopyrightText: 2018 Sveriges Television AB
//
// SPDX-License-Identifier: Apache-2.0

package se.svt.oss.junit5.redis

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import uk.org.webcompere.systemstubs.environment.EnvironmentVariables
import uk.org.webcompere.systemstubs.jupiter.SystemStubsExtension

@ExtendWith(
    SystemStubsExtension::class
)
class EnvironmentResolverTest {

    @Test
    fun `returns null if env property not set`(env: EnvironmentVariables) {
        env.execute {
            env.set(REDIS_SERVER_PROPERTY, null)
            assertThat(findRedisPathFromEnv()).isNull()
        }
    }

    @Test
    fun `returns null if env server env path exists but is not a file`(env: EnvironmentVariables) {
        env.set(REDIS_SERVER_PROPERTY, "abd123").execute {
            assertThat(findRedisPathFromEnv()).isNull()
        }
    }

    @Test
    fun `returns path from redis server env if a valid file path set`(env: EnvironmentVariables) {
        val path = createTempFile().absolutePath
        env.set(REDIS_SERVER_PROPERTY, path).execute {
            assertThat(findRedisPathFromEnv()).isEqualTo(path)
        }
    }
}
