package org.unittest.courserecord.model;

import java.util.List;

import org.junit.jupiter.api.extension.ConditionEvaluationResult;
import org.junit.jupiter.api.extension.ExecutionCondition;
import org.junit.jupiter.api.extension.ExtensionContext;

public class DropCourseConditionExtension implements ExecutionCondition {

    // Sadece dropCourse testlerinin çalışmasına izin verir.

    @Override
    public ConditionEvaluationResult evaluateExecutionCondition(final ExtensionContext context) {
        if (List.of("Student").containsAll(context.getTags()) ||
                List.of("Student", "dropCourse","calculateGpa").containsAll(context.getTags())) {
            return ConditionEvaluationResult.enabled("Drop course is enabled");
        }
        return ConditionEvaluationResult.disabled("Only drop course allowed to run");
    }
}
