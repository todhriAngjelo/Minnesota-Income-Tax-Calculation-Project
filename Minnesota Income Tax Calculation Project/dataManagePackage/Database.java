package dataManagePackage;
import dataManagePackage.Receipt.*;
import inputManagePackage.*;
import outputManagePackage.*;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

public class Database {
	private static String taxpayersInfoFilesPath;
	private static ArrayList<Taxpayer> taxpayersArrayList = new ArrayList<Taxpayer>();
	
	public static void setTaxpayersInfoFilesPath(String taxpayersInfoFilesPath){
		Database.taxpayersInfoFilesPath = taxpayersInfoFilesPath;
	}
	
	public static String getTaxpayersInfoFilesPath(){
		return Database.taxpayersInfoFilesPath;
	}
	
	public static void proccessTaxpayersDataFromFilesIntoDatabase(String afmInfoFilesFolderPath, List<String> taxpayersAfmInfoFiles){
		InputSystem.addTaxpayersDataFromFilesIntoDatabase(afmInfoFilesFolderPath, taxpayersAfmInfoFiles);
	}
	
	public static void addTaxpayerToList(Taxpayer taxpayer){
		taxpayersArrayList.add(taxpayer);
	}
	
	public static int getTaxpayersArrayListSize(){
		return taxpayersArrayList.size();
	}
	
	public static Taxpayer getTaxpayerFromArrayList(int index){
		return taxpayersArrayList.get(index);
	}
	
	public static void removeTaxpayerFromArrayList(int index){
		taxpayersArrayList.remove(index);
	}
	
	public static String getTaxpayerNameAfmValuesPairList(int index){
		Taxpayer taxpayer = taxpayersArrayList.get(index);
		return taxpayer.getName() + " | " + taxpayer.getAFM();
	}
	
	public static String[] getTaxpayersNameAfmValuesPairList(){
		String[] taxpayersNameAfmValuesPairList = new String[taxpayersArrayList.size()];
		
		int c = 0;
		for (Taxpayer taxpayer : taxpayersArrayList){
			taxpayersNameAfmValuesPairList[c++] = taxpayer.getName() + " | " + taxpayer.getAFM();
		}
		
		return taxpayersNameAfmValuesPairList;
	}
	
	public static void updateTaxpayerInputFile(int index){
		File taxpayersInfoFilesPathFileObject = new File(taxpayersInfoFilesPath);
		FilenameFilter fileNameFilter = new FilenameFilter(){
            public boolean accept(File dir, String name) {
               return (name.toLowerCase().endsWith("_info.txt") || name.toLowerCase().endsWith("_info.xml"));
            }
         };
		
		for (File file : taxpayersInfoFilesPathFileObject.listFiles(fileNameFilter)){
			if (!file.getName().contains(taxpayersArrayList.get(index).getAFM())) continue;
			
			if (file.getName().toLowerCase().endsWith(".txt")){
				OutputSystem.saveUpdatedTaxpayerTxtInputFile(file.getAbsolutePath(), index);
			}
			if (file.getName().toLowerCase().endsWith(".xml")){
				OutputSystem.saveUpdatedTaxpayerXmlInputFile(file.getAbsolutePath(), index);
			}
			break;
		}
	}	
}