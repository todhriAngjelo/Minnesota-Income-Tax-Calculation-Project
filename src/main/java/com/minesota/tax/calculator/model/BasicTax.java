package com.minesota.tax.calculator.model;

import java.math.BigDecimal;

public class BasicTax {

    private BigDecimal amount;
    private TaxCategory taxCategory;

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public TaxCategory getTaxCategory() {
        return taxCategory;
    }

    public void setTaxCategory(TaxCategory taxCategory) {
        this.taxCategory = taxCategory;
    }
}