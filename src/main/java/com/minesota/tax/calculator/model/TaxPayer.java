package com.minesota.tax.calculator.model;

import com.minesota.tax.calculator.util.TaxCategoryBuilder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class TaxPayer {

    private final String name;
    private final String vat;
    private final String familyStatus;
    private final double income;
    private final List<Receipt> receipts;
    private double basicTax;
    private double taxIncrease;
    private double taxDecrease;
    private double totalTax;

    public TaxPayer(String name, String vat, String familyStatus, String income) { // todo make income BigDecimal at constructor
        this.name = name;
        this.vat = vat;
        this.familyStatus = familyStatus;
        this.income = Double.parseDouble(income);

        setBasicTaxBasedOnFamilyStatus();
        taxIncrease = 0;
        taxDecrease = 0;

        receipts = new ArrayList<>();
    }

    private void setBasicTaxBasedOnFamilyStatus() {
        switch (familyStatus.toLowerCase()) {
            case ("married filing jointly"):
                basicTax = TaxCategoryBuilder.getBasicTaxBy(FamilyStatusEnum.MARRIED_FILLING_JOINTLY, income);
                break;
            case ("married filing separately"):
                basicTax = TaxCategoryBuilder.getBasicTaxBy(FamilyStatusEnum.MARRIED_FILLING_SEPARATELY, income);
                break;
            case ("single"):
                basicTax = TaxCategoryBuilder.getBasicTaxBy(FamilyStatusEnum.SINGLE, income);
                break;
            case ("head of household"):
                basicTax = TaxCategoryBuilder.getBasicTaxBy(FamilyStatusEnum.HEAD_OF_HOUSEHOLD, income);
                break;
        }

        totalTax = basicTax;
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

    public Receipt getReceipt(int recipientId) {
        return receipts.get(recipientId);
    }

    public List<Receipt> getReceiptsArrayList() {
        return receipts;
    }

    public String[] getReceiptsList() {
        String[] receiptsList = new String[receipts.size()];

        int c = 0;
        for (Receipt receipt : receipts) {
            receiptsList[c++] = receipt.getId() + " | " + receipt.getDate() + " | " + receipt.getAmount();
        }

        return receiptsList;
    }

    public double getTotalReceiptsAmount() {
        double totalReceiptsAmount = 0;

        for (Receipt receipt : receipts) {
            totalReceiptsAmount += receipt.getAmount();
        }

        return (new BigDecimal(totalReceiptsAmount).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
    }

    public String getName() {
        return name;
    }

    public String getAFM() {
        return vat;
    }

    public String getFamilyStatus() {
        return familyStatus;
    }

    public double getIncome() {
        return (new BigDecimal(income).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
    }

    public double getBasicTax() {
        return (new BigDecimal(basicTax).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
    }

    public double getTaxIncrease() {
        return (new BigDecimal(taxIncrease).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
    }

    public double getTaxDecrease() {
        return (new BigDecimal(taxDecrease).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
    }

    public double getTotalTax() {
        return (new BigDecimal(totalTax).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
        // todo refactor these to use BigDecimal.valueOf()
    }

    public void addReceiptToList(Receipt receipt) {
        receipts.add(receipt);

        calculateTaxpayerTaxIncreaseOrDecreaseBasedOnReceipts();
    }

    public void removeReceiptFromList(int index) {
        receipts.remove(index);

        calculateTaxpayerTaxIncreaseOrDecreaseBasedOnReceipts();
    }

    public void calculateTaxpayerTaxIncreaseOrDecreaseBasedOnReceipts() {
        double totalReceiptsAmount = 0;
        for (Receipt receipt : receipts) {
            totalReceiptsAmount += receipt.getAmount();
        }

        taxIncrease = 0;
        taxDecrease = 0;
        if ((totalReceiptsAmount / income) < 0.2) {
            taxIncrease = basicTax * 0.08;
        } else if ((totalReceiptsAmount / income) < 0.4) {
            taxIncrease = basicTax * 0.04;
        } else if ((totalReceiptsAmount / income) < 0.6) {
            taxDecrease = basicTax * 0.15;
        } else {
            taxDecrease = basicTax * 0.30;
        }

        totalTax = basicTax + taxIncrease - taxDecrease;
    }
}