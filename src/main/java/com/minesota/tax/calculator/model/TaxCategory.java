package com.minesota.tax.calculator.model;

public class TaxCategory {

    private FamilyStatusEnum familyStatus;
    private double minTax;
    private double incomeBasedTaxPercentage;
    private int[] limits;

    public TaxCategory(FamilyStatusEnum familyStatus, double minTax, double incomeBasedTaxPercentage, int[] limits) {
        this.familyStatus = familyStatus;
        this.minTax = minTax;
        this.incomeBasedTaxPercentage = incomeBasedTaxPercentage;
        this.limits = limits;
    }

    public FamilyStatusEnum getFamilyStatus() {
        return familyStatus;
    }

    public void setFamilyStatus(FamilyStatusEnum familyStatus) {
        this.familyStatus = familyStatus;
    }

    public double getMinTax() {
        return minTax;
    }

    public void setMinTax(double minTax) {
        this.minTax = minTax;
    }

    public double getIncomeBasedTaxPercentage() {
        return incomeBasedTaxPercentage;
    }

    public void setIncomeBasedTaxPercentage(double incomeBasedTaxPercentage) {
        this.incomeBasedTaxPercentage = incomeBasedTaxPercentage;
    }

    public int[] getLimits() {
        return limits;
    }

    public void setLimits(int[] limits) {
        this.limits = limits;
    }
}
