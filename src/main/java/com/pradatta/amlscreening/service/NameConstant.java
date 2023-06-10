package com.pradatta.amlscreening.service;

import java.util.Arrays;
import java.util.List;

public class NameConstant {
    //Limited lists if Name Affixes for the purpose of the test. Should be much bigger, and preferably stored in DB
    public static List<String> NAME_AFFIXES = Arrays.asList("mr", "mrs", "miss", "ms", "mx", "sir", "von", "dame", "dr",
                                                             "cllr", "lady", "lord");

    //Limited lists if Noise word for the purpose of the test. Should be much bigger, and preferably stored in DB
    public static List<String> NOISE_WORD = Arrays.asList("to", "the", "and", "van", "king", "sir", "general", "president", "lieutenant");
}
