package org.unittest.courserecord.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@DisplayNameGeneration(DisplayNameGenerator.Simple.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class StudentTestWithTestLifeCycle {

    private Student ferat  = new Student("1","Ferat","Akkoc");

    @BeforeAll
    void setUp() {

    }

    @BeforeEach
    void setUp2() {

    }

    @Test
    void stateCannotChangeWhenLifeCycleIsPerMethod() {
        assertEquals("Ferat",ferat.getName());
        ferat =  new Student("id2","Mehmet","Akkoc");
    }

    @Test
    void stateCanChangeWhenLifeCycleIsPerClass() {
        assertEquals("Ferat",ferat.getName());
        ferat =  new Student("id2","Mehmet","Akkoc");
    }
}
