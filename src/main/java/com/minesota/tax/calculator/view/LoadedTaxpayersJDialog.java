package com.minesota.tax.calculator.view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import com.minesota.tax.calculator.manager.OutputSystem;
import com.minesota.tax.calculator.model.Database;

public class LoadedTaxpayersJDialog extends JDialog {

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
        setTitle("Φορολογούμενοι");

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
        showSelectedTaxpayerInfoButton.setHorizontalAlignment(SwingConstants.LEFT);
        String buttonText = "<html>"
                + "Εμφάνιση στοιχείων"
                + "<br>"
                + "επιλεγμένου φορολογούμενου"
                + "</html>";
        showSelectedTaxpayerInfoButton.setText(buttonText);
        showSelectedTaxpayerInfoButton.setFont(new Font("Tahoma", Font.BOLD, 13));
        showSelectedTaxpayerInfoButton.setBounds(300, 12, 240, 71);
        getContentPane().add(showSelectedTaxpayerInfoButton);

        JButton deleteSelectedTaxpayerFromDatabaseButton = new JButton();
        deleteSelectedTaxpayerFromDatabaseButton.setHorizontalAlignment(SwingConstants.LEFT);
        buttonText = "<html>"
                + "Διαγραφή επιλεγμένου"
                + "<br>"
                + "φορολογούμενου"
                + "</html>";
        deleteSelectedTaxpayerFromDatabaseButton.setText(buttonText);
        deleteSelectedTaxpayerFromDatabaseButton.setFont(new Font("Tahoma", Font.BOLD, 13));
        deleteSelectedTaxpayerFromDatabaseButton.setBounds(300, 93, 240, 71);
        getContentPane().add(deleteSelectedTaxpayerFromDatabaseButton);

        JButton showSelectedTaxpayerReceiptsButton = new JButton();
        buttonText = "<html>"
                + "Εμφάνιση αποδείξεων"
                + "<br>"
                + "επιλεγμένου φορολογούμενου"
                + "</html>";
        showSelectedTaxpayerReceiptsButton.setText(buttonText);
        showSelectedTaxpayerReceiptsButton.setFont(new Font("Tahoma", Font.BOLD, 13));
        showSelectedTaxpayerReceiptsButton.setBounds(300, 175, 240, 71);
        getContentPane().add(showSelectedTaxpayerReceiptsButton);

        JButton showSelectedTaxpayerPieChartButton = new JButton("Διάγραμμα πίτας αποδείξεων");
        showSelectedTaxpayerPieChartButton.setFont(new Font("Tahoma", Font.BOLD, 13));
        showSelectedTaxpayerPieChartButton.setBounds(300, 257, 240, 71);
        getContentPane().add(showSelectedTaxpayerPieChartButton);

        JButton showSelectedTaxpayerBarChartButton = new JButton("Ραβδόγραμμα ανάλυσης φόρου");
        showSelectedTaxpayerBarChartButton.setFont(new Font("Tahoma", Font.BOLD, 13));
        showSelectedTaxpayerBarChartButton.setBounds(300, 340, 240, 71);
        getContentPane().add(showSelectedTaxpayerBarChartButton);

        JButton saveSelectedTaxpayerInfoToTxtButton = new JButton("Αποθήκευση στοιχείων φορολογούμενου σε txt");
        saveSelectedTaxpayerInfoToTxtButton.setForeground(Color.RED);
        saveSelectedTaxpayerInfoToTxtButton.setFont(new Font("Tahoma", Font.BOLD, 13));
        saveSelectedTaxpayerInfoToTxtButton.setBounds(10, 422, 530, 29);
        getContentPane().add(saveSelectedTaxpayerInfoToTxtButton);

        JButton saveSelectedTaxpayerInfoToXmlButton = new JButton("Αποθήκευση στοιχείων φορολογούμενου σε xml");
        saveSelectedTaxpayerInfoToXmlButton.setForeground(Color.RED);
        saveSelectedTaxpayerInfoToXmlButton.setFont(new Font("Tahoma", Font.BOLD, 13));
        saveSelectedTaxpayerInfoToXmlButton.setBounds(10, 455, 530, 29);
        getContentPane().add(saveSelectedTaxpayerInfoToXmlButton);
        DefaultListCellRenderer renderer = (DefaultListCellRenderer) loadedTaxpayersJList.getCellRenderer();
        renderer.setHorizontalAlignment(SwingConstants.CENTER);

        showSelectedTaxpayerInfoButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                Database database = Database.getInstance();

