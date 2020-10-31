package com.minesota.tax.calculator.model;

public class BasicReceipt extends Receipt {
	private final String kind = "Basic";

	public BasicReceipt(String id, String date, String amount, String name, String country, String city, String street, String number) {
		super(id, date, amount, name, country, city, street, number);
		super.kind = kind;
	}
}
