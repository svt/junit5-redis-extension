
# JUnit5 Redis Extension

![GitHub tag (latest SemVer)](https://img.shields.io/github/v/tag/svt/junit5-redis-extension)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[![REUSE status](https://api.reuse.software/badge/github.com/svt/junit5-redis-extension)](https://api.reuse.software/info/github.com/svt/junit5-redis-extension)

A JUnit5 extension to set up embedded Redis for tests.
  
## Dependencies

- Junit 5
- Embedded Redis - <https://github.com/signalapp/embedded-redis>
- FreePortFinder - <https://github.com/alexpanov/freeportfinder>

## Usage

Add to build.gradle.kts

```kotlin
testImplementation("se.svt.oss:junit5-redis-extension:3.Y.Z")
```

A standard junit5 extension that will start an embedded Redis server on a random port before all tests in a class
 is run and shut it down after all tests are run.
System property `embedded-redis.port` will be set to hold the port used by the Redis server.

Example on usage in test

```kotlin


@ExtendWith(EmbeddedRedisExtension::class)
class SomeIntegrationTest {

    @Test
    fun doSomeTest() {
       val redisUri = URI.create("redis://localhost:" + System.getProperty("embedded-redis.port"))
       // Do something against the redis instance
    }
}
```

If running multiple tests within the same JVM, a new Redis instance using a new random port will be created for each
 test instance.
To enforce use of the same port for each test instance, the extension can be registered programmatically with the `reusePort`
 constructor parameter set to true.

## How to test the software

```console
./gradlew clean test
```

----
## Configuration

By default, the package uses Redis 6.0.5, as provided by the [embedded-redis package](https://github.com/signalapp/embedded-redis).
If you wish to use another Redis version, you can do so by setting the environment variable ``REDIS_SERVER``
to the path to your Redis-server executable, which the package will then use instead, by providing the path to the `RedisServer` constructor.
t

## Getting help

If you have questions, concerns, bug reports, etc, please file an issue in this repository's Issue Tracker.

## Getting involved

General instructions for contributing [CONTRIBUTING](CONTRIBUTING.adoc).

## License

This software is released under the:

[Apache License 2.0](LICENSE)

Copyright 2018 Sveriges Television AB

Properties and various configuration files are released under CC0 1.0 Universal (Public Domain).

## Primary Maintainer

Gustav Grusell <https://github.com/grusell>

