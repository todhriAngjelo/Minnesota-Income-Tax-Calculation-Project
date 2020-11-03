package com.minesota.tax.calculator.manager;

import java.awt.Dialog.ModalExclusionType;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.text.DecimalFormat;

import javax.swing.*;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.PieSectionLabelGenerator;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import com.minesota.tax.calculator.model.Database;
import com.minesota.tax.calculator.model.Receipt;
import com.minesota.tax.calculator.model.TaxPayer;

public class OutputSystem {

    private DefaultPieDataset receiptPieChartDataset;
    private JFreeChart receiptPieJFreeChart;
    private PiePlot piePlot;
    private ChartFrame receiptPieChartFrame;
    private static OutputSystem outputSystemInstance = null;

    public static OutputSystem getInstance() {
        if (outputSystemInstance == null) {
            outputSystemInstance = new OutputSystem();
        }

        return outputSystemInstance;
    }

    public DefaultPieDataset getReceiptPieChartDataset() {
        return receiptPieChartDataset;
    }

    public JFreeChart getReceiptPieJFreeChart() {
        return receiptPieJFreeChart;
    }

    public PiePlot getPiePlot() {
        return piePlot;
    }

    public ChartFrame getReceiptPieChartFrame() {
        return receiptPieChartFrame;
    }

