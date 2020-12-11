package com.minesota.tax.calculator.util;

import com.minesota.tax.calculator.model.Receipt;
import com.minesota.tax.calculator.model.TaxPayer;

import java.util.List;

public class TaxPayerUtils {

    private TaxPayerUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static double getReceiptsTotalAmountFor(String receiptKind, List<Receipt> receipts) {

        return receipts.stream()
                .filter(rec -> rec.getKind().equals(receiptKind))
                .map(Receipt::getAmount)
                .reduce((double) 0, Double::sum);
    }

    public static double getReceiptsTotalAmount(List<Receipt> receipts) {
        return receipts.stream()
                .map(Receipt::getAmount)
                .reduce((double) 0, Double::sum);
    }

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
