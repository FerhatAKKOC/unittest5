package org.unittest.courserecord.model;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import lombok.extern.java.Log;

@Log
public class BenchmarkCallbackExtension implements BeforeAllCallback, AfterAllCallback, BeforeTestExecutionCallback, AfterTestExecutionCallback {

    public static final String START_TIME = "StartTime";

    @Override
    public void beforeAll(final ExtensionContext context) throws Exception {
        getStoreForContainer(context).put(START_TIME, LocalDateTime.now());
    }

    @Override
    public void afterAll(final ExtensionContext context) throws Exception {
        final LocalDateTime startTime = getStoreForContainer(context).remove(START_TIME, LocalDateTime.class);
        final long runTime = startTime.until(LocalDateTime.now(), ChronoUnit.MILLIS);
        log.info(String.format("Run time for test container<%s>: %s ms",
                context.getRequiredTestClass().getSimpleName(), runTime));
    }

    @Override
    public void beforeTestExecution(final ExtensionContext context) throws Exception {
        getStoreForMethod(context).put(START_TIME, LocalDateTime.now());
    }

    @Override
    public void afterTestExecution(final ExtensionContext context) throws Exception {
        final LocalDateTime startTime = getStoreForMethod(context).remove(START_TIME, LocalDateTime.class);
        final long runTime = startTime.until(LocalDateTime.now(), ChronoUnit.MILLIS);
        log.info(String.format("Run time for test method<%s>: %s ms",
                context.getRequiredTestMethod().getName(), runTime));
    }

    private ExtensionContext.Store getStoreForContainer(final ExtensionContext context) {
        return context.getStore(ExtensionContext.Namespace.create(getClass(), context.getRequiredTestClass()));
    }

    private ExtensionContext.Store getStoreForMethod(final ExtensionContext context) {
        return context.getStore(ExtensionContext.Namespace.create(getClass(), context.getRequiredTestMethod()));
    }
}
