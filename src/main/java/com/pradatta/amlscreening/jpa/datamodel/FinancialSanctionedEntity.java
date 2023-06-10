package com.pradatta.amlscreening.jpa.datamodel;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "FINANCIAL_SANCTIONED_ENTITY")
public class FinancialSanctionedEntity {
    private @Id @GeneratedValue Long id;
    private SanctionEntityType type;
    private Long logicalId;
    private String firstName;
    private String middleName;
    private String lastName;
    private String wholeName;
    private Date publicationDate;
    private String publicationUrl;

    @CreationTimestamp
    private Timestamp createdOn;

    @UpdateTimestamp
    private Timestamp updatedOn;


    public FinancialSanctionedEntity() {
    }

    /**
     *
     * @param type
     * @param logicalId
     * @param firstName
     * @param lastName
     * @param wholeName
     * @param publicationDate
     * @param publicationUrl
     */
    public FinancialSanctionedEntity(SanctionEntityType type, Long logicalId, String firstName, String middleName, String lastName, String wholeName, Date publicationDate, String publicationUrl) {
        this.type = type;
        this.logicalId = logicalId;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.wholeName = wholeName;
        this.publicationDate = publicationDate;
        this.publicationUrl = publicationUrl;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public SanctionEntityType getType() {
        return type;
    }

    public void setType(SanctionEntityType type) {
        this.type = type;
    }

    public Long getLogicalId() {
        return logicalId;
    }

    public void setLogicalId(Long logicalId) {
        this.logicalId = logicalId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getWholeName() {
        return wholeName;
    }

    public void setWholeName(String wholeName) {
        this.wholeName = wholeName;
    }

    public Date getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(Date publicationDate) {
        this.publicationDate = publicationDate;
    }

    public String getPublicationUrl() {
        return publicationUrl;
    }

    public void setPublicationUrl(String publicationUrl) {
        this.publicationUrl = publicationUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FinancialSanctionedEntity that)) return false;
        return getType() == that.getType() && Objects.equals(getLogicalId(), that.getLogicalId()) && Objects.equals(getFirstName(), that.getFirstName()) && Objects.equals(getLastName(), that.getLastName()) && Objects.equals(getWholeName(), that.getWholeName()) && Objects.equals(getPublicationDate(), that.getPublicationDate()) && Objects.equals(getPublicationUrl(), that.getPublicationUrl());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getType(), getLogicalId(), getFirstName(), getLastName(), getWholeName());
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public Timestamp getUpdatedOn() {
        return updatedOn;
    }

    public Timestamp getCreatedOn() {
        return createdOn;
    }
}
