# JUnit5 Redis Extension

**Description**:  A JUnit5 extension to setup embedded Redis for tests.

  - **Technology stack**: Kotlin, Spring 5, Embedded Redis
  - **Status**:  1.0
  
## Dependencies

Spring 5
Embedded Redis - https://github.com/ozimov/embedded-redis

## Usage

Add to build.gradle
```
testImplementation 'se.svt.oss.junit5:junit5-redis-extension:X.Y.Z
```

A standard junit5 extension that will start an embedded Redis server on test before all tests in a class is run and shut it down after all tests run.

Example on usage in test
```


@ExtendWith(
    value = [
        SpringExtension::class,
        EmbeddedRedisExtension::class]
)
@ActiveProfiles("test")
@SpringBootTest
@ContextConfiguration(initializers = [(RandomPortInitializer::class)])
class SomeIntegrationTest {

    @Value("\${random-port.port1}")
    var myRandomPort: Int = 0

    @Test
    fun blabla....
}
```


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

## Primary Maintainers

Gustav Grusell https://github.com/grusell

----
