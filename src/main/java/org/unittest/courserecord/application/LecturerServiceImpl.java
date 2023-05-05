package org.unittest.courserecord.application;

import java.util.Optional;

import org.unittest.courserecord.model.Course;
import org.unittest.courserecord.model.Lecturer;
import org.unittest.courserecord.model.LecturerRepository;
import org.unittest.courserecord.model.Semester;

public class LecturerServiceImpl implements LecturerService {

    private final LecturerRepository lecturerRepository;

    public LecturerServiceImpl(LecturerRepository lecturerRepository) {
        this.lecturerRepository = lecturerRepository;
    }

    @Override
    public Optional<Lecturer> findLecturer(Course course, Semester semester) {
        if (course == null || semester == null) {
            throw new IllegalArgumentException("Can't find lecturer without course and semester");
        }
        return lecturerRepository.findByCourseAndSemester(course, semester);
    }
}
