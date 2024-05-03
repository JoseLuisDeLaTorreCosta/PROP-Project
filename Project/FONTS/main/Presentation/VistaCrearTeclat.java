package main.Presentation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class VistaCrearTeclat {
	private CtrlPresentacio iCtrlPresentacio;
	private JFrame frame;
	private JButton btnCrearTeclat;
	private JComboBox<String> algorithmComboBox;
	private JComboBox<String> documentComboBox;
	private JPanel panel = new JPanel();
	private JTextField nameTextField = new JTextField();
	private List<String> documentsList;

	public VistaCrearTeclat(CtrlPresentacio pCtrlPresentacio, List<String> documentsList) {
		this.documentsList = documentsList;
		this.iCtrlPresentacio = pCtrlPresentacio;
		initComponents();
	}

	private void initComponents() {
		frame = new JFrame("Crear Teclat");
		frame.setSize(new Dimension(800, 600));
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		panel.setLayout(new BorderLayout());

		initSelectors();
		initButtons();

		frame.add(panel);

		// Center the frame on the screen
		frame.setLocationRelativeTo(null);
	}

	private void initSelectors() {
		// Create a combo box for algorithm selection
		String[] algorithms = { "QAP", "Hill Climbing" };
		algorithmComboBox = new JComboBox<>(algorithms);

		// Create a combo box for document selection
		documentComboBox = new JComboBox<>();
		updateDocumentComboBox();

		JPanel topPanel = new JPanel(new GridLayout(3, 2, 10, 10)); // 3 rows, 2 columns with 10-pixel gap
		topPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30)); // Increase padding

		JLabel algorithmLabel = new JLabel("Algorisme a utilitzar:");
		JLabel nameLabel = new JLabel("Nom:");
		nameTextField.setColumns(15);

		topPanel.add(nameLabel);
		topPanel.add(nameTextField);
		topPanel.add(algorithmLabel);
		topPanel.add(algorithmComboBox);
		topPanel.add(new JLabel("Document ID:"));
		topPanel.add(documentComboBox);

		panel.add(topPanel, BorderLayout.NORTH);
	}

	private void updateDocumentComboBox() {
		documentComboBox.removeAllItems();
		for (String document : documentsList) {
			documentComboBox.addItem(document);
		}
	}

	private void initButtons() {
		JPanel bottomPanel = new JPanel(new FlowLayout());
		bottomPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

		btnCrearTeclat = new JButton("Crear Teclat");

		btnCrearTeclat.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String name = nameTextField.getText();
				String algorithmSelected = (String) algorithmComboBox.getSelectedItem();
				String documentId = (String) documentComboBox.getSelectedItem();

				if (isAlphanumericWithUnderscore(name) && name.length() >= 1 && name.length() <= 12) {
					iCtrlPresentacio.crearTeclat(name, algorithmSelected, documentId);
				} else {
					JOptionPane.showMessageDialog(frame,
							"El nom només pot estar format per caràcters alfanumèrics i '_'. No pot estar buit i com a màxim 12 caràcters.", "Error",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		bottomPanel.add(btnCrearTeclat);

		panel.add(bottomPanel, BorderLayout.SOUTH);
	}

	public void makeVisible() {
		frame.setVisible(true);
	}

	public void tancarVista() {
		frame.setVisible(false);
	}

	private boolean isAlphanumericWithUnderscore(String s) {
		return s.matches("[a-zA-Z0-9_]+");
	}
}