                if (loadedTaxpayersJList.getSelectedIndex() != -1) {
                    JOptionPane.showMessageDialog(null, database.getTaxpayerFromArrayList(loadedTaxpayersJList.getSelectedIndex()).toString(), loadedTaxpayersJList.getSelectedValue().toString(), JOptionPane.PLAIN_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "Δεν έχεις επιλέξει κάποιον φορολογούμενο απο την λίστα.", "Σφάλμα", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        deleteSelectedTaxpayerFromDatabaseButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                Database database = Database.getInstance();

                if (loadedTaxpayersJList.getSelectedIndex() != -1) {
                    int dialogResult = JOptionPane.showConfirmDialog(null, "Διαγραφή επιλεγμένου φορολογούμενου(" + loadedTaxpayersJList.getSelectedValue().toString() + ") απο την βάση δεδομένων?", "Επιβεβαίωση διαγραφής", JOptionPane.YES_NO_OPTION);
                    if (dialogResult == JOptionPane.YES_OPTION) {
                        database.removeTaxpayerFromArrayList(loadedTaxpayersJList.getSelectedIndex());

                        fillLoadedTaxpayersJList();

                        JLabel totalLoadedTaxpayersJLabel = (JLabel) appMainWindow.getContentPane().getComponent(1);
                        totalLoadedTaxpayersJLabel.setText(Integer.toString(database.getTaxpayersArrayListSize()));

                        if (database.getTaxpayersArrayListSize() == 0) dispose();
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Δεν έχεις επιλέξει κάποιον φορολογούμενο απο την λίστα.", "Σφάλμα", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        showSelectedTaxpayerReceiptsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                if (loadedTaxpayersJList.getSelectedIndex() != -1) {
                    TaxpayerReceiptsManagementJDialog taxpayerReceiptsManagementJDialog = new TaxpayerReceiptsManagementJDialog(loadedTaxpayersJList.getSelectedValue().toString(), loadedTaxpayersJList.getSelectedIndex());
                    taxpayerReceiptsManagementJDialog.fillTaxpayerReceiptsJList();
                    taxpayerReceiptsManagementJDialog.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(null, "Δεν έχεις επιλέξει κάποιον φορολογούμενο απο την λίστα.", "Σφάλμα", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        showSelectedTaxpayerPieChartButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                int taxpayerIndex = loadedTaxpayersJList.getSelectedIndex();
                OutputSystem outputSystem = OutputSystem.getInstance();

                if (taxpayerIndex != -1) {
                    outputSystem.createTaxpayerReceiptsPieJFreeChart(taxpayerIndex);
                } else {
                    JOptionPane.showMessageDialog(null, "Δεν έχεις επιλέξει κάποιον φορολογούμενο απο την λίστα.", "Σφάλμα", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        showSelectedTaxpayerBarChartButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                int taxpayerIndex = loadedTaxpayersJList.getSelectedIndex();
                OutputSystem outputSystem = OutputSystem.getInstance();

                if (taxpayerIndex != -1) {
                    outputSystem.createTaxpayerTaxAnalysisBarJFreeChart(taxpayerIndex);
                } else {
                    JOptionPane.showMessageDialog(null, "Δεν έχεις επιλέξει κάποιον φορολογούμενο απο την λίστα.", "Σφάλμα", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        saveSelectedTaxpayerInfoToTxtButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int taxpayerIndex = loadedTaxpayersJList.getSelectedIndex();
                Database database = Database.getInstance();
                OutputSystem outputSystem = OutputSystem.getInstance();

                if (taxpayerIndex != -1) {
                    JFileChooser saveFileFolderChooser = new JFileChooser();
                    saveFileFolderChooser.setCurrentDirectory(new java.io.File("."));
                    saveFileFolderChooser.setDialogTitle("Επιλέξτε φάκελο αποθήκευσης " + database.getTaxpayerFromArrayList(taxpayerIndex).getAFM() + "_LOG.txt");
                    saveFileFolderChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

                    if (saveFileFolderChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                        String savePath = saveFileFolderChooser.getSelectedFile().toString();

                        outputSystem.saveTaxpayerInfoToTxtLogFile(savePath, taxpayerIndex);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Δεν έχεις επιλέξει κάποιον φορολογούμενο απο την λίστα.", "Σφάλμα", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        saveSelectedTaxpayerInfoToXmlButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int taxpayerIndex = loadedTaxpayersJList.getSelectedIndex();
                Database database = Database.getInstance();
                OutputSystem outputSystem = OutputSystem.getInstance();

                if (taxpayerIndex != -1) {
                    JFileChooser saveFileFolderChooser = new JFileChooser();
                    saveFileFolderChooser.setCurrentDirectory(new java.io.File("."));
                    saveFileFolderChooser.setDialogTitle("Επιλέξτε φάκελο αποθήκευσης " + database.getTaxpayerFromArrayList(taxpayerIndex).getAFM() + "_LOG.xml");
                    saveFileFolderChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

                    if (saveFileFolderChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                        String savePath = saveFileFolderChooser.getSelectedFile().toString();

                        outputSystem.saveTaxpayerInfoToXmlLogFile(savePath, taxpayerIndex);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Δεν έχεις επιλέξει κάποιον φορολογούμενο απο την λίστα.", "Σφάλμα", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
    }

    public void fillLoadedTaxpayersJList() {
        Database database = Database.getInstance();
        String[] jlistValues = database.getTaxpayersNameAfmValuesPairList();

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
