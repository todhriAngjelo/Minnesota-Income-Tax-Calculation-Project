package com.minesota.tax.calculator.view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.*;

import com.minesota.tax.calculator.model.Database;

public class MainJFrameWindow {

	private JFrame taxationMainWindowJFrame;


	public MainJFrameWindow() {
		initialize();
	}

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainJFrameWindow window = new MainJFrameWindow();
					window.taxationMainWindowJFrame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	private void initialize() {
		taxationMainWindowJFrame = new JFrame();
		taxationMainWindowJFrame.setResizable(false);
		taxationMainWindowJFrame.setTitle("Διαχείρηση φορολογίας");
		taxationMainWindowJFrame.setBounds(-1, -1, 357, 228);
		taxationMainWindowJFrame.setLocationRelativeTo(null);
		taxationMainWindowJFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		taxationMainWindowJFrame.getContentPane().setLayout(null);

		JLabel label = new JLabel("Συν. αριθμός φορολογούμενων:");
		label.setForeground(Color.BLUE);
		label.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 13));
		label.setBounds(30, 11, 218, 33);
		taxationMainWindowJFrame.getContentPane().add(label);

		JLabel totalLoadedTaxpayersJLabel = new JLabel("0");
		totalLoadedTaxpayersJLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		totalLoadedTaxpayersJLabel.setForeground(Color.RED);
		totalLoadedTaxpayersJLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
		totalLoadedTaxpayersJLabel.setBounds(247, 20, 75, 14);
		taxationMainWindowJFrame.getContentPane().add(totalLoadedTaxpayersJLabel);

		JSeparator separator = new JSeparator();
		separator.setBounds(29, 42, 293, 2);
		taxationMainWindowJFrame.getContentPane().add(separator);

		JButton openTaxpayerLoadDataJDialog = new JButton("Φόρτωση δεδομένων φορολογούμενου (-ων)");
		openTaxpayerLoadDataJDialog.setFont(new Font("Tahoma", Font.BOLD, 11));
		openTaxpayerLoadDataJDialog.setBounds(27, 55, 295, 53);
		taxationMainWindowJFrame.getContentPane().add(openTaxpayerLoadDataJDialog);

		JButton showLoadedTaxpayersDataButton = new JButton("Εμφάνιση λίστας φορολογουμένων");
		showLoadedTaxpayersDataButton.setEnabled(false);
		showLoadedTaxpayersDataButton.setFont(new Font("Tahoma", Font.BOLD, 11));
		showLoadedTaxpayersDataButton.setBounds(27, 121, 295, 53);
		taxationMainWindowJFrame.getContentPane().add(showLoadedTaxpayersDataButton);

		totalLoadedTaxpayersJLabel.addPropertyChangeListener("text", new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent e) {
				showLoadedTaxpayersDataButton.setEnabled(!totalLoadedTaxpayersJLabel.getText().equals("0"));
			}
		});

		openTaxpayerLoadDataJDialog.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser afmInfoFilesFolderChooser = new JFileChooser();
				afmInfoFilesFolderChooser.setCurrentDirectory(new java.io.File("."));
				afmInfoFilesFolderChooser.setDialogTitle("Επιλέξτε τον φάκελο που περιέχει τα <AFM>_INFO.* αρχεία");
				afmInfoFilesFolderChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

				if (afmInfoFilesFolderChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
					String afmInfoFilesFolderPath = afmInfoFilesFolderChooser.getSelectedFile().toString();
					JOptionPane.showMessageDialog(null, afmInfoFilesFolderPath, "Διαδρομή φακέλου αρχείων εισόδου", JOptionPane.INFORMATION_MESSAGE);

					Database.setTaxpayersInfoFilesPath(afmInfoFilesFolderPath);

					TaxpayerLoadDataJDialog taxpayerLoadDataJDialog = new TaxpayerLoadDataJDialog(taxationMainWindowJFrame);
					taxpayerLoadDataJDialog.fillTaxpayersAfmInfoFilesJList(afmInfoFilesFolderPath);
					taxpayerLoadDataJDialog.setVisible(true);
				}

			}
		});

		showLoadedTaxpayersDataButton.addActionListener(arg0 -> {
			LoadedTaxpayersJDialog loadedTaxpayersJDialog = new LoadedTaxpayersJDialog(taxationMainWindowJFrame);
			loadedTaxpayersJDialog.fillLoadedTaxpayersJList();
			loadedTaxpayersJDialog.setVisible(true);
		});
	}
}