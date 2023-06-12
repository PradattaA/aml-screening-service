package com.pradatta.amlscreening.service;

import com.pradatta.amlscreening.jpa.datamodel.FinancialSanctionedEntity;
import com.pradatta.amlscreening.jpa.datamodel.SanctionEntityType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static com.pradatta.amlscreening.api.response.builder.FinancialSanctionedEntityMapper.from;
import static com.pradatta.amlscreening.jpa.datamodel.SanctionEntityType.*;
import static com.pradatta.amlscreening.service.util.StringUtil.*;

@Component
public class FuzzyNameSearch {


    @Value("${aml-screening.fuzzymatch.first-name-weight:30}")
    private int firstNameWeight;

    @Value("${aml-screening.fuzzymatch.middle-name-weight:10}")
    private int middleNameWeight;

    @Value("${aml-screening.fuzzymatch.last-name-weight:20}")
    private int lastNameWeight;

    @Value("${aml-screening.fuzzymatch.min-match-threshold:0.7}")
    private double minMatchThreshold;

    private static final Logger log = LoggerFactory.getLogger(FuzzyNameSearch.class);
    private Collection<FinancialSanctionedEntity> collectionToSearch;

    public void init(Collection<FinancialSanctionedEntity> collectionToSearch) {
        this.collectionToSearch = collectionToSearch;
    }

    public List<com.pradatta.amlscreening.api.response.FinancialSanctionedEntity> search(List<String> cleanedUpName) {

        List<com.pradatta.amlscreening.api.response.FinancialSanctionedEntity> matchResult = new ArrayList<>();
        for (FinancialSanctionedEntity financialSanctionedEntity : collectionToSearch) {
            SanctionEntityType entityType = financialSanctionedEntity.getType();
            double matchScore;
            //Check for Persons Name
            if(entityType == PERSON) {
                matchScore = compareNameParts(financialSanctionedEntity, cleanedUpName);
            } else {
                //Check for Organization Names
                matchScore = compareOrganizationName(financialSanctionedEntity, cleanedUpName);
            }
            if (matchScore > minMatchThreshold)
                matchResult.add(from(financialSanctionedEntity, matchScore));
        }
        return matchResult;
    }

    private double compareNameParts(FinancialSanctionedEntity sanctionedEntity, List<String> cleanedUpName) {
        double matchScore = 0;
        if (cleanedUpName.size()==1) {
            matchScore = matchOneWordNames(sanctionedEntity, cleanedUpName);
        } else if (cleanedUpName.size()==2) {
            matchScore = matchTwoWordNames(sanctionedEntity, cleanedUpName);
        } else if (cleanedUpName.size()==3) {
            matchScore = matchThreeWordNames(sanctionedEntity, cleanedUpName);
        } else {
            //TODO
            //Handle Names with 4 or more parts
            log.info("Names with 4 or more parts. Needs special handling");
        }
        return matchScore;
    }

    private double compareOrganizationName(FinancialSanctionedEntity sanctionedEntity, List<String> cleanedUpName) {
        String joinedName = removeSpaces(cleanUpNamesForMatching(String.join("", cleanedUpName)));
        return matchOneWordNames(sanctionedEntity, Collections.singletonList(joinedName));
    }

    private double matchThreeWordNames(FinancialSanctionedEntity sanctionedEntity, List<String> cleanedUpName) {
        String firstPart = cleanedUpName.get(0);
        String secondPart = cleanedUpName.get(1);
        String thirdPart = cleanedUpName.get(2);

        String firstName = cleanUpNamesForMatching(sanctionedEntity.getFirstName());
        String middleName = cleanUpNamesForMatching(sanctionedEntity.getMiddleName());
        String lastName = cleanUpNamesForMatching(sanctionedEntity.getLastName());

        //to handle cases where middle name saved together as last name
        String[] lastNameParts = lastName.split(" ");
        if(lastNameParts.length == 2) {
            middleName = lastNameParts[0];
            lastName = lastNameParts[1];
        }


        //First Name, Middle Name & Last Name
        double combinedScore1 = getCombinedScore(firstPart, secondPart, thirdPart, firstName, middleName, lastName);

        //Last Name & First Name, Middle Name
        double combinedScore2 = getCombinedScore(secondPart, thirdPart, firstPart, firstName, middleName, lastName);

        //First Name & two Last name
        double combinedScore3 = getCombinedScore(firstPart, secondPart + thirdPart, firstName, lastName);

        double combinedScore4 = getCombinedScore(firstPart, secondPart + thirdPart, firstName, middleName + lastName);

        //two Last name & First Name
        double combinedScore5 = getCombinedScore(thirdPart, firstPart + secondPart, firstName, lastName);

        //Return Max Score
        return Math.max(combinedScore1, Math.max(combinedScore2, Math.max(combinedScore3, Math.max(combinedScore4, combinedScore5))));
    }

