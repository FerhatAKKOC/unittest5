package org.unittest.courserecord.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.TestReporter;

@DisplayName("Student Test With TestInfo and TestReporter parameter")
public class JunitParameterizedStudentTest {

    private Student student;

    public JunitParameterizedStudentTest(TestInfo testInfo) {
        assertEquals("Student Test With TestInfo and TestReporter parameter",testInfo.getDisplayName());
    }

    @BeforeEach
    void setUpStudent(TestInfo testInfo) {
        if (testInfo.getTags().contains("create")) {
            student = new Student("1", "Ahmet", "Yildiz");
        }else {
            student = new Student("1", "Mehmet", "Yildiz");
        }
    }

    @Test
    @DisplayName("Create Student")
    @Tag("create")
    void createStudent(TestInfo testInfo) {
        assertTrue(testInfo.getTags().contains("create"));
        assertEquals("Ahmet", student.getName());
    }

    @Test
    @DisplayName("Add Course to Student")
    @Tag("addCourse")
    void addCourseToStudent(TestReporter reporter) {
        reporter.publishEntry("StartTime", LocalDate.now().toString());
        assertEquals("Mehmet", student.getName());
        reporter.publishEntry("EndTime", LocalDate.now().toString());
    }
}
