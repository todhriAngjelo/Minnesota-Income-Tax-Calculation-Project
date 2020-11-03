package com.minesota.tax.calculator.view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import com.minesota.tax.calculator.model.Database;

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

		JLabel label = new JLabel("Αποδείξεις Φορολογούμενου");
		label.setForeground(Color.RED);
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setFont(new Font("Tahoma", Font.BOLD, 14));
		label.setBounds(10, 11, 250, 22);
		getContentPane().add(label);

		JButton insertNewReceiptButton = new JButton("Εισαγωγή νέας απόδειξης");
		insertNewReceiptButton.setFont(new Font("Tahoma", Font.BOLD, 12));
		insertNewReceiptButton.setBounds(270, 114, 194, 65);
		getContentPane().add(insertNewReceiptButton);

		JButton deleteSelectedReceiptButton = new JButton();
		deleteSelectedReceiptButton.setHorizontalAlignment(SwingConstants.LEFT);
		String buttonText = "<html>"
				+ "Διαγραφή επιλεγμένης"
				+ "<br>"
				+ "απόδειξης"
				+ "</html>";
		deleteSelectedReceiptButton.setText(buttonText);
		deleteSelectedReceiptButton.setFont(new Font("Tahoma", Font.BOLD, 12));
		deleteSelectedReceiptButton.setBounds(270, 190, 194, 65);
		getContentPane().add(deleteSelectedReceiptButton);

		JButton showSelectedReceiptDetailsButton = new JButton();
		buttonText = "<html>"
				+ "Εμφάνιση πληροφοριών"
				+ "<br>"
				+ "επιλεγμένης απόδειξης"
				+ "</html>";
		showSelectedReceiptDetailsButton.setText(buttonText);
		showSelectedReceiptDetailsButton.setFont(new Font("Tahoma", Font.BOLD, 12));
		showSelectedReceiptDetailsButton.setBounds(270, 38, 194, 65);
		getContentPane().add(showSelectedReceiptDetailsButton);

		showSelectedReceiptDetailsButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Database database = Database.getInstance();

				if (taxpayerReceiptsJList.getSelectedIndex() != -1) {
					JOptionPane.showMessageDialog(null, database.getTaxpayerFromArrayList(taxpayerID).getReceipt(taxpayerReceiptsJList.getSelectedIndex()).toString(), taxpayerReceiptsJList.getSelectedValue().toString(), JOptionPane.PLAIN_MESSAGE);
				} else {
					JOptionPane.showMessageDialog(null, "Δεν έχεις επιλέξει κάποια απόδειξη απο την λίστα.", "Σφάλμα", JOptionPane.WARNING_MESSAGE);
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

		deleteSelectedReceiptButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Database database = Database.getInstance();

				if (taxpayerReceiptsJList.getSelectedIndex() != -1) {
					int dialogResult = JOptionPane.showConfirmDialog(null, "Διαγραφή επιλεγμένης απόδειξης(" + taxpayerReceiptsJList.getSelectedValue().toString() + ") ?", "Επιβεβαίωση διαγραφής", JOptionPane.YES_NO_OPTION);
					if (dialogResult == JOptionPane.YES_OPTION) {
						database.getTaxpayerFromArrayList(taxpayerID).removeReceiptFromList(taxpayerReceiptsJList.getSelectedIndex());

						database.updateTaxpayerInputFile(taxpayerID);

						fillTaxpayerReceiptsJList();
					}
				} else {
					JOptionPane.showMessageDialog(null, "Δεν έχεις επιλέξει κάποια απόδειξη απο την λίστα.", "Σφάλμα", JOptionPane.WARNING_MESSAGE);
				}
			}
		});
	}

	public void fillTaxpayerReceiptsJList() {
		Database database = Database.getInstance();
		String[] jlistValues = database.getTaxpayerFromArrayList(taxpayerID).getReceiptsList();

		taxpayerReceiptsJList.setModel(new AbstractListModel() {
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