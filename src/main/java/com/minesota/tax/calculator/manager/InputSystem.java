package com.minesota.tax.calculator.manager;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;

import com.minesota.tax.calculator.model.Database;
import com.minesota.tax.calculator.model.Receipt;
import com.minesota.tax.calculator.model.TaxPayer;

public class InputSystem {

    private final String[][] txtFileTags = {
            {"Name: ", ""}, {"AFM: ", ""}, {"Status: ", ""}, {"Income: ", ""},                                          //Taxpayer info
            {"Receipts:", ""},                                                                                          //Receipts begin tag
            {"Receipt ID: ", ""}, {"Date: ", ""}, {"Kind: ", ""}, {"Amount: ", ""},                                     //Receipt info
            {"Company: ", ""}, {"Country: ", ""}, {"City: ", ""}, {"Street: ", ""}, {"Number: ", ""}                    //Receipt info
    };
    private final String[][] xmlFileTags = {
            {"<Name> ", " </Name>"}, {"<AFM> ", " </AFM>"}, {"<Status> ", " </Status>"}, {"<Income> ", " </Income>"},   //Taxpayer info
            {"<Receipts>", "</Receipts>"},                                                                              //Receipts begin/end tag
            {"<ReceiptID> ", " </ReceiptID>"}, {"<Date> ", " </Date>"}, {"<Kind> ", " </Kind>"},                        //Receipt info
            {"<Amount> ", " </Amount>"}, {"<Company> ", " </Company>"}, {"<Country> ", " </Country>"},                  //Receipt info
            {"<City> ", " </City>"}, {"<Street> ", " </Street>"}, {"<Number> ", " </Number>"}                           //Receipt info
    };
    private static InputSystem inputSystemInstance = null;

    public static InputSystem getInstance() {
        if (inputSystemInstance == null) {
            inputSystemInstance = new InputSystem();
        }

        return inputSystemInstance;
    }

    public void addTaxpayersDataFromFilesIntoDatabase(String afmInfoFilesFolderPath, List<String> taxpayersAfmInfoFiles) {
        for (String afmInfoFile : taxpayersAfmInfoFiles) {
            Scanner inputStream = openFile(afmInfoFilesFolderPath, afmInfoFile);
            if (afmInfoFile.endsWith(".txt")) {
                loadTaxpayerDataFromFileIntoDatabase(inputStream, txtFileTags);
            } else if (afmInfoFile.endsWith(".xml")) {
                loadTaxpayerDataFromFileIntoDatabase(inputStream, xmlFileTags);
            }
        }
    }

    private void loadTaxpayerDataFromFileIntoDatabase(Scanner inputStream, String[][] tagArray) {
        String taxpayerName = getParameterValueFromFileLine(inputStream.nextLine(), tagArray[0][0], tagArray[0][1]);
        String taxpayerAFM = getParameterValueFromFileLine(inputStream.nextLine(), tagArray[1][0], tagArray[1][1]);
        String taxpayerStatus = getParameterValueFromFileLine(inputStream.nextLine(), tagArray[2][0], tagArray[2][1]);
        String taxpayerIncome = getParameterValueFromFileLine(inputStream.nextLine(), tagArray[3][0], tagArray[3][1]);
        TaxPayer newTaxpayer = new TaxPayer(taxpayerName, taxpayerAFM, taxpayerStatus, taxpayerIncome);

        String fileLine;
        while (inputStream.hasNextLine()) {
            fileLine = inputStream.nextLine();
            if (fileLine.equals("")) continue;
            if (fileLine.contains(tagArray[4][0])) continue;
            if (fileLine.contains("</Receipts>")) break;

            String receiptID = getParameterValueFromFileLine(fileLine, tagArray[5][0], tagArray[5][1]);
            String receiptDate = getParameterValueFromFileLine(inputStream.nextLine(), tagArray[6][0], tagArray[6][1]);
            String receiptKind = getParameterValueFromFileLine(inputStream.nextLine(), tagArray[7][0], tagArray[7][1]);
            String receiptAmount = getParameterValueFromFileLine(inputStream.nextLine(), tagArray[8][0], tagArray[8][1]);
            String receiptCompany = getParameterValueFromFileLine(inputStream.nextLine(), tagArray[9][0], tagArray[9][1]);
            String receiptCountry = getParameterValueFromFileLine(inputStream.nextLine(), tagArray[10][0], tagArray[10][1]);
            String receiptCity = getParameterValueFromFileLine(inputStream.nextLine(), tagArray[11][0], tagArray[11][1]);
            String receiptStreet = getParameterValueFromFileLine(inputStream.nextLine(), tagArray[12][0], tagArray[12][1]);
            String receiptNumber = getParameterValueFromFileLine(inputStream.nextLine(), tagArray[13][0], tagArray[13][1]);
            Receipt newReceipt = new Receipt(receiptKind, receiptID, receiptDate, receiptAmount, receiptCompany, receiptCountry, receiptCity, receiptStreet, receiptNumber);

            newTaxpayer.addReceiptToList(newReceipt);
        }

        Database database = Database.getInstance();
        database.addTaxpayerToList(newTaxpayer);
    }

    private Scanner openFile(String afmInfoFileFolderPath, String afmInfoFile) {
        try {
            return new Scanner(new FileInputStream(afmInfoFileFolderPath + "\\" + afmInfoFile));
        } catch (FileNotFoundException e) {
            System.out.println("Problem opening " + afmInfoFile + " file.");
            System.exit(0);
        }
        return null;
    }

    private String getParameterValueFromFileLine(String fileLine, String parameterStartField, String parameterEndField) {
        return fileLine.substring(parameterStartField.length(), fileLine.length() - parameterEndField.length());
    }
}