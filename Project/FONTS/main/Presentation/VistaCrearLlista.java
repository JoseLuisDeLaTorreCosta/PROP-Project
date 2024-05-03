package main.Presentation;

import java.util.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

public class VistaCrearLlista {
    CtrlPresentacio iCP;
    private JFrame frameVista;
    private JPanel mainPanel;
    private String name;
    private ArrayList<String> llista;
    private ArrayList<Integer> frequencies;

    private JTextField nameTextField;
    private JTextField llistaTextField;
    private JTextField[] pseudoFrequencies;

    private void initComponents() {
        if (frameVista == null) {
            frameVista = new JFrame();
            frameVista.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            frameVista.setMinimumSize(new Dimension(800, 600));
        }
        frameVista.setTitle("Crear Nova Llista de Paraules");

        Border blackline = BorderFactory.createLineBorder(Color.black);
        Border emptyBorder = BorderFactory.createEmptyBorder(30, 30, 30, 30);

        JLabel nameLabel = new JLabel("Introdueix el nom de la nova llista:");
        nameLabel.setBorder(emptyBorder);
        if (nameTextField == null) {
            nameTextField = new JTextField(20);
            nameTextField.setBorder(blackline);
        }
        JPanel namePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        namePanel.add(nameLabel);
        namePanel.add(nameTextField);

        JLabel label = new JLabel("Introdueix les paraules de la nova llista, separades per espais:");
        label.setBorder(emptyBorder);

        JPanel introPanel = new JPanel(new BorderLayout());
        introPanel.add(namePanel, BorderLayout.NORTH);
        introPanel.add(label, BorderLayout.SOUTH);

        if (llistaTextField == null) {
            llistaTextField = new JTextField(50);
            llistaTextField.setBorder(blackline);
        }
        JPanel alfabetPanel = new JPanel(new FlowLayout());
        alfabetPanel.add(llistaTextField);

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

    public VistaCrearLlista(CtrlPresentacio pCP) {
        iCP = pCP;
        initComponents();
    }

    private void showException(String errorMsg) {
        JOptionPane.showMessageDialog(frameVista, errorMsg, "Error!", JOptionPane.WARNING_MESSAGE);
    }

    private void changeVistaToFreqEntry() {
        name = nameTextField.getText();
        name = name.trim();
        if (name.length() <= 0) {
            showException("No s'ha introduït un nom per la llista.");
            return;
        }

        if (iCP.existeixDocument(name)) {
            showException("Ja existeix un Document amb el nom introduit");
            return;
        }
        
        String stringLlista = llistaTextField.getText();
        stringLlista = stringLlista.trim();
        if (stringLlista.length() <= 0) {
            showException("No s'ha introduït cap paraula a la llista.");
            return;
        }
        String[] pseudoLlista = stringLlista.split(" ");

        llista = new ArrayList<String>();
        for (int i = 0; i < pseudoLlista.length; ++i) {
            if (llista.contains(pseudoLlista[i])) {
                showException("La llista conté paraules repetides.");
                return;
            }
            else if (pseudoLlista[i].contains("_")) {
                showException("Ho sentim, però no és possible guardar el caràcter '_'.");
                return;
            }
            else llista.add(pseudoLlista[i]);
        }

        Border blackline = BorderFactory.createLineBorder(Color.black);
        Border emptyBorder = BorderFactory.createEmptyBorder(30, 30, 30, 30);

        pseudoFrequencies = new JTextField[llista.size()];
        JPanel taulaPanel = new JPanel(new GridLayout(llista.size(), 2));
        for (int i = 0; i < llista.size(); ++i) {
            JLabel paraula = new JLabel(llista.get(i));
            paraula.setHorizontalAlignment(SwingConstants.CENTER);
            paraula.setVerticalAlignment(SwingConstants.CENTER);
            paraula.setBorder(blackline);
            taulaPanel.add(paraula);

            pseudoFrequencies[i] = new JTextField(1);
            pseudoFrequencies[i].setHorizontalAlignment(SwingConstants.CENTER);
            pseudoFrequencies[i].setBorder(blackline);
            taulaPanel.add(pseudoFrequencies[i]);
        }

        JLabel label = new JLabel("Introdueix les freqüències corresponents a cada paraula de la llista:");
        label.setBorder(emptyBorder);

        JButton cancelButton = new JButton("Cancel·lar");
        cancelButton.addActionListener(new CancelListener());
        JButton createButton = new JButton("Crear Llista");
        createButton.addActionListener(new CreateLlistaListener());
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBorder(emptyBorder);
        buttonPanel.add(cancelButton);
        buttonPanel.add(createButton);

        mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(label, BorderLayout.NORTH);
        mainPanel.add(taulaPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        frameVista.getContentPane().removeAll();
        frameVista.getContentPane().add(mainPanel, BorderLayout.CENTER);
        frameVista.setTitle("Crear Nova Llista '" + name + "'");
    }

    private boolean llegirFrequencies() {
        frequencies = new ArrayList<Integer>();
        for (int i = 0; i < llista.size(); ++i) {
            String valorStr = pseudoFrequencies[i].getText();
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
            frequencies.add(valorNum);
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
            changeVistaToFreqEntry();
            showVista();
        }
    }

    private class CreateLlistaListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (llegirFrequencies()) {
                iCP.crearLlista(name, llista, frequencies);
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
