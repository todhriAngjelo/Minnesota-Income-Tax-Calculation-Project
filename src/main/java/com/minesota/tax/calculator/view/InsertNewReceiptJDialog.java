package com.minesota.tax.calculator.view;

import com.minesota.tax.calculator.manager.FilesManager;
import com.minesota.tax.calculator.model.Receipt;
import com.minesota.tax.calculator.util.TaxPayerUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class InsertNewReceiptJDialog extends JDialog {

    public InsertNewReceiptJDialog(int taxpayerID) {

        setResizable(false);
        setBounds(100, 100, 310, 530);
        getContentPane().setLayout(null);
        setLocationRelativeTo(null);
        setModalityType(ModalityType.APPLICATION_MODAL);
        setType(Type.POPUP);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setTitle("Insert a new receipt");

        JTextField receiptIdTextField = new JTextField();
        receiptIdTextField.setFont(new Font("Tahoma", Font.BOLD, 12));
        receiptIdTextField.setHorizontalAlignment(SwingConstants.CENTER);
        receiptIdTextField.setBounds(119, 11, 165, 25);
        getContentPane().add(receiptIdTextField);
        receiptIdTextField.setColumns(10);

        JTextField dateTextField = new JTextField();
        dateTextField.setFont(new Font("Tahoma", Font.BOLD, 12));
        dateTextField.setHorizontalAlignment(SwingConstants.CENTER);
        dateTextField.setBounds(119, 63, 165, 25);
        getContentPane().add(dateTextField);
        dateTextField.setColumns(10);

        JTextField amountTextField = new JTextField();
        amountTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent keyEvent) {
                char charTyped = keyEvent.getKeyChar();
                if (!Character.isDigit(charTyped) && charTyped != '.') {
                    keyEvent.consume();
                }
                if (charTyped == '.' && amountTextField.getText().contains(".")) {
                    keyEvent.consume();
                }
            }
        });
        amountTextField.setFont(new Font("Tahoma", Font.BOLD, 12));
        amountTextField.setHorizontalAlignment(SwingConstants.CENTER);
        amountTextField.setBounds(119, 167, 165, 25);
        getContentPane().add(amountTextField);
        amountTextField.setColumns(10);

        JTextField companyTextField = new JTextField();
        companyTextField.setFont(new Font("Tahoma", Font.BOLD, 12));
        companyTextField.setHorizontalAlignment(SwingConstants.CENTER);
        companyTextField.setBounds(119, 219, 165, 25);
        getContentPane().add(companyTextField);
        companyTextField.setColumns(10);

        JTextField countryTextField = new JTextField();
        countryTextField.setFont(new Font("Tahoma", Font.BOLD, 12));
        countryTextField.setHorizontalAlignment(SwingConstants.CENTER);
        countryTextField.setBounds(119, 271, 165, 25);
        getContentPane().add(countryTextField);
        countryTextField.setColumns(10);

        JTextField cityTextField = new JTextField();
        cityTextField.setFont(new Font("Tahoma", Font.BOLD, 12));
        cityTextField.setHorizontalAlignment(SwingConstants.CENTER);
        cityTextField.setBounds(119, 323, 165, 25);
        getContentPane().add(cityTextField);
        cityTextField.setColumns(10);

        JTextField streetTextField = new JTextField();
        streetTextField.setFont(new Font("Tahoma", Font.BOLD, 12));
        streetTextField.setHorizontalAlignment(SwingConstants.CENTER);
        streetTextField.setBounds(119, 375, 165, 25);
        getContentPane().add(streetTextField);
        streetTextField.setColumns(10);

        JTextField numberTextField = new JTextField();
        numberTextField.setFont(new Font("Tahoma", Font.BOLD, 12));
        numberTextField.setHorizontalAlignment(SwingConstants.CENTER);
        numberTextField.setBounds(119, 427, 165, 25);
        getContentPane().add(numberTextField);
        numberTextField.setColumns(10);

        JPanel okCancelButtonsPanel = new JPanel();
        okCancelButtonsPanel.setBounds(0, 465, 304, 33);
        getContentPane().add(okCancelButtonsPanel);
        okCancelButtonsPanel.setLayout(null);

        JButton okButton = new JButton("OK");
        okButton.setBounds(10, 5, 104, 23);
        okButton.setFont(new Font("Tahoma", Font.BOLD, 11));
        okButton.setActionCommand("OK");
        okCancelButtonsPanel.add(okButton);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.setBounds(192, 5, 104, 23);
        cancelButton.setFont(new Font("Tahoma", Font.BOLD, 11));
        cancelButton.setActionCommand("Cancel");
        okCancelButtonsPanel.add(cancelButton);

        JLabel receiptIdLabel = new JLabel("Receipt ID:");
        receiptIdLabel.setForeground(Color.BLUE);
        receiptIdLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
        receiptIdLabel.setBounds(20, 11, 99, 25);
        getContentPane().add(receiptIdLabel);

        JLabel dateLabel = new JLabel("Date:");
        dateLabel.setForeground(Color.BLUE);
        dateLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
        dateLabel.setBounds(20, 61, 99, 25);
        getContentPane().add(dateLabel);

        JLabel kindLabel = new JLabel("Kind:");
        kindLabel.setForeground(Color.BLUE);
        kindLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
        kindLabel.setBounds(20, 113, 99, 25);
        getContentPane().add(kindLabel);

        JLabel amountLabel = new JLabel("Amount:");
        amountLabel.setForeground(Color.BLUE);
        amountLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
        amountLabel.setBounds(20, 165, 99, 25);
        getContentPane().add(amountLabel);

        JLabel companyLabel = new JLabel("Company:");
        companyLabel.setForeground(Color.BLUE);
        companyLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
        companyLabel.setBounds(20, 217, 99, 25);
        getContentPane().add(companyLabel);

        JLabel countryLabel = new JLabel("Country:");
        countryLabel.setForeground(Color.BLUE);
        countryLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
        countryLabel.setBounds(20, 269, 99, 25);
        getContentPane().add(countryLabel);

        JLabel cityLabel = new JLabel("City:");
        cityLabel.setForeground(Color.BLUE);
        cityLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
        cityLabel.setBounds(20, 321, 99, 25);
        getContentPane().add(cityLabel);

        JLabel streetLabel = new JLabel("Street:");
        streetLabel.setForeground(Color.BLUE);
        streetLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
        streetLabel.setBounds(20, 375, 99, 25);
        getContentPane().add(streetLabel);

        JLabel numberLabel = new JLabel("Number:");
        numberLabel.setForeground(Color.BLUE);
        numberLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
        numberLabel.setBounds(20, 427, 99, 25);
        getContentPane().add(numberLabel);

        JComboBox kindComboBox = new JComboBox();
        kindComboBox.setFont(new Font("Tahoma", Font.BOLD, 12));
        kindComboBox.setModel(new DefaultComboBoxModel(new String[]{"Select Kind", "Entertainment", "Basic", "Travel", "Health", "Other"}));
        kindComboBox.setBounds(119, 115, 165, 25);
        getContentPane().add(kindComboBox);

        okButton.addActionListener(e -> {
            FilesManager filesManager = FilesManager.getInstance();
            if (!kindComboBox.getSelectedItem().toString().equals("Select Kind") && !receiptIdTextField.getText().equals("") && !dateTextField.getText().equals("")
                    && !amountTextField.getText().equals("") && !companyTextField.getText().equals("") && !countryTextField.getText().equals("")
                    && !cityTextField.getText().equals("") && !streetTextField.getText().equals("") && !numberTextField.getText().equals("")) {

                Receipt newReceipt = new Receipt(kindComboBox.getSelectedItem().toString(), receiptIdTextField.getText(),
                        dateTextField.getText(), amountTextField.getText(), companyTextField.getText(),
                        countryTextField.getText(), cityTextField.getText(), streetTextField.getText(), numberTextField.getText());

                filesManager.getCachedTaxPayers().get(taxpayerID).getReceipts().add(newReceipt);
                TaxPayerUtils.applyTaxPayerTaxAdjustments(filesManager.getCachedTaxPayers().get(taxpayerID));

                filesManager.updateTaxpayerReceipts(taxpayerID);

                dispose();
            } else {
                JOptionPane.showMessageDialog(null, "Empty fields detected", "Error", JOptionPane.WARNING_MESSAGE);
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }
}