    private double matchTwoWordNames(FinancialSanctionedEntity sanctionedEntity, List<String> cleanedUpName) {
        //Only First Name & Last Name
        //Need to Check reverse order
        String firstPart = cleanedUpName.get(0).toLowerCase();
        String secondPart = cleanedUpName.get(1).toLowerCase();

        String firstName = cleanUpNamesForMatching(sanctionedEntity.getFirstName()).toLowerCase();
        String lastName = cleanUpNamesForMatching(sanctionedEntity.getLastName()).toLowerCase();

        // firstName lastName
        double combinedScore = getCombinedScore(firstPart, secondPart, firstName, lastName);

        // lastName firstname
        double reversedCombinedScore = getCombinedScore(secondPart, firstPart, firstName, lastName);

        //return best score
        return Math.max(combinedScore, reversedCombinedScore);
    }

    private double matchOneWordNames(FinancialSanctionedEntity sanctionedEntity, List<String> cleanedUpName) {
        String firstPart = cleanedUpName.get(0);
        String wholeName = cleanUpNamesForMatching(sanctionedEntity.getWholeName()).toLowerCase();
        int optimalStringAlignmentDistance = optimalStringAlignmentDistance(removeSpaces(wholeName), removeSpaces(firstPart));
        return getScore(firstPart, wholeName, optimalStringAlignmentDistance);
    }

    private static double getScore(String string1, String string2, double distance) {
        int maxLength = Math.max(string1.length(), string2.length());
        return (maxLength - distance) / maxLength;
    }

    private double getCombinedScore(String firstCandidate, String lastNameCandidate, String firstName,
                                    String lastName) {

        int firstNameDistance = firstCandidate.length()==1 ?
                checkInitial(firstName, firstCandidate):
                optimalStringAlignmentDistance(removeSpaces(firstName), removeSpaces(firstCandidate));
        double firstNameScore = getScore(firstCandidate, firstName, firstNameDistance);

        int lastNameDistance = lastNameCandidate.length()==1 ?
                checkInitial(lastName, lastNameCandidate):
                optimalStringAlignmentDistance(removeSpaces(lastName), removeSpaces(lastNameCandidate));

        double lastNameScore = getScore(lastNameCandidate, lastName, lastNameDistance);

        if (firstCandidate.length()==1 && lastNameCandidate.length()==1) {
            //Both Part of the name to be searched is Initial
            //then reduce the match score by 60%
            firstNameScore *= 0.6;
            lastNameScore *= 0.6;
        }

        return ((firstNameScore * firstNameWeight) + (lastNameScore * lastNameWeight))
                / (firstNameWeight + lastNameWeight);
    }

    private int checkInitial(String name, String nameToCheck) {
        return nameToCheck.charAt(0)==name.charAt(0) ?
                0:name.length();
    }

    private double getCombinedScore(String firstCandidate, String middleNameCandidate, String lastNameCandidate,
                                    String firstName, String middleName, String lastName) {
        int firstNameDistance = firstCandidate.length()==1 ?
                checkInitial(firstName, firstCandidate):
                optimalStringAlignmentDistance(removeSpaces(firstName), removeSpaces(firstCandidate));

        double firstNameScore = getScore(firstCandidate, firstName, firstNameDistance);

        int middleNameDistance = middleNameCandidate.length()==1 ?
                checkInitial(middleName, middleNameCandidate):
                optimalStringAlignmentDistance(removeSpaces(middleName), removeSpaces(middleNameCandidate));

        double middleNameScore = getScore(middleNameCandidate, middleName, middleNameDistance);

        int lastNameDistance = lastNameCandidate.length()==1 ?
                checkInitial(lastName, lastNameCandidate):
                optimalStringAlignmentDistance(removeSpaces(lastName), removeSpaces(lastNameCandidate));

        double lastNameScore = getScore(lastNameCandidate, lastName, lastNameDistance);

        if (firstCandidate.length()==1 && middleNameCandidate.length()==1 && lastNameCandidate.length()==1) {
            //All Part of the name to be searched is Initial
            //then reduce the match score by 60%
            firstNameScore *= 0.6;
            middleNameScore *= 0.6;
            lastNameScore *= 0.6;
        }

        return ((firstNameScore * firstNameWeight) + (middleNameScore * middleNameWeight) + (lastNameScore * lastNameWeight))
                / (firstNameWeight + middleNameWeight + lastNameWeight);
    }

}
