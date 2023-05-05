package org.unittest.courserecord.model;

import java.util.logging.Logger;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public class TestLoggerExtention implements BeforeAllCallback, AfterAllCallback {

    private static Logger log = Logger.getLogger((TestLoggerExtention.class.getName()));

    @Override
    public void afterAll(final ExtensionContext context) throws Exception {
        log.info(String.format("Test is started...%s",context.getDisplayName()));

    }

    @Override
    public void beforeAll(final ExtensionContext context) throws Exception {
        log.info(String.format("Test is ended...%s",context.getDisplayName()));
    }
}
