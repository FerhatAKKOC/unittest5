package org.unittest;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class HelloWorldTest {

    @Test
    void sayHelloWorldTest() {

        HelloWorld helloWorld = new HelloWorld();
        assertEquals("Hello World",helloWorld.sayHello(),"Say Hello to World");

    }

}