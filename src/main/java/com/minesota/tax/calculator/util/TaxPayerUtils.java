package com.minesota.tax.calculator.util;

import com.minesota.tax.calculator.model.Receipt;
import com.minesota.tax.calculator.model.TaxPayer;

import java.util.List;

/**
 * This class holds the logic for some basic taxpayer functions, such as total receipts amounts
 * calculations, taxPayer tax adjustment based on his receipts total etc.
 */
public class TaxPayerUtils {

    private TaxPayerUtils() {
        throw new IllegalStateException("Utility class. This class can not be initialized.");
    }

    /**
     * Sums total amount on the list of receipts with a specific receipt kind
     *
     * @param receiptKind the receipt kind ( e.g. entertainment )
     * @param receipts    the list of receipts
     * @return the total receipts amount
     */
    public static double getReceiptsTotalAmount(String receiptKind, List<Receipt> receipts) {

        return receipts.stream()
                .filter(rec -> rec.getKind().equals(receiptKind))
                .map(Receipt::getAmount)
                .reduce((double) 0, Double::sum);
    }

    /**
     * Sums total amount on the list of receipts
     *
     * @param receipts the list of receipts
     * @return the total receipts amount
     */
    public static double getReceiptsTotalAmount(List<Receipt> receipts) {
        return receipts.stream()
                .map(Receipt::getAmount)
                .reduce((double) 0, Double::sum);
    }

    /**
     * Based on the receipts that a tax payer has turned in the system he gets
     * a tax increase or decrease accordingly
     *
     * @param taxPayer the taxpayer object to be adjusted
     */
    public static void applyTaxPayerTaxAdjustments(TaxPayer taxPayer) {
        double receiptsTotal = getReceiptsTotalAmount(taxPayer.getReceipts());

        if ((receiptsTotal / taxPayer.getIncome()) < 0.2) {
            taxPayer.setTaxIncrease(taxPayer.getBasicTax() * 0.08);
        } else if ((receiptsTotal / taxPayer.getIncome()) < 0.4) {
            taxPayer.setTaxIncrease(taxPayer.getBasicTax() * 0.04);
        } else if ((receiptsTotal / taxPayer.getIncome()) < 0.6) {
            taxPayer.setTaxDecrease(taxPayer.getBasicTax() * 0.15);
        } else {
            taxPayer.setTaxDecrease(taxPayer.getBasicTax() * 0.30);
        }

        taxPayer.setTotalTax(taxPayer.getBasicTax() + taxPayer.getTaxIncrease() - taxPayer.getTaxDecrease());
    }
}