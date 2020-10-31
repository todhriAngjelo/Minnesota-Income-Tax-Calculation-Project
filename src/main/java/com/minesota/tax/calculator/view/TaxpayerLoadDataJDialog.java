package com.minesota.tax.calculator.view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FilenameFilter;
import java.util.List;

import javax.swing.*;

import com.minesota.tax.calculator.model.Database;

public class TaxpayerLoadDataJDialog extends JDialog {

	private final JList<String> taxpayersAfmInfoFilesJList;

	private String afmInfoFilesFolderPath;

	public TaxpayerLoadDataJDialog(JFrame appMainWindow) {

		setResizable(false);
		setModalityType(ModalityType.APPLICATION_MODAL);
		setType(Type.POPUP);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 486, 332);
		getContentPane().setLayout(null);
		setLocationRelativeTo(null);
		setTitle("Αρχεία φόρτωσης δεδομένων");

		JScrollPane scrollPaneForList = new JScrollPane();
		scrollPaneForList.setBounds(10, 11, 250, 258);
		getContentPane().add(scrollPaneForList);

		taxpayersAfmInfoFilesJList = new JList<>();
		taxpayersAfmInfoFilesJList.setForeground(Color.BLUE);
		taxpayersAfmInfoFilesJList.setFont(new Font("Tahoma", Font.BOLD, 11));
		scrollPaneForList.setViewportView(taxpayersAfmInfoFilesJList);
		taxpayersAfmInfoFilesJList.setVisibleRowCount(100);

		JButton loadDataFromSelectedAfmInfoFilesButton = new JButton();
		loadDataFromSelectedAfmInfoFilesButton.setBounds(270, 11, 198, 68);
		String text = "<html>"
				+ "Φόρτωση δεδομένων"
				+ "<br>"
				+ "επιλεγμένων αρχείων"
				+ "</html>";
		loadDataFromSelectedAfmInfoFilesButton.setText(text);
		loadDataFromSelectedAfmInfoFilesButton.setFont(new Font("Tahoma", Font.BOLD, 13));
		getContentPane().add(loadDataFromSelectedAfmInfoFilesButton);

		JButton selectAllButton = new JButton("Επιλογή όλων");
		selectAllButton.setBounds(10, 274, 250, 23);
		selectAllButton.setFont(new Font("Tahoma", Font.BOLD, 11));
		getContentPane().add(selectAllButton);

		selectAllButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				taxpayersAfmInfoFilesJList.setSelectionInterval(0, taxpayersAfmInfoFilesJList.getModel().getSize() - 1);
			}
		});

		loadDataFromSelectedAfmInfoFilesButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				List<String> afmInfoFilesListToLoad = taxpayersAfmInfoFilesJList.getSelectedValuesList();

				if (afmInfoFilesListToLoad.size() > 0) {
					String confirmDialogText = "Φόρτωση δεδομένων φορολογούμενων απο τα ακόλουθα αρχεία:\n";
					for (String afmInfoFileName : afmInfoFilesListToLoad) {
						confirmDialogText += afmInfoFileName + "\n";
					}
					confirmDialogText += "Είστε σίγουρος?";

					int dialogResult = JOptionPane.showConfirmDialog(null, confirmDialogText, "Επιβεβαίωση", JOptionPane.YES_NO_OPTION);
					if (dialogResult == JOptionPane.YES_OPTION) {
						Database.proccessTaxpayersDataFromFilesIntoDatabase(afmInfoFilesFolderPath, afmInfoFilesListToLoad);
						JLabel totalLoadedTaxpayersJLabel = (JLabel) appMainWindow.getContentPane().getComponent(1);
						totalLoadedTaxpayersJLabel.setText(Integer.toString(Database.getTaxpayersArrayListSize()));

						dispose();
					}
				} else {
					JOptionPane.showMessageDialog(null, "Δεν έχεις επιλέξει αρχείο(α) απο την λίστα.", "Σφάλμα", JOptionPane.WARNING_MESSAGE);
				}
			}
		});
	}

	public void fillTaxpayersAfmInfoFilesJList(String afmInfoFilesFolderPath) {
		this.afmInfoFilesFolderPath = afmInfoFilesFolderPath;

		File folder = new File(afmInfoFilesFolderPath);
		File[] folderFiles = folder.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return (name.toLowerCase().endsWith("_info.txt") || name.toLowerCase().endsWith("_info.xml"));
			}
		});

		String[] jlistValues = new String[folderFiles.length];
		int jlistValuesItems = 0;
		for (File file : folderFiles) {
			if (file.isFile()) {
				jlistValues[jlistValuesItems++] = file.getName();
			}
		}

		taxpayersAfmInfoFilesJList.setModel(new AbstractListModel() {
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