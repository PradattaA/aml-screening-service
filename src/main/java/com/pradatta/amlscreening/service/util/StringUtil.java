package com.pradatta.amlscreening.service.util;

import com.pradatta.amlscreening.service.NameConstant;

import java.util.ArrayList;
import java.util.List;

public class StringUtil {

    public static String removeSpecialCharacters(String name) {
        //Convert , to space and then remove all special character except while space
        return name.stripLeading().stripTrailing().replaceAll(",", " ")
                .replaceAll("[^a-zA-Z\\s,]", "");
    }

    public static String removeSpaces(String name) {
        return name.replaceAll("[^a-zA-Z,]", "");
    }

    public static List<String> removeCommonWords(String[] nameParts) {
        List<String> newNameParts = new ArrayList<String>();
        for (String namePart : nameParts) {
            if (!NameConstant.NAME_AFFIXES.contains(namePart.toLowerCase()) && !NameConstant.NOISE_WORD.contains(
                    namePart.toLowerCase())) {
                newNameParts.add(namePart);
            }
        }
        return newNameParts;
    }

    /**
     * Removed special character and common name affixes and words
     * Return concatenated names in lower case
     * @param name Name
     * @return Return processed name string
     */
    public static String cleanUpNamesForMatching(String name) {
        String s = removeSpecialCharacters(name);
        List<String> cleanUpNamedParts = removeCommonWords(splitNameParts(s));

        StringBuilder matchReadyString = new StringBuilder();
        for (String cleanUpNamedPart : cleanUpNamedParts) {
            matchReadyString.append(cleanUpNamedPart.toLowerCase());
            matchReadyString.append(" ");
        }
        return matchReadyString.toString();
    }

    public static String[] splitNameParts(String name) {
        return name.split("\\s+");
    }

    /**
     * Used algorithm : <a href="https://en.wikipedia.org/wiki/Damerau–Levenshtein_distance">Damerau–Levenshtein_distance</a>
     *
     * @param s1
     * @param s2
     * @return
     */
    public static int optimalStringAlignmentDistance(String s1, String s2) {
        int[][] dp = new int[s1.length() + 1][s2.length() + 1];

        for (int i = 0; i <= s1.length(); i++) {
            dp[i][0] = i;
        }
        for (int j = 0; j <= s2.length(); j++) {
            dp[0][j] = j;
        }

        for (int i = 1; i <= s1.length(); i++) {
            for (int j = 1; j <= s2.length(); j++) {
                if (s1.charAt(i - 1)==s2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    dp[i][j] = 1 + Math.min(Math.min(dp[i - 1][j], dp[i][j - 1]), dp[i - 1][j - 1]);
                }
            }
        }

        return dp[s1.length()][s2.length()];
    }

}