    public void saveUpdatedTaxpayerTxtInputFile(String filePath, int taxpayerIndex) {
        PrintWriter outputStream = null;
        try {
            outputStream = new PrintWriter(new FileOutputStream(filePath));
        } catch (FileNotFoundException e) {
            System.out.println("Problem opening: " + filePath);
        }

        Database database = Database.getInstance();
        TaxPayer taxpayer = database.getTaxpayerFromArrayList(taxpayerIndex);
        outputStream.println("Name: " + taxpayer.getName());
        outputStream.println("AFM: " + taxpayer.getAFM());
        outputStream.println("Status: " + taxpayer.getFamilyStatus());
        outputStream.println("Income: " + taxpayer.getIncome());

        if (taxpayer.getReceiptsArrayList().size() > 0) {
            outputStream.println();
            outputStream.println("Receipts:");
            outputStream.println();

            for (Receipt receipt : taxpayer.getReceiptsArrayList()) {
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

    public void saveUpdatedTaxpayerXmlInputFile(String filePath, int taxpayerIndex) {
        PrintWriter outputStream = null;
        try {
            outputStream = new PrintWriter(new FileOutputStream(filePath));
        } catch (FileNotFoundException e) {
            System.out.println("Problem opening: " + filePath);
        }

        Database database = Database.getInstance();
        TaxPayer taxpayer = database.getTaxpayerFromArrayList(taxpayerIndex);
        outputStream.println("<Name> " + taxpayer.getName() + " </Name>");
        outputStream.println("<AFM> " + taxpayer.getAFM() + " </AFM>");
        outputStream.println("<Status> " + taxpayer.getFamilyStatus() + " </Status>");
        outputStream.println("<Income> " + taxpayer.getIncome() + " </Income>");

        if (taxpayer.getReceiptsArrayList().size() > 0) {
            outputStream.println();
            outputStream.println("<Receipts>");
            outputStream.println();

            for (Receipt receipt : taxpayer.getReceiptsArrayList()) {
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


    public void saveTaxpayerInfoToTxtLogFile(String folderSavePath, int taxpayerIndex) {
        Database database = Database.getInstance();
        TaxPayer taxpayer = database.getTaxpayerFromArrayList(taxpayerIndex);

        PrintWriter outputStream = null;
        try {
            outputStream = new PrintWriter(new FileOutputStream(folderSavePath + "//" + taxpayer.getAFM() + "_LOG.txt"));
        } catch (FileNotFoundException e) {
            System.out.println("Problem opening: " + folderSavePath + "//" + taxpayer.getAFM() + "_LOG.txt");
        }

        outputStream.println("Name: " + taxpayer.getName());
        outputStream.println("AFM: " + taxpayer.getAFM());
        outputStream.println("Income: " + taxpayer.getIncome());
        outputStream.println("Basic Tax: " + taxpayer.getBasicTax());
        if (taxpayer.getTaxInxrease() != 0) {
            outputStream.println("Tax Increase: " + taxpayer.getTaxInxrease());
        } else {
            outputStream.println("Tax Decrease: " + taxpayer.getTaxDecrease());
        }
        outputStream.println("Total Tax: " + taxpayer.getTotalTax());
        outputStream.println("Total Receipts Amount: " + taxpayer.getTotalReceiptsAmount());
        outputStream.println("Entertainment: " + taxpayer.getEntertainmentReceiptsTotalAmount());
        outputStream.println("Basic: " + taxpayer.getBasicReceiptsTotalAmount());
        outputStream.println("Travel: " + taxpayer.getTravelReceiptsTotalAmount());
        outputStream.println("Health: " + taxpayer.getHealthReceiptsTotalAmount());
        outputStream.println("Other: " + taxpayer.getOtherReceiptsTotalAmount());

        outputStream.close();

        JOptionPane.showMessageDialog(null, "Η αποθήκευση ολοκληρώθηκε", "Μήνυμα", JOptionPane.INFORMATION_MESSAGE);
    }

    public void saveTaxpayerInfoToXmlLogFile(String folderSavePath, int taxpayerIndex) {
        Database database = Database.getInstance();
        TaxPayer taxpayer = database.getTaxpayerFromArrayList(taxpayerIndex);

        PrintWriter outputStream = null;
        try {
            outputStream = new PrintWriter(new FileOutputStream(folderSavePath + "//" + taxpayer.getAFM() + "_LOG.xml"));
        } catch (FileNotFoundException e) {
            System.out.println("Problem opening: " + folderSavePath + "//" + taxpayer.getAFM() + "_LOG.xml");
        }

        outputStream.println("<Name> " + taxpayer.getName() + " </Name>");
        outputStream.println("<AFM> " + taxpayer.getAFM() + " </AFM>");
        outputStream.println("<Income> " + taxpayer.getIncome() + " </Income>");
        outputStream.println("<BasicTax> " + taxpayer.getBasicTax() + " </BasicTax>");
        if (taxpayer.getTaxInxrease() != 0) {
            outputStream.println("<TaxIncrease> " + taxpayer.getTaxInxrease() + " </TaxIncrease>");
        } else {
            outputStream.println("<TaxDecrease> " + taxpayer.getTaxDecrease() + " </TaxDecrease>");
        }
        outputStream.println("<TotalTax> " + taxpayer.getTotalTax() + " </TotalTax>");
        outputStream.println("<Receipts> " + taxpayer.getTotalReceiptsAmount() + " </Receipts>");
        outputStream.println("<Entertainment> " + taxpayer.getEntertainmentReceiptsTotalAmount() + " </Entertainment>");
        outputStream.println("<Basic> " + taxpayer.getBasicReceiptsTotalAmount() + " </Basic>");
        outputStream.println("<Travel> " + taxpayer.getTravelReceiptsTotalAmount() + " </Travel>");
        outputStream.println("<Health> " + taxpayer.getHealthReceiptsTotalAmount() + " </Health>");
        outputStream.println("<Other> " + taxpayer.getOtherReceiptsTotalAmount() + " </Other>");

        outputStream.close();

        JOptionPane.showMessageDialog(null, "Η αποθήκευση ολοκληρώθηκε", "Μήνυμα", JOptionPane.INFORMATION_MESSAGE);
    }

    public void createTaxpayerReceiptsPieJFreeChart(int taxpayerIndex) {
        receiptPieChartDataset = new DefaultPieDataset();
        Database database = Database.getInstance();
        TaxPayer taxpayer = database.getTaxpayerFromArrayList(taxpayerIndex);

        receiptPieChartDataset.setValue("Basic", taxpayer.getBasicReceiptsTotalAmount());
        receiptPieChartDataset.setValue("Entertainment", taxpayer.getEntertainmentReceiptsTotalAmount());
        receiptPieChartDataset.setValue("Travel", taxpayer.getTravelReceiptsTotalAmount());
        receiptPieChartDataset.setValue("Health", taxpayer.getHealthReceiptsTotalAmount());
        receiptPieChartDataset.setValue("Other", taxpayer.getOtherReceiptsTotalAmount());

        receiptPieJFreeChart = ChartFactory.createPieChart("Receipt Pie Chart", receiptPieChartDataset);
        piePlot = (PiePlot) receiptPieJFreeChart.getPlot();
        PieSectionLabelGenerator generator = new StandardPieSectionLabelGenerator("{0}: {1}$ ({2})", new DecimalFormat("0.00"), new DecimalFormat("0.00%"));
        piePlot.setLabelGenerator(generator);

        receiptPieChartFrame = new ChartFrame(database.getTaxpayerNameAfmValuesPairList(taxpayerIndex), receiptPieJFreeChart);
        receiptPieChartFrame.pack();
        receiptPieChartFrame.setResizable(false);
        receiptPieChartFrame.setLocationRelativeTo(null);
        receiptPieChartFrame.setModalExclusionType(ModalExclusionType.APPLICATION_EXCLUDE);
        receiptPieChartFrame.setVisible(true);
    }

    public void createTaxpayerTaxAnalysisBarJFreeChart(int taxpayerIndex) {
        DefaultCategoryDataset taxAnalysisBarChartDataset = new DefaultCategoryDataset();
        Database database = Database.getInstance();
        TaxPayer taxpayer = database.getTaxpayerFromArrayList(taxpayerIndex);

        String taxVariationType = taxpayer.getTaxInxrease() != 0 ? "Tax Increase" : "Tax Decrease";
        double taxVariationAmount = taxpayer.getTaxInxrease() != 0 ? taxpayer.getTaxInxrease() : taxpayer.getTaxDecrease() * (-1);

        taxAnalysisBarChartDataset.setValue(taxpayer.getBasicTax(), "Tax", "Basic Tax");
        taxAnalysisBarChartDataset.setValue(taxVariationAmount, "Tax", taxVariationType);
        taxAnalysisBarChartDataset.setValue(taxpayer.getTotalTax(), "Tax", "Total Tax");

        JFreeChart taxAnalysisJFreeChart = ChartFactory.createBarChart("Tax Analysis Bar Chart", "", "Tax Analysis in $", taxAnalysisBarChartDataset, PlotOrientation.VERTICAL, true, true, false);

        ChartFrame receiptPieChartFrame = new ChartFrame(database.getTaxpayerNameAfmValuesPairList(taxpayerIndex), taxAnalysisJFreeChart);
        receiptPieChartFrame.pack();
        receiptPieChartFrame.setResizable(false);
        receiptPieChartFrame.setLocationRelativeTo(null);
        receiptPieChartFrame.setModalExclusionType(ModalExclusionType.APPLICATION_EXCLUDE);
        receiptPieChartFrame.setVisible(true);
    }
}