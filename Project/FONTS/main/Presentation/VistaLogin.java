package main.Presentation;

import javax.swing.*;
import java.awt.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class VistaLogin {
	private CtrlPresentacio iCtrlPresentacio;
	private JFrame frameVista = new JFrame("Vista Iniciar Sessió");
	private JTabbedPane tabbedPane = new JTabbedPane();
	private JPanel panelSignIn = new JPanel(new GridBagLayout());
	private JPanel panelSignUp = new JPanel(new GridBagLayout());

	private JLabel labelUsernameSignIn = new JLabel("Nom d'usuari");
	private JLabel labelPasswordSignIn = new JLabel("Contrasenya");
	private JTextField textUsernameSignIn = new JTextField(15);
	private JPasswordField textPasswordSignIn = new JPasswordField(15);
	private JButton signIn = new JButton("Iniciar Sessió");
	private JLabel labelUsernameSignUp = new JLabel("Nom d'usuari");
	private JLabel labelPasswordSignUp = new JLabel("Contrasenya");
	private JLabel labelRepeatPasswordSignUp = new JLabel("Repetir la contrasenya");
	private JTextField textUsernameSignUp = new JTextField(15);
	private JPasswordField textPasswordSignUp = new JPasswordField(15);
	private JPasswordField textRepeatPasswordSignUp = new JPasswordField(15);
	private JButton signUp = new JButton("Registrar-se");

	public VistaLogin(CtrlPresentacio pCtrlPresentacio) {
		iCtrlPresentacio = pCtrlPresentacio;
		initComponents();
	}

	public void makeVisible() {
		frameVista.pack();
		frameVista.setLocationRelativeTo(null);
		frameVista.setVisible(true);
	}

	public void tancarVista() {
		frameVista.dispose();
	}

	private void initComponents() {
		init_frameVista();
		init_panelSignIn();
		init_panelSignUp();

		tabbedPane.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				int selectedIndex = tabbedPane.getSelectedIndex();
				if (selectedIndex == 0) { // "Iniciar Sessió" tab
					resetSignInFields();
				} else if (selectedIndex == 1) { // "Registrar-se" tab
					resetSignUpFields();
				}
			}
		});
	}

	private void init_frameVista() {
		frameVista.setMinimumSize(new Dimension(400, 300));
		frameVista.setPreferredSize(frameVista.getMinimumSize());
		frameVista.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frameVista.getContentPane().add(tabbedPane);

		tabbedPane.addTab("Iniciar Sessió", panelSignIn);
		tabbedPane.addTab("Registrar-se", panelSignUp);
	}

	private void init_panelSignIn() {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 5, 5, 5);

		gbc.gridx = 0;
		gbc.gridy = 0;
		panelSignIn.add(labelUsernameSignIn, gbc);

		gbc.gridx = 1;
		gbc.gridwidth = 2;
		panelSignIn.add(textUsernameSignIn, gbc);

		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 1;
		panelSignIn.add(labelPasswordSignIn, gbc);

		gbc.gridx = 1;
		gbc.gridwidth = 2;
		panelSignIn.add(textPasswordSignIn, gbc);

		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.gridwidth = 3;
		panelSignIn.add(signIn, gbc);

		gbc.gridy = 3;

		// Afegir ActionListener al botó de Iniciar Sessió
		signIn.addActionListener(e -> {
			String username = textUsernameSignIn.getText();
			String password = new String(textPasswordSignIn.getPassword());

			if (username.isEmpty() || password.isEmpty()) {
				JOptionPane.showMessageDialog(frameVista, "El nom d'usuari i la contrasenya no poden estar buits", "Error",
						JOptionPane.ERROR_MESSAGE);
			} else {
				iCtrlPresentacio.iniciarSessio(username, password);
			}
		});
	}

	private void init_panelSignUp() {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 5, 5, 5);

		gbc.gridx = 0;
		gbc.gridy = 0;
		panelSignUp.add(labelUsernameSignUp, gbc);

		gbc.gridx = 1;
		gbc.gridwidth = 2;
		panelSignUp.add(textUsernameSignUp, gbc);

		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 1;
		panelSignUp.add(labelPasswordSignUp, gbc);

		gbc.gridx = 1;
		gbc.gridwidth = 2;
		panelSignUp.add(textPasswordSignUp, gbc);

		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.gridwidth = 1;
		panelSignUp.add(labelRepeatPasswordSignUp, gbc);

		gbc.gridx = 1;
		gbc.gridwidth = 2;
		panelSignUp.add(textRepeatPasswordSignUp, gbc);

		gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.gridwidth = 3;
		panelSignUp.add(signUp, gbc);

		// Afegir ActionListener al botó de Registrar-se
		signUp.addActionListener(e -> {
			String username = textUsernameSignUp.getText();
			String password = new String(textPasswordSignUp.getPassword());
			String repeatPassword = new String(textRepeatPasswordSignUp.getPassword());

			// Check username format (no white spaces, only letters, numbers, '-', '_')
			if (!username.matches("[\\w\\d-]+")) {
				this.mostrarMissatgeError("Format de nom d'usuari no vàlid. Utilitzeu només lletres, números, '-', i '_'.");
			} else if (password.length() < 6 || !password.matches(".*[a-z].*") || !password.matches(".*[A-Z].*")
					|| !password.matches(".*\\d.*")) {
				// Check password format (at least 6 characters, one lowercase, one uppercase,
				// one digit)
				this.mostrarMissatgeError("Format de contrasenya no vàlid. Utilitzeu almenys 6 caràcters amb una lletra minúscula, una lletra majúscula i un dígit.");
			} else if (!password.equals(repeatPassword)) {
				// Check if password and repeat password match
				this.mostrarMissatgeError("Les contrasenyes no coincideixen.");
			} else {
				iCtrlPresentacio.registrarUsuari(username, password);
			}
		});

	}

	private void resetSignInFields() {
		textUsernameSignIn.setText("");
		textPasswordSignIn.setText("");
	}

	private void resetSignUpFields() {
		textUsernameSignUp.setText("");
		textPasswordSignUp.setText("");
		textRepeatPasswordSignUp.setText("");
	}

	public void mostrarMissatgeError(String errorMessage) {
		JOptionPane.showMessageDialog(frameVista, errorMessage, "Error", JOptionPane.ERROR_MESSAGE);
	}
}
