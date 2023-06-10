package com.pradatta.amlscreening.service;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static com.pradatta.amlscreening.service.NameConstant.NAME_AFFIXES;
import static com.pradatta.amlscreening.service.NameConstant.NOISE_WORD;

@Component
public class AmlScreeningService {



    public boolean checkSanctionListForName(String name) {
        String[] nameParts = splitNameParts(name);
        List<String> cleanedUpName = removeCommonWords(nameParts);

        findAllPossibleMatch(cleanedUpName);

        return false;
    }

    private void findAllPossibleMatch(List<String> cleanedUpName) {

    }

    private List<String> removeCommonWords(String[] nameParts) {
        List<String> newNameParts = new ArrayList<>();
        for (String namePart : nameParts) {
            if (!NAME_AFFIXES.contains(namePart.toLowerCase()) && !NOISE_WORD.contains(namePart.toLowerCase())) {
                newNameParts.add(namePart);
            }
        }
        return newNameParts;
    }

    private String[] splitNameParts(String name) {
        return name.split("\\s+");
    }

    public static void main(String[] args) {
        AmlScreeningService amlScreeningService = new AmlScreeningService();
        String[] strings = amlScreeningService.splitNameParts("Mr Pradatta the Adhikary");
        List<String> strings1 = amlScreeningService.removeCommonWords(strings);
        System.out.println("strings1 = " + strings1);
    }
}
