package com.pradatta.amlscreening.service.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class StringUtilTest {

    @Test
    void removeSpecialCharacters() {
    }

    @Test
    void removeCommonWords() {
    }

    @Test
    void splitNameParts() {
    }

    @ParameterizedTest(name = "When string1 {0} & string2 {1} then Expected distance is: {2}")
    @DisplayName("Optimal String Alignment Distance is returned as expected")
    @CsvSource(value = {
            "Harry Potter           | Harry Poter   |  1",
            "HarryPotter            | Harry Poter   |  2",
            "Ron                    | Ronald        |  3",
            "abcd                   | abef          |  2"
    }, delimiter = '|')
    void optimalStringAlignmentDistance(String s1, String s2, int expectedDistance) {
        int distance = StringUtil.optimalStringAlignmentDistance(s1, s2);
        assertEquals(expectedDistance, distance);
    }
}