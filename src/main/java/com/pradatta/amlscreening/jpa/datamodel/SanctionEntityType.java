package com.pradatta.amlscreening.jpa.datamodel;

public enum SanctionEntityType {
    PERSON ("PERSON"),
    ENTERPRISE ("ENTERPRISE");

    final String typeValue;
    SanctionEntityType(String typeValue) {
        this.typeValue = typeValue;
    }



    public static SanctionEntityType getEnum(String value) {
        SanctionEntityType sanctionEntityType = switch (value) {
            case "P" -> PERSON;
            case "E" -> ENTERPRISE;
            default -> throw new IllegalArgumentException();
        };
        return sanctionEntityType;
    }

    @Override
    public String toString() {
        return typeValue;
    }
}
