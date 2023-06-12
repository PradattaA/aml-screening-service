package com.pradatta.amlscreening.service;

import com.pradatta.amlscreening.jpa.datamodel.FinancialSanctionedEntity;
import com.pradatta.amlscreening.service.util.StringUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.*;

import static com.pradatta.amlscreening.jpa.datamodel.SanctionEntityType.PERSON;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FuzzyNameSearchTest {

    FuzzyNameSearch fuzzyNameSearch;

    @BeforeEach
    void setUp() throws IOException {
        fuzzyNameSearch = new FuzzyNameSearch();
        org.springframework.test.util.ReflectionTestUtils.setField(fuzzyNameSearch, "firstNameWeight", 30);
        org.springframework.test.util.ReflectionTestUtils.setField(fuzzyNameSearch, "middleNameWeight", 10);
        org.springframework.test.util.ReflectionTestUtils.setField(fuzzyNameSearch, "lastNameWeight", 20);
        org.springframework.test.util.ReflectionTestUtils.setField(fuzzyNameSearch, "minMatchThreshold", 0.7);
    }

    @Test
    @DisplayName("Fuzzy Search works")
    void search() {
        //given
        Collection<FinancialSanctionedEntity> financialSanctionedEntities = List.of(new FinancialSanctionedEntity(
                PERSON, new Random().nextLong(), "Osama", "Bin", "Laden", "Osama Bin Laden",
                new Date(), "www.testurl.com"));

        fuzzyNameSearch.init(financialSanctionedEntities);

        //when
        String name = "Osamma bin Laden";
        name = StringUtil.removeSpecialCharacters(name).toLowerCase();

        String[] nameParts = StringUtil.splitNameParts(name);

        List<String> cleanedUpName = StringUtil.removeCommonWords(nameParts);
        List<com.pradatta.amlscreening.api.response.FinancialSanctionedEntity> result = fuzzyNameSearch.search(
                cleanedUpName);


        //then
        assertEquals(1, result.size());

        double matchScore = result.get(0).getMatchScore();

        assertTrue(matchScore >= 0.9, "Match Score (" + matchScore + ") should be greater than current (0.9)");
    }
}