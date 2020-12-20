package com.minesota.tax.calculator.view;

import com.minesota.tax.calculator.manager.FilesManager;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.List;

import static com.minesota.tax.calculator.util.ApplicationConstants.TAHOMA;

public class TaxpayerLoadDataJDialog extends JDialog {

	private final JList<String> taxpayersAfmInfoFilesJList;
	private String taxpayersFolderPath;

	public TaxpayerLoadDataJDialog(JFrame appMainWindow) {

		setResizable(false);
		setModalityType(ModalityType.APPLICATION_MODAL);
		setType(Type.POPUP);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 486, 332);
		getContentPane().setLayout(null);
		setLocationRelativeTo(null);
		setTitle("Data files of taxpayers");

		JScrollPane scrollPaneForList = new JScrollPane();
		scrollPaneForList.setBounds(10, 11, 250, 258);
		getContentPane().add(scrollPaneForList);

		taxpayersAfmInfoFilesJList = new JList<>();
		taxpayersAfmInfoFilesJList.setForeground(Color.BLUE);
		taxpayersAfmInfoFilesJList.setFont(new Font(TAHOMA, Font.BOLD, 11));
		scrollPaneForList.setViewportView(taxpayersAfmInfoFilesJList);
		taxpayersAfmInfoFilesJList.setVisibleRowCount(100);

		JButton loadDataFromSelectedAfmInfoFilesButton = new JButton();
		loadDataFromSelectedAfmInfoFilesButton.setBounds(270, 11, 198, 68);
		String text = "<html>"
				+ "Load selected"
				+ "<br>"
				+ "files data"
				+ "</html>";
		loadDataFromSelectedAfmInfoFilesButton.setText(text);
		loadDataFromSelectedAfmInfoFilesButton.setFont(new Font("Tahoma", Font.BOLD, 13));
		getContentPane().add(loadDataFromSelectedAfmInfoFilesButton);

		JButton selectAllButton = new JButton("Select all");
		selectAllButton.setBounds(10, 274, 250, 23);
		selectAllButton.setFont(new Font("Tahoma", Font.BOLD, 11));
		getContentPane().add(selectAllButton);

		selectAllButton.addActionListener(e -> taxpayersAfmInfoFilesJList.setSelectionInterval(0, taxpayersAfmInfoFilesJList.getModel().getSize() - 1));

		loadDataFromSelectedAfmInfoFilesButton.addActionListener(e -> {
			List<String> infoFiles = taxpayersAfmInfoFilesJList.getSelectedValuesList();
			FilesManager filesManager = FilesManager.getInstance();

			if (!infoFiles.isEmpty()) {
				String confirmDialogText = "Load taxpayers data from the following files:\n";
				for (String afmInfoFileName : infoFiles) {
					confirmDialogText = confirmDialogText.concat(afmInfoFileName).concat("\n");
				}
				confirmDialogText += "Are you sure?";

				int dialogResult = JOptionPane.showConfirmDialog(null, confirmDialogText, "Confirmation", JOptionPane.YES_NO_OPTION);
				if (dialogResult == JOptionPane.YES_OPTION) {
					filesManager.cacheTaxPayers(taxpayersFolderPath, infoFiles);
					JLabel totalLoadedTaxpayersJLabel = (JLabel) appMainWindow.getContentPane().getComponent(1);
					totalLoadedTaxpayersJLabel.setText(Integer.toString(filesManager.getCachedTaxPayers().size()));

					dispose();
				}
			} else {
				JOptionPane.showMessageDialog(null, "No file selected", "Error", JOptionPane.WARNING_MESSAGE);
			}
		});
	}

	// todo refactor this to the FilesManager class. We don't want dependency with IO here
	public void fillTaxpayersAfmInfoFilesJList(String afmInfoFilesFolderPath) {
		this.taxpayersFolderPath = afmInfoFilesFolderPath;

		File folder = new File(afmInfoFilesFolderPath);
		File[] folderFiles = folder.listFiles((dir, name) -> (name.toLowerCase().endsWith("_info.txt") || name.toLowerCase().endsWith("_info.xml")));

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