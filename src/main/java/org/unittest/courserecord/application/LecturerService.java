package org.unittest.courserecord.application;

import java.util.Optional;

import org.unittest.courserecord.model.Course;
import org.unittest.courserecord.model.Lecturer;
import org.unittest.courserecord.model.Semester;

public interface LecturerService {

    Optional<Lecturer> findLecturer(Course course, Semester semester);

    static boolean testMockito(){
        return true;
    }
}
