package com.pradatta.amlscreening.jpa.datamodel;

public enum SanctionEntityType {
    PERSON,
    ENTERPRISE;

    public static SanctionEntityType getEnum(String value) {
        SanctionEntityType sanctionEntityType;
        switch (value) {
            case "P":
                sanctionEntityType = PERSON;
                break;
            case "E":
                sanctionEntityType = ENTERPRISE;
                break;
            default:
                throw new IllegalArgumentException();
        }
        return sanctionEntityType;
    }
}
