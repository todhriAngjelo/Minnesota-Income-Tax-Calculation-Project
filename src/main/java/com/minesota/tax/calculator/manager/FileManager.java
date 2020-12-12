package com.minesota.tax.calculator.manager;

import com.minesota.tax.calculator.model.Receipt;
import com.minesota.tax.calculator.model.TaxPayer;
import com.minesota.tax.calculator.model.enumeration.FamilyStatusEnum;
import com.minesota.tax.calculator.util.FileUtils;
import com.minesota.tax.calculator.util.TaxPayerUtils;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static com.minesota.tax.calculator.app.ApplicationConstants.txtFileTags;
import static com.minesota.tax.calculator.app.ApplicationConstants.xmlFileTags;
import static com.minesota.tax.calculator.util.TaxCategoryBuilder.getBasicTaxBy;

public class FileManager {

    private static FileManager fileManagerInstance = null;
    private final List<TaxPayer> cachedTaxPayers = new ArrayList<>();
    private String taxPayersFilePath;

    public static FileManager getInstance() {
        if (fileManagerInstance == null) {
            fileManagerInstance = new FileManager();
        }

        return fileManagerInstance;
    }

    /**
     * Returns a formatted array of strings which represents
     * the loaded tax payers with the following format:
     * <p>
     * TaxPayerFullName | TaxPayerVat
     * <p>
     * Example:
     * Apostolos Zarras | 130456093
     */
    public static String[] getFormattedTaxPayersStrings() {
        FileManager fileManager = FileManager.getInstance();
        String[] taxpayersNameAfmValuesPairList = new String[fileManager.getCachedTaxPayers().size()];

        int c = 0;
        for (TaxPayer taxpayer : fileManager.getCachedTaxPayers()) {
            taxpayersNameAfmValuesPairList[c++] = taxpayer.getName() + " | " + taxpayer.getVat();
        }

        return taxpayersNameAfmValuesPairList;
    }

    /**
     * Retrieves a folder path and a list of taxPayers file names, parses the
     * xml or txt files and caches the tax payers info
     * <br>
     *
     * @param folderPath        the folder path where the tax payer file names are contained
     * @param taxPayerFileNames the tax payer file names with their respective file extension
     */
    public void saveTaxPayers(String folderPath, List<String> taxPayerFileNames) {

        for (String tpFile : taxPayerFileNames) {
            Scanner scanner = FileUtils.getScanner(folderPath, tpFile);
            if (tpFile.endsWith(".txt")) {
                cachedTaxPayers.add(parseTaxPayerFile(scanner, txtFileTags));
            } else if (tpFile.endsWith(".xml")) {
                cachedTaxPayers.add(parseTaxPayerFile(scanner, xmlFileTags));
            }
        }
    }

    private TaxPayer parseTaxPayerFile(Scanner inputStream, String[][] tagArray) {
        String taxpayerName = getParameterValueFromFileLine(inputStream.nextLine(), tagArray[0][0], tagArray[0][1]);
        String taxpayerAFM = getParameterValueFromFileLine(inputStream.nextLine(), tagArray[1][0], tagArray[1][1]);
        String taxpayerStatus = getParameterValueFromFileLine(inputStream.nextLine(), tagArray[2][0], tagArray[2][1]);
        String taxpayerIncome = getParameterValueFromFileLine(inputStream.nextLine(), tagArray[3][0], tagArray[3][1]);
        TaxPayer newTaxpayer = new TaxPayer(
                taxpayerName,
                taxpayerAFM,
                FamilyStatusEnum.fromValue(taxpayerStatus),
                taxpayerIncome,
                getBasicTaxBy(taxpayerStatus.toLowerCase(), Double.parseDouble(taxpayerIncome)));

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

            newTaxpayer.getReceipts().add(newReceipt);
            TaxPayerUtils.applyTaxPayerTaxAdjustments(newTaxpayer);
        }

        return newTaxpayer;
    }

    private String getParameterValueFromFileLine(String fileLine, String parameterStartField, String parameterEndField) {
        return fileLine.substring(parameterStartField.length(), fileLine.length() - parameterEndField.length());
    }

    public void updateTaxpayerInputFile(int index) {
        File taxpayersInfoFilesPathFileObject = new File(taxPayersFilePath);
        FilenameFilter fileNameFilter = (dir, name) -> (name.toLowerCase().endsWith("_info.txt") || name.toLowerCase().endsWith("_info.xml"));
        OutputSystem outputSystem = OutputSystem.getInstance();

        for (File file : taxpayersInfoFilesPathFileObject.listFiles(fileNameFilter)) {
            if (!file.getName().contains(cachedTaxPayers.get(index).getVat())) continue;

            if (file.getName().toLowerCase().endsWith(".txt")) {
                outputSystem.saveUpdatedTaxpayerTxtInputFile(file.getAbsolutePath(), index);
            }
            if (file.getName().toLowerCase().endsWith(".xml")) {
                outputSystem.saveUpdatedTaxpayerXmlInputFile(file.getAbsolutePath(), index);
            }
            break;
        }
    }

    public String getTaxPayersFilePath() {
        return taxPayersFilePath;
    }

    public void setTaxPayersFilePath(String taxPayersFilePath) {
        this.taxPayersFilePath = taxPayersFilePath;
    }

    public List<TaxPayer> getCachedTaxPayers() {
        return cachedTaxPayers;
    }
}