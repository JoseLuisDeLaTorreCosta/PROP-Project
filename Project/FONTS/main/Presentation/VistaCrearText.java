package main.Presentation;

import java.util.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

public class VistaCrearText {
    CtrlPresentacio iCP;
    private JFrame frameVista;
    private JPanel mainPanel;
    private String name;
    private String text;

    private JTextField nameTextField;
    private JTextArea textTextArea;

    private void initComponents() {
        frameVista = new JFrame("Crear Nou Text");
        frameVista.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frameVista.setMinimumSize(new Dimension(800, 600));

        Border blackline = BorderFactory.createLineBorder(Color.black);
        Border emptyBorder = BorderFactory.createEmptyBorder(30, 30, 30, 30);

        JLabel nameLabel = new JLabel("Introdueix el nom del nou text:");
        nameLabel.setBorder(emptyBorder);
        nameTextField = new JTextField(20);
        nameTextField.setBorder(blackline);
        JPanel namePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        namePanel.add(nameLabel);
        namePanel.add(nameTextField);

        JLabel label = new JLabel("Introdueix el text:");
        label.setBorder(emptyBorder);

        JPanel introPanel = new JPanel(new BorderLayout());
        introPanel.add(namePanel, BorderLayout.NORTH);
        introPanel.add(label, BorderLayout.SOUTH);

        textTextArea = new JTextArea();
        textTextArea.setEditable(true);
        textTextArea.setLineWrap(true);
        textTextArea.setWrapStyleWord(true);
        textTextArea.setMinimumSize(new Dimension(500, 500));
        textTextArea.setBorder(blackline);

        JButton exitButton = new JButton("Sortir");
        exitButton.addActionListener(new ExitListener());
        JButton createButton = new JButton("Crear Text");
        createButton.addActionListener(new CreateTextListener());
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBorder(emptyBorder);
        buttonPanel.add(exitButton);
        buttonPanel.add(createButton);

        mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(introPanel, BorderLayout.NORTH);
        mainPanel.add(textTextArea, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        frameVista.getContentPane().add(mainPanel, BorderLayout.CENTER);
    }

    public VistaCrearText(CtrlPresentacio pCP) {
        iCP = pCP;
        initComponents();
    }

    private void showException(String errorMsg) {
        JOptionPane.showMessageDialog(frameVista, errorMsg, "Error!", JOptionPane.WARNING_MESSAGE);
    }

    private boolean llegirText() {
        name = nameTextField.getText();
        name = name.trim();
        if (name.length() <= 0) {
            showException("No s'ha introduït un nom pel text.");
            return false;
        }

        if (iCP.existeixDocument(name)) {
            showException("Ja existeix un Document amb el nom introduit");
            return false;
        }

        text = textTextArea.getText();
        text = text.trim();
        if (text.length() <= 0) {
            showException("No s'ha introduït cap text.");
            return false;
        }

        if (text.contains("_")) {
            showException("Ho sentim, però no és possible guardar el caràcter '_'.");
            return false;
        }

        return true;
    }

    public void showVista() {
        frameVista.pack();
        frameVista.setLocationRelativeTo(null);
        frameVista.setVisible(true);
    }

    private class CreateTextListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (llegirText()) {
                iCP.crearText(name, text);
                frameVista.dispose();
            }
        }
    }

    private class ExitListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            frameVista.dispose();
        }
    }
}
