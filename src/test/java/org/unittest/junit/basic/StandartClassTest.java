package org.unittest.junit.basic;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Random;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class StandartClassTest {

    private static String oneInstancePerClass;
    private Integer oneInstancePerMethod;

    @BeforeAll
    static void beforeAll() {
        oneInstancePerClass = String.valueOf(new Random().nextInt());
        System.out.println("beforeAll");
    }

    @AfterAll
    static void afterAll() {
        oneInstancePerClass = null;
        System.out.println("afterAll");
    }

    @BeforeEach
    void beforeEach() {
        oneInstancePerMethod = new Random().nextInt();
        System.out.println("\nbeforeEach");
    }

    @AfterEach
    void afterEach() {
        oneInstancePerMethod = null;
        System.out.println("afterEach\n");
    }

    @Test
    void testSomething1() {
        System.out.println("test-1");
    }
    @Test
    void testSomething2() {
        System.out.println("test-2");
    }

    @Test
    @Disabled
    void testSomething3() {
        System.out.println("test-3");
    }

    @Test
    void testSomethingFailed() {
        Assertions.fail("A failing test...");
    }

}