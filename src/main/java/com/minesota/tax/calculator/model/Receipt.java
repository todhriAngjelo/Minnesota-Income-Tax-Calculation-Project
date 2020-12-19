package com.minesota.tax.calculator.model;

public class Receipt {
    private String kind;
    private String id;
    private String date;
    private double amount;
    private Company company;

    public Receipt(String kind, String id, String date, String amount, String name, String country, String city, String street, String number) {
        this.kind = kind;
        this.id = id;
        this.date = date;
        this.amount = Double.parseDouble(amount);
        this.company = new Company(name, country, city, street, number);
    }

    public String getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public String getKind() {
        return kind;
    }

    public double getAmount() {
        return amount;
    }

    public Company getCompany() {
        return company;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public String toString() {
        return "ID: " + id
                + "\nDate: " + date
                + "\nKind: " + kind
                + "\nAmount: " + String.format("%.2f", amount)
                + "\nCompany: " + company.getName()
                + "\nCountry: " + company.getCountry()
                + "\nCity: " + company.getCity()
                + "\nStreet: " + company.getStreet()
                + "\nNumber: " + company.getNumber();
    }
}