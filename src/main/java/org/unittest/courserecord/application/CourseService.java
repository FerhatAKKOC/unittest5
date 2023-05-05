package org.unittest.courserecord.application;

import java.util.Optional;

import org.unittest.courserecord.model.Course;
import org.unittest.courserecord.model.Department;

public interface CourseService {

    Optional<Course> findCourse(Course course);

    Optional<Course> findCourse(Department department, String code, String name);
}
