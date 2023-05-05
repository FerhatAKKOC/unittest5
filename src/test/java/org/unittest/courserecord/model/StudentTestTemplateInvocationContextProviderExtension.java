package org.unittest.courserecord.model;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.extension.Extension;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.jupiter.api.extension.TestTemplateInvocationContext;
import org.junit.jupiter.api.extension.TestTemplateInvocationContextProvider;

public class StudentTestTemplateInvocationContextProviderExtension implements TestTemplateInvocationContextProvider {

    @Override
    public boolean supportsTestTemplate(final ExtensionContext context) {
        return context.getRequiredTestClass() == StudentWithTemplateTest.class;
    }

    @Override
    public Stream<TestTemplateInvocationContext> provideTestTemplateInvocationContexts(final ExtensionContext extensionContext) {
        return Stream.of(
                testTemplateInvocationContext("101", 1),
                testTemplateInvocationContext("103", 2),
                testTemplateInvocationContext("105", 3),
                testTemplateInvocationContext("107", 4),
                testTemplateInvocationContext("109", 5));
    }

    private TestTemplateInvocationContext testTemplateInvocationContext(String courseCode, int numberOfInvocation) {
        return new TestTemplateInvocationContext() {
            @Override
            public String getDisplayName(final int invocationIndex) {
                return "Add Course to Student ==> Add one course to student and student has " + invocationIndex + " courses";
            }

            @Override
            public List<Extension> getAdditionalExtensions() {
                return Collections.singletonList(new ParameterResolver() {
                    @Override
                    public boolean supportsParameter(final ParameterContext parameterContext, final ExtensionContext extensionContext) throws ParameterResolutionException {
                        if (parameterContext.getIndex() == 0 && parameterContext.getParameter().getType() == LecturerCourseRecord.class) {
                            return true;
                        }
                        if (parameterContext.getIndex() == 1 && parameterContext.getParameter().getType() == int.class) {
                            return true;
                        }
                        return false;
                    }

                    @Override
                    public Object resolveParameter(final ParameterContext parameterContext, final ExtensionContext extensionContext) throws ParameterResolutionException {
                        if (parameterContext.getParameter().getType() == LecturerCourseRecord.class) {
                            return new LecturerCourseRecord(new Course(courseCode), new Semester());
                        }
                        if (parameterContext.getParameter().getType() == int.class) {
                            return numberOfInvocation;
                        }
                        throw new IllegalArgumentException("not supporter parameter");
                    }
                });
            }
        };
    }
}
