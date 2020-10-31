package com.minesota.tax.calculator.model;

public class ReceiptFactory {

	public static Receipt createNewReceipt(String kind, String id, String date, String amount, String name, String country, String city, String street, String number) {
		if (kind.equalsIgnoreCase("Entertainment")) {
			return new EntertainmentReceipt(id, date, amount, name, country, city, street, number);
		} else if (kind.equalsIgnoreCase("Basic")) {
			return new BasicReceipt(id, date, amount, name, country, city, street, number);
		} else if (kind.equalsIgnoreCase("Travel")) {
			return new TravelReceipt(id, date, amount, name, country, city, street, number);
		} else if (kind.equalsIgnoreCase("Health")) {
			return new HealthReceipt(id, date, amount, name, country, city, street, number);
		} else if (kind.equalsIgnoreCase("Other")) {
			return new OtherReceipt(id, date, amount, name, country, city, street, number);
		} else {
			return null;
		}
	}
}
