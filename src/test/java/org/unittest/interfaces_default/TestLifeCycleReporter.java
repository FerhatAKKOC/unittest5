package org.unittest.interfaces_default;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.TestReporter;

public interface TestLifeCycleReporter {

    @BeforeEach
    default void reportStart(TestInfo info, TestReporter reporter) {
        reporter.publishEntry("Start", info.getTestMethod().get().getName());
    }


    @AfterEach
    default void reportEnd(TestInfo info, TestReporter reporter) {
        reporter.publishEntry("End", info.getTestMethod().get().getName());
    }
}
