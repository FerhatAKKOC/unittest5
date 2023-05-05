package org.unittest.hamcrest;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.either;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

public class HamcrestTest {

    @Test
    void testTemelEslestirme() {

        String text1 = "Baris";
        String text2 = "Baris2";

        assertEquals("Baris", text1);

        assertThat(text1, is(equalTo("Baris")));
        assertThat(text1, is(notNullValue()));
        assertThat(text1, containsString("ris"));
        assertThat(text1, anyOf(containsString("ris"), containsString("Bar")));
    }

    @Test
    void testListeler() {

        List<String> sehirler = List.of("Istanbul", "Ankara", "Izmir");

        assertThat(sehirler, hasItem("Istanbul"));
        assertThat(sehirler, hasItems("Istanbul", "Ankara"));
        assertThat(sehirler, allOf(hasItems("Istanbul", "Ankara"), not(hasItem("Bursa"))));
        assertThat(sehirler, either(hasItems("Istanbul", "Izmir")).or(not(hasItem("Bursa"))));

    }
}
