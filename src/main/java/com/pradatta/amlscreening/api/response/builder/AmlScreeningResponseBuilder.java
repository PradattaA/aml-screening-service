package com.pradatta.amlscreening.api.response.builder;

import com.pradatta.amlscreening.api.response.AmlScreeningResponse;
import com.pradatta.amlscreening.api.response.FinancialSanctionedEntity;
import com.pradatta.amlscreening.api.response.ScreeningStatusColour;

import java.util.Collection;

public class AmlScreeningResponseBuilder {
    private Collection<FinancialSanctionedEntity> financialSanctionedEntities;
    private boolean matched;
    private ScreeningStatusColour statusColour;
    private double matchScore;

    public AmlScreeningResponseBuilder setFinancialSanctionedEntities(
            Collection<FinancialSanctionedEntity> financialSanctionedEntities) {
        this.financialSanctionedEntities = financialSanctionedEntities;
        return this;
    }

    public AmlScreeningResponseBuilder setMatched(boolean matched) {
        this.matched = matched;
        return this;
    }

    public AmlScreeningResponseBuilder setStatusColour(ScreeningStatusColour statusColour) {
        this.statusColour = statusColour;
        return this;
    }

    public AmlScreeningResponseBuilder setMatchScore(double matchScore) {
        this.matchScore = matchScore;
        return this;
    }

    public AmlScreeningResponse createAmlScreeningResponse() {
        return new AmlScreeningResponse(financialSanctionedEntities, matched, statusColour, matchScore);
    }
}