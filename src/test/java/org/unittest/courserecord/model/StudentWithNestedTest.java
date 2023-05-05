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

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;

//@ExtendWith(TestLoggerExtention.class)  // field olarakta tanımlanabilir.
@ExtendWith(DropCourseConditionExtension.class)
@Tag("Student")
@DisplayName("Student test")
class StudentWithNestedTest {

    @RegisterExtension
    static TestLoggerExtention testLoggerExtention = new TestLoggerExtention();

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

    @Nested
    @DisplayName("Add Course to Student")
    @Tag("addCourse")
    class AddCourse {
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

    @Nested
    @DisplayName("Drop Course from Student")
    @Tag("dropCourse")
    class DropCourseFromStudent {

        // throws illegal argument exception for null lecturer course record
        // throws illegal argument exception if the student did't register course before
        // throws not active semester exception if the semester is not active
        // throws not active semester exception if the add drop period is closed for the semester
        // drop course from student

        @TestFactory
        Stream<DynamicTest> dropCourseFromStudentFactory() {

            final Student studentAhmet = new Student("id1", "Ferat", "Akkoc");

            return Stream.of(
                    dynamicTest("throws illegal argument exception for null lecturer course record", () -> {
                        assertThrows(IllegalArgumentException.class, () -> studentAhmet.dropCourse(null));
                    }),
                    dynamicTest("throws illegal argument exception if the student did't register course before", () -> {
                        final LecturerCourseRecord lecturerCourseRecord = new LecturerCourseRecord(new Course("101"), new Semester());
                        assertThrows(IllegalArgumentException.class, () -> studentAhmet.dropCourse(lecturerCourseRecord));
                    }),
                    dynamicTest("throws not active semester exception if the semester is not active", () -> {
                        final Semester notActiveSemester = notActiveSemester();
                        assumeFalse(notActiveSemester.isActive());
                        final LecturerCourseRecord lecturerCourseRecord = new LecturerCourseRecord(new Course("101"), notActiveSemester);
                        Student studentMehmet = new Student("id1", "Mehmet", "Yilmaz", Set.of(new StudentCourseRecord(lecturerCourseRecord)));
                        assertThrows(NotActiveSemesterException.class, () -> studentMehmet.dropCourse(lecturerCourseRecord));
                    }),
                    dynamicTest("throws not active semester exception if the add drop period is closed for the semester", () -> {
                        final Semester addDropPeriodClosedSemester = addDropPeriodClosedSemester();
                        assumeFalse(addDropPeriodClosedSemester.isAddDropAllowed());
                        final LecturerCourseRecord lecturerCourseRecord = new LecturerCourseRecord(new Course("101"), addDropPeriodClosedSemester);
                        Student studentMehmet = new Student("id1", "Mehmet", "Yilmaz", Set.of(new StudentCourseRecord(lecturerCourseRecord)));
                        assertThrows(NotActiveSemesterException.class, () -> studentMehmet.dropCourse(lecturerCourseRecord));
                    }),
                    dynamicTest("drop course from student", () -> {
                        final Semester addDropPeriodOpenSemester = addDropPeriodOpenSemester();
                        assumeTrue(addDropPeriodOpenSemester.isAddDropAllowed());
                        final LecturerCourseRecord lecturerCourseRecord = new LecturerCourseRecord(new Course("101"), addDropPeriodOpenSemester);
                        Student studentMehmet = new Student("id1", "Mehmet", "Yilmaz", Set.of(new StudentCourseRecord(lecturerCourseRecord)));
                        assertEquals(1, studentMehmet.getStudentCourseRecords().size());
                        studentMehmet.dropCourse(lecturerCourseRecord);
                        assertTrue(studentMehmet.getStudentCourseRecords().isEmpty());
                    }));
        }

        // ders ekleme çıkarma dönemi açık olan
        private Semester addDropPeriodOpenSemester() {
            final Semester activeSemester = new Semester();
            final LocalDate semesterDate = LocalDate.of(activeSemester.getYear(), activeSemester.getTerm().getStartMonth(), 1);
            final LocalDate now = LocalDate.now();
            activeSemester.setAddDropPeriodInWeek(Long.valueOf(semesterDate.until(now, ChronoUnit.WEEKS) + 1).intValue());
            return activeSemester;
        }

        private Semester addDropPeriodClosedSemester() {
            final Semester activeSemester = new Semester();
            activeSemester.setAddDropPeriodInWeek(0);
            if (LocalDate.now().getDayOfMonth() == 1) {
                activeSemester.setAddDropPeriodInWeek(-1);
            }
            return activeSemester;
        }

        private Semester notActiveSemester() {
            final Semester activeSemester = new Semester();
            return new Semester(LocalDate.of(activeSemester.getYear() - 1, 1, 1));
        }

    }

    @Nested
    @DisplayName("Calculate Gpa for a Student")
    @Tag("calculateGpa")
    class CalculateGpaDisable {

        @Test
        @DisplayName("Calculate Gpa for a Student")
        void calculateGpa() {
            Student student = new Student("id1", "Ferat", "Akkoc");

            final LecturerCourseRecord lecturerCourseRecord101 = new LecturerCourseRecord(new Course("101"), new Semester());
            lecturerCourseRecord101.setCredit(3);
            final LecturerCourseRecord lecturerCourseRecord103 = new LecturerCourseRecord(new Course("103"), new Semester());
            lecturerCourseRecord103.setCredit(2);
            final LecturerCourseRecord lecturerCourseRecord105 = new LecturerCourseRecord(new Course("105"), new Semester());
            lecturerCourseRecord105.setCredit(1);
            student.addCourse(lecturerCourseRecord101);
            student.addCourse(lecturerCourseRecord103);
            student.addCourse(lecturerCourseRecord105);
            student.addGrade(lecturerCourseRecord101, StudentCourseRecord.Grade.A1);
            student.addGrade(lecturerCourseRecord103, StudentCourseRecord.Grade.B1);
            student.addGrade(lecturerCourseRecord105, StudentCourseRecord.Grade.C);

            assertEquals(BigDecimal.valueOf(3.33), student.gpa());
        }
    }


    @Nested
    @DisplayName("Calculate Gpa for a Student")
    @Tag("calculateGpa")
    @ExtendWith(ParameterResolverForGpaCalculation.class)
    class CalculateGpa {

        @Test
        @DisplayName("Calculate Gpa for a Student")
        void calculateGpa(Student student,
                          Map<LecturerCourseRecord, StudentCourseRecord.Grade> lecturerCourseRecordGradeMap,
                          BigDecimal expectedGpa) {

            lecturerCourseRecordGradeMap.forEach((lecturerCourseRecord, grade) -> {
                student.addCourse(lecturerCourseRecord);
                student.addGrade(lecturerCourseRecord, grade);
            });

            assertEquals(expectedGpa, student.gpa());
        }
    }

}
