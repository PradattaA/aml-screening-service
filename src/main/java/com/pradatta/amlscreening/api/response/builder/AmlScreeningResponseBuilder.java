package com.pradatta.amlscreening.api.response.builder;

import com.pradatta.amlscreening.api.response.AmlScreeningResponse;
import com.pradatta.amlscreening.api.response.FinancialSanctionedEntity;
import com.pradatta.amlscreening.api.response.ScreeningStatusColour;

public class AmlScreeningResponseBuilder {
    private FinancialSanctionedEntity financialSanctionedEntity;
    private boolean matched;
    private ScreeningStatusColour statusColour;
    private float confidence;

    public AmlScreeningResponseBuilder setFinancialSanctionedEntity(
            FinancialSanctionedEntity financialSanctionedEntity) {
        this.financialSanctionedEntity = financialSanctionedEntity;
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

    public AmlScreeningResponseBuilder setConfidence(float confidence) {
        this.confidence = confidence;
        return this;
    }

    public AmlScreeningResponse createAmlScreeningResponse() {
        return new AmlScreeningResponse(financialSanctionedEntity, matched, statusColour, confidence);
    }
}