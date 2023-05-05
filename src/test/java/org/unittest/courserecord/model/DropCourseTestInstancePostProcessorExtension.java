package org.unittest.courserecord.model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestInstancePostProcessor;

import lombok.extern.java.Log;

@Log
public class DropCourseTestInstancePostProcessorExtension implements TestInstancePostProcessor {

    @Override
    public void postProcessTestInstance(final Object testInstance, final ExtensionContext context) throws Exception {
        final StudentWithTestInstancePostProcessor.DropCourseFromStudent dropCourseFromStuden = (StudentWithTestInstancePostProcessor.DropCourseFromStudent) testInstance;
        log.info("Student Ahmet is injected!");
        dropCourseFromStuden.studentAhmet = new Student("id1", "Ferat", "Akkoc");

        dropCourseFromStuden.addDropPeriodOpenSemester = addDropPeriodOpenSemester();
        dropCourseFromStuden.addDropPeriodClosedSemester = addDropPeriodClosedSemester();
        dropCourseFromStuden.notActiveSemester = notActiveSemester();
    }

    // ders ekleme çıkarma dönemi açık olan
    private Semester addDropPeriodOpenSemester() {
        final Semester activeSemester = new Semester();
        final LocalDate semesterDate = LocalDate.of(activeSemester.getYear(), activeSemester.getTerm().getStartMonth(), 1);
        final LocalDate now = LocalDate.now();
        activeSemester.setAddDropPeriodInWeek(Long.valueOf(semesterDate.until(now, ChronoUnit.WEEKS) + 1).intValue());
        return activeSemester;
    }

    private Semester addDropPeriodClosedSemester() {
        final Semester activeSemester = new Semester();
        activeSemester.setAddDropPeriodInWeek(0);
        if (LocalDate.now().getDayOfMonth() == 1) {
            activeSemester.setAddDropPeriodInWeek(-1);
        }
        return activeSemester;
    }

    private Semester notActiveSemester() {
        final Semester activeSemester = new Semester();
        return new Semester(LocalDate.of(activeSemester.getYear() - 1, 1, 1));
    }
}
