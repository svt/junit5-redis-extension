// SPDX-FileCopyrightText: 2018 Sveriges Television AB
//
// SPDX-License-Identifier: Apache-2.0

package se.svt.oss.junit5.redis

import java.nio.file.Paths
import kotlin.io.path.exists

fun findRedisPathFromEnv(): String? {
    val path = System.getenv(REDIS_SERVER_PROPERTY) ?: return null
    return when (Paths.get(path).exists()) {
        true -> path
        else -> null
    }
}
