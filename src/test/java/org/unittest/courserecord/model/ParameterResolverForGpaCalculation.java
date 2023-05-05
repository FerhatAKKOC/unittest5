package org.unittest.courserecord.model;

import java.math.BigDecimal;
import java.util.Map;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

public class ParameterResolverForGpaCalculation implements ParameterResolver {
    @Override
    public boolean supportsParameter(final ParameterContext parameterContext, final ExtensionContext extensionContext) throws ParameterResolutionException {

        if (parameterContext.getParameter().getType() == Student.class && parameterContext.getIndex() == 0) {
            return true;
        }
        if (parameterContext.getParameter().getType() == Map.class && parameterContext.getIndex() == 1) {
            return true;
        }
        if (parameterContext.getParameter().getType() == BigDecimal.class && parameterContext.getIndex() == 2) {
            return true;
        }
        return false;
    }

    @Override
    public Object resolveParameter(final ParameterContext parameterContext, final ExtensionContext extensionContext) throws ParameterResolutionException {

        if (parameterContext.getParameter().getType() == Student.class) {
            return new Student("id1", "Ferat", "Akkoc");
        }
        if (parameterContext.getParameter().getType() == Map.class) {
            final LecturerCourseRecord lecturerCourseRecord101 = new LecturerCourseRecord(new Course("101"), new Semester());
            lecturerCourseRecord101.setCredit(3);
            final LecturerCourseRecord lecturerCourseRecord103 = new LecturerCourseRecord(new Course("103"), new Semester());
            lecturerCourseRecord103.setCredit(2);
            final LecturerCourseRecord lecturerCourseRecord105 = new LecturerCourseRecord(new Course("105"), new Semester());
            lecturerCourseRecord105.setCredit(1);

            return Map.of(
                    lecturerCourseRecord101, StudentCourseRecord.Grade.A1,
                    lecturerCourseRecord103, StudentCourseRecord.Grade.B1,
                    lecturerCourseRecord105, StudentCourseRecord.Grade.C);
        }
        if (parameterContext.getParameter().getType() == BigDecimal.class) {
            return BigDecimal.valueOf(3.33);
        }

        throw new IllegalArgumentException("not supported parameter");
    }
}
