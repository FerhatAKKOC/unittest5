package org.unittest.courserecord.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;

@TestInstance(TestInstance.Lifecycle.PER_CLASS) // hep aynı öğrenci ile çalışmak istiyoruz
public class StudentWithTemplateTest {
    private Student student;

    @BeforeAll
    void beforeAll() {
        student = new Student("1", "Ferat", "Akkoc");
    }

    @ExtendWith(StudentTestTemplateInvocationContextProviderExtension.class)
    @TestTemplate
    void addCourseToStudent(LecturerCourseRecord lecturerCourseRecord, int numberOfInvocation) {
        student.addCourse(lecturerCourseRecord);
        assertEquals(numberOfInvocation, student.getStudentCourseRecords().size());
    }
}
