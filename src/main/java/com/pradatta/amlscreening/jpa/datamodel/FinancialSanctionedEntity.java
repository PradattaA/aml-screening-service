package com.pradatta.amlscreening.jpa.datamodel;

import com.pradatta.amlscreening.service.Soundex;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "financial_sanctioned_entity", indexes = @Index(columnList = "first_name_soundex, middle_name_soundex, last_name_soundex, whole_name_soundex"))
public class FinancialSanctionedEntity {
    private @Id
    @GeneratedValue
    @Column(name = "id") Long id;
    @Column(name = "type")
    private SanctionEntityType type;
    @Column(name = "logical_id")
    private Long logicalId;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "middle_name")
    private String middleName;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "whole_name")
    private String wholeName;
    @Column(name = "publication_date")
    private Date publicationDate;
    @Column(name = "publication_url")
    private String publicationUrl;

    @CreationTimestamp
    @Column(name = "created_on")
    private Timestamp createdOn;

    @UpdateTimestamp
    @Column(name = "updated_on")
    private Timestamp updatedOn;

    @Column(name = "first_name_soundex")
    private String firstNameSoundex;
    @Column(name = "middle_name_soundex")
    private String middleNameSoundex;
    @Column(name = "last_name_soundex")
    private String lastNameSoundex;
    @Column(name = "whole_name_soundex")
    private String wholeNameSoundex;


    public FinancialSanctionedEntity() {
    }

    /**
     * @param type            Entity Type
     * @param logicalId       Logical ID
     * @param firstName       First Name
     * @param lastName        Last Name
     * @param wholeName       Whole Name
     * @param publicationDate Publication Date
     * @param publicationUrl  Publication URL
     */
    public FinancialSanctionedEntity(SanctionEntityType type, Long logicalId, String firstName, String middleName,
                                     String lastName, String wholeName, Date publicationDate, String publicationUrl) {
        this.type = type;
        this.logicalId = logicalId;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.wholeName = wholeName;
        this.publicationDate = publicationDate;
        this.publicationUrl = publicationUrl;

        this.firstNameSoundex = Soundex.getCode(firstName);
        this.middleNameSoundex = Soundex.getCode(middleName);
        this.lastNameSoundex = Soundex.getCode(lastName);
        this.wholeNameSoundex = Soundex.getCode(wholeName);

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
        if (this==o) return true;
        if (!(o instanceof FinancialSanctionedEntity that)) return false;
        return getType()==that.getType() && Objects.equals(getLogicalId(), that.getLogicalId()) && Objects.equals(
                getFirstName(), that.getFirstName()) && Objects.equals(getLastName(),
                                                                       that.getLastName()) && Objects.equals(
                getWholeName(), that.getWholeName()) && Objects.equals(getPublicationDate(),
                                                                       that.getPublicationDate()) && Objects.equals(
                getPublicationUrl(), that.getPublicationUrl());
    }

    @Override
    public int hashCode() {
        return getId()==null ?
                Objects.hash(getType(), getLogicalId(), getFirstName(), getLastName(), getWholeName()):
                Objects.hash(getId(), getType(), getLogicalId(), getFirstName(), getLastName(), getWholeName());
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

    public String getFirstNameSoundex() {
        return firstNameSoundex;
    }

    public String getMiddleNameSoundex() {
        return middleNameSoundex;
    }

    public String getLastNameSoundex() {
        return lastNameSoundex;
    }

    public String getWholeNameSoundex() {
        return wholeNameSoundex;
    }


}
