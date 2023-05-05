package org.unittest.parameterresolvers;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.unittest.courserecord.model.Student;
import org.unittest.interfaces_default.CreateDomain;
import org.unittest.interfaces_default.TestLifeCycleReporter;

public class StudentTestWithDefaulyMethods
        implements CreateDomain<Student>, TestLifeCycleReporter {
    @Override
    public Student createDomain() {
        return new Student("1", "Ferat", "Akkoc");
    }

    @Test
    void createStudent() {

        final Student student = createDomain();

        assertAll("Student",
                () -> assertEquals("1", student.getId()),
                () -> assertEquals("Ferat", student.getName()),
                () -> assertEquals("Akkoc", student.getSurname()));
    }
}
