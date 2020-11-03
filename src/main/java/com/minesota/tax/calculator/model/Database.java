package com.minesota.tax.calculator.model;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

import com.minesota.tax.calculator.manager.InputSystem;
import com.minesota.tax.calculator.manager.OutputSystem;

public class Database {

    private String taxpayersInfoFilesPath;
    private final ArrayList<TaxPayer> taxpayersArrayList = new ArrayList<>();
    private static Database databaseInstance = null;

    public static Database getInstance() {
        if (databaseInstance == null) {
            databaseInstance = new Database();
        }

        return databaseInstance;
    }

    public String getTaxpayersInfoFilesPath() {
        return taxpayersInfoFilesPath;
    }

    public void setTaxpayersInfoFilesPath(String taxpayersInfoFilesPath) {
        this.taxpayersInfoFilesPath = taxpayersInfoFilesPath;
    }

    public void proccessTaxpayersDataFromFilesIntoDatabase(String afmInfoFilesFolderPath, List<String> taxpayersAfmInfoFiles) {
        InputSystem inputSystem = InputSystem.getInstance();
        inputSystem.addTaxpayersDataFromFilesIntoDatabase(afmInfoFilesFolderPath, taxpayersAfmInfoFiles);
    }

    public void addTaxpayerToList(TaxPayer taxpayer) {
        taxpayersArrayList.add(taxpayer);
    }

    public int getTaxpayersArrayListSize() {
        return taxpayersArrayList.size();
    }

    public TaxPayer getTaxpayerFromArrayList(int index) {
        return taxpayersArrayList.get(index);
    }

    public void removeTaxpayerFromArrayList(int index) {
        taxpayersArrayList.remove(index);
    }

    public String getTaxpayerNameAfmValuesPairList(int index) {
        TaxPayer taxpayer = taxpayersArrayList.get(index);
        return taxpayer.getName() + " | " + taxpayer.getAFM();
    }

    public String[] getTaxpayersNameAfmValuesPairList() {
        String[] taxpayersNameAfmValuesPairList = new String[taxpayersArrayList.size()];

        int c = 0;
        for (TaxPayer taxpayer : taxpayersArrayList) {
            taxpayersNameAfmValuesPairList[c++] = taxpayer.getName() + " | " + taxpayer.getAFM();
        }

        return taxpayersNameAfmValuesPairList;
    }

    public void updateTaxpayerInputFile(int index) {
        File taxpayersInfoFilesPathFileObject = new File(taxpayersInfoFilesPath);
        FilenameFilter fileNameFilter = (dir, name) -> (name.toLowerCase().endsWith("_info.txt") || name.toLowerCase().endsWith("_info.xml"));
        OutputSystem outputSystem = OutputSystem.getInstance();

        for (File file : taxpayersInfoFilesPathFileObject.listFiles(fileNameFilter)) {
            if (!file.getName().contains(taxpayersArrayList.get(index).getAFM())) continue;

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