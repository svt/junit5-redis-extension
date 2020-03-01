# JUnit5 Redis Extension

**Description**:  A JUnit5 extension to setup embedded Redis for tests.

  - **Technology stack**: Kotlin, Junit 5, Embedded Redis
  - **Status**:  1.0
  
## Dependencies

Junit 5
Embedded Redis - https://github.com/ozimov/embedded-redis
FreePortFinder - https://github.com/alexpanov/freeportfinder

## Usage

Add to build.gradle
```
testImplementation 'se.svt.oss.junit5:junit5-redis-extension:X.Y.Z
```

A standard junit5 extension that will start an embedded Redis server on a random port before all tests in a class
 is run and shut it down after all tests are run. System property `embedded-redis.port` will be set to hold the port
  used byt
  the redis server.

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

If running multiple tests within the same JVM, a new redis instance using a new random port will be created for each
 test
 instance. To enforce use of the same port for each test instance, 
 the extension can be registered programatically with the `reusePort`
 constructor parameter set to true.

## How to test the software

```./gradlew test```

----

## Update version and release to jcenter/bintray (for maintainers)

1. Make sure you are on master branch and that everything is pushed to master
2. ./gradlew release to tag a new version (uses Axion release plugin - needs ssh key for repo)
3.  ./gradlew bintrayUpload to upload to repo - needs BINTRAY_KEY and BINTRAY_USER environment variables

## License

Copyright 2018 Sveriges Television AB

This software is released under the Apache 2.0 License.
Properties and various configuration files are released under CC0 1.0 Universal (Public Domain).

## Primary Maintainers

Gustav Grusell https://github.com/grusell

----
