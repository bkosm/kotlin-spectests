# kotlin-spectests

![Tests](https://github.com/bkosm/kotlin-spec-tests/actions/workflows/gradle.yml/badge.svg)
![Scheduled](https://github.com/bkosm/kotlin-spec-tests/actions/workflows/scheduled.yml/badge.svg)
![Jitpack](https://jitpack.io/v/bkosm/kotlin-spec-tests.svg)
---

A Kotlin library for creating dynamic tests more fluently.

## Why should I use it?

You can create readable jest-style assertions that show up as subtests.

```kotlin
//...
import io.github.bkosm.spectests.SpecScope.Companion.describedBy

//...
internal class ProviderTest {
    @TestFactory
    fun `provider keeps its state`() = describedBy {
        val uut = StatefulProvider()

        it("works first time") {
            assert(uut.provide() == Success)
        }

        it("throws on subsequent requests") {
            assertThrows<ProviderError> {
                uut.provide()
            }
        }
    }
}
```

---

Your parameterized tests won't look like hacks anymore.

```kotlin
//...
internal class ValidatorTest {
    @TestFactory
    fun `can parse various float formats`() = describedBy {
        val uut = CoolValidator()

        listOf(
            "0" to 0,
            "0,1" to 0.1,
            ",1" to 0.1
        ).forEach { (input, expected) ->
            it("can parse '$input'") {
                val result = uut.validate(input)
                assert(result.value == expected)
            }
        }
    }

    @TestFactory
    fun `throws validation errors on malformed input`() = describedBy {
        val uut = CoolValidator()

        listOf("0,", "0,0,", ",,1").forEach {
            it("throws ValidationError for '$it'")
            assertThrows<ValidationError> {
                uut.validate(it)
            }
        }
    }
}
```

---

It's no rocket science - just a `DynamicTest` wrapper that utilizes `@TestFactory` annotation. Depends only on JUnit 5
and so do all your Kotlin projects.

```kotlin
fun describedBy(block: SpecScope.() -> Unit): Collection<DynamicNode> =
    SpecScope().run {
        block()
        testSpecs
    }
```

---

## How can I check it out?

The releases are available via [Jitpack](https://jitpack.io/#bkosm/kotlin-spec-tests).

```kotlin
// build.gradle.kts

//...
repositories {
    mavenCentral()
    //...
    maven { url = uri("https://jitpack.io") }
}

dependencies {
    //...
    testImplementation("com.github.bkosm:kotlin-spec-tests:0.2.0")
}
//...
```

---

## Notable shortcomings

1. Only a single nested `it` subtest works. You can do more, and it will compile - but nested ones won't be executed.

---