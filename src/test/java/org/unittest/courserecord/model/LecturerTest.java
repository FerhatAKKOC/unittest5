package org.unittest.courserecord.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.Simple.class)
class LecturerTest {
    private Lecturer lecturer;

    @BeforeEach
    void setUp() {
        lecturer = new Lecturer();
    }

    private static LecturerCourseRecord lecturerCourseRecord() {
        return new LecturerCourseRecord(new Course(), new Semester());
    }

    @Test
    @DisplayName("When a new course added to a lecturer, size of course of lecturer increase one")
    void whenACourseAddedToLecturerThenLecturerCourseSizeIncrease() {
        assertEquals(0, lecturer.getLecturerCourseRecords().size());
        lecturer.addLecturerCourseRecord(lecturerCourseRecord());
        assertEquals(1, lecturer.getLecturerCourseRecords().size());
    }

    @Test
    @DisplayName("lecturerCourseRecordHasLecturerInfoWhenAddedToLecturer")
    void lecturerCourseRecordHasLecturerInfoWhenAddedToLecturer() {
        final LecturerCourseRecord lecturerCourseRecord = lecturerCourseRecord();
        lecturer.addLecturerCourseRecord(lecturerCourseRecord);
        assertEquals(lecturer, lecturerCourseRecord.getLecturer());
    }

    @Test
    @DisplayName("Null exception thrown when null course added")
    void throwsIllegalArgumentExceptionWhenANullCourseIsAddedToLecturer() {
        final LecturerCourseRecord lecturerCourseRecord = new LecturerCourseRecord(null, new Semester());
        final IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class, () -> lecturer.addLecturerCourseRecord(lecturerCourseRecord));
        assertEquals("Can't add a null course to lecturer", illegalArgumentException.getMessage());
    }

    @Test
    @DisplayName("throwsNotActiveSemestreExceptionWhenACourseAddedForNotActiveSemestreToLecturer")
    void throwsNotActiveSemestreExceptionWhenACourseAddedForNotActiveSemestreToLecturer() {

        final Semester activeSemestre = new Semester();
        LocalDate lastYear = LocalDate.of(activeSemestre.getYear() - 1, 1, 1);
        final Semester notActiveSemestre = new Semester(lastYear);

        LecturerCourseRecord lecturerCourseRecord = new LecturerCourseRecord(new Course(), notActiveSemestre);

        assertThrows(NotActiveSemesterException.class, () -> lecturer.addLecturerCourseRecord(lecturerCourseRecord));
        NotActiveSemesterException notActiveSemesterException = assertThrows(NotActiveSemesterException.class, () -> lecturer.addLecturerCourseRecord(lecturerCourseRecord));
        assertEquals(notActiveSemestre.toString(), notActiveSemesterException.getMessage());
    }
}
