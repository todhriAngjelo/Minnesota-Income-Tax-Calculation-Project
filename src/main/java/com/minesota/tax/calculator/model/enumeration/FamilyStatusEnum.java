package com.minesota.tax.calculator.model.enumeration;

public enum FamilyStatusEnum {

    SINGLE("single"),
    MARRIED_FILLING_JOINTLY("married filing jointly"),
    MARRIED_FILLING_SEPARATELY("married filing separately"),
    HEAD_OF_HOUSEHOLD("head of household");

    private final String description;

    FamilyStatusEnum(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static FamilyStatusEnum fromValue(String description) {
        if (description == null) return null;
        // todo handle this. if we go here app will break

        for (FamilyStatusEnum familyStatus : FamilyStatusEnum.values()) {
            if (familyStatus.getDescription().equalsIgnoreCase(description)) return familyStatus;
        }

        return null;
        // todo handle this. if we go here app will break
    }
}