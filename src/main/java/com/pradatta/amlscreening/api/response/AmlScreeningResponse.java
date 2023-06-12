package com.pradatta.amlscreening.api.response;


import java.util.Collection;

public class AmlScreeningResponse {
    public Collection<FinancialSanctionedEntity> getMatchedFinancialSanctionedEntity() {
        return matchedFinancialSanctionedEntity;
    }

    public boolean isMatched() {
        return matched;
    }

    public ScreeningStatusColour getStatusColour() {
        return statusColour;
    }

    public double getMatchScore() {
        return matchScore;
    }

    private Collection<FinancialSanctionedEntity> matchedFinancialSanctionedEntity;

    private boolean matched;

    private ScreeningStatusColour statusColour;

    public void setMatchedFinancialSanctionedEntity(
            Collection<FinancialSanctionedEntity> matchedFinancialSanctionedEntity) {
        this.matchedFinancialSanctionedEntity = matchedFinancialSanctionedEntity;
    }

    public void setMatched(boolean matched) {
        this.matched = matched;
    }

    public void setStatusColour(ScreeningStatusColour statusColour) {
        this.statusColour = statusColour;
    }

    public void setMatchScore(double matchScore) {
        this.matchScore = matchScore;
    }

    private double matchScore;

    public AmlScreeningResponse() {
    }

    public AmlScreeningResponse(Collection<FinancialSanctionedEntity> matchedFinancialSanctionedEntity, boolean matched,
                                ScreeningStatusColour statusColour, double matchScore) {
        this.matchedFinancialSanctionedEntity = matchedFinancialSanctionedEntity;
        this.matched = matched;
        this.statusColour = statusColour;
        this.matchScore = matchScore;
    }
}
