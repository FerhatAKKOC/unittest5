package org.unittest.junit.basic.displayname;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Hesap Makinesi Testleri")
public class CalculatorTest {

    @Test
    @DisplayName("Tam sayıların toplanması senaryosu")
    void additionTwoInteger() {
    }

    @Test
    @DisplayName("Tam sayıların çıkarması senaryosu")
    void substractionTwoInteger() {
    }
}
