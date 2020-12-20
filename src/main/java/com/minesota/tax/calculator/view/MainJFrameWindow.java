package com.minesota.tax.calculator.view;

import com.minesota.tax.calculator.manager.FilesManager;

import javax.swing.*;
import java.awt.*;

import static com.minesota.tax.calculator.util.ApplicationConstants.TAHOMA;

public class MainJFrameWindow {

    private final FilesManager filesManager = FilesManager.getInstance();

    private JFrame taxationMainWindowJFrame;

    public MainJFrameWindow() {
        initialize();
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                MainJFrameWindow window = new MainJFrameWindow();
                window.taxationMainWindowJFrame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void initialize() {
        taxationMainWindowJFrame = new JFrame();
        taxationMainWindowJFrame.setResizable(false);
        taxationMainWindowJFrame.setTitle("Tax Manager");
        taxationMainWindowJFrame.setBounds(-1, -1, 357, 228);
        taxationMainWindowJFrame.setLocationRelativeTo(null);
        taxationMainWindowJFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        taxationMainWindowJFrame.getContentPane().setLayout(null);

        JLabel label = new JLabel("Total number of taxpayers:");
        label.setForeground(Color.BLUE);
        label.setFont(new Font(TAHOMA, Font.BOLD | Font.ITALIC, 13));
        label.setBounds(30, 11, 218, 33);
        taxationMainWindowJFrame.getContentPane().add(label);

        JLabel totalLoadedTaxpayersJLabel = new JLabel("0");
        totalLoadedTaxpayersJLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        totalLoadedTaxpayersJLabel.setForeground(Color.RED);
        totalLoadedTaxpayersJLabel.setFont(new Font(TAHOMA, Font.BOLD, 14));
        totalLoadedTaxpayersJLabel.setBounds(247, 20, 75, 14);
        taxationMainWindowJFrame.getContentPane().add(totalLoadedTaxpayersJLabel);

        JSeparator separator = new JSeparator();
        separator.setBounds(29, 42, 293, 2);
        taxationMainWindowJFrame.getContentPane().add(separator);

        JButton openTaxpayerLoadDataJDialog = new JButton("Load taxpayers data");
        openTaxpayerLoadDataJDialog.setFont(new Font(TAHOMA, Font.BOLD, 11));
        openTaxpayerLoadDataJDialog.setBounds(27, 55, 295, 53);
        taxationMainWindowJFrame.getContentPane().add(openTaxpayerLoadDataJDialog);

        JButton showLoadedTaxpayersDataButton = new JButton("Display taxpayers list");
        showLoadedTaxpayersDataButton.setEnabled(false);
        showLoadedTaxpayersDataButton.setFont(new Font(TAHOMA, Font.BOLD, 11));
        showLoadedTaxpayersDataButton.setBounds(27, 121, 295, 53);
        taxationMainWindowJFrame.getContentPane().add(showLoadedTaxpayersDataButton);

        totalLoadedTaxpayersJLabel.addPropertyChangeListener("text", e -> showLoadedTaxpayersDataButton.setEnabled(!totalLoadedTaxpayersJLabel.getText().equals("0")));

        openTaxpayerLoadDataJDialog.addActionListener(arg0 -> {
            JFileChooser afmInfoFilesFolderChooser = new JFileChooser();
            afmInfoFilesFolderChooser.setCurrentDirectory(new java.io.File("."));
            afmInfoFilesFolderChooser.setDialogTitle("Select the folder that contains <AFM>_INFO.* files");
            afmInfoFilesFolderChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

            if (afmInfoFilesFolderChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                String taxPayersFilePath = afmInfoFilesFolderChooser.getSelectedFile().toString();
                JOptionPane.showMessageDialog(null, taxPayersFilePath, "Path of input files", JOptionPane.INFORMATION_MESSAGE);

                filesManager.setTaxPayersFilePath(taxPayersFilePath);

                TaxpayerLoadDataJDialog taxpayerLoadDataJDialog = new TaxpayerLoadDataJDialog(taxationMainWindowJFrame);
                taxpayerLoadDataJDialog.fillTaxpayersAfmInfoFilesJList(taxPayersFilePath);
                taxpayerLoadDataJDialog.setVisible(true);
            }

        });

        showLoadedTaxpayersDataButton.addActionListener(arg0 -> {
            LoadedTaxpayersJDialog loadedTaxpayersJDialog = new LoadedTaxpayersJDialog(taxationMainWindowJFrame);
            loadedTaxpayersJDialog.fillLoadedTaxpayersJList();
            loadedTaxpayersJDialog.setVisible(true);
        });
    }
}