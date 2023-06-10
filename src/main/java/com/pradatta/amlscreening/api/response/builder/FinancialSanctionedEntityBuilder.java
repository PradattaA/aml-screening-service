package com.pradatta.amlscreening.api.response.builder;

import com.pradatta.amlscreening.api.response.FinancialSanctionedEntity;
import com.pradatta.amlscreening.jpa.datamodel.SanctionEntityType;

import java.util.Date;

public class FinancialSanctionedEntityBuilder {
    private SanctionEntityType type;
    private Long logicalId;
    private String firstName;
    private String middleName;
    private String lastName;
    private String wholeName;
    private Date publicationDate;
    private String publicationUrl;

    public FinancialSanctionedEntityBuilder setType(SanctionEntityType type) {
        this.type = type;
        return this;
    }

    public FinancialSanctionedEntityBuilder setLogicalId(Long logicalId) {
        this.logicalId = logicalId;
        return this;
    }

    public FinancialSanctionedEntityBuilder setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public FinancialSanctionedEntityBuilder setMiddleName(String middleName) {
        this.middleName = middleName;
        return this;
    }

    public FinancialSanctionedEntityBuilder setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public FinancialSanctionedEntityBuilder setWholeName(String wholeName) {
        this.wholeName = wholeName;
        return this;
    }

    public FinancialSanctionedEntityBuilder setPublicationDate(Date publicationDate) {
        this.publicationDate = publicationDate;
        return this;
    }

    public FinancialSanctionedEntityBuilder setPublicationUrl(String publicationUrl) {
        this.publicationUrl = publicationUrl;
        return this;
    }

    public FinancialSanctionedEntity createFinancialSanctionedEntity() {
        return new FinancialSanctionedEntity(type, logicalId, firstName, middleName, lastName, wholeName,
                                             publicationDate, publicationUrl);
    }
}