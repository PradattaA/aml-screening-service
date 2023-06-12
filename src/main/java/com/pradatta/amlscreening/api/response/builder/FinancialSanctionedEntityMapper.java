package com.pradatta.amlscreening.api.response.builder;

import com.pradatta.amlscreening.api.response.FinancialSanctionedEntity;
import org.springframework.stereotype.Component;

@Component
public class FinancialSanctionedEntityMapper {

    public static FinancialSanctionedEntity from(
            com.pradatta.amlscreening.jpa.datamodel.FinancialSanctionedEntity entity, double matchScore) {
        return new FinancialSanctionedEntityBuilder()
                .setLogicalId(entity.getLogicalId())
                .setFirstName(entity.getFirstName())
                .setLastName(entity.getLastName())
                .setMiddleName(entity.getMiddleName())
                .setType(entity.getType())
                .setWholeName(entity.getWholeName())
                .setPublicationDate(entity.getPublicationDate())
                .setPublicationUrl(entity.getPublicationUrl())
                .setMatchScore(matchScore)
                .createFinancialSanctionedEntity();
    }
}
