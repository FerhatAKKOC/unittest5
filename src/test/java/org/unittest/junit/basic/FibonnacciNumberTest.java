package org.unittest.junit.basic;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class FibonnacciNumberTest {

    @Test
    @DisplayName("Find Fibonnacci number for Specific order")
    void findFibonacciNumber() {
        FibonnacciNumber fibonnacciNumber = new FibonnacciNumber();
        // 1 1 2 3 5 8
        assertThrows(IllegalArgumentException.class, () -> fibonnacciNumber.find(0));

        assertAll("Fibonacci Numbers",
                () -> assertEquals(1, fibonnacciNumber.find(1)),
                () -> assertEquals(1, fibonnacciNumber.find(2)),
                () -> assertEquals(2, fibonnacciNumber.find(3)),
                () -> assertEquals(3, fibonnacciNumber.find(4)),
                () -> assertEquals(5, fibonnacciNumber.find(5)),
                () -> assertEquals(8, fibonnacciNumber.find(6))
        );
    }
}
