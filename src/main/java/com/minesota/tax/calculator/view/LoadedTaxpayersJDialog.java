package com.minesota.tax.calculator.view;

import com.minesota.tax.calculator.manager.ChartsManager;
import com.minesota.tax.calculator.manager.FilesManager;

import javax.swing.*;
import java.awt.*;

public class LoadedTaxpayersJDialog extends JDialog {

    private static final String NO_TAXPAYER_SELECTED_DIALOG_MESSAGE = "No taxpayer selected";
    private static final String ERROR_TITLE = "Error";
    private final FilesManager filesManager = FilesManager.getInstance();
    private final ChartsManager chartsManager = ChartsManager.getInstance();
    private final JList loadedTaxpayersJList;
    private final JFrame appMainWindow;

    public LoadedTaxpayersJDialog(JFrame appMainWindow) {
        this.appMainWindow = appMainWindow;

        setResizable(false);
        setBounds(100, 100, 556, 525);
        getContentPane().setLayout(null);
        setLocationRelativeTo(null);
        setModalityType(ModalityType.APPLICATION_MODAL);
        setType(Type.POPUP);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setTitle("Taxpayers list");

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(10, 11, 280, 400);
        getContentPane().add(scrollPane);

        loadedTaxpayersJList = new JList();
        loadedTaxpayersJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        scrollPane.setViewportView(loadedTaxpayersJList);
        loadedTaxpayersJList.setForeground(Color.BLUE);
        loadedTaxpayersJList.setFont(new Font("Tahoma", Font.BOLD, 14));
        loadedTaxpayersJList.setVisibleRowCount(100);

        JButton showSelectedTaxpayerInfoButton = new JButton();
        String buttonText = "<html>"
                + "Display selected"
                + "<br>"
                + "taxpayers info"
                + "</html>";
        showSelectedTaxpayerInfoButton.setText(buttonText);
        showSelectedTaxpayerInfoButton.setFont(new Font("Tahoma", Font.BOLD, 13));
        showSelectedTaxpayerInfoButton.setBounds(300, 12, 240, 71);
        getContentPane().add(showSelectedTaxpayerInfoButton);

        JButton deleteSelectedTaxpayerFromDatabaseButton = new JButton();
        buttonText = "<html>"
                + "Delete selected"
                + "<br>"
                + "taxpayer"
                + "</html>";
        deleteSelectedTaxpayerFromDatabaseButton.setText(buttonText);
        deleteSelectedTaxpayerFromDatabaseButton.setFont(new Font("Tahoma", Font.BOLD, 13));
        deleteSelectedTaxpayerFromDatabaseButton.setBounds(300, 93, 240, 71);
        getContentPane().add(deleteSelectedTaxpayerFromDatabaseButton);

        JButton showSelectedTaxpayerReceiptsButton = new JButton();
        buttonText = "<html>"
                + "Display selected"
                + "<br>"
                + "taxpayer receipts"
                + "</html>";
        showSelectedTaxpayerReceiptsButton.setText(buttonText);
        showSelectedTaxpayerReceiptsButton.setFont(new Font("Tahoma", Font.BOLD, 13));
        showSelectedTaxpayerReceiptsButton.setBounds(300, 175, 240, 71);
        getContentPane().add(showSelectedTaxpayerReceiptsButton);

        JButton showSelectedTaxpayerPieChartButton = new JButton("Receipts pie chart");
        showSelectedTaxpayerPieChartButton.setFont(new Font("Tahoma", Font.BOLD, 13));
        showSelectedTaxpayerPieChartButton.setBounds(300, 257, 240, 71);
        getContentPane().add(showSelectedTaxpayerPieChartButton);

        JButton showSelectedTaxpayerBarChartButton = new JButton("Tax analysis bar chart");
        showSelectedTaxpayerBarChartButton.setFont(new Font("Tahoma", Font.BOLD, 13));
        showSelectedTaxpayerBarChartButton.setBounds(300, 340, 240, 71);
        getContentPane().add(showSelectedTaxpayerBarChartButton);

        JButton saveSelectedTaxpayerInfoToTxtButton = new JButton("Save taxpayers info as .txt file");
        saveSelectedTaxpayerInfoToTxtButton.setForeground(Color.RED);
        saveSelectedTaxpayerInfoToTxtButton.setFont(new Font("Tahoma", Font.BOLD, 13));
        saveSelectedTaxpayerInfoToTxtButton.setBounds(10, 422, 530, 29);
        getContentPane().add(saveSelectedTaxpayerInfoToTxtButton);

        JButton saveSelectedTaxpayerInfoToXmlButton = new JButton("Save taxpayers info as .xml file");
        saveSelectedTaxpayerInfoToXmlButton.setForeground(Color.RED);
        saveSelectedTaxpayerInfoToXmlButton.setFont(new Font("Tahoma", Font.BOLD, 13));
        saveSelectedTaxpayerInfoToXmlButton.setBounds(10, 455, 530, 29);
        getContentPane().add(saveSelectedTaxpayerInfoToXmlButton);
        DefaultListCellRenderer renderer = (DefaultListCellRenderer) loadedTaxpayersJList.getCellRenderer();
        renderer.setHorizontalAlignment(SwingConstants.CENTER);

        // show selected taxpayer info
        showSelectedTaxpayerInfoButton.addActionListener(arg0 -> {

            if (loadedTaxpayersJList.getSelectedIndex() != -1) {
                JOptionPane.showMessageDialog(
                        null,
                        filesManager.getCachedTaxPayers().get(loadedTaxpayersJList.getSelectedIndex()).toString(),
                        loadedTaxpayersJList.getSelectedValue().toString(),
                        JOptionPane.PLAIN_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(
                        null,
                        NO_TAXPAYER_SELECTED_DIALOG_MESSAGE,
                        ERROR_TITLE,
                        JOptionPane.WARNING_MESSAGE);
            }
        });

        // delete selected taxpayer
        deleteSelectedTaxpayerFromDatabaseButton.addActionListener(arg0 -> {

            if (loadedTaxpayersJList.getSelectedIndex() != -1) {
                int dialogResult = JOptionPane.showConfirmDialog(null, "Delete selected taxpayer(" + loadedTaxpayersJList.getSelectedValue().toString() + ") from database?", "Are you sure?", JOptionPane.YES_NO_OPTION);
                if (dialogResult == JOptionPane.YES_OPTION) {
                    filesManager.getCachedTaxPayers().remove(loadedTaxpayersJList.getSelectedIndex());

                    fillLoadedTaxpayersJList();

                    JLabel totalLoadedTaxpayersJLabel = (JLabel) appMainWindow.getContentPane().getComponent(1);
                    totalLoadedTaxpayersJLabel.setText(Integer.toString(filesManager.getCachedTaxPayers().size()));

                    if (filesManager.getCachedTaxPayers().isEmpty()) dispose();
                }
            } else {
                JOptionPane.showMessageDialog(null, NO_TAXPAYER_SELECTED_DIALOG_MESSAGE, ERROR_TITLE, JOptionPane.WARNING_MESSAGE);
            }
        });

        // show taxpayer receipts
        showSelectedTaxpayerReceiptsButton.addActionListener(arg0 -> {
            if (loadedTaxpayersJList.getSelectedIndex() != -1) {
                TaxpayerReceiptsManagementJDialog taxpayerReceiptsManagementJDialog = new TaxpayerReceiptsManagementJDialog(loadedTaxpayersJList.getSelectedValue().toString(), loadedTaxpayersJList.getSelectedIndex());
                taxpayerReceiptsManagementJDialog.fillTaxpayerReceiptsJList();
                taxpayerReceiptsManagementJDialog.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(null, NO_TAXPAYER_SELECTED_DIALOG_MESSAGE, ERROR_TITLE, JOptionPane.WARNING_MESSAGE);
            }
        });

        // show taxpayer pie charts
        showSelectedTaxpayerPieChartButton.addActionListener(arg0 -> {
            int taxpayerIndex = loadedTaxpayersJList.getSelectedIndex();

            if (taxpayerIndex != -1) {
                chartsManager.createTaxpayerReceiptsPieJFreeChart(taxpayerIndex);
            } else {
                JOptionPane.showMessageDialog(null, NO_TAXPAYER_SELECTED_DIALOG_MESSAGE, ERROR_TITLE, JOptionPane.WARNING_MESSAGE);
            }
        });

        // show selected taxpayer bar charts
        showSelectedTaxpayerBarChartButton.addActionListener(arg0 -> {
            int taxpayerIndex = loadedTaxpayersJList.getSelectedIndex();

            if (taxpayerIndex != -1) {
                chartsManager.createTaxpayerTaxAnalysisBarJFreeChart(taxpayerIndex);
            } else {
                JOptionPane.showMessageDialog(null, NO_TAXPAYER_SELECTED_DIALOG_MESSAGE, ERROR_TITLE, JOptionPane.WARNING_MESSAGE);
            }
        });

        // create taxpayer txt log file
        saveSelectedTaxpayerInfoToTxtButton.addActionListener(e -> {
            int taxpayerIndex = loadedTaxpayersJList.getSelectedIndex();

            if (taxpayerIndex != -1) {
                JFileChooser saveFileFolderChooser = new JFileChooser();
                saveFileFolderChooser.setCurrentDirectory(new java.io.File("."));
                saveFileFolderChooser.setDialogTitle("Choose save file for " + filesManager.getCachedTaxPayers().get(taxpayerIndex).getVat() + "_LOG.txt");
                saveFileFolderChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

                if (saveFileFolderChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    String savePath = saveFileFolderChooser.getSelectedFile().toString();

                    filesManager.createTxtTaxpayerLogFile(savePath, taxpayerIndex);
                }
            } else {
                JOptionPane.showMessageDialog(null, NO_TAXPAYER_SELECTED_DIALOG_MESSAGE, ERROR_TITLE, JOptionPane.WARNING_MESSAGE);
            }
        });

        // create taxpayer xml log file
        saveSelectedTaxpayerInfoToXmlButton.addActionListener(e -> {
            int taxpayerIndex = loadedTaxpayersJList.getSelectedIndex();

            if (taxpayerIndex != -1) {
                JFileChooser saveFileFolderChooser = new JFileChooser();
                saveFileFolderChooser.setCurrentDirectory(new java.io.File("."));
                saveFileFolderChooser.setDialogTitle("Choose save file for " + filesManager.getCachedTaxPayers().get(taxpayerIndex).getVat() + "_LOG.xml");
                saveFileFolderChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

                if (saveFileFolderChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    String savePath = saveFileFolderChooser.getSelectedFile().toString();

                    filesManager.createXmlTaxpayerLogFile(savePath, taxpayerIndex);
                }
            } else {
                JOptionPane.showMessageDialog(null, NO_TAXPAYER_SELECTED_DIALOG_MESSAGE, ERROR_TITLE, JOptionPane.WARNING_MESSAGE);
            }
        });
    }

    public void fillLoadedTaxpayersJList() {
        String[] jlistValues = FilesManager.getFormattedTaxPayersStrings();

        loadedTaxpayersJList.setModel(new AbstractListModel() {
            final String[] values = jlistValues;
            public int getSize() {
                return values.length;
            }
            public Object getElementAt(int index) {
                return values[index];
            }
        });
    }
}
