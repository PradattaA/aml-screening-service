package com.pradatta.amlscreening.service;

import com.pradatta.amlscreening.api.response.AmlScreeningResponse;
import com.pradatta.amlscreening.api.response.ScreeningStatusColour;
import com.pradatta.amlscreening.api.response.builder.AmlScreeningResponseBuilder;
import com.pradatta.amlscreening.jpa.datamodel.FinancialSanctionedEntity;
import com.pradatta.amlscreening.jpa.repository.FinancialSanctionedEntityRepository;
import com.pradatta.amlscreening.service.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.*;

import static com.pradatta.amlscreening.api.response.ScreeningStatusColour.*;

@Component
public class AmlScreeningService {

    private static final Logger log = LoggerFactory.getLogger(AmlScreeningService.class);

    @Value("${aml-screening.fuzzymatch.match-threshold:0.85}")
    private double matchThreshold;

    @Autowired
    FinancialSanctionedEntityRepository financialSanctionedEntityRepository;

    @Autowired
    FuzzyNameSearch fuzzyNameSearch;

    public AmlScreeningResponse checkSanctionListForName(String name) {
        name = StringUtil.removeSpecialCharacters(name).toLowerCase();

        String[] nameParts = StringUtil.splitNameParts(name);

        List<String> cleanedUpName = StringUtil.removeCommonWords(nameParts);

        Collection<FinancialSanctionedEntity> allPossibleMatch = findAllPossibleMatch(cleanedUpName);

        fuzzyNameSearch.init(allPossibleMatch);

        List<com.pradatta.amlscreening.api.response.FinancialSanctionedEntity> searchResult = fuzzyNameSearch.search(
                cleanedUpName);

        return buildAmlScreeningResponse(searchResult);
    }

    private AmlScreeningResponse buildAmlScreeningResponse(
            List<com.pradatta.amlscreening.api.response.FinancialSanctionedEntity> searchResult) {
        if (searchResult.isEmpty()) {
            return new AmlScreeningResponseBuilder()
                    .setMatched(false)
                    .setMatchScore(0)
                    .setStatusColour(GREEN)
                    .setFinancialSanctionedEntities(searchResult)
                    .createAmlScreeningResponse();
        } else {
            com.pradatta.amlscreening.api.response.FinancialSanctionedEntity maxConfidenceEntity = Collections.max(
                    searchResult,
                    (o1, o2) -> (int) ((o1.getMatchScore() * 10) - (o2.getMatchScore() * 10)));
            ScreeningStatusColour statusColour = maxConfidenceEntity.getMatchScore() >= matchThreshold ?
                    RED:AMBER;

            return new AmlScreeningResponseBuilder()
                    .setMatched(true)
                    .setMatchScore(maxConfidenceEntity.getMatchScore())
                    .setStatusColour(statusColour)
                    .setFinancialSanctionedEntities(searchResult)
                    .createAmlScreeningResponse();
        }
    }


