package com.minesota.tax.calculator.model;

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
}