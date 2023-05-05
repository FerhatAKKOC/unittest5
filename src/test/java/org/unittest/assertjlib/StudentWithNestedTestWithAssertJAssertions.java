package org.unittest.assertjlib;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.stream.Stream;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;
import org.unittest.courserecord.model.Student;

//@ExtendWith(TestLoggerExtention.class)  // field olarakta tanımlanabilir.
//@ExtendWith(DropCourseConditionExtension.class)
@Tag("Student")
@DisplayName("Student test")
class StudentWithNestedTestWithAssertJAssertions {

    // @RegisterExtension
    // static TestLoggerExtention testLoggerExtention = new TestLoggerExtention();

    @Nested
    @DisplayName("Create Student")
    @Tag("createStudent")
    class CreateStudent {

        @Test
        @DisplayName("Every student must have id, name and surname")
        // @Tag("createStudent")
        @Tags({ @Tag("addCourse"), // Birden fazla tag'de bu şekilde eklenebilir
                @Tag("addLecture1"),
                @Tag("addLecture2"),
                @Tag("addLecture3") })
        void shouldCreateStudentWithIdNameAndSurname() {
            Student ferat = new Student("1", "ferat", "Akkoç");

            assertThat(ferat.getName()).as("Ferat")
                    .isEqualTo("ferat")
                    .isNotEqualTo("ferattt")
                    .startsWith("f");

            // assertEquals("ferat", ferat.getName());
            // assertEquals("ferat", ferat.getName(), "Student's name");
            // assertNotEquals("feratt", ferat.getName(), "Student's name");
            // assertTrue(ferat.getName().startsWith("f"));
            // assertTrue(ferat.getName().startsWith("f"), "Student name");
            // assertTrue(ferat.getName().startsWith("f"), () -> "Students Name"); // lambda with lazily implemented

            // supplier
            assertThat(new Student("id1", "Mehmet", "Can").getName()).as("Mehmet")
                    .doesNotEndWith("M")
                    .doesNotEndWithIgnoringCase("m");
            // assertFalse(() -> {
            // Student mehmet = new Student("id1", "Mehmet", "Can");
            // return mehmet.getName().endsWith("M");
            // }, () -> "Student's name " + "end with M");

            final Student ahmet = new Student("2", "ahmet", "Yılmaz");

            assertThat(List.of(ferat, ahmet))
                    .extracting(Student::getName)
                    .containsOnly("ferat", "ahmet");
            // assertArrayEquals(new String[] { "ferat", "ahmet" }, Stream.of(ferat, ahmet).map(Student::getName).toArray());

            Student student = ferat;
            assertThat(ferat).as("ferat")
                    .isSameAs(student)
                    .isNotSameAs(ahmet)
                    .extracting(Student::getName)
                    .asString()
                    .isEqualTo("ferat")
                    .isNotEqualTo("feratttt")
                    .startsWithIgnoringCase("F");
            // assertSame(student, ferat);
            // assertNotSame(student, ahmet);
        }

        @Test
        @DisplayName("Every student must have id, name and surname with grouped assertions")
        @Tag("createStudent")
        void shouldCreateStudentWithIdNameAndSurnameWithGroupedAssertions() {

            /**
             * grouped assertions
             * failed grouped assertions
             * dependent assertions
             */

            Student ferat = new Student("1", "Ferat", "Akkoc");

            SoftAssertions softAssertions = new SoftAssertions();
            softAssertions.assertThat(ferat.getName()).as("Ferat's name").isEqualTo("Ferat");
            softAssertions.assertThat(ferat.getName()).as("Ferat's name").isEqualTo("Ferat");
            softAssertions.assertThat(ferat.getName()).as("Ferat's name").isNotEqualTo("Ferattt");
            softAssertions.assertAll();
            // assertAll("Student name check",
            // () -> assertEquals("Ferat", ferat.getName()),
            // () -> assertEquals("Ferat", ferat.getName()),
            // () -> assertNotEquals("Feratt", ferat.getName()));

            assertAll("Student name character check",
                    () -> assertTrue(ferat.getName().startsWith("F")),
                    () -> assertTrue(ferat.getName().startsWith("Fe"), () -> "Student name start with " + "Fe"),
                    () -> assertFalse(() -> {
                        Student mehmet = new Student("id1", "Mehmet", "Cam");
                        return mehmet.getName().endsWith("M");
                    }, () -> "Student's name " + "end with M"));

            assertAll(
                    () -> {
                        final Student ahmet = new Student("2", "Ahmet", "Yilmaz");
                        assertArrayEquals(new String[] { "Ferat", "Ahmet" }, Stream.of(ferat, ahmet).map(Student::getName).toArray());
                    },
                    () -> {
                        Student student = ferat;
                        final Student ahmet = new Student("2", "Ahmet", "Yilmaz");
                        assertSame(student, ferat);
                        assertNotSame(student, ahmet);
                    });
        }
    }

    @Nested
    @DisplayName("Add Course to Student")
    @Tag("addCourse")
    class AddCourse {

        @Nested
        @DisplayName("Add Course to Student (Exceptional)")
        class AddCourseToStudentExceptionaScenario {

            @Test
            @DisplayName("Get an exception when add a null lecturer course record to student")
            @Tag("addCourse")
            void throwsExceptionWhenAddtoNullCourseToStudent() {

                final Student ferat = new Student("1", "Ferat", "Akkoc");

                assertThatIllegalArgumentException().as("Throws IllegalArgumentException")
                        .isThrownBy(() -> ferat.addCourse(null));
                // assertThrows(IllegalArgumentException.class, () -> ferat.addCourse(null));
                // assertThrows(IllegalArgumentException.class, () -> ferat.addCourse(null), "Throws IllegalArgumentException");

                Throwable throwable = catchThrowable(() -> ferat.addCourse(null));
                assertThat(throwable)
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("Can't add course with null lecturer course record");
                // final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> ferat.addCourse(null));
                // assertEquals("Can't add course with null lecturer course record", exception.getMessage());
            }
        }
    }
}
