package com.pradatta.amlscreening.service;

import com.pradatta.amlscreening.service.util.StringUtil;
import org.apache.commons.lang3.StringUtils;

public class Soundex {

    /**
     * The complete algorithm to find soundex code is as below:
     * <p>
     * Retain the first letter of the name and drop all other occurrences of a, e, i, o, u, y, h, w.
     * Replace consonants with digits as follows (after the first letter):
     * b, f, p, v → 1
     * c, g, j, k, q, s, x, z → 2
     * d, t → 3
     * l → 4
     * m, n → 5
     * r → 6
     * If two or more letters with the same number are adjacent in the original name (before step 1), only retain the first letter;
     * also two letters with the same number separated by ‘h’ or ‘w’ are coded as a single number,
     * whereas such letters separated by a vowel are coded twice. This rule also applies to the first letter.
     * <p>
     * Iterate the previous step until you have one letter and three numbers. If you have too few letters in your word that you can’t assign three numbers,
     * append with zeros until there are three numbers. If you have more than 3 letters, just retain the first 3 numbers.
     * <a href="https://en.wikipedia.org/wiki/Soundex">link</a>
     *
     * @param string String to calculate Soundex
     * @return Soundex Code
     */
    public static String getCode(String string) {
        string = StringUtil.removeSpecialCharacters(string);
        if (StringUtils.isBlank(string) || StringUtils.isEmpty(string)) {
            return null;
        }

        char[] charArray = string.toUpperCase().toCharArray();

        //Keep the first letter
        char firstLetter = charArray[0];

        //Convert letters to numeric code
        for (int i = 0; i < charArray.length; i++) {
            switch (charArray[i]) {
                case 'B', 'F', 'P', 'V' -> charArray[i] = '1';
                case 'C', 'G', 'J', 'K', 'Q', 'S', 'X', 'Z' -> charArray[i] = '2';
                case 'D', 'T' -> charArray[i] = '3';
                case 'L' -> charArray[i] = '4';
                case 'M', 'N' -> charArray[i] = '5';
                case 'R' -> charArray[i] = '6';
                default -> charArray[i] = '0';
            }
        }

        //Remove duplicates
        //Keeping First letter as is
        StringBuilder output = new StringBuilder(String.valueOf(firstLetter));

        for (int i = 1; i < charArray.length; i++) {
            if (charArray[i]!=charArray[i - 1] && charArray[i]!='0')
                output.append(charArray[i]);
        }

        //Pad with 0's or substring
        output.append("0000");
        return output.substring(0, 4);
    }
}
