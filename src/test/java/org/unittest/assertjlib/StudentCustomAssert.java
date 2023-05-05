package org.unittest.assertjlib;

import java.util.Objects;

import org.assertj.core.api.AbstractAssert;
import org.unittest.courserecord.model.Student;

// Custom Assertion
public class StudentCustomAssert extends AbstractAssert<StudentCustomAssert, Student> {
    protected StudentCustomAssert(final Student student) {
        super(student, StudentCustomAssert.class);
    }

    public static StudentCustomAssert assertThat(Student actual) {
        return new StudentCustomAssert(actual);
    }

    public StudentCustomAssert hasName(String name) {
        isNotNull();

        if (!Objects.equals(name, actual.getName())) {
            failWithMessage("Expected student's name % but was found %s", name, actual.getName());
        }
        return this;
    }
}
