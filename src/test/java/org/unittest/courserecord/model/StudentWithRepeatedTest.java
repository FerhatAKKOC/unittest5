package org.unittest.courserecord.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.RepetitionInfo;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS) // hep aynı öğrenci ile çalışmak istiyoruz
public class StudentWithRepeatedTest {
    private Student student;

    @BeforeAll
    void beforeAll() {
        student = new Student("1", "Ferat", "Akkoc");
    }


//    @RepeatedTest(value = 5, name = LONG_DISPLAY_NAME)
    @DisplayName("Add Course to Student")
    @RepeatedTest(value = 5, name = "{displayName} ==> Add one course to student and student has {currentRepetition} courses")
    void addCourseToStudent(RepetitionInfo repetitionInfo) {
        LecturerCourseRecord lecturerCourseRecord =
                new LecturerCourseRecord(new Course(String.valueOf(repetitionInfo.getCurrentRepetition())), new Semester());

        student.addCourse(lecturerCourseRecord);
        assertEquals(repetitionInfo.getCurrentRepetition(),student.getStudentCourseRecords().size());
    }
}
