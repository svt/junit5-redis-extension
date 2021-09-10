// SPDX-FileCopyrightText: 2018 Sveriges Television AB
//
// SPDX-License-Identifier: Apache-2.0

package se.svt.oss.junit5.redis

import java.nio.file.Paths
import kotlin.io.path.exists

private val redisPortRegex = "([0-9]+)".toRegex()

fun findPortFromSystemProperty(): Int? {
    val uri = System.getProperty(REDIS_PORT_PROPERTY) ?: return null
    return redisPortRegex.matchEntire(uri)?.groups?.get(1)?.value?.toInt()
}

fun findRedisPathFromSystemProperty(): String? {
    val path = System.getProperty(REDIS_SERVER_PROPERTY) ?: return null
    return when (Paths.get(path).exists()) {
        true -> path
        else -> null
    }
}
