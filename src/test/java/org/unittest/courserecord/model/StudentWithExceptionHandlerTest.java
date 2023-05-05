package org.unittest.courserecord.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@Tag("Student")
@DisplayName("Student test")
class StudentWithExceptionHandlerTest {

    @ExtendWith(IllegalArgumentExceptionHandlerExtension.class)
    @Nested
    @DisplayName("Add Course to Student (Exceptional)")
    @Tag("exceptional")
    class AddCourseToStudentExceptionaScenario {

        @Test
        @DisplayName("Get an exception when add a null lecturer course record to student")
        @Tag("addCourse")
        void throwsExceptionWhenAddtoNullCourseToStudent() {
            final Student ferat = new Student("1", "Ferat", "Akkoc");
            ferat.addCourse(null);
        }
    }
}
