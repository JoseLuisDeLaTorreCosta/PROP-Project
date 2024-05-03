package main.Presentation;

import java.util.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

public class VistaCrearDocument {
    CtrlPresentacio iCP;
    private JFrame frameVista;
    private JPanel mainPanel;

    private void initComponents() {
        frameVista = new JFrame("Crear Nou Document");
        frameVista.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frameVista.setMinimumSize(new Dimension(800, 600));

        Border emptyBorder = BorderFactory.createEmptyBorder(30, 30, 30, 30);

        JButton buttonAlfabet = new JButton("Alfabet");
        buttonAlfabet.addActionListener(new AlfabetListener());
        JButton buttonLlista = new JButton("Llista de paraules");
        buttonLlista.addActionListener(new LlistaListener());
        JButton buttonText = new JButton("Text");
        buttonText.addActionListener(new TextListener());
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBorder(emptyBorder);
        buttonPanel.add(buttonAlfabet);
        buttonPanel.add(buttonLlista);
        buttonPanel.add(buttonText);

        JLabel createLabel = new JLabel("De quin tipus és el document que vols crear?");
        createLabel.setHorizontalAlignment(SwingConstants.CENTER);
        createLabel.setVerticalAlignment((SwingConstants.CENTER));
        createLabel.setBorder(emptyBorder);

        JPanel createPanel = new JPanel(new BorderLayout());
        createPanel.add(createLabel, BorderLayout.NORTH);
        createPanel.add(buttonPanel, BorderLayout.SOUTH);

        JButton buttonAlfabet2 = new JButton("Alfabet");
        buttonAlfabet2.addActionListener(new AlfabetImportListener());
        JButton buttonLlista2 = new JButton("Llista de paraules");
        buttonLlista2.addActionListener(new LlistaImportListener());
        JButton buttonText2 = new JButton("Text");
        buttonText2.addActionListener(new TextImportListener());
        JPanel buttonPanel2 = new JPanel(new FlowLayout());
        buttonPanel2.setBorder(emptyBorder);
        buttonPanel2.add(buttonAlfabet2);
        buttonPanel2.add(buttonLlista2);
        buttonPanel2.add(buttonText2);

        JLabel importLabel = new JLabel("De quin tipus és el document que vols importar?");
        importLabel.setHorizontalAlignment(SwingConstants.CENTER);
        importLabel.setVerticalAlignment(SwingConstants.CENTER);
        importLabel.setBorder(emptyBorder);

        JPanel importPanel = new JPanel(new BorderLayout());
        importPanel.add(importLabel, BorderLayout.NORTH);
        importPanel.add(buttonPanel2, BorderLayout.SOUTH);

        JButton exitButton = new JButton("Sortir");
        exitButton.addActionListener(new ExitListener());
        JPanel auxPanel = new JPanel(new FlowLayout());
        auxPanel.setBorder(emptyBorder);
        auxPanel.add(exitButton);
        JPanel exitPanel = new JPanel(new BorderLayout());
        exitPanel.add(auxPanel, BorderLayout.SOUTH);

        mainPanel = new JPanel(new GridLayout(3, 1));
        mainPanel.add(createPanel);
        mainPanel.add(importPanel);
        mainPanel.add(exitPanel);

        frameVista.getContentPane().add(mainPanel, BorderLayout.CENTER);
    }

    public VistaCrearDocument(CtrlPresentacio pCP) {
        iCP = pCP;
        initComponents();
    }

    private void showException(String errorMsg) {
        JOptionPane.showMessageDialog(frameVista, errorMsg, "Error!", JOptionPane.WARNING_MESSAGE);
    }

    public void showVista() {
        frameVista.pack();
        frameVista.setLocationRelativeTo(null);
        frameVista.setVisible(true);
    }

    public void closeVista() {
        frameVista.setVisible(false);
        frameVista.dispose();
    }

    private class AlfabetListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            closeVista();
            iCP.crearAlfabet();
        }
    }

    private class LlistaListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            closeVista();
            iCP.crearLlista();
        }
    }

    private class TextListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            closeVista();
            iCP.crearText();
        }
    }

    private class AlfabetImportListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (iCP.importarAlfabet(frameVista)) closeVista();
            else showException("No s'ha pogut llegir l'alfabet correctament.");
        }
    }

    private class LlistaImportListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (iCP.importarLlista(frameVista)) closeVista();
            else showException("No s'ha pogut llegir la llista de paraules correctament.");
        }
    }

    private class TextImportListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (iCP.importarText(frameVista)) closeVista();
            else showException("No s'ha pogut llegir el text correctament.");
        }
    }

    private class ExitListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            closeVista();
        }
    }
}