package com.minesota.tax.calculator.view;

import com.minesota.tax.calculator.manager.FileManager;
import com.minesota.tax.calculator.model.Receipt;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TaxpayerReceiptsManagementJDialog extends JDialog {

    private final JList taxpayerReceiptsJList;
    private final int taxpayerID;

    public TaxpayerReceiptsManagementJDialog(String windowTitle, int taxpayerID) {
        this.taxpayerID = taxpayerID;

        setResizable(false);
        setBounds(100, 100, 480, 460);
        getContentPane().setLayout(null);
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(10, 38, 250, 384);
        getContentPane().add(scrollPane);
        setTitle(windowTitle);
        setLocationRelativeTo(null);
        setModalityType(ModalityType.APPLICATION_MODAL);
        setType(Type.POPUP);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        taxpayerReceiptsJList = new JList();
        scrollPane.setViewportView(taxpayerReceiptsJList);
        taxpayerReceiptsJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        taxpayerReceiptsJList.setForeground(Color.BLUE);
        taxpayerReceiptsJList.setFont(new Font("Tahoma", Font.BOLD, 13));

        JLabel label = new JLabel("Taxpayers receipts");
        label.setForeground(Color.RED);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setFont(new Font("Tahoma", Font.BOLD, 14));
        label.setBounds(10, 11, 250, 22);
        getContentPane().add(label);

        JButton insertNewReceiptButton = new JButton("Insert a new receipt");
        insertNewReceiptButton.setFont(new Font("Tahoma", Font.BOLD, 12));
        insertNewReceiptButton.setBounds(270, 114, 194, 65);
        getContentPane().add(insertNewReceiptButton);

        JButton deleteSelectedReceiptButton = new JButton();
        deleteSelectedReceiptButton.setHorizontalAlignment(SwingConstants.LEFT);
        String buttonText = "<html>"
                + "Delete selected"
                + "<br>"
                + "receipt"
                + "</html>";
        deleteSelectedReceiptButton.setText(buttonText);
        deleteSelectedReceiptButton.setFont(new Font("Tahoma", Font.BOLD, 12));
        deleteSelectedReceiptButton.setBounds(270, 190, 194, 65);
        getContentPane().add(deleteSelectedReceiptButton);

        JButton showSelectedReceiptDetailsButton = new JButton();
        buttonText = "<html>"
                + "Display selected"
                + "<br>"
                + "receipts info"
                + "</html>";
        showSelectedReceiptDetailsButton.setText(buttonText);
        showSelectedReceiptDetailsButton.setFont(new Font("Tahoma", Font.BOLD, 12));
        showSelectedReceiptDetailsButton.setBounds(270, 38, 194, 65);
        getContentPane().add(showSelectedReceiptDetailsButton);

        showSelectedReceiptDetailsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                FileManager fileManager = FileManager.getInstance();

                if (taxpayerReceiptsJList.getSelectedIndex() != -1) {
                    JOptionPane.showMessageDialog(
                            null,
                            fileManager.getCachedTaxPayers().get(taxpayerID).getReceipts().get(taxpayerReceiptsJList.getSelectedIndex()).toString(),
                            taxpayerReceiptsJList.getSelectedValue().toString(),
                            JOptionPane.PLAIN_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "No receipts selected", "Error", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        insertNewReceiptButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                InsertNewReceiptJDialog insertNewReceiptJDialog = new InsertNewReceiptJDialog(taxpayerID);
                insertNewReceiptJDialog.setVisible(true);

                dispose();
            }
        });

        deleteSelectedReceiptButton.addActionListener(e -> {
            FileManager fileManager = FileManager.getInstance();

            if (taxpayerReceiptsJList.getSelectedIndex() != -1) {
                int dialogResult = JOptionPane.showConfirmDialog(null, "Delete selected receipt(" + taxpayerReceiptsJList.getSelectedValue().toString() + ") ?", "Are you sure?", JOptionPane.YES_NO_OPTION);
                if (dialogResult == JOptionPane.YES_OPTION) {
                    fileManager.getCachedTaxPayers().get(taxpayerID).getReceipts().remove(taxpayerReceiptsJList.getSelectedIndex()); // todo check if i can remove - remove by index

                    fileManager.updateTaxpayerInputFile(taxpayerID);

                    fillTaxpayerReceiptsJList();
                }
            } else {
                JOptionPane.showMessageDialog(null, "No receipts selected", "Error", JOptionPane.WARNING_MESSAGE);
            }
        });
    }

    public void fillTaxpayerReceiptsJList() {
        FileManager fileManager = FileManager.getInstance();

        String[] receiptsList = new String[fileManager.getCachedTaxPayers().get(taxpayerID).getReceipts().size()];

        int c = 0;
        for (Receipt receipt : fileManager.getCachedTaxPayers().get(taxpayerID).getReceipts()) {
            receiptsList[c++] = receipt.getId() + " | " + receipt.getDate() + " | " + receipt.getAmount();
        }


        taxpayerReceiptsJList.setModel(new AbstractListModel() {
            final String[] values = receiptsList;

            public int getSize() {
                return values.length;
            }

            public Object getElementAt(int index) {
                return values[index];
            }
        });
    }
}