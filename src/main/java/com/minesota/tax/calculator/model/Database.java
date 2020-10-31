package com.minesota.tax.calculator.model;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

import com.minesota.tax.calculator.manager.InputSystem;
import com.minesota.tax.calculator.manager.OutputSystem;

public class Database {
	private static String taxpayersInfoFilesPath;
	private static final ArrayList<TaxPayer> taxpayersArrayList = new ArrayList<>();

	public static String getTaxpayersInfoFilesPath() {
		return Database.taxpayersInfoFilesPath;
	}

	public static void setTaxpayersInfoFilesPath(String taxpayersInfoFilesPath) {
		Database.taxpayersInfoFilesPath = taxpayersInfoFilesPath;
	}

	public static void proccessTaxpayersDataFromFilesIntoDatabase(String afmInfoFilesFolderPath, List<String> taxpayersAfmInfoFiles) {
		InputSystem.addTaxpayersDataFromFilesIntoDatabase(afmInfoFilesFolderPath, taxpayersAfmInfoFiles);
	}

	public static void addTaxpayerToList(TaxPayer taxpayer) {
		taxpayersArrayList.add(taxpayer);
	}

	public static int getTaxpayersArrayListSize() {
		return taxpayersArrayList.size();
	}

	public static TaxPayer getTaxpayerFromArrayList(int index) {
		return taxpayersArrayList.get(index);
	}

	public static void removeTaxpayerFromArrayList(int index) {
		taxpayersArrayList.remove(index);
	}

	public static String getTaxpayerNameAfmValuesPairList(int index) {
		TaxPayer taxpayer = taxpayersArrayList.get(index);
		return taxpayer.getName() + " | " + taxpayer.getAFM();
	}

	public static String[] getTaxpayersNameAfmValuesPairList() {
		String[] taxpayersNameAfmValuesPairList = new String[taxpayersArrayList.size()];

		int c = 0;
		for (TaxPayer taxpayer : taxpayersArrayList) {
			taxpayersNameAfmValuesPairList[c++] = taxpayer.getName() + " | " + taxpayer.getAFM();
		}

		return taxpayersNameAfmValuesPairList;
	}

	public static void updateTaxpayerInputFile(int index) {
		File taxpayersInfoFilesPathFileObject = new File(taxpayersInfoFilesPath);
		FilenameFilter fileNameFilter = (dir, name) -> (name.toLowerCase().endsWith("_info.txt") || name.toLowerCase().endsWith("_info.xml"));

		for (File file : taxpayersInfoFilesPathFileObject.listFiles(fileNameFilter)) {
			if (!file.getName().contains(taxpayersArrayList.get(index).getAFM())) continue;

			if (file.getName().toLowerCase().endsWith(".txt")) {
				OutputSystem.saveUpdatedTaxpayerTxtInputFile(file.getAbsolutePath(), index);
			}
			if (file.getName().toLowerCase().endsWith(".xml")) {
				OutputSystem.saveUpdatedTaxpayerXmlInputFile(file.getAbsolutePath(), index);
			}
			break;
		}
	}
}