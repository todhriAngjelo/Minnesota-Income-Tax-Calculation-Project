package com.minesota.tax.calculator.model;

import com.minesota.tax.calculator.model.enumeration.FamilyStatusEnum;

import java.util.ArrayList;
import java.util.List;

public class TaxPayer {

    private String name;
    private String vat;
    private FamilyStatusEnum familyStatus;
    private double income;
    private List<Receipt> receipts;
    private double basicTax;
    private double taxIncrease;
    private double taxDecrease;
    private double totalTax;

    public TaxPayer(String name, String vat, FamilyStatusEnum familyStatus, String income, double basicTax) {
        this.name = name;
        this.vat = vat;
        this.familyStatus = familyStatus;
        this.income = Double.parseDouble(income);
        this.basicTax = basicTax;
        taxIncrease = 0;
        taxDecrease = 0;
        receipts = new ArrayList<>();
    }

    public String toString() {
        return "Name: " + name
                + "\nVat number: " + vat
                + "\nStatus: " + familyStatus
                + "\nIncome: " + String.format("%.2f", income)
                + "\nBasicTax: " + String.format("%.2f", basicTax)
                + "\nTaxIncrease: " + String.format("%.2f", taxIncrease)
                + "\nTaxDecrease: " + String.format("%.2f", taxDecrease);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVat() {
        return vat;
    }

    public void setVat(String vat) {
        this.vat = vat;
    }

    public FamilyStatusEnum getFamilyStatus() {
        return familyStatus;
    }

    public void setFamilyStatus(FamilyStatusEnum familyStatus) {
        this.familyStatus = familyStatus;
    }

    public double getIncome() {
        return income;
    }

    public void setIncome(double income) {
        this.income = income;
    }

    public List<Receipt> getReceipts() {
        return receipts;
    }

    public void setReceipts(List<Receipt> receipts) {
        this.receipts = receipts;
    }

    public double getBasicTax() {
        return basicTax;
    }

    public void setBasicTax(double basicTax) {
        this.basicTax = basicTax;
    }

    public double getTaxIncrease() {
        return taxIncrease;
    }

    public void setTaxIncrease(double taxIncrease) {
        this.taxIncrease = taxIncrease;
    }

    public double getTaxDecrease() {
        return taxDecrease;
    }

    public void setTaxDecrease(double taxDecrease) {
        this.taxDecrease = taxDecrease;
    }

    public double getTotalTax() {
        return totalTax;
    }

    public void setTotalTax(double totalTax) {
        this.totalTax = totalTax;
    }
}