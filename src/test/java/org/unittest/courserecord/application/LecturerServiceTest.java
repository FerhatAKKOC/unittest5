package org.unittest.courserecord.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.unittest.courserecord.model.Course;
import org.unittest.courserecord.model.Lecturer;
import org.unittest.courserecord.model.LecturerRepository;
import org.unittest.courserecord.model.Semester;

class LecturerServiceTest {

    @Test
    void findLecturer() {

        final Course course = new Course("101");
        final Semester semester = new Semester();
        final Lecturer lecturer = new Lecturer();
        final LecturerRepository lecturerRepository = mock(LecturerRepository.class);
        final LecturerService lecturerService = new LecturerServiceImpl(lecturerRepository);

        when(lecturerRepository.findByCourseAndSemester(course, semester)).thenReturn(Optional.of(lecturer));

        final Optional<Lecturer> lecturerOpt = lecturerService.findLecturer(course, semester);

        assertThat(lecturerOpt)
                .isNotPresent()
                .get()
                .isSameAs(lecturer);

        verify(lecturerRepository).findByCourseAndSemester(course, semester);
    }

    @Test
    void testMockitoConstraint(){
        Mockito.when(LecturerService.testMockito()).thenReturn(true);
        assertThat(LecturerService.testMockito()).isTrue();
    }
}
