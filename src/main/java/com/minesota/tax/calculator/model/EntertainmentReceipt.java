package com.minesota.tax.calculator.model;

public class EntertainmentReceipt extends Receipt {
	private final String kind = "Entertainment";

	public EntertainmentReceipt(String id, String date, String amount, String name, String country, String city, String street, String number) {
		super(id, date, amount, name, country, city, street, number);
		super.kind = kind;
	}
}
