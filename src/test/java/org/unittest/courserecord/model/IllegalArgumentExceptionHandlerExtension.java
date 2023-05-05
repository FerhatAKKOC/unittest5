package org.unittest.courserecord.model;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestExecutionExceptionHandler;

import lombok.extern.java.Log;

@Log
public class IllegalArgumentExceptionHandlerExtension implements TestExecutionExceptionHandler {

    @Override
    public void handleTestExecutionException(final ExtensionContext context, final Throwable throwable) throws Throwable {
        log.severe(String.format("IllegalArgumentException was thrown by a method %s " +
                "with description: %s", context.getRequiredTestMethod().getName(), throwable.getMessage()));
//        throw throwable;
    }

}
