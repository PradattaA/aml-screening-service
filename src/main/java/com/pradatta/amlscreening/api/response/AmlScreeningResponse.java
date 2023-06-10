package com.pradatta.amlscreening.api.response;


import java.io.Serializable;

public class AmlScreeningResponse implements Serializable {
    private FinancialSanctionedEntity financialSanctionedEntity;

    private boolean matched;

    private ScreeningStatusColour statusColour;

    private float confidence;


    public AmlScreeningResponse(FinancialSanctionedEntity financialSanctionedEntity, boolean matched,
                                ScreeningStatusColour statusColour, float confidence) {
        this.financialSanctionedEntity = financialSanctionedEntity;
        this.matched = matched;
        this.statusColour = statusColour;
        this.confidence = confidence;
    }
}
