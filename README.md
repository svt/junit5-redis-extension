![GitHub tag (latest SemVer)](https://img.shields.io/github/v/tag/svt/junit5-redis-extension)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[![REUSE status](https://api.reuse.software/badge/github.com/svt/junit5-redis-extension)](https://api.reuse.software/info/github.com/svt/junit5-redis-extension)

# JUnit5 Redis Extension

**Description**:  A JUnit5 extension to set up embedded Redis for tests.

  - **Technology stack**: Kotlin, Junit 5, Embedded Redis, JVM11++ (since version 3.x.x)
  - **Status**:  Mature
  
## Dependencies

- Junit 5 
- Embedded Redis - https://github.com/signalapp/embedded-redis
- FreePortFinder - https://github.com/alexpanov/freeportfinder

## Usage

Add to build.gradle.kts
```
testImplementation("se.svt.oss:junit5-redis-extension:3.Y.Z")
```

A standard junit5 extension that will start an embedded Redis server on a random port before all tests in a class
 is run and shut it down after all tests are run. 
System property `embedded-redis.port` will be set to hold the port used by the Redis server.

Example on usage in test
```


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

```./gradlew clean test```

----
## Configuration

By default, the package uses Redis 6.0.5, as provided by the [embedded-redis package](https://github.com/signalapp/embedded-redis).
If you wish to use another Redis version, you can do so by setting the environment variable ``REDIS_SERVER``
to the path to your Redis-server executable, which the package will then use instead, by providing the path to the `RedisServer` constructor.

## License

Copyright 2018 Sveriges Television AB

This software is released under the Apache 2.0 License.  
Properties and various configuration files are released under CC0 1.0 Universal (Public Domain).

## Primary Maintainers

Gustav Grusell https://github.com/grusell

----
