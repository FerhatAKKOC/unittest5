package org.unittest.parameterresolvers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeFalse;
import static org.junit.jupiter.api.Assumptions.assumeTrue;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import java.util.Set;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.extension.ExtendWith;
import org.unittest.courserecord.model.Course;
import org.unittest.courserecord.model.LecturerCourseRecord;
import org.unittest.courserecord.model.NotActiveSemesterException;
import org.unittest.courserecord.model.Semester;
import org.unittest.courserecord.model.Student;
import org.unittest.courserecord.model.StudentCourseRecord;
import org.unittest.interfaces_default.DropCourseParameterResolverExtension;

@Tag("Student")
@DisplayName("Student test")
class StudentWithParameterResolverTest {

    @ExtendWith(DropCourseParameterResolverExtension.class)
    @Nested
    @DisplayName("Drop Course from Student")
    @Tag("dropCourse")
    class DropCourseFromStudent {

        final Student studentAhmet;
        final Semester addDropPeriodOpenSemester;
        final Semester addDropPeriodClosedSemester;
        final Semester notActiveSemester;

        public DropCourseFromStudent(final Student studentAhmet,
                final Semester addDropPeriodOpenSemester,
                final Semester addDropPeriodClosedSemester,
                final Semester notActiveSemester) {
            this.studentAhmet = studentAhmet;
            this.addDropPeriodOpenSemester = addDropPeriodOpenSemester;
            this.addDropPeriodClosedSemester = addDropPeriodClosedSemester;
            this.notActiveSemester = notActiveSemester;
        }

        @TestFactory
        Stream<DynamicTest> dropCourseFromStudentFactory() {
            return Stream.of(
                    dynamicTest("throws illegal argument exception for null lecturer course record", () -> {
                        assertThrows(IllegalArgumentException.class, () -> studentAhmet.dropCourse(null));
                    }),
                    dynamicTest("throws illegal argument exception if the student did't register course before", () -> {
                        final LecturerCourseRecord lecturerCourseRecord = new LecturerCourseRecord(new Course("101"), new Semester());
                        assertThrows(IllegalArgumentException.class, () -> studentAhmet.dropCourse(lecturerCourseRecord));
                    }),
                    dynamicTest("throws not active semester exception if the semester is not active", () -> {
                        // final Semester notActiveSemester = notActiveSemester();
                        assumeFalse(notActiveSemester.isActive());
                        final LecturerCourseRecord lecturerCourseRecord = new LecturerCourseRecord(new Course("101"), notActiveSemester);
                        Student studentMehmet = new Student("id1", "Mehmet", "Yilmaz", Set.of(new StudentCourseRecord(lecturerCourseRecord)));
                        assertThrows(NotActiveSemesterException.class, () -> studentMehmet.dropCourse(lecturerCourseRecord));
                    }),
                    dynamicTest("throws not active semester exception if the add drop period is closed for the semester", () -> {
                        // final Semester addDropPeriodClosedSemester = addDropPeriodClosedSemester();
                        assumeFalse(addDropPeriodClosedSemester.isAddDropAllowed());
                        final LecturerCourseRecord lecturerCourseRecord = new LecturerCourseRecord(new Course("101"), addDropPeriodClosedSemester);
                        Student studentMehmet = new Student("id1", "Mehmet", "Yilmaz", Set.of(new StudentCourseRecord(lecturerCourseRecord)));
                        assertThrows(NotActiveSemesterException.class, () -> studentMehmet.dropCourse(lecturerCourseRecord));
                    }),
                    dynamicTest("drop course from student", () -> {
                        // final Semester addDropPeriodOpenSemester = addDropPeriodOpenSemester();
                        assumeTrue(addDropPeriodOpenSemester.isAddDropAllowed());
                        final LecturerCourseRecord lecturerCourseRecord = new LecturerCourseRecord(new Course("101"), addDropPeriodOpenSemester);
                        Student studentMehmet = new Student("id1", "Mehmet", "Yilmaz", Set.of(new StudentCourseRecord(lecturerCourseRecord)));
                        assertEquals(1, studentMehmet.getStudentCourseRecords().size());
                        studentMehmet.dropCourse(lecturerCourseRecord);
                        assertTrue(studentMehmet.getStudentCourseRecords().isEmpty());
                    }));
        }
    }
}
