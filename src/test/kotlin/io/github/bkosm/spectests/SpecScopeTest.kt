package io.github.bkosm.spectests

import io.github.bkosm.spectests.SpecScope.Companion.describedBy
import org.junit.jupiter.api.DynamicNode
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestFactory
import org.junit.jupiter.api.assertThrows

internal class SpecScopeTest {

    @TestFactory
    fun `spec works`() = describedBy {
        it("1") {
            assert(true)
        }

        it("2") {
            assert(true)
        }
    }

    @Test
    fun `top-level failing assertions cause initializationError`() {
        assertThrows<AssertionError> {
            describedBy {
                assert(false)
            }
        }
    }

    @Test
    fun `failing assertions contained in sub-tests are perceived as normal tests`() {
        val tests = describedBy {
            it("1") {
                assert(true)
            }
            it("2") {
                assert(false)
            }
        }

        tests.assertTestNames(contain = listOf("1", "2"))
    }

    @Test
    fun `depending on spec flow, tests can be omitted`() {
        val tests = describedBy {
            it("1") {
                assert(true)
            }

            if (false) {
                it("2") {
                    assert(true)
                }
            }
        }

        tests.assertTestNames(contain = listOf("1"), doNotContain = listOf("2"))
    }

    @Test
    fun `calling it in sub-test ignores declarations`() {
        val tests = describedBy {
            it("1") {
                assert(true)
                it("3") {
                    assertThrows<AssertionError> { // not executed
                        assert(true)
                    }
                }
            }

            it("2") {
                assert(false)
                it("4") {
                    assertThrows<AssertionError> { // not executed
                        assert(false)
                    }
                }
            }
        }

        tests.assertTestNames(contain = listOf("1", "2"), doNotContain = listOf("3", "4"))
    }

    @Test
    fun `flat iterative sub-test declarations work`() {
        val expectedTestNames = listOf("1", "2", "3")

        val tests = describedBy {
            expectedTestNames.forEach { name ->
                it(name) {
                    assert(false)
                }
            }
        }

        tests.assertTestNames(contain = expectedTestNames)
    }

    private fun Collection<DynamicNode>.assertTestNames(
        contain: List<String> = emptyList(),
        doNotContain: List<String> = emptyList(),
    ) {
        val testNames = this.map { it.displayName }

        contain.forEach {
            assert(testNames.contains(it)) { "actual tests do not contain expected '$it'" }
        }
        doNotContain.forEach {
            assert(!testNames.contains(it)) { "actual tests contain unexpected '$it'" }
        }
    }
}
