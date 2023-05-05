package org.unittest.assertjlib;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.Assertions.in;
import static org.assertj.core.api.Assertions.tuple;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;
import org.unittest.courserecord.model.Course;
import org.unittest.courserecord.model.Department;
import org.unittest.courserecord.model.LecturerCourseRecord;
import org.unittest.courserecord.model.NoCourseFoundForStudentException;
import org.unittest.courserecord.model.Semester;
import org.unittest.courserecord.model.Student;
import org.unittest.courserecord.model.StudentCourseRecord;
import org.unittest.courserecord.model.UniversityMember;

public class StudentWithAssertJAssertionsTest {
    @Test
    void createStudent() {
        final Student student = new Student("id1", "Ferat", "Akkoc");

        assertThat(student.getName()).as("Message when fail :")
                .isInstanceOf(String.class)
                .doesNotContainOnlyWhitespaces()
                .isNotEmpty()
                .isNotBlank()
                .isEqualTo("Ferat")
                .isEqualToIgnoringCase("ferat")
                .isIn("Ali", "Mehmet", "Ferat") // listedeki birine uyuyor.
                .isNotIn("Veli", "Deli")
                .startsWith("Fe")
                .doesNotStartWith("sda")
                .endsWith("at")
                .doesNotEndWith("dasda")
                .contains("era")
                .contains(List.of("F", "r", "e"))
                .hasSize(5)
                .matches("^F\\w{3}t$");
    }

    @Test
    void addCourseToStudent() {
        final Student ahmet = new Student("id1", "Ahmet", "Yilmaz", LocalDate.of(1990, 1, 1));
        final Student mehmet = new Student("id2", "Mehmet", "Kural", LocalDate.of(1992, 1, 1));
        final Student canan = new Student("id3", "Canan", "Sahin", LocalDate.of(1995, 1, 1));
        final Student elif = new Student("id4", "Elif", "Oz", LocalDate.of(1991, 1, 1));
        final Student hasan = new Student("id5", "Hasan", "Kartal", LocalDate.of(1990, 1, 1));
        final Student mucahit = new Student("id6", "Mucahit", "Kurt", LocalDate.of(1980, 1, 1));

        final List<Student> students = List.of(ahmet, mehmet, canan, elif, hasan);

        assertThat(students).as("Student's List")
                .isNotNull()
                .isNotEmpty()
                .hasSize(5)
                .contains(ahmet, mehmet) // ahmet, mehmet olur, başkası da olabilir.
                .containsOnly(ahmet, mehmet, canan, elif, hasan) // listedeki hepsi olmalı, sırası önemli değil.
                .containsExactly(ahmet, mehmet, canan, elif, hasan) // hepsi önemli, sırasıda önemli.
                .containsExactlyInAnyOrder(mehmet, ahmet, canan, elif, hasan);

        assertThat(students)
                .filteredOn(student -> student.getBirthDate().until(LocalDate.now(), ChronoUnit.YEARS) >= 25)
                .containsOnly(ahmet, mehmet, canan, elif, hasan);

        assertThat(students)
                .filteredOn(new Condition<>() {
                    @Override
                    public boolean matches(Student value) {
                        return value.getBirthDate().until(LocalDate.now(), ChronoUnit.YEARS) >= 25;
                    }
                })
                .hasSize(5)
                .containsOnly(ahmet, mehmet, canan, elif, hasan);

        assertThat(students)
                .filteredOn("birthDate", in(LocalDate.of(1990, 1, 1)))
                .hasSize(2)
                .containsOnly(ahmet, hasan);

        assertThat(students)
                .extracting(Student::getName)// Öğrenci isimlerinden oluşan başka bir collection verir.
                .filteredOn(name -> name.contains("e"))
                .hasSize(2)
                .containsOnly("Ahmet", "Mehmet");

        assertThat(students)
                .filteredOn(student -> student.getName().contains("e"))
                .extracting(Student::getName, Student::getSurname) // >2 field 'den oluşan collection oluşturabiliriz.
                .containsOnly(
                        tuple("Ahmet", "Yilmaz"), // Bu durumda tuple kullanırız.
                        tuple("Mehmet", "Kural"));

        final LecturerCourseRecord lecturerCourseRecord101 = new LecturerCourseRecord(new Course("101"), new Semester());
        final LecturerCourseRecord lecturerCourseRecord103 = new LecturerCourseRecord(new Course("103"), new Semester());
        final LecturerCourseRecord lecturerCourseRecord105 = new LecturerCourseRecord(new Course("105"), new Semester());

        ahmet.addCourse(lecturerCourseRecord101);
        ahmet.addCourse(lecturerCourseRecord103);

        canan.addCourse(lecturerCourseRecord101);
        canan.addCourse(lecturerCourseRecord103);
        canan.addCourse(lecturerCourseRecord105);

        assertThat(students)
                .filteredOn(student -> student.getName().equals("Ahmet") || student.getName().equals("Canan"))
                .flatExtracting(Student::getStudentCourseRecords)
                .hasSize(5)
                .extracting(StudentCourseRecord::getLecturerCourseRecord)
                .filteredOn(courseRecord -> courseRecord.getCourse().getCode().equals("101"))
                .hasSize(2);
    }

    @Test
    void anotherCreateStudentTestWithObjects() {
        final Department department = new Department();
        final Student ahmet = new Student("id1", "Ahmet", "Yilmaz", LocalDate.of(1990, 1, 1));
        ahmet.setDepartment(department);
        final Student mehmet = new Student("id2", "Mehmet", "Yilmaz", LocalDate.of(1995, 1, 1));
        mehmet.setDepartment(department);

        assertThat(ahmet).as("Check student Ahmet info")
                .isNotNull()
                .hasSameClassAs(mehmet)
                .isExactlyInstanceOf(Student.class) // subClass'ı değil, kendisi
                .isInstanceOf(UniversityMember.class) // subclass'ı da kabul eder.
                .isNotEqualTo(mehmet)
                .isEqualToComparingOnlyGivenFields(mehmet, "surname")
                .isEqualToIgnoringGivenFields(mehmet, "id", "name", "birthDate")
                .matches(student -> student.getBirthDate().getYear() == 1990)
                .hasFieldOrProperty("name")
                .hasNoNullFieldsOrProperties() // null field yada property var mı?
                .extracting(Student::getName, Student::getSurname) // herhangi bir alanını çekip kontrol ediyoruz.
                .containsOnly("Ahmet", "Yilmaz");

        // Custom assertion writing.
        StudentCustomAssert.assertThat(mehmet).as("Studen's mehmet info check")
                .hasName("Mehmet");

    }

    @Test
    void addCourseToStudentWithExceptionalScenarios() {
        Student student = new Student("id1", "Ferat", "Akkoc");

        assertThatThrownBy(() -> student.addCourse(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Can't add course")
                .hasStackTraceContaining("Student");

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> student.addCourse(null))
                .withMessageContaining("Can't add course")
                .withNoCause();

        assertThatIllegalArgumentException()
                .isThrownBy(() -> student.addCourse(null))
                .withMessageContaining("Can't add course")
                .withNoCause();

        assertThatCode(() -> student.addCourse(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Can't add course");

        assertThatCode(() -> student.addCourse(new LecturerCourseRecord(new Course("101"), new Semester())))
                .doesNotThrowAnyException();

        assertThatThrownBy(() -> student.addGrade(null, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasCauseExactlyInstanceOf(NoCourseFoundForStudentException.class);

        Throwable throwable = catchThrowable(() -> student.addCourse(null));

        assertThat(throwable)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Can't add course");
    }
}