    /**
     * Input names can be in multiple format
     * <p>
     * firstName middleName lastName
     * firstName lastName
     * firstName lastName last Name
     * lastName firstname middleName
     * lastName firstname
     * Let's limit the assumption to only these for the sake of this tasks
     * We should consider more different format as they appear in real life for the prod code
     *
     * @param cleanedUpName Cleaned Up Name (without special characters or common/affixes words)
     */
    private Collection<FinancialSanctionedEntity> findAllPossibleMatch(List<String> cleanedUpName) {

        Set<FinancialSanctionedEntity> matchedEntity = new HashSet<>();

        if (cleanedUpName.size()==1) {
            //Only one part
            //Check with both First Name & Last Name
            String firstPart = cleanedUpName.get(0);
            String firstPartSoundex = Soundex.getCode(firstPart);

            // soundex(firstPart) --> firstNameSoundex
            // soundex(firstPart) --> lastNameSoundex
            // Collect results
            matchedEntity.addAll(financialSanctionedEntityRepository.findByFirstNameSoundex(
                    firstPartSoundex));
            matchedEntity.addAll(financialSanctionedEntityRepository.findByLastNameSoundex(
                    firstPartSoundex));
            matchedEntity.addAll(financialSanctionedEntityRepository.findByWholeNameSoundex(
                    firstPartSoundex));
            matchedEntity.addAll(financialSanctionedEntityRepository.findByWholeNameContaining(
                    firstPart));
        } else if (cleanedUpName.size()==2) {
            //Only First Name & Last Name
            //Need to Check reverse order
            String firstPart = cleanedUpName.get(0);
            String secondPart = cleanedUpName.get(1);

            String firstPartSoundex = Soundex.getCode(firstPart);
            String secondPartSoundex = Soundex.getCode(secondPart);

            // firstName lastName
            // soundex(firstPart) --> firstNameSoundex
            // soundex(secondPart) --> lastNameSoundex
            // Collect results
            matchedEntity.addAll(financialSanctionedEntityRepository.findByFirstNameSoundex(
                    firstPartSoundex));
            matchedEntity.addAll(financialSanctionedEntityRepository.findByLastNameSoundex(
                    secondPartSoundex));


            // lastName firstname
            // soundex(firstPart) --> lastNameSoundex
            // soundex(secondPart) --> firstNameSoundex
            // Collect results
            matchedEntity.addAll(financialSanctionedEntityRepository.findByLastNameSoundex(
                    firstPartSoundex));
            matchedEntity.addAll(financialSanctionedEntityRepository.findByFirstNameSoundex(
                    secondPartSoundex));


        } else if (cleanedUpName.size()==3) {

            String firstPart = cleanedUpName.get(0);
            String secondPart = cleanedUpName.get(1);
            String thirdPart = cleanedUpName.get(2);

            String firstPartSoundex = Soundex.getCode(firstPart);
            String secondPartSoundex = Soundex.getCode(secondPart);
            String thirdPartSoundex = Soundex.getCode(thirdPart);

            String firstSecondPartSoundex = Soundex.getCode(firstPart + secondPart);
            String secondThirdPartSoundex = Soundex.getCode(secondPart + thirdPart);


            //First Name, Middle Name & Last Name
            // soundex(firstPart) --> firstNameSoundex
            // soundex(secondPart) --> middleNameSoundex
            // soundex(thirdPart) --> lastNameSoundex
            // Collect results
            matchedEntity.addAll(financialSanctionedEntityRepository.findByFirstNameSoundex(
                    firstPartSoundex));
            matchedEntity.addAll(financialSanctionedEntityRepository.findByMiddleNameSoundex(
                    secondPartSoundex));
            matchedEntity.addAll(financialSanctionedEntityRepository.findByLastNameSoundex(
                    thirdPartSoundex));

            //Last Name & First Name, Middle Name
            // soundex(firstPart) --> lastNameSoundex
            // soundex(secondPart) --> firstNameSoundex
            // soundex(thirdPart) --> middleNameSoundex
            // Collect results
            matchedEntity.addAll(financialSanctionedEntityRepository.findByLastNameSoundex(
                    firstPartSoundex));
            matchedEntity.addAll(financialSanctionedEntityRepository.findByFirstNameSoundex(
                    secondPartSoundex));
            matchedEntity.addAll(financialSanctionedEntityRepository.findByMiddleNameSoundex(
                    thirdPartSoundex));


            //First Name & two Last name
            // soundex(firstPart) --> firstNameSoundex
            // soundex(secondPart+thirdPart) --> lastNameSoundex
            // Collect results
            matchedEntity.addAll(financialSanctionedEntityRepository.findByFirstNameSoundex(
                    firstPartSoundex));
            matchedEntity.addAll(financialSanctionedEntityRepository.findByLastNameSoundex(
                    secondThirdPartSoundex));

            //two Last name & First Name
            // soundex(firstPart+secondPart) --> lastNameSoundex
            // soundex(thirdPart) --> firstNameSoundex
            // Collect results

            matchedEntity.addAll(financialSanctionedEntityRepository.findByLastNameSoundex(
                    firstSecondPartSoundex));
            matchedEntity.addAll(financialSanctionedEntityRepository.findByFirstNameSoundex(
                    thirdPart));

        } else {
            //TODO
            //Handle Names with 4 or more parts
            log.info("Names with 4 or more parts. Needs special handling");
        }
        return matchedEntity;
    }
}
