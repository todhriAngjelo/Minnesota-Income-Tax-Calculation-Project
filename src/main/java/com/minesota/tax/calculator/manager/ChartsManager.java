package com.minesota.tax.calculator.manager;

import com.minesota.tax.calculator.model.TaxPayer;
import com.minesota.tax.calculator.util.TaxPayerUtils;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.PieSectionLabelGenerator;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import java.awt.Dialog.ModalExclusionType;
import java.text.DecimalFormat;

public class ChartsManager {

    private static ChartsManager chartsManagerInstance = null;
    private final FilesManager filesManager = FilesManager.getInstance();
    private PiePlot piePlot;

    public static ChartsManager getInstance() {
        if (chartsManagerInstance == null) {
            chartsManagerInstance = new ChartsManager();
        }

        return chartsManagerInstance;
    }

    public void createTaxpayerReceiptsPieJFreeChart(int taxpayerIndex) {
        DefaultPieDataset receiptPieChartDataset = new DefaultPieDataset();
        TaxPayer taxpayer = filesManager.getCachedTaxPayers().get(taxpayerIndex);

        receiptPieChartDataset.setValue("Basic", TaxPayerUtils.getReceiptsTotalAmount("Basic", taxpayer.getReceipts()));
        receiptPieChartDataset.setValue("Entertainment", TaxPayerUtils.getReceiptsTotalAmount("Entertainment", taxpayer.getReceipts()));
        receiptPieChartDataset.setValue("Travel", TaxPayerUtils.getReceiptsTotalAmount("Travel", taxpayer.getReceipts()));
        receiptPieChartDataset.setValue("Health", TaxPayerUtils.getReceiptsTotalAmount("Health", taxpayer.getReceipts()));
        receiptPieChartDataset.setValue("Other", TaxPayerUtils.getReceiptsTotalAmount("Other", taxpayer.getReceipts()));

        JFreeChart receiptPieJFreeChart = ChartFactory.createPieChart("Receipt Pie Chart", receiptPieChartDataset);
        piePlot = (PiePlot) receiptPieJFreeChart.getPlot();
        PieSectionLabelGenerator generator = new StandardPieSectionLabelGenerator("{0}: {1}$ ({2})", new DecimalFormat("0.00"), new DecimalFormat("0.00%"));
        piePlot.setLabelGenerator(generator);

        ChartFrame receiptPieChartFrame = new ChartFrame(FilesManager.getInstance().getFormattedTaxPayersStrings()[taxpayerIndex], receiptPieJFreeChart);
        receiptPieChartFrame.pack();
        receiptPieChartFrame.setResizable(false);
        receiptPieChartFrame.setLocationRelativeTo(null);
        receiptPieChartFrame.setModalExclusionType(ModalExclusionType.APPLICATION_EXCLUDE);
        receiptPieChartFrame.setVisible(true);
    }


    public void createTaxpayerTaxAnalysisBarJFreeChart(int taxpayerIndex) {
        DefaultCategoryDataset taxAnalysisBarChartDataset = new DefaultCategoryDataset();
        TaxPayer taxpayer = filesManager.getCachedTaxPayers().get(taxpayerIndex);

        String taxVariationType = taxpayer.getTaxIncrease() != 0 ? "Tax Increase" : "Tax Decrease";
        double taxVariationAmount = taxpayer.getTaxIncrease() != 0 ? taxpayer.getTaxIncrease() : taxpayer.getTaxDecrease() * (-1);

        taxAnalysisBarChartDataset.setValue(taxpayer.getBasicTax(), "Tax", "Basic Tax");
        taxAnalysisBarChartDataset.setValue(taxVariationAmount, "Tax", taxVariationType);
        taxAnalysisBarChartDataset.setValue(taxpayer.getTotalTax(), "Tax", "Total Tax");

        JFreeChart taxAnalysisJFreeChart = ChartFactory.createBarChart("Tax Analysis Bar Chart", "", "Tax Analysis in $", taxAnalysisBarChartDataset, PlotOrientation.VERTICAL, true, true, false);

        ChartFrame receiptPieChartFrame = new ChartFrame(FilesManager.getInstance().getFormattedTaxPayersStrings()[taxpayerIndex], taxAnalysisJFreeChart);
        receiptPieChartFrame.pack();
        receiptPieChartFrame.setResizable(false);
        receiptPieChartFrame.setLocationRelativeTo(null);
        receiptPieChartFrame.setModalExclusionType(ModalExclusionType.APPLICATION_EXCLUDE);
        receiptPieChartFrame.setVisible(true);
    }

    public PiePlot getPiePlot() { // for testing
        return piePlot;
    }
}