package org.unittest.courserecord.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumingThat;

import java.time.LocalDate;
import java.util.Random;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.ArgumentConversionException;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.converter.JavaTimeConversionPattern;
import org.junit.jupiter.params.converter.SimpleArgumentConverter;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

public class StudentWithParameterizedTest {

    private Student student;

    @TestInstance(TestInstance.Lifecycle.PER_CLASS) // hep aynı ogrenciyi kullanmak istiyoruz
    @Nested
    class ValueSourceDemo {
        private int studentCourseSize = 0;

        @BeforeAll
        void beforeAll() {
            student = new Student("id1", "Ferat", "AKkoc");
        }

        @ParameterizedTest
        @ValueSource(strings = { "101", "103", "105" })
        void addCourseToStudent(String courseCode) {

            final LecturerCourseRecord lecturerCourseRecord =
                    new LecturerCourseRecord(new Course(courseCode), new Semester());

            student.addCourse(lecturerCourseRecord);
            studentCourseSize++;
            assertEquals(studentCourseSize, student.getStudentCourseRecords().size());
            assertTrue(student.isTakeCourse(new Course(courseCode)));
        }
    }

    @TestInstance(TestInstance.Lifecycle.PER_CLASS) // hep aynı ogrenciyi kullanmak istiyoruz
    @Nested
    class EnumSourceDemo {

        @BeforeAll
        void beforeAll() {
            student = new Student("1", "Ferat", "Akkoç");
        }

        @ParameterizedTest
        @EnumSource(Course.CourseType.class) // enum sınıfını alıyor.
        void addCourseToStudent(Course.CourseType courseType) {
            Course course = Course.newCourse()
                    .withCode(String.valueOf(new Random().nextInt(200)))
                    .withCourseType(courseType)
                    .course();
            final LecturerCourseRecord lecturerCourseRecord = new LecturerCourseRecord(course, new Semester());
            student.addCourse(lecturerCourseRecord);

            assertFalse(student.getStudentCourseRecords().isEmpty());
            assertTrue(student.isTakeCourse(course));
        }

        @ParameterizedTest
        @EnumSource(value = Course.CourseType.class, names = "MANDATORY") // enum sınıfından istediklerimizi alıyoruz
        void addMandatoryCourseToStudent(Course.CourseType courseType) {
            Course course = Course.newCourse()
                    .withCode(String.valueOf(new Random().nextInt(200)))
                    .withCourseType(courseType)
                    .course();
            final LecturerCourseRecord lecturerCourseRecord = new LecturerCourseRecord(course, new Semester());
            student.addCourse(lecturerCourseRecord);

            assertEquals(Course.CourseType.MANDATORY, course.getCourseType());
            assertFalse(student.getStudentCourseRecords().isEmpty());
            assertTrue(student.isTakeCourse(course));
        }

        @ParameterizedTest
        @EnumSource(value = Course.CourseType.class, mode = EnumSource.Mode.EXCLUDE, names = "MANDATORY") // enum sınıfından istediklerimizi alıyoruz
        void addElectiveCourseToStudent(Course.CourseType courseType) {
            Course course = Course.newCourse()
                    .withCode(String.valueOf(new Random().nextInt(200)))
                    .withCourseType(courseType)
                    .course();
            final LecturerCourseRecord lecturerCourseRecord = new LecturerCourseRecord(course, new Semester());
            student.addCourse(lecturerCourseRecord);

            assertEquals(Course.CourseType.ELECTIVE, course.getCourseType());
            assertFalse(student.getStudentCourseRecords().isEmpty());
            assertTrue(student.isTakeCourse(course));
        }
    }

    @TestInstance(TestInstance.Lifecycle.PER_CLASS) // hep aynı ogrenciyi kullanmak istiyoruz
    @Nested
    class MethodSourceDemo {
        private int studentCourseSize = 0;

        @BeforeAll
        void beforeAll() {
            student = new Student("id1", "Ferat", "AKkoc");
        }

        @ParameterizedTest
        @MethodSource // method aynı isimli bir factory metod arıyor.
        void addCourseToStudent(String courseCode) {

            final LecturerCourseRecord lecturerCourseRecord =
                    new LecturerCourseRecord(new Course(courseCode), new Semester());

            student.addCourse(lecturerCourseRecord);
            studentCourseSize++;
            assertEquals(studentCourseSize, student.getStudentCourseRecords().size());
            assertTrue(student.isTakeCourse(new Course(courseCode)));
        }

        // factory methods name
        Stream<String> addCourseToStudent() {
            return Stream.of("101", "103", "105");
        }

        /************************ Arguments.of multiple argument input ***************************/
        @ParameterizedTest
        @MethodSource("courseWithCodeAndType")
        void addCourseToStudent(String courseCode, Course.CourseType courseType) {

            final Course course = new Course(courseCode);
            course.setCourseType(courseType);

            final LecturerCourseRecord lecturerCourseRecord = new LecturerCourseRecord(course, new Semester());

            student.addCourse(lecturerCourseRecord);
            studentCourseSize++;
            assertEquals(studentCourseSize, student.getStudentCourseRecords().size());
            assertTrue(student.isTakeCourse(new Course(courseCode)));
            assumingThat(courseCode.equals("101"), () -> assertEquals(Course.CourseType.MANDATORY, course.getCourseType()));
            assumingThat(courseCode.equals("103"), () -> assertEquals(Course.CourseType.ELECTIVE, course.getCourseType()));
            assumingThat(courseCode.equals("105"), () -> assertEquals(Course.CourseType.MANDATORY, course.getCourseType()));
        }

        Stream<Arguments> courseWithCodeAndType() {
            return Stream.of(
                    Arguments.of("101", Course.CourseType.MANDATORY),
                    Arguments.of("103", Course.CourseType.ELECTIVE),
                    Arguments.of("105", Course.CourseType.MANDATORY));
        }
    }

    @TestInstance(TestInstance.Lifecycle.PER_CLASS) // hep aynı ogrenciyi kullanmak istiyoruz
    @Nested
    class CsvSourceDemo {

        private int studentCourseSize = 0;

        @BeforeAll
        void beforeAll() {
            student = new Student("id1", "Ferat", "Akkoc");
        }

        @DisplayName("Add course to Student")
        @ParameterizedTest(name = "{index} ==> Parameters: first:{0}, second:{1}")
        @CsvSource({ "101,MANDATORY", "103,ELECTIVE", "105,MANDATORY" })
        void addCourseToStudent(String courseCode, Course.CourseType courseType) {

            final Course course = new Course(courseCode);
            course.setCourseType(courseType);

            final LecturerCourseRecord lecturerCourseRecord = new LecturerCourseRecord(course, new Semester());

            student.addCourse(lecturerCourseRecord);
            studentCourseSize++;
            assertEquals(studentCourseSize, student.getStudentCourseRecords().size());
            assertTrue(student.isTakeCourse(new Course(courseCode)));
            assumingThat(courseCode.equals("101"), () -> assertEquals(Course.CourseType.MANDATORY, course.getCourseType()));
            assumingThat(courseCode.equals("103"), () -> assertEquals(Course.CourseType.ELECTIVE, course.getCourseType()));
            assumingThat(courseCode.equals("105"), () -> assertEquals(Course.CourseType.MANDATORY, course.getCourseType()));
        }

        @ParameterizedTest
        @CsvFileSource(resources = "/courseCodeAndTypes.csv", numLinesToSkip = 1) // numLinesToSkip: header skip
        void addCourseToStudentWithCsvFile(String courseCode, Course.CourseType courseType) {

            final Course course = new Course(courseCode);
            course.setCourseType(courseType);

            final LecturerCourseRecord lecturerCourseRecord = new LecturerCourseRecord(course, new Semester());

            student.addCourse(lecturerCourseRecord);
            studentCourseSize++;
            assertEquals(studentCourseSize, student.getStudentCourseRecords().size());
            assertTrue(student.isTakeCourse(new Course(courseCode)));
            assumingThat(courseCode.equals("101"), () -> assertEquals(Course.CourseType.MANDATORY, course.getCourseType()));
            assumingThat(courseCode.equals("103"), () -> assertEquals(Course.CourseType.ELECTIVE, course.getCourseType()));
            assumingThat(courseCode.equals("105"), () -> assertEquals(Course.CourseType.MANDATORY, course.getCourseType()));
        }
    }

    @TestInstance(TestInstance.Lifecycle.PER_CLASS) // hep aynı ogrenciyi kullanmak istiyoruz
    @Nested
    class ArgumentsSourceDemo {

        private int studentCourseSize = 0;

        @BeforeAll
        void beforeAll() {
            student = new Student("id1", "Ferat", "Akkoc");
        }

        @ParameterizedTest
        @ArgumentsSource(CourseCodeAndTypeProvider.class)
        void addCourseToStudent(String courseCode, Course.CourseType courseType) {

            final Course course = new Course(courseCode);
            course.setCourseType(courseType);

            final LecturerCourseRecord lecturerCourseRecord = new LecturerCourseRecord(course, new Semester());

            student.addCourse(lecturerCourseRecord);
            studentCourseSize++;
            assertEquals(studentCourseSize, student.getStudentCourseRecords().size());
            assertTrue(student.isTakeCourse(new Course(courseCode)));
            assumingThat(courseCode.equals("101"), () -> assertEquals(Course.CourseType.MANDATORY, course.getCourseType()));
            assumingThat(courseCode.equals("103"), () -> assertEquals(Course.CourseType.ELECTIVE, course.getCourseType()));
            assumingThat(courseCode.equals("105"), () -> assertEquals(Course.CourseType.MANDATORY, course.getCourseType()));
        }
    }

    static class CourseCodeAndTypeProvider implements ArgumentsProvider {
        @Override
        public Stream<? extends Arguments> provideArguments(final ExtensionContext extensionContext) throws Exception {
            return Stream.of(
                    Arguments.of("101", Course.CourseType.MANDATORY),
                    Arguments.of("103", Course.CourseType.ELECTIVE),
                    Arguments.of("105", Course.CourseType.MANDATORY));
        }
    }

    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @Nested
    class TypeConversionAndCustomDisplayNameDemo {

        @BeforeAll
        void beforeAll() {
            student = new Student("1", "Ferat", "Akkoç");
        }

        // ************* enum conversion *****************

        // CourseType içerisinde constructor yada static bir factory metod varsa ve bu stringleri parametre olarak alıyorsa
        // bu stringler enum'a conversion edilir.(implicit)
        @ParameterizedTest
        @ValueSource(strings = { "MANDATORY", "ELECTIVE" })
        void addCourseToStudent(Course.CourseType courseType) {
            Course course = Course.newCourse()
                    .withCode(String.valueOf(new Random().nextInt(200)))
                    .withCourseType(courseType)
                    .course();
            final LecturerCourseRecord lecturerCourseRecord = new LecturerCourseRecord(course, new Semester());
            student.addCourse(lecturerCourseRecord);

            assertFalse(student.getStudentCourseRecords().isEmpty());
            assertTrue(student.isTakeCourse(course));
        }

        // ************* user guide for other options *********************

        // *************** factory method of constructor converison ********

        // Course içerisindeki constructor'ı kullanarak, Course obj oluşturup, onu da metod'a gönderecek.
        @ParameterizedTest
        @ValueSource(strings = { "101", "103" })
        void addCourseToStudent(Course course) {
            final LecturerCourseRecord lecturerCourseRecord = new LecturerCourseRecord(course, new Semester());
            student.addCourse(lecturerCourseRecord);

            assertFalse(student.getStudentCourseRecords().isEmpty());
            assertTrue(student.isTakeCourse(course));
        }

        // ****************** conversion using SimpleConverter with @ConvertWith **************

        @ParameterizedTest
        @ValueSource(strings = { "101", "103", "105" })
        void addCourseToStudentWithConverter(@ConvertWith(CourseConverter.class) Course course) {
            final LecturerCourseRecord lecturerCourseRecord = new LecturerCourseRecord(course, new Semester());
            student.addCourse(lecturerCourseRecord);

            assertFalse(student.getStudentCourseRecords().isEmpty());
            assertTrue(student.isTakeCourse(course));
        }

        // ****************** conversion with @JavaTimeConversionPattern *************************
        @ValueSource(strings = { "01.09.2018", "01.01.2018", "01.06.2018" })
        void addCourseToStudentWithLocalDate(@JavaTimeConversionPattern("dd.MM.yyyy") LocalDate localDate) {

            Course course = Course.newCourse().withCode(String.valueOf(new Random().nextInt(200))).course();

            final LecturerCourseRecord lecturerCourseRecord = new LecturerCourseRecord(course, new Semester(localDate));
            student.addCourse(lecturerCourseRecord);

            assertFalse(student.getStudentCourseRecords().isEmpty());
            assertTrue(student.isTakeCourse(course));
        }

        // display name {index}, {arguments}, {0} usage
        @DisplayName("Add course with localdate info")
        @ParameterizedTest(name = "{index} ==> Parametreler: {arguments}")
        @ValueSource(strings = { "01.09.2018", "01.01.2018", "01.06.2018" })
        void addCourseToStudentWithLocalDateAndCustomDisplayName(@JavaTimeConversionPattern("dd.MM.yyyy") LocalDate localDate) {

            Course course = Course.newCourse().withCode(String.valueOf(new Random().nextInt(200))).course();

            final LecturerCourseRecord lecturerCourseRecord = new LecturerCourseRecord(course, new Semester(localDate));
            student.addCourse(lecturerCourseRecord);

            assertFalse(student.getStudentCourseRecords().isEmpty());
            assertTrue(student.isTakeCourse(course));
        }

    }

    static class CourseConverter extends SimpleArgumentConverter {
        @Override
        protected Object convert(final Object o, final Class<?> aClass) throws ArgumentConversionException {
            return new Course((String) o);
        }
    }
}
