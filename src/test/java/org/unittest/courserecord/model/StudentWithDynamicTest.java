package org.unittest.courserecord.model;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.DynamicContainer.dynamicContainer;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import java.util.Iterator;
import java.util.function.Function;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DynamicNode;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.TestReporter;
import org.junit.jupiter.api.function.ThrowingConsumer;

public class StudentWithDynamicTest {

    private Student student;

    @BeforeEach
    void setUp() {
        student = new Student("id1", "Ahmet", "Yilmaz");
    }

    @TestFactory
    Stream<DynamicNode> addCourseToStudentWithCourseCodeAndCourseType(TestReporter testReporter) {

        return Stream.of("101", "103", "105")
                .map(courseCode -> dynamicContainer("Add course<" + courseCode + "> to student", // her bir code için bir dynamic container oluşturuyoruz.
                        Stream.of(Course.CourseType.values())
                                .map(courseType -> dynamicTest("Add course<" + courseType + "> to student", () -> { // Her container içerisinde de 2 adet test oluşturuyoruz

                                    // Actual test code
                                    final Course course = Course.newCourse().withCode(courseCode).withCourseType(courseType).course();
                                    final LecturerCourseRecord lecturerCourseRecord = new LecturerCourseRecord(course, new Semester());
                                    student.addCourse(lecturerCourseRecord);
                                    assertTrue(student.isTakeCourse(course));
                                    testReporter.publishEntry("Student Course Size", String.valueOf(student.getStudentCourseRecords().size()));
                                }))));

    }

    @TestFactory
    Stream<DynamicTest> addCourseToStudentWithCourseCode() {
        final Iterator<String> courseCodeGenerator = Stream.of("101", "103", "105").iterator();
        Function<String, String> displayNameGenerator = courseCode -> "Add course<" + courseCode + "> to student";

        ThrowingConsumer<String> testExecutor = courseCode -> {
            final Course course = Course.newCourse().withCode(courseCode).withCourseType(Course.CourseType.MANDATORY).course();
            final LecturerCourseRecord lecturerCourseRecord = new LecturerCourseRecord(course, new Semester());
            student.addCourse(lecturerCourseRecord);
            assertTrue(student.isTakeCourse(course));
        };

        return DynamicTest.stream(courseCodeGenerator, displayNameGenerator, testExecutor);
    }
}
