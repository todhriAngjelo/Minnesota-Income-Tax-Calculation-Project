package com.minesota.tax.calculator.model;

import com.minesota.tax.calculator.manager.InputSystem;
import com.minesota.tax.calculator.manager.OutputSystem;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

public class Database {

    private String taxpayersInfoFilesPath;
    private final List<TaxPayer> taxPayers = new ArrayList<>();
    private static Database databaseInstance = null;

    public static Database getInstance() {
        if (databaseInstance == null) {
            databaseInstance = new Database();
        }

        return databaseInstance;
    }

    public void setTaxpayersInfoFilesPath(String taxpayersInfoFilesPath) {
        this.taxpayersInfoFilesPath = taxpayersInfoFilesPath;
    }

    public void proccessTaxpayersDataFromFilesIntoDatabase(String afmInfoFilesFolderPath, List<String> taxpayersAfmInfoFiles) {
        InputSystem inputSystem = InputSystem.getInstance();
        inputSystem.addTaxpayersDataFromFilesIntoDatabase(afmInfoFilesFolderPath, taxpayersAfmInfoFiles);
    }

    public void addTaxpayerToList(TaxPayer taxpayer) {
        taxPayers.add(taxpayer);
    }

    public int getTaxpayersArrayListSize() {
        return taxPayers.size();
    }

    public TaxPayer getTaxPayerFromIndex(int index) {
        return taxPayers.get(index);
    }

    public void removeTaxpayerFromArrayList(int index) {
        taxPayers.remove(index);
    }

    public String getTaxpayerNameAfmValuesPairList(int index) {
        TaxPayer taxpayer = taxPayers.get(index);
        return taxpayer.getName() + " | " + taxpayer.getVat();
    }

    public String[] getTaxpayersNameAfmValuesPairList() {
        String[] taxpayersNameAfmValuesPairList = new String[taxPayers.size()];

        int c = 0;
        for (TaxPayer taxpayer : taxPayers) {
            taxpayersNameAfmValuesPairList[c++] = taxpayer.getName() + " | " + taxpayer.getVat();
        }

        return taxpayersNameAfmValuesPairList;
    }

    public void updateTaxpayerInputFile(int index) {
        File taxpayersInfoFilesPathFileObject = new File(taxpayersInfoFilesPath);
        FilenameFilter fileNameFilter = (dir, name) -> (name.toLowerCase().endsWith("_info.txt") || name.toLowerCase().endsWith("_info.xml"));
        OutputSystem outputSystem = OutputSystem.getInstance();

        for (File file : taxpayersInfoFilesPathFileObject.listFiles(fileNameFilter)) {
            if (!file.getName().contains(taxPayers.get(index).getVat())) continue;

            if (file.getName().toLowerCase().endsWith(".txt")) {
                outputSystem.saveUpdatedTaxpayerTxtInputFile(file.getAbsolutePath(), index);
            }
            if (file.getName().toLowerCase().endsWith(".xml")) {
                outputSystem.saveUpdatedTaxpayerXmlInputFile(file.getAbsolutePath(), index);
            }
            break;
        }
    }
}