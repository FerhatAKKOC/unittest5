package org.unittest.unittestnedir;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class FizzBuzzTest {

    private FizzBuzz fizzBuzz;

    @BeforeEach
    void setUp() {
        fizzBuzz = new FizzBuzz();
    }

    @Test
    void returnFizzWhenTheNumberDividedByThree() {
        assertEquals("Fizz", fizzBuzz.stringFor(3));
    }

    @Test
    void returnBuzzWhenNumberDividedByFive() {
        assertEquals("Buzz",fizzBuzz.stringFor(5));
    }

    @Test
    void returnFizzBuzzWhenTheNumberDividedByThreeAndFive() {
        assertEquals("FizzBuzz",fizzBuzz.stringFor(15));
    }

    @Test
    void returnTheNumberItselfWhenTheNumberIsnotDividedAnyOfThreeFive() {
        assertEquals("7",fizzBuzz.stringFor(7));
    }

    @Test
    void throwsIllegalExceptionWhenTheNumberIsOutOfScope() {
        assertThrows(IllegalArgumentException.class,() -> fizzBuzz.stringFor(101));
        assertThrows(IllegalArgumentException.class,() -> fizzBuzz.stringFor(-1));
    }
}