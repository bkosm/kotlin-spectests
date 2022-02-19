package io.github.`bart-kosmala`.`spec-tests`

import org.junit.jupiter.api.DynamicNode
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.DynamicTest.dynamicTest
import java.util.concurrent.CopyOnWriteArrayList

class SpecScope {
    private val testSpecs = CopyOnWriteArrayList<DynamicTest>()

    fun it(testName: String, testBody: () -> Unit) {
        testSpecs.add(dynamicTest(testName) { testBody() })
    }

    companion object {
        fun describedBy(block: SpecScope.() -> Unit): Collection<DynamicNode> =
            SpecScope().run {
                block()
                testSpecs
            }
    }
}
