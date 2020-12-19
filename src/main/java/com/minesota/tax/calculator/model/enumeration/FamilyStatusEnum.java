package com.minesota.tax.calculator.model.enumeration;

import java.util.logging.Logger;

public enum FamilyStatusEnum {

    SINGLE("single"),
    MARRIED_FILLING_JOINTLY("married filing jointly"),
    MARRIED_FILLING_SEPARATELY("married filing separately"),
    HEAD_OF_HOUSEHOLD("head of household");

    private static Logger logger = Logger.getLogger(FamilyStatusEnum.class.getName());
    private final String description;

    FamilyStatusEnum(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static FamilyStatusEnum fromValue(String description) {
        if (description == null) {
            logger.warning("empty description was passed at family status enum");
            return null;
        }

        for (FamilyStatusEnum familyStatus : FamilyStatusEnum.values()) {
            if (familyStatus.getDescription().equalsIgnoreCase(description)) return familyStatus;
        }

        logger.warning("no " + description + "family status enum description was found");
        return null;
    }
}