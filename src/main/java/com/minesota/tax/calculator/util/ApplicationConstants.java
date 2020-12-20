package com.minesota.tax.calculator.util;

public class ApplicationConstants {

    public static final String TAHOMA = "Tahoma";
    public static final String[][] txtFileTags = {
            {"Name: ", ""}, {"AFM: ", ""}, {"Status: ", ""}, {"Income: ", ""},                                          //Taxpayer info
            {"Receipts:", ""},                                                                                          //Receipts begin tag
            {"Receipt ID: ", ""}, {"Date: ", ""}, {"Kind: ", ""}, {"Amount: ", ""},                                     //Receipt info
            {"Company: ", ""}, {"Country: ", ""}, {"City: ", ""}, {"Street: ", ""}, {"Number: ", ""}                    //Receipt info
    };
    public static final String[][] xmlFileTags = {
            {"<Name> ", " </Name>"}, {"<AFM> ", " </AFM>"}, {"<Status> ", " </Status>"}, {"<Income> ", " </Income>"},   //Taxpayer info
            {"<Receipts>", "</Receipts>"},                                                                              //Receipts begin/end tag
            {"<ReceiptID> ", " </ReceiptID>"}, {"<Date> ", " </Date>"}, {"<Kind> ", " </Kind>"},                        //Receipt info
            {"<Amount> ", " </Amount>"}, {"<Company> ", " </Company>"}, {"<Country> ", " </Country>"},                  //Receipt info
            {"<City> ", " </City>"}, {"<Street> ", " </Street>"}, {"<Number> ", " </Number>"}                           //Receipt info
    };

    public static final String TXT_LOG_FILE_SUFFIX = "_LOG.txt";
    public static final String XML_LOG_FILE_SUFFIX = "_LOG.xml";
    public static final String PROBLEM_OPENING_STRING = "Problem opening: ";

}
