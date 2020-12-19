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

    /**
     * Returns an instance of the fila manager object ( singleton )
     */
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
    public String[] getFormattedTaxPayersStrings() {
        FilesManager filesManager = FilesManager.getInstance();
        String[] taxpayersNameAfmValuesPairList = new String[filesManager.getCachedTaxPayers().size()];

        int c = 0;
        for (TaxPayer taxpayer : filesManager.getCachedTaxPayers()) {
            taxpayersNameAfmValuesPairList[c++] = taxpayer.getName() + " | " + taxpayer.getVat();
        }

        return taxpayersNameAfmValuesPairList;
    }

    public Scanner getScanner(String folderPath, String filename) {
        try {
            return new Scanner(new FileInputStream(folderPath + "\\" + filename));
        } catch (FileNotFoundException e) {
            logger.warning(PROBLEM_OPENING_STRING + filename + "file. ");
            System.exit(0);
        }
        return null;
    }

    /**
     * Retrieves a folder path and a list of taxPayers file names, parses the
     * xml or txt files and caches the tax payers info
     * <br>
     *
     * @param folderPath the folder path where the tax payer file names are contained
     * @param filenames  the tax payer file names with their respective file extension
     */
    public void cacheTaxPayers(String folderPath, List<String> filenames) {

        for (String filename : filenames) {
            Scanner scanner = getScanner(folderPath, filename);
            if (filename.endsWith(".txt")) {
                cachedTaxPayers.add(parseTaxPayerFile(scanner, txtFileTags));
            } else if (filename.endsWith(".xml")) {
                cachedTaxPayers.add(parseTaxPayerFile(scanner, xmlFileTags));
            }
        }
    }

    /**
     * Given a taxpayer index and a folder save path, saves selected taxpayer into _LOG.txt file.
     * The log file contains the taxpayer basic information,
     * his calculated tax information ( total tax, basic tax, increased/decreased tax ) and
     * his receipts total amounts.
     *
     * @param folderSavePath save folder path
     * @param taxpayerIndex  the selected taxpayer index
     */
    public void createTxtTaxpayerLogFile(String folderSavePath, int taxpayerIndex) {
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

    /**
     * Given a taxpayer index and a folder save path, saves selected taxpayer into _LOG.xml file.
     * The log file contains the taxpayer basic information,
     * his calculated tax information ( total tax, basic tax, increased/decreased tax ) and
     * his receipts total amounts.
     *
     * @param folderSavePath save folder path
     * @param taxpayerIndex  the selected taxpayer index
     */
    public void createXmlTaxpayerLogFile(String folderSavePath, int taxpayerIndex) {
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

    /**
     * Given a taxpayer index this method replaces the already existing _INFO file with a new one
     * that holds the current taxpayer instance details ( with the new/modified/deleted receipts )
     *
     * @param index the taxpayer index
     */
    public void updateTaxpayerReceipts(int index) {
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
            updateTxtTaxpayerFile(filepath.getAbsolutePath(), index);

        if (filepath.getName().toLowerCase().endsWith(".xml"))
            updateXmlTaxpayerFile(filepath.getAbsolutePath(), index);
    }

    public List<TaxPayer> getCachedTaxPayers() {
        return cachedTaxPayers;
    }

    /**
     * Abstractly parses a txt or xml file ( given the proper tagArrays ) and returns
     * a taxpayer object.
     *
     * @param inputStream the scanner input stream
     * @param tagArray    the xml or txt tag array
     * @return a taxpayer from the parsed txt or xml file
     */
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

//    // todo refactor file parsing mechanism
//    void parseTxtFile(String folderPath, String filename) {
//        Scanner scanner = getScanner(folderPath, filename);
//        Map<String, String> taxPayerMap = new HashMap<>();
//        Map<Integer, Map<String, String>> receiptsMap = new HashMap<>();
//
//        // scan basic taxpayer info
//        while (scanner.hasNextLine()) {
//            String currentLine = scanner.nextLine();
//
//            if (currentLine.equals("") || currentLine.equals("\n")) continue;
//            if (currentLine.equals("Receipts:")) break;
//
//            String[] splitLine = currentLine.split(":");
//            taxPayerMap.put(splitLine[0], splitLine[1]);
//        }
//
//        // scan taxpayer receipts info
//        scanner.nextLine();
//        int receiptId;
//        Map<String, String> receiptMap = new HashMap<>();
//        while (scanner.hasNextLine()) {
//            String currentLine = scanner.nextLine();
//
//            if (currentLine.equals("") || currentLine.equals("\n")) {
//                receiptsMap.put(Integer.parseInt(receiptMap.get("Receipt ID")), receiptMap);
//                receiptMap.clear();
//                continue;
//            }
//
//            String[] splitLine = currentLine.split(":");
//            receiptMap.put(splitLine[0], splitLine[1]);
//        }
//

//    }

    private String getParameterValueFromFileLine(String fileLine, String parameterStartField, String parameterEndField) {
        return fileLine.substring(parameterStartField.length(), fileLine.length() - parameterEndField.length());
    }

    /**
     * Given a filepath and a taxpayer index it creates/updates a xml file
     * with the current taxpayer info
     *
     * @param filePath      the filepath to create/replace
     * @param taxpayerIndex the taxpayer index
     */
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
     * Given a filepath and a taxpayer index it creates/updates a txt file
     * with the current taxpayer info
     *
     * @param filePath      the filepath to create/replace
     * @param taxpayerIndex the taxpayer index
     */
    private void updateTxtTaxpayerFile(String filePath, int taxpayerIndex) {
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

    public void setTaxPayersFilePath(String taxPayersFilePath) {
        this.taxPayersFilePath = taxPayersFilePath;
    }
}