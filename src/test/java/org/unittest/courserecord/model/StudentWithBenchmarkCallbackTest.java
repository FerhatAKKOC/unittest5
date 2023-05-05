package org.unittest.courserecord.model;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTimeout;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeFalse;
import static org.junit.jupiter.api.Assumptions.assumeTrue;
import static org.junit.jupiter.api.Assumptions.assumingThat;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import java.time.Duration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@Tag("Student")
@DisplayName("Student test")
class StudentWithBenchmarkCallbackTest {

    @ExtendWith(BenchmarkCallbackExtension.class)
    @Nested
    @DisplayName("Add Course to Student")
    @Tag("addCourse")
    class AddCourse {
        @Test
        @DisplayName("Add course to a student less than 10ms")
        @Tag("addCourse")
        void addCourseToStudentWithTimeConstraints() {

            assertTimeout(Duration.ofMillis(10), () -> {
                /* Nothing to do */ });

            final String result = assertTimeout(Duration.ofMillis(10), () -> {
                return "some string result";
            });
            assertEquals("some string result", result);

            final Student student = new Student("1", "Ahmet", "Can");
            LecturerCourseRecord lecturerCourseRecord = new LecturerCourseRecord(new Course("Math"), new Semester());
            assertTimeout(Duration.ofMillis(6), () -> student.addCourse(lecturerCourseRecord));

            /* Timeout olursa kodun executed edilmesini beklemiyor, orada bitiyor. */
            assertTimeoutPreemptively(Duration.ofMillis(6), () -> student.addCourse(lecturerCourseRecord));
        }

        @Nested
        @DisplayName("Add Course to Student (Exceptional)")
        class AddCourseToStudentExceptionaScenario {

            @Test
            @DisplayName("Get an exception when add a null lecturer course record to student")
            @Tag("addCourse")
            void throwsExceptionWhenAddtoNullCourseToStudent() {

                final Student ferat = new Student("1", "Ferat", "Akkoc");
                assertThrows(IllegalArgumentException.class, () -> ferat.addCourse(null));
                assertThrows(IllegalArgumentException.class, () -> ferat.addCourse(null), "Throws IllegalArgumentException");

                final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> ferat.addCourse(null));
                assertEquals("Can't add course with null lecturer course record", exception.getMessage());

            }

        }
    }

}
