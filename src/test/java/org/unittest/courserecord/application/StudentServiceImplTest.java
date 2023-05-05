package org.unittest.courserecord.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.atMost;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.unittest.courserecord.model.Course;
import org.unittest.courserecord.model.Lecturer;
import org.unittest.courserecord.model.LecturerCourseRecord;
import org.unittest.courserecord.model.Semester;
import org.unittest.courserecord.model.Student;
import org.unittest.courserecord.model.StudentCourseRecord;
import org.unittest.courserecord.model.StudentRepository;

//@ExtendWith(MockitoExtension.classs)
class StudentServiceImplTest {

    private Course course;
    @Mock
    private Student student;
    @Mock
    private Lecturer lecturer;
    @Mock
    private CourseService courseService;
    @Mock
    private LecturerService lecturerService;
    @Mock
    private StudentRepository studentRepository;
    @InjectMocks
    private StudentServiceImpl studentService;

    @Captor
    private ArgumentCaptor<Student> studentArgumentCaptor;

    @Captor
    private ArgumentCaptor<String> stringArgumentCaptor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        course = new Course("101");
    }

    @Test
    void addCourse() {
        Semester semester = new Semester();
        when(courseService.findCourse(course)).thenReturn(Optional.of(course));
        when(lecturerService.findLecturer(course, semester)).thenReturn(Optional.of(lecturer));
        when(studentRepository.findById("id1"))
                .thenReturn(Optional.of(new Student("id1", "Ferat", "Akkoc"))) // 1. method call
                .thenThrow(new IllegalArgumentException("Can't find a student")); // 2. method call
        when(lecturer.lecturerCourseRecord(course, semester)).thenReturn(new LecturerCourseRecord(course, semester));

        studentService.addCourse("id1", course, semester);

        assertThatThrownBy(() -> studentService.findStudent("id1"))
                .isInstanceOf(IllegalArgumentException.class);

        // final Optional<Student> studentOptional = studentService.findStudent("id1");
        // assertThat(studentOptional).as("Student")
        // .isPresent()
        // .get()
        // .matches(student -> student.isTakeCourse(course));

        verify(courseService).findCourse(course);
        verify(courseService, times(1)).findCourse(course);
        verify(courseService, atLeast(1)).findCourse(course);
        verify(courseService, atMost(1)).findCourse(course);
        verify(studentRepository, times(2)).findById("id1");
        verify(lecturerService).findLecturer(any(Course.class), any(Semester.class));
        verify(lecturer).lecturerCourseRecord(argThat(course1 -> course1.getCode().equals("101")), any(Semester.class));
    }

    @Test
    void dropCourse() {
        LecturerCourseRecord lecturerCourseRecord = new LecturerCourseRecord(course, new Semester());

        when(studentRepository.findById("id1"))
                .thenReturn(Optional.empty())
                .thenReturn(Optional.of(student));
        when(courseService.findCourse(course))
                .thenReturn(Optional.empty())
                .thenReturn(Optional.of(course));
        when(lecturerService.findLecturer(eq(course), any(Semester.class)))
                .thenReturn(Optional.empty())
                .thenReturn(Optional.ofNullable(lecturer));
        when(lecturer.lecturerCourseRecord(eq(course), any(Semester.class)))
                .thenReturn(lecturerCourseRecord);

        assertThatIllegalArgumentException()
                .isThrownBy(() -> studentService.dropCourse("id1", course))
                .withMessageContaining("Can't find a student with id<id1>");

        assertThatIllegalArgumentException()
                .isThrownBy(() -> studentService.dropCourse("id1", course))
                .withMessageContaining("Can't find a course");

        assertThatIllegalArgumentException()
                .isThrownBy(() -> studentService.dropCourse("id1", course))
                .withMessageContaining("Can't find a lecturer");

        studentService.dropCourse("id1", course);

        InOrder inOrder = Mockito.inOrder(
                studentRepository,
                courseService,
                lecturerService,
                lecturer,
                student);

        inOrder.verify(studentRepository, times(2)).findById("id1");
        inOrder.verify(courseService, times(1)).findCourse(course);
        inOrder.verify(studentRepository, times(1)).findById("id1");
        inOrder.verify(courseService, times(1)).findCourse(course);
        inOrder.verify(lecturerService, times(1)).findLecturer(eq(course), any(Semester.class));
        inOrder.verify(studentRepository, times(1)).findById("id1");
        inOrder.verify(courseService, times(1)).findCourse(course);
        inOrder.verify(lecturerService, times(1)).findLecturer(eq(course), any(Semester.class));
        inOrder.verify(lecturer).lecturerCourseRecord(argThat(course1 -> course1.getCode().equals("101")), any(Semester.class));
        inOrder.verify(student).dropCourse(lecturerCourseRecord);
        inOrder.verify(studentRepository).save(student);

        verifyNoMoreInteractions(studentRepository,
                courseService,
                lecturerService,
                lecturer,
                student);
    }

    @Test
    void deleteStudent() {

        Student student = new Student("id1", "Ferat", "Akkoc");
        when(studentRepository.findById("id1")).thenAnswer(invocation -> Optional.of(student));

        doNothing() // 1st call
                .doThrow(new IllegalArgumentException("There is no student in repo")) // 2nd call
                .doAnswer(invocation -> { // 3rd call
                    final Student studentFerat = invocation.getArgument(0);
                    System.out.println(String.format("Student %s will be deleted", studentFerat));
                    return null;
                }).when(studentRepository).delete(student);

        studentService.deleteStudent("id1"); // 1st call
        assertThatIllegalArgumentException()
                .isThrownBy(() -> studentService.deleteStudent("id1"))
                .withMessageContaining("There is no student in repo");
        studentService.deleteStudent("id1");

        // verify(studentRepository,times(3)).findById("id1");
        verify(studentRepository, times(3)).findById(stringArgumentCaptor.capture());
        verify(studentRepository, times(3)).delete(studentArgumentCaptor.capture());

        assertThat(stringArgumentCaptor.getAllValues())
                .hasSize(3)
                .containsOnly("id1", "id1", "id1");

        assertThat(studentArgumentCaptor.getAllValues())
                .hasSize(3)
                .extracting(Student::getId)
                .containsOnly("id1", "id1", "id1");
    }

    @Test
    void getTranscriptionOfAStudent() {

        final List<StudentCourseRecord.Grade> grades = Arrays.asList(StudentCourseRecord.Grade.values());

        Student student1 = mock(Student.class);
        when(studentRepository.findById("id1")).thenReturn(Optional.of(student1));
        when(student1.getStudentCourseRecords()).thenAnswer(invocation -> {
            final Semester semester = new Semester(LocalDate.of(2015, 1, 1));
            return Stream.of("101", "103", "105", "107", "109")
                    .map(Course::new)
                    .map(course1 -> new LecturerCourseRecord(course1, semester))
                    .peek(lecturerCourseRecord -> lecturerCourseRecord.setCredit(new Random().nextInt(3)))
                    .map(StudentCourseRecord::new)
                    .peek(studentCourseRecord -> {
                        Collections.shuffle(grades);
                        studentCourseRecord.setGrade(grades.get(0));
                    })
                    .collect(Collectors.toSet());
        });

        assertThat(studentService.transcript("id1"))
                .hasSize(5)
                .extracting(TranscriptItem::getCourse)
                .extracting(Course::getCode)
                .containsOnly("101", "103", "105", "107", "109");

        assertThat(studentService.transcript("id1"))
                .extracting(TranscriptItem::getCredit)
                .containsAnyOf(1, 2, 3);

        assertThat(studentService.transcript("id1"))
                .extracting(TranscriptItem::getGrade)
                .containsAnyOf(StudentCourseRecord.Grade.A1, StudentCourseRecord.Grade.A2, StudentCourseRecord.Grade.B1, StudentCourseRecord.Grade.B2, StudentCourseRecord.Grade.C, StudentCourseRecord.Grade.D, StudentCourseRecord.Grade.E, StudentCourseRecord.Grade.F);

        assertThat(studentService.transcript("id1"))
                .filteredOn(transcriptItem -> transcriptItem.getCourse().getCode().equals("101"))
                .extracting(TranscriptItem::getCourse, TranscriptItem::getSemester)
                .containsOnly(Tuple.tuple(new Course("101"), new Semester(LocalDate.of(2015, 1, 1))));

        verify(studentRepository, times(4)).findById("id1");
        verify(student1, times(4)).getStudentCourseRecords();
    }

    @Test
    void addCourseWithSpyStudent() {
        Semester semester = new Semester();
        when(courseService.findCourse(course)).thenReturn(Optional.of(course));
        when(lecturerService.findLecturer(course, semester)).thenReturn(Optional.of(lecturer));

        Student studentReal = new Student("id1", "Ferat", "Akkoc");
        Student studentFerat = spy(studentReal);

//        doThrow(new IllegalArgumentException("Spy failed!")).when(studentFerat).addCourse(any(LecturerCourseRecord.class));

        when(studentRepository.findById("id1"))
                .thenReturn(Optional.of(studentFerat)) // 1. method call
                .thenThrow(new IllegalArgumentException("Can't find a student")); // 2. method call
        when(lecturer.lecturerCourseRecord(course, semester)).thenReturn(new LecturerCourseRecord(course, semester));

        studentService.addCourse("id1", course, semester);

        //-----------------  spy  -----------------------
        // Collection'larda eklenen course hem spy hemde gerçek obje'ye ekleniyor.
        assertThat(studentFerat).matches(student1 -> student1.isTakeCourse(course));
        assertThat(studentReal).matches(student1 -> student1.isTakeCourse(course));

        // Collection'lardan farklı olarak, eklenen field sadece gerçek obje'de değişikliğe neden oluyor.
        studentFerat.setBirthDate(LocalDate.of(1990,1,1));
        assertThat(studentFerat.getBirthDate()).isNotNull();
        assertThat(studentReal.getBirthDate()).isNull();

        assertThatThrownBy(() -> studentService.findStudent("id1"))
                .isInstanceOf(IllegalArgumentException.class);

        // final Optional<Student> studentOptional = studentService.findStudent("id1");
        // assertThat(studentOptional).as("Student")
        // .isPresent()
        // .get()
        // .matches(student -> student.isTakeCourse(course));

        verify(courseService).findCourse(course);
        verify(courseService, times(1)).findCourse(course);
        verify(courseService, atLeast(1)).findCourse(course);
        verify(courseService, atMost(1)).findCourse(course);
        verify(studentRepository, times(2)).findById("id1");
        verify(lecturerService).findLecturer(any(Course.class), any(Semester.class));
        verify(lecturer).lecturerCourseRecord(argThat(course1 -> course1.getCode().equals("101")), any(Semester.class));

    }

    @Test
    void addCourseWithPartialMock() {
        Semester semester = new Semester();
        when(courseService.findCourse(course)).thenReturn(Optional.of(course));
        when(lecturerService.findLecturer(course, semester)).thenReturn(Optional.of(lecturer));
        Student studentFerat = mock(Student.class);
        when(studentFerat.isTakeCourse(any(Course.class))).thenReturn(true);
        when(studentRepository.findById("id1"))
                .thenReturn(Optional.of(studentFerat)) // 1. method call
                .thenThrow(new IllegalArgumentException("Can't find a student")); // 2. method call
        when(lecturer.lecturerCourseRecord(course, semester)).thenReturn(new LecturerCourseRecord(course, semester));

        studentService.addCourse("id1", course, semester);

        assertThatThrownBy(() -> studentService.findStudent("id1"))
                .isInstanceOf(IllegalArgumentException.class);

         final Optional<Student> studentOptional = studentService.findStudent("id1");
         assertThat(studentOptional).as("Student")
         .isPresent()
         .get()
         .matches(student -> student.isTakeCourse(course));

        verify(courseService).findCourse(course);
        verify(courseService, times(1)).findCourse(course);
        verify(courseService, atLeast(1)).findCourse(course);
        verify(courseService, atMost(1)).findCourse(course);
        verify(studentRepository, times(2)).findById("id1");
        verify(lecturerService).findLecturer(any(Course.class), any(Semester.class));
        verify(lecturer).lecturerCourseRecord(argThat(course1 -> course1.getCode().equals("101")), any(Semester.class));
    }
}
