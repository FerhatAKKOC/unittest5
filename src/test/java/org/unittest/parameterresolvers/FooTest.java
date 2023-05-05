package org.unittest.parameterresolvers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(FooParameterResolver.class)
public class FooTest {

    @Test
    public void testIt(Foo fooInstance) {
        // When the JUnit platform runs your unit test, it will get a Foo instance from FooParameterResolver and pass it to the testIt() method
        assertEquals("Ferat", fooInstance.getName());
    }
}
