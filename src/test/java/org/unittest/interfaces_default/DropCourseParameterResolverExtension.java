package org.unittest.interfaces_default;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.unittest.courserecord.model.Semester;
import org.unittest.courserecord.model.Student;

import lombok.extern.java.Log;

@Log
public class DropCourseParameterResolverExtension implements ParameterResolver {

    @Override
    public boolean supportsParameter(final ParameterContext param, final ExtensionContext context) throws ParameterResolutionException {

        log.info("Type : " + param.getParameter().getType().getName());
        if (param.getParameter().getType() == Student.class && param.getIndex() == 1) {
            return true;
        }
        if (param.getParameter().getType() == Semester.class && List.of(2, 3, 4).contains(param.getIndex())) {
            return true;
        }
        return false;
    }

    @Override
    public Object resolveParameter(final ParameterContext param, final ExtensionContext context) throws ParameterResolutionException {

        log.info("resolveParameter : " + param.getParameter().getType().getName());

        if (param.getParameter().getType() == Student.class ) {
            return new Student("id1", "Ferat", "Akkoc");
        }
        if (param.getParameter().getType() == Semester.class) {
            switch (param.getIndex()) {
                case 2:
                    return addDropPeriodOpenSemester();
                case 3:
                    return notActiveSemester();
                case 4:
                    return notActiveSemester();
            }
        }
        throw new IllegalArgumentException("Couldn't create not supported paramters");
    }

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
