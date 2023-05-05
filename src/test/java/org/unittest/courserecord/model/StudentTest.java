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
import static org.junit.jupiter.api.Assumptions.assumeTrue;
import static org.junit.jupiter.api.Assumptions.assumingThat;

import java.time.Duration;
import java.util.stream.Stream;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;

@DisplayName("Student test")
class StudentTest {

    /**
     * Assertions.*
     * assertEquals
     * assertEquals with message
     * assertNotEquals
     * assertTrue with lazily evulated message
     * assertFlase with boolean supplier
     * assertNull
     * assertNotNull
     * assertArrayEquals
     * assertSame
     */

    @Test
    @DisplayName("Every student must have id, name and surname")
//    @Tag("createStudent")
    @Tags({ @Tag("addCourse"),  // Birden fazla tag'de bu şekilde eklenebilir
            @Tag("addLecture1"),
            @Tag("addLecture2"),
            @Tag("addLecture3") })
    void shouldCreateStudentWithIdNameAndSurname() {
        Student ferat = new Student("1", "ferat", "Akkoç");

        assertEquals("ferat", ferat.getName());
        assertEquals("ferat", ferat.getName(), "Student's name");
        assertNotEquals("feratt", ferat.getName(), "Student's name");

        assertTrue(ferat.getName().startsWith("f"));
        assertTrue(ferat.getName().startsWith("f"), "Student name");
        assertTrue(ferat.getName().startsWith("f"), () -> "Students Name"); // lambda with lazily implemented
        // supplier
        assertFalse(() -> {
            Student mehmet = new Student("id1", "Mehmet", "Can");
            return mehmet.getName().endsWith("M");
        }, () -> "Student's name " + "end with M");

        final Student ahmet = new Student("2", "ahmet", "Yılmaz");

        assertArrayEquals(new String[] { "ferat", "ahmet" }, Stream.of(ferat, ahmet).map(Student::getName).toArray());

        Student student = ferat;

        assertSame(student, ferat);
        assertNotSame(student, ahmet);
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

        assertAll("Student name check",
                () -> assertEquals("Ferat", ferat.getName()),
                () -> assertEquals("Ferat", ferat.getName()),
                () -> assertNotEquals("Feratt", ferat.getName()));

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

    @Test
    @DisplayName("Add course to a student less than 10ms")
    @Tag("addCourse")
    void addCourseToStudentWithTimeConstraints() {

        /**
         * timeoutExceeded
         * timeoutExceededWithPreemptiveTermination
         * timeoutNotExceeded
         * timeoutNotExceededWithResult
         * timeoutNotExceededWithMethod
         */

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

    @Test
    @DisplayName("creation at only development machine")
    @Tag("createStudent")
    void shouldCreateStudentWithNameAndSurnameArDevelopmentMachine() {

        // System.setProperty("ENV", "dev");
        assumeTrue(System.getProperty("ENV") != null, "Aborting Test : System Property ENV doesn't exist!");
        assumeTrue(System.getProperty("ENV").equals("dev"), "Aborting Test : Not a developer machine");

        final Student ferat = new Student("1", "Ferat", "Akkoc");
        assertAll("Student Information",
                () -> assertEquals("Ferat", ferat.getName()),
                () -> assertEquals("Akkoc", ferat.getSurname()),
                () -> assertEquals("1", ferat.getId()));
    }

    @Test
    @DisplayName("creation at different environment")
    @Tag("createStudent")
    void shouldCreateStudentWithNameAndSurnameWithSpecificEnvironment() {

        final Student ferat = new Student("1", "Ferat", "Akkoc");
        final String env = System.getProperty("ENV");

        // ifade de yer alan assertion, ilk baştaki assumption'a baglıdır.
        assumingThat(env != null && env.equals("dev"), () -> assertEquals("1", ferat.getName()));

        assertAll("Student Information",
                () -> assertEquals("Ferat", ferat.getName()),
                () -> assertEquals("Akkoc", ferat.getSurname()),
                () -> assertEquals("1", ferat.getId()));
    }

    @Test
    @DisplayName("Test that student must have only number id")
    @Disabled("No more valid Scenario") // method ve class bazında kullanılabilir.
    @Tag("createStudent")
    void shouldCreateStudentWithNumberId() {
        assertThrows(IllegalArgumentException.class, () -> new Student("id", "Ferat", "Akkoc"));
    }
}
