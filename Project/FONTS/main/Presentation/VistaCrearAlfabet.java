package main.Presentation;

import java.util.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

public class VistaCrearAlfabet {
    CtrlPresentacio iCP;
    private JFrame frameVista;
    private JPanel mainPanel;
    private String name;
    private ArrayList<Character> alfabet;
    private int[][] taulaMarkov;
    private JTextField nameTextField;
    private JTextField alfabetTextField;
    private JTextField[][] pseudoTaulaMarkov;

    private void initComponents() {
        if (frameVista == null) {
            frameVista = new JFrame();
            frameVista.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            frameVista.setMinimumSize(new Dimension(800, 600));
        }
        frameVista.setTitle("Crear Nou Alfabet");

        Border blackline = BorderFactory.createLineBorder(Color.black);
        Border emptyBorder = BorderFactory.createEmptyBorder(30, 30, 30, 30);

        JLabel nameLabel = new JLabel("Introdueix el nom del nou alfabet:");
        nameLabel.setBorder(emptyBorder);
        if (nameTextField == null) {
            nameTextField = new JTextField(20);
            nameTextField.setBorder(blackline);
        }
        JPanel namePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        namePanel.add(nameLabel);
        namePanel.add(nameTextField);

        JLabel label = new JLabel("Introdueix els caracters del nou alfabet, separats per espais:");
        label.setBorder(emptyBorder);

        JPanel introPanel = new JPanel(new BorderLayout());
        introPanel.add(namePanel, BorderLayout.NORTH);
        introPanel.add(label, BorderLayout.SOUTH);

        if (alfabetTextField == null) {
            alfabetTextField = new JTextField(50);
            alfabetTextField.setBorder(blackline);
        }
        JPanel alfabetPanel = new JPanel(new FlowLayout());
        alfabetPanel.add(alfabetTextField);

        JButton exitButton = new JButton("Sortir");
        exitButton.addActionListener(new ExitListener());
        JButton confirmButton = new JButton("Confirmar");
        confirmButton.addActionListener(new IntroListener());
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBorder(emptyBorder);
        buttonPanel.add(exitButton);
        buttonPanel.add(confirmButton);

        mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(introPanel, BorderLayout.NORTH);
        mainPanel.add(alfabetPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        frameVista.getContentPane().removeAll();
        frameVista.getContentPane().add(mainPanel, BorderLayout.CENTER);
    }

    public VistaCrearAlfabet(CtrlPresentacio pCP) {
        iCP = pCP;
        initComponents();
    }

    private void showException(String errorMsg) {
        JOptionPane.showMessageDialog(frameVista, errorMsg, "Error!", JOptionPane.WARNING_MESSAGE);
    }

    private void changeVistaToMarkovEntry() {
        name = nameTextField.getText();
        name = name.trim();
        if (name.length() <= 0) {
            showException("No s'ha introduït un nom per l'alfabet.");
            return;
        }

        if (iCP.existeixDocument(name)) {
            showException("Ja existeix un Document amb el nom introduit");
            return;
        }

        String stringAlfabet = alfabetTextField.getText();
        stringAlfabet = stringAlfabet.trim();

        if (stringAlfabet.length() <= 0) {
            showException("No s'ha introduït cap caràcter per l'alfabet.");
            return;
        }

        String[] pseudoAlfabet = stringAlfabet.split(" ");

        alfabet = new ArrayList<Character>();
        for (int i = 0; i < pseudoAlfabet.length; ++i) {
            if (pseudoAlfabet[i].length() > 1) {
                showException("Els caràcters no estan correctament separats.");
                return;
            }

            if (alfabet.contains(pseudoAlfabet[i].charAt(0))) {
                showException("L'alfabet conté caràcters repetits.");
                return;
            }
            else if (pseudoAlfabet[i].charAt(0) == '_') {
                showException("Ho sentim, però no és possible guardar el caràcter '_'.");
            }
            else alfabet.add(pseudoAlfabet[i].charAt(0));

        }

        int files, columnes;
        files = columnes = alfabet.size() + 1;
        JPanel markovPanel = new JPanel(new GridLayout(files, columnes));

        pseudoTaulaMarkov = new JTextField[files][columnes];

        Border blackline = BorderFactory.createLineBorder(Color.black);
        Border emptyBorder = BorderFactory.createEmptyBorder(30, 30, 30, 30);

        for (int i = 0; i < files; ++i) {
            for (int j = 0; j < columnes; ++j) {
                if (i == 0 || j == 0) {
                    JLabel label = new JLabel();
                    label.setHorizontalAlignment(SwingConstants.CENTER);
                    label.setVerticalAlignment(SwingConstants.CENTER);
                    label.setPreferredSize(new Dimension(10, 10));
                    label.setBorder(blackline);
                    if (i == 0 && j == 0) label.setText(String.valueOf(' '));
                    else if (i == 0) label.setText(String.valueOf(alfabet.get(j - 1)));
                    else if (j == 0) label.setText(String.valueOf(alfabet.get(i - 1)));
                    markovPanel.add(label);
                }
                else {
                    pseudoTaulaMarkov[i - 1][j - 1] = new JTextField(1);
                    pseudoTaulaMarkov[i - 1][j - 1].setHorizontalAlignment(SwingConstants.CENTER);
                    pseudoTaulaMarkov[i - 1][j - 1].setBorder(blackline);
                    markovPanel.add(pseudoTaulaMarkov[i - 1][j - 1]);
                }
            }
        }

        JLabel label = new JLabel("Introdueix els valors a la taula de Markov de l'alfabet:");
        label.setBorder(emptyBorder);
        JButton cancelButton = new JButton("Cancel·lar");
        cancelButton.addActionListener(new CancelListener());
        JButton createButton = new JButton("Crear Alfabet");
        createButton.addActionListener(new CreateAlfabetListener());
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBorder(emptyBorder);
        buttonPanel.add(cancelButton);
        buttonPanel.add(createButton);

        mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(label, BorderLayout.NORTH);
        mainPanel.add(markovPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        frameVista.getContentPane().removeAll();
        frameVista.getContentPane().add(mainPanel, BorderLayout.CENTER);
        frameVista.setTitle("Crear Nou Alfabet '" + name + "'");
    }

    private boolean llegirTaulaMarkov() {
        taulaMarkov = new int[alfabet.size()][alfabet.size()];
        for (int i = 0; i < taulaMarkov.length; ++i) {
            for (int j = 0; j < taulaMarkov[0].length; ++j) {
                String valorStr = pseudoTaulaMarkov[i][j].getText();
                valorStr = valorStr.trim();
                if (valorStr.length() <= 0) {
                    showException("Falten valors a la taula.");
                    return false;
                }
                int valorNum;
                try {
                    valorNum = Integer.valueOf(valorStr);
                } catch (NumberFormatException exc) {
                    showException("Només s'accepten valors enters positius.");
                    return false;
                }
                if (valorNum < 0) {
                    showException("Només s'accepten valors enters positius.");
                    return false;
                }
                taulaMarkov[i][j] = valorNum;
            }
        }
        return true;
    }

    public void showVista() {
        frameVista.pack();
        frameVista.setLocationRelativeTo(null);
        frameVista.setVisible(true);
    }

    private class IntroListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            changeVistaToMarkovEntry();
            showVista();
        }
    }

    private class CreateAlfabetListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (llegirTaulaMarkov()) {
                iCP.crearAlfabet(name, alfabet, taulaMarkov);
                frameVista.dispose();
            }
        }
    }

    private class ExitListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            frameVista.dispose();
        }
    }

    private class CancelListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            initComponents();
            showVista();
        }
    }
}
