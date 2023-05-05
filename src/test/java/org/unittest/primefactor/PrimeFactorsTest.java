package org.unittest.primefactor;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.RepetitionInfo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PrimeFactorsTest {

    private Map<Integer, List<Integer>> primeFactorExpectation = new HashMap<>();

    @BeforeAll
    void beforeAll() {
        primeFactorExpectation.put(1, Collections.emptyList());
        primeFactorExpectation.put(2, List.of(2));
        primeFactorExpectation.put(3, List.of(3));
        primeFactorExpectation.put(4, List.of(2, 2));
        primeFactorExpectation.put(5, List.of(5));
        primeFactorExpectation.put(6, List.of(2, 3));
        primeFactorExpectation.put(7, List.of(7));
        primeFactorExpectation.put(8, List.of(2, 2, 2));
        primeFactorExpectation.put(9, List.of(3, 3));
    }

    @Test
    @DisplayName("Generate PrimeFactors")
    void generateWithStandardTests() {

        assertAll("prime factors",
                () -> assertEquals(Collections.EMPTY_LIST, PrimeFactors.generate(1)),
                () -> assertEquals(List.of(2), PrimeFactors.generate(2)),
                () -> assertEquals(List.of(3), PrimeFactors.generate(3)),
                () -> assertEquals(List.of(2, 2), PrimeFactors.generate(4)),
                () -> assertEquals(List.of(5), PrimeFactors.generate(5)),
                () -> assertEquals(List.of(2, 3), PrimeFactors.generate(6)),
                () -> assertEquals(List.of(7), PrimeFactors.generate(7)),
                () -> assertEquals(List.of(2, 2, 2), PrimeFactors.generate(8)),
                () -> assertEquals(List.of(3, 3), PrimeFactors.generate(9)));
    }

    @RepeatedTest(9)
    void generateWithRepeatedTest(RepetitionInfo repetitionInfo) {
        int i = repetitionInfo.getCurrentRepetition();
        assertEquals(primeFactorExpectation.get(i), PrimeFactors.generate(i));
    }

    @ParameterizedTest(name = "Generate Prime Factors for {arguments}")
    @ValueSource(ints = { 1, 2, 3, 4, 5, 6, 7, 8, 9 })
    void generateWithParameterizedTest(Integer i) {
        assertEquals(primeFactorExpectation.get(i), PrimeFactors.generate(i));
    }

    @TestFactory
    Stream<DynamicTest> generateWithDynamicTest() {
        return Stream.of(1, 2, 3, 4, 5, 6, 7, 8, 9)
                .map(number -> DynamicTest.dynamicTest("Generate prime factors for " + number,
                        () -> assertEquals(primeFactorExpectation.get(number), PrimeFactors.generate(number))));
    }

}
