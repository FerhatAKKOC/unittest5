package org.unittest.courserecord.model;


import static java.lang.annotation.ElementType.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIf;
import org.junit.jupiter.api.condition.DisabledIfEnvironmentVariable;
import org.junit.jupiter.api.condition.DisabledIfSystemProperty;
import org.junit.jupiter.api.condition.DisabledOnJre;
import org.junit.jupiter.api.condition.DisabledOnOs;
import org.junit.jupiter.api.condition.EnabledIf;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import org.junit.jupiter.api.condition.EnabledOnJre;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.JRE;
import org.junit.jupiter.api.condition.OS;

public class ConditionalStudentTest {

    // run on MAC os
    @EnabledOnOs({OS.MAC})
    @Test
    void shouldCreateStudentOnlyOnMac() {
        final Student ahmet = new Student("1", "Ahmet", "Can");
        assertAll("Student Information",
                () -> assertEquals("Ahmet", ahmet.getName()),
                () -> assertEquals("Can", ahmet.getSurname()),
                () -> assertEquals("1", ahmet.getId())
        );
    }

    @DisabledOnOs({OS.MAC})
    @Test
    void shouldCreateStudentOnlyOnNonMac() {
        final Student ahmet = new Student("1", "Ahmet", "Can");
        assertAll("Student Information",
                () -> assertEquals("Ahmet", ahmet.getName()),
                () -> assertEquals("Can", ahmet.getSurname()),
                () -> assertEquals("1", ahmet.getId())
        );
    }

    @EnabledOnJre(JRE.JAVA_10)
    @Test
    void shouldCreateStudentOnlyOnJRE10() {
        final Student ahmet = new Student("1", "Ahmet", "Can");
        assertAll("Student Information",
                () -> assertEquals("Ahmet", ahmet.getName()),
                () -> assertEquals("Can", ahmet.getSurname()),
                () -> assertEquals("1", ahmet.getId())
        );
    }

    @EnabledOnJre({JRE.JAVA_9, JRE.JAVA_10})
    @Test
    void shouldCreateStudentOnlyOnJRE9orJRE10() {
        final Student ahmet = new Student("1", "Ahmet", "Can");
        assertAll("Student Information",
                () -> assertEquals("Ahmet", ahmet.getName()),
                () -> assertEquals("Can", ahmet.getSurname()),
                () -> assertEquals("1", ahmet.getId())
        );
    }

    @DisabledOnJre(JRE.JAVA_10)
    @Test
    void shouldCreateStudentOnlyOnNonJRE10() {
        final Student ahmet = new Student("1", "Ahmet", "Can");
        assertAll("Student Information",
                () -> assertEquals("Ahmet", ahmet.getName()),
                () -> assertEquals("Can", ahmet.getSurname()),
                () -> assertEquals("1", ahmet.getId())
        );
    }

    @EnabledIfSystemProperty(named = "os.arch", matches = ".*64.*")
    @Test
    void shouldCreateStudentOnlyOn64Architectures() {
        final Student ahmet = new Student("1", "Ahmet", "Can");
        assertAll("Student Information",
                () -> assertEquals("Ahmet", ahmet.getName()),
                () -> assertEquals("Can", ahmet.getSurname()),
                () -> assertEquals("1", ahmet.getId())
        );
    }

    @DisabledIfSystemProperty(named = "ENV", matches = "dev")
    @Test
    void shouldCreateStudentOnlyOnDevMachine() {
        final Student ahmet = new Student("1", "Ahmet", "Can");
        assertAll("Student Information",
                () -> assertEquals("Ahmet", ahmet.getName()),
                () -> assertEquals("Can", ahmet.getSurname()),
                () -> assertEquals("1", ahmet.getId())
        );
    }


    @EnabledIfEnvironmentVariable(named = "ENV", matches = "staging-server")
    @Test
    void shouldCreateStudentOnlyOnStagingEnv() {
        final Student ahmet = new Student("1", "Ahmet", "Can");
        assertAll("Student Information",
                () -> assertEquals("Ahmet", ahmet.getName()),
                () -> assertEquals("Can", ahmet.getSurname()),
                () -> assertEquals("1", ahmet.getId())
        );
    }

    @DisabledIfEnvironmentVariable(named = "ENV", matches = "staging-server")
    @Test
    void shouldCreateStudentOnlyOnNonCIEnv() {
        final Student ahmet = new Student("1", "Ahmet", "Can");
        assertAll("Student Information",
                () -> assertEquals("Ahmet", ahmet.getName()),
                () -> assertEquals("Can", ahmet.getSurname()),
                () -> assertEquals("1", ahmet.getId())
        );
    }

    @EnabledIf("2 * 3 == 6") // Static JavaScript expression.
    @Test
    void shouldCreateStudentIfStaticJSExpressionIsEvaluatedToTrue() {
        final Student ahmet = new Student("1", "Ahmet", "Can");
        assertAll("Student Information",
                () -> assertEquals("Ahmet", ahmet.getName()),
                () -> assertEquals("Can", ahmet.getSurname()),
                () -> assertEquals("1", ahmet.getId())
        );
    }

    @DisabledIf("Math.random() < 0.314159") // Dynamic JavaScript expression.
    @Test
    void shouldCreateStudentIfDynamicJSExpressionIsEvaluatedToTrue() {
        final Student ahmet = new Student("1", "Ahmet", "Can");
        assertAll("Student Information",
                () -> assertEquals("Ahmet", ahmet.getName()),
                () -> assertEquals("Can", ahmet.getSurname()),
                () -> assertEquals("1", ahmet.getId())
        );
    }

    @DisabledIf("/64/.test(systemProperty.get('os.arch'))") // Regular expression testing bound system property.
    @Test
    void shouldCreateStudentOnlyOn32BitArchitectures() {
        final Student ahmet = new Student("1", "Ahmet", "Can");
        assertAll("Student Information",
                () -> assertEquals("Ahmet", ahmet.getName()),
                () -> assertEquals("Can", ahmet.getSurname()),
                () -> assertEquals("1", ahmet.getId())
        );
    }

    @EnabledIf("'staging-server' == systemEnvironment.get('ENV')")
    @Test
    void shouldCreateStudentOnlyOnStagingEnvEvaluatedWithJS() {
        final Student ahmet = new Student("1", "Ahmet", "Can");
        assertAll("Student Information",
                () -> assertEquals("Ahmet", ahmet.getName()),
                () -> assertEquals("Can", ahmet.getSurname()),
                () -> assertEquals("1", ahmet.getId())
        );
    }

    @TestOnMacWithJRE10
    void shouldCreateStudentOnlyOnMacWithJRE10() {
        final Student ahmet = new Student("1", "Ahmet", "Can");
        assertAll("Student Information",
                () -> assertEquals("Ahmet", ahmet.getName()),
                () -> assertEquals("Can", ahmet.getSurname()),
                () -> assertEquals("1", ahmet.getId())
        );
    }

    @Target(METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @Test
    @EnabledOnOs(OS.MAC)
    @EnabledOnJre(JRE.JAVA_9)
    @interface TestOnMacWithJRE10 {
    }

}
