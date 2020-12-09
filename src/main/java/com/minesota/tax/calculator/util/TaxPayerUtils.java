package com.minesota.tax.calculator.util;

import com.minesota.tax.calculator.model.Receipt;

import java.util.List;

public class TaxPayerUtils {

    private TaxPayerUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static double getReceiptsTotalAmountByKind(String receiptKind, List<Receipt> receipts) {

        return receipts.stream()
                .filter(rec -> rec.getKind().equals(receiptKind))
                .map(Receipt::getAmount)
                .reduce((double) 0, Double::sum);
    }
}
