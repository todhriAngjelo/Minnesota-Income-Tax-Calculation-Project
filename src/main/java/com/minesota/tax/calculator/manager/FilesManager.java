package com.minesota.tax.calculator.manager;

import com.minesota.tax.calculator.app.ApplicationConstants;
import com.minesota.tax.calculator.model.Receipt;
import com.minesota.tax.calculator.model.TaxPayer;
import com.minesota.tax.calculator.model.enumeration.FamilyStatusEnum;
import com.minesota.tax.calculator.util.TaxPayerUtils;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;

import static com.minesota.tax.calculator.app.ApplicationConstants.*;
import static com.minesota.tax.calculator.util.BasicTaxBuilder.getBasicTaxBy;


/**
 * Class responsible for the files management and parsing business logic. Also holds the list of taxPayers that are
 * loaded and cached in the application
 */
public class FilesManager {

    private static final String PROBLEM_OPENING_STRING = "Problem opening: ";
    static Logger logger = Logger.getLogger(FilesManager.class.getName());

    private static FilesManager filesManagerInstance = null;
    private final List<TaxPayer> cachedTaxPayers = new ArrayList<>();
    private String taxPayersFilePath;

    public static FilesManager getInstance() {
        if (filesManagerInstance == null) {
            filesManagerInstance = new FilesManager();
        }

        return filesManagerInstance;
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
        FilesManager filesManager = FilesManager.getInstance();
        String[] taxpayersNameAfmValuesPairList = new String[filesManager.getCachedTaxPayers().size()];

        int c = 0;
        for (TaxPayer taxpayer : filesManager.getCachedTaxPayers()) {
            taxpayersNameAfmValuesPairList[c++] = taxpayer.getName() + " | " + taxpayer.getVat();
        }

        return taxpayersNameAfmValuesPairList;
    }

    public static Scanner getScanner(String afmInfoFileFolderPath, String afmInfoFile) {
        try {
            return new Scanner(new FileInputStream(afmInfoFileFolderPath + "\\" + afmInfoFile));
        } catch (FileNotFoundException e) {
            logger.warning(PROBLEM_OPENING_STRING + afmInfoFile + "file. ");
            System.exit(0);
        }
        return null;
    }

    /**
     * Retrieves a folder path and a list of taxPayers file names, parses the
     * xml or txt files and caches the tax payers info
     * <br>
     *
     * @param folderPath        the folder path where the tax payer file names are contained
     * @param taxPayerFileNames the tax payer file names with their respective file extension
     */
    public void cacheTaxPayers(String folderPath, List<String> taxPayerFileNames) {

        for (String tpFile : taxPayerFileNames) {
            Scanner scanner = getScanner(folderPath, tpFile);
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
        File filepath = new File(taxPayersFilePath);
        FilenameFilter filenameFilter = (dir, name) -> (name.toLowerCase().endsWith("_info.txt") || name.toLowerCase().endsWith("_info.xml"));

        File[] filteredFiles = filepath.listFiles(filenameFilter);

        if (filteredFiles == null) {
            logger.warning("No log files found at the directory");
            return;
        }

        if (filteredFiles.length > 1) {
            logger.warning("More than one info files found at the directory");
            return; // break update execution
        }

        if (filteredFiles[0] != null && filteredFiles[0].getName().toLowerCase().endsWith(".txt"))
            updateTxtTaxpayerLogFile(filepath.getAbsolutePath(), index);

        if (filepath.getName().toLowerCase().endsWith(".xml"))
            updateXmlTaxpayerFile(filepath.getAbsolutePath(), index);
    }

    private void updateXmlTaxpayerFile(String filePath, int taxpayerIndex) {
        PrintWriter outputStream = null;
        try {
            outputStream = new PrintWriter(new FileOutputStream(filePath));
        } catch (FileNotFoundException e) {
            logger.warning("Problem opening" + filePath);
        }

        if (outputStream == null) {
            logger.warning("Something went wrong while updating xml file. Empty output stream");
            return;
        }

        TaxPayer taxpayer = cachedTaxPayers.get(taxpayerIndex);
        outputStream.println("<Name> " + taxpayer.getName() + " </Name>");
        outputStream.println("<AFM> " + taxpayer.getVat() + " </AFM>");
        outputStream.println("<Status> " + taxpayer.getFamilyStatus().getDescription() + " </Status>");
        outputStream.println("<Income> " + taxpayer.getIncome() + " </Income>");

        if (!taxpayer.getReceipts().isEmpty()) {
            outputStream.println();
            outputStream.println("<Receipts>");
            outputStream.println();

            for (Receipt receipt : taxpayer.getReceipts()) {
                outputStream.println("<ReceiptID> " + receipt.getId() + " </ReceiptID>");
                outputStream.println("<Date> " + receipt.getDate() + " </Date>");
                outputStream.println("<Kind> " + receipt.getKind() + " </Kind>");
                outputStream.println("<Amount> " + receipt.getAmount() + " </Amount>");
                outputStream.println("<Company> " + receipt.getCompany().getName() + " </Company>");
                outputStream.println("<Country> " + receipt.getCompany().getCountry() + " </Country>");
                outputStream.println("<City> " + receipt.getCompany().getCity() + " </City>");
                outputStream.println("<Street> " + receipt.getCompany().getStreet() + " </Street>");
                outputStream.println("<Number> " + receipt.getCompany().getNumber() + " </Number>");
                outputStream.println();
            }

            outputStream.println("</Receipts>");
        }

        outputStream.close();
    }

    /**
     * Given a taxpayer index and a folder save path, saves selected taxpayer into _LOG.txt file
     *
     * @param folderSavePath save folder path
     * @param taxpayerIndex  the selected taxpayer index
     */
    public void saveTxtTaxpayerToFile(String folderSavePath, int taxpayerIndex) {
        TaxPayer taxpayer = cachedTaxPayers.get(taxpayerIndex);

        PrintWriter outputStream = null;
        try {
            outputStream = new PrintWriter(new FileOutputStream(folderSavePath + "//" + taxpayer.getVat() + ApplicationConstants.TXT_LOG_FILE_SUFFIX));
        } catch (FileNotFoundException e) {
            logger.warning(PROBLEM_OPENING_STRING + folderSavePath + "//" + taxpayer.getVat() + TXT_LOG_FILE_SUFFIX);
        }

        outputStream.println("Name: " + taxpayer.getName());
        outputStream.println("AFM: " + taxpayer.getVat());
        outputStream.println("Income: " + taxpayer.getIncome());
        outputStream.println("Basic Tax: " + taxpayer.getBasicTax());
        if (taxpayer.getTaxIncrease() != 0) {
            outputStream.println("Tax Increase: " + taxpayer.getTaxIncrease());
        } else {
            outputStream.println("Tax Decrease: " + taxpayer.getTaxDecrease());
        }
        outputStream.println("Total Tax: " + taxpayer.getTotalTax());
        outputStream.println("Total Receipts Amount: " + TaxPayerUtils.getReceiptsTotalAmount(taxpayer.getReceipts()));
        outputStream.println("Entertainment: " + TaxPayerUtils.getReceiptsTotalAmount("Entertainment", taxpayer.getReceipts()));
        outputStream.println("Basic: " + TaxPayerUtils.getReceiptsTotalAmount("Basic", taxpayer.getReceipts()));
        outputStream.println("Travel: " + TaxPayerUtils.getReceiptsTotalAmount("Travel", taxpayer.getReceipts()));
        outputStream.println("Health: " + TaxPayerUtils.getReceiptsTotalAmount("Health", taxpayer.getReceipts()));
        outputStream.println("Other: " + TaxPayerUtils.getReceiptsTotalAmount("Other", taxpayer.getReceipts()));

        outputStream.close();

        JOptionPane.showMessageDialog(null, "File saved", "Message", JOptionPane.INFORMATION_MESSAGE);
    }

    private void updateTxtTaxpayerLogFile(String filePath, int taxpayerIndex) {
        PrintWriter outputStream = null;
        try {
            outputStream = new PrintWriter(new FileOutputStream(filePath));
        } catch (FileNotFoundException e) {
            logger.warning(PROBLEM_OPENING_STRING + filePath);
        }

        TaxPayer taxpayer = cachedTaxPayers.get(taxpayerIndex);
        outputStream.println("Name: " + taxpayer.getName());
        outputStream.println("AFM: " + taxpayer.getVat());
        outputStream.println("Status: " + taxpayer.getFamilyStatus().getDescription());
        outputStream.println("Income: " + taxpayer.getIncome());

        if (!taxpayer.getReceipts().isEmpty()) {
            outputStream.println();
            outputStream.println("Receipts:");
            outputStream.println();

            for (Receipt receipt : taxpayer.getReceipts()) {
                outputStream.println("Receipt ID: " + receipt.getId());
                outputStream.println("Date: " + receipt.getDate());
                outputStream.println("Kind: " + receipt.getKind());
                outputStream.println("Amount: " + receipt.getAmount());
                outputStream.println("Company: " + receipt.getCompany().getName());
                outputStream.println("Country: " + receipt.getCompany().getCountry());
                outputStream.println("City: " + receipt.getCompany().getCity());
                outputStream.println("Street: " + receipt.getCompany().getStreet());
                outputStream.println("Number: " + receipt.getCompany().getNumber());
                outputStream.println();
            }
        }

        outputStream.close();
    }

    public void updateXmlTaxpayerLogFile(String folderSavePath, int taxpayerIndex) {
        TaxPayer taxpayer = cachedTaxPayers.get(taxpayerIndex);

        PrintWriter outputStream = null;
        try {
            outputStream = new PrintWriter(new FileOutputStream(folderSavePath + "//" + taxpayer.getVat() + XML_LOG_FILE_SUFFIX));
        } catch (FileNotFoundException e) {
            logger.warning(PROBLEM_OPENING_STRING + folderSavePath + "//" + taxpayer.getVat() + XML_LOG_FILE_SUFFIX);
        }

        if (outputStream == null) {
            logger.warning("Something went wrong while updating xml file. Empty output stream");
            return;
        }

        outputStream.println("<Name> " + taxpayer.getName() + " </Name>");
        outputStream.println("<AFM> " + taxpayer.getVat() + " </AFM>");
        outputStream.println("<Income> " + taxpayer.getIncome() + " </Income>");
        outputStream.println("<BasicTax> " + taxpayer.getBasicTax() + " </BasicTax>");
        if (taxpayer.getTaxIncrease() != 0) {
            outputStream.println("<TaxIncrease> " + taxpayer.getTaxIncrease() + " </TaxIncrease>");
        } else {
            outputStream.println("<TaxDecrease> " + taxpayer.getTaxDecrease() + " </TaxDecrease>");
        }
        outputStream.println("<TotalTax> " + taxpayer.getTotalTax() + " </TotalTax>");
        outputStream.println("<Receipts> " + TaxPayerUtils.getReceiptsTotalAmount(taxpayer.getReceipts()) + " </Receipts>");
        outputStream.println("<Entertainment> " + TaxPayerUtils.getReceiptsTotalAmount("Entertainment", taxpayer.getReceipts()) + " </Entertainment>");
        outputStream.println("<Basic> " + TaxPayerUtils.getReceiptsTotalAmount("Basic", taxpayer.getReceipts()) + " </Basic>");
        outputStream.println("<Travel> " + TaxPayerUtils.getReceiptsTotalAmount("Travel", taxpayer.getReceipts()) + " </Travel>");
        outputStream.println("<Health> " + TaxPayerUtils.getReceiptsTotalAmount("Health", taxpayer.getReceipts()) + " </Health>");
        outputStream.println("<Other> " + TaxPayerUtils.getReceiptsTotalAmount("Other", taxpayer.getReceipts()) + " </Other>");

        outputStream.close();

        JOptionPane.showMessageDialog(null, "File saved", "Message", JOptionPane.INFORMATION_MESSAGE);
    }

    public void setTaxPayersFilePath(String taxPayersFilePath) {
        this.taxPayersFilePath = taxPayersFilePath;
    }

    public List<TaxPayer> getCachedTaxPayers() {
        return cachedTaxPayers;
    }
}