package main.Presentation;

import java.util.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

public class VistaTeclat {
    private CtrlPresentacio iCP;
    private JFrame frameVista;
    private String name;
    private char[][] teclat;
    private char[][] preModifyTeclat;
    private JPanel mainPanel;
    private JButton[][] teclatButton;
    private JButton selectedKey1;
    private int selectedKey1RowIndex;
    private int selectedKey1ColumnIndex;
    private boolean oneKeySelected;

    private void initComponents() {
        if (frameVista == null) {
            frameVista = new JFrame();
            frameVista.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            frameVista.setMinimumSize(new Dimension(800, 600));
        }
        frameVista.setTitle(name);

        int files = teclat.length;
        int columnes = teclat[0].length;
        JPanel teclatPanel = new JPanel(new GridLayout(files, columnes));

        Border blackline = BorderFactory.createLineBorder(Color.black);
        Border emptyBorder = BorderFactory.createEmptyBorder(30, 30, 30, 30);

        for (int i = 0; i < files; ++i) {
            for (int j = 0; j < columnes; ++j) {
                JLabel tecla = new JLabel(String.valueOf(teclat[i][j]));
                tecla.setHorizontalAlignment(SwingConstants.CENTER);
                tecla.setVerticalAlignment(SwingConstants.CENTER);
                tecla.setPreferredSize(new Dimension(10, 10));
                tecla.setBorder(blackline);
                teclatPanel.add(tecla);
            }
        }

        JButton exitButton = new JButton("Sortir");
        exitButton.addActionListener(new ExitListener());
        JButton modifyButton = new JButton("Modificar");
        modifyButton.addActionListener(new ModificarListener());
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBorder(emptyBorder);
        buttonPanel.add(exitButton);
        buttonPanel.add(modifyButton);

        mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(teclatPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        frameVista.getContentPane().removeAll();
        frameVista.getContentPane().add(mainPanel, BorderLayout.CENTER);
    }

    public VistaTeclat(CtrlPresentacio pCP, String nomTeclat, char[][] teclat) {
        iCP = pCP;
        this.name = nomTeclat;
        this.teclat = teclat;
        preModifyTeclat = new char[teclat.length][teclat[0].length];
        for (int i = 0; i < teclat.length; ++i) {
            for (int j = 0; j < teclat[0].length; ++j) preModifyTeclat[i][j] = teclat[i][j];
        }
        oneKeySelected = false;
        initComponents();
    }

    private void showException(String errorMsg) {
        JOptionPane.showMessageDialog(frameVista, errorMsg, "Error!", JOptionPane.WARNING_MESSAGE);
    }

    private void changeVistaToModifyTeclat() {
        if (!iCP.iniciarModificacioTeclat()) {
            showException("Ja hi ha un altre teclat en modificació.");
            return;
        }

        //comprovar que el teclat no estigui desactualitzat
        if (!iCP.existeixTeclat(name)) {
            showException("Aquest teclat ja no existeix. Recomanem tancar aquesta finestra.");
            return;
        }

        if (!iCP.teclatActualitzat(name, teclat)) {
            showException("Aquest teclat no està correctament actualitzat. Recomanem tancar aquesta finesta.");
            return;
        }

        Border emptyBorder = BorderFactory.createEmptyBorder(30, 30, 30, 30);

        int files = teclat.length;
        int columnes = teclat[0].length;

        teclatButton = new JButton[files][columnes];
        JPanel teclatPanel = new JPanel(new GridLayout(files, columnes));
        for (int i = 0; i < files; ++i) {
            for (int j = 0; j < columnes; ++j) {
                teclatButton[i][j] = new JButton(String.valueOf(teclat[i][j]));
                teclatButton[i][j].setOpaque(true);
                teclatButton[i][j].addActionListener(new KeySelectListener());
                teclatPanel.add(teclatButton[i][j]);
            }
        }

        JButton cancelButton = new JButton("Cancel·lar");
        cancelButton.addActionListener(new CancelListener());
        JButton confirmButton = new JButton("Confirmar");
        confirmButton.addActionListener(new ConfirmarListener());
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBorder(emptyBorder);
        buttonPanel.add(cancelButton);
        buttonPanel.add(confirmButton);

        mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(teclatPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        frameVista.getContentPane().removeAll();
        frameVista.getContentPane().add(mainPanel, BorderLayout.CENTER);
        frameVista.setTitle("Modificar Teclat '" + name + "'");
    }

    private void selectKey(String selectedKey) {
        int files = teclatButton.length;
        int columnes = teclatButton[0].length;

        if (oneKeySelected) {
            for (int i = 0; i < files; ++i) {
                for (int j = 0; j < columnes; ++j) {
                    if (teclatButton[i][j].getText().equals(selectedKey)) {
                        teclat[i][j] = selectedKey1.getText().charAt(0);
                        teclat[selectedKey1RowIndex][selectedKey1ColumnIndex] = selectedKey.charAt(0);
                        teclatButton[i][j].setText(selectedKey1.getText());
                        selectedKey1.setText(selectedKey);
                        oneKeySelected = false;
                    }
                    if (!oneKeySelected) break;
                }
                if (!oneKeySelected) break;
            }

            selectedKey1.setBackground(null);
            selectedKey1 = null;
        }
        else {
            for (int i = 0; i < files; ++i) {
                for (int j = 0; j < columnes; ++j) {
                    if (teclatButton[i][j].getText().equals(selectedKey)) {
                        selectedKey1 = teclatButton[i][j];
                        teclatButton[i][j].setBackground(Color.red);
                        selectedKey1RowIndex = i;
                        selectedKey1ColumnIndex = j;
                        oneKeySelected = true;
                    }
                    if (oneKeySelected) break;
                }
                if (oneKeySelected) break;
            }
        }
    }

    public void showVista() {
        frameVista.pack();
        frameVista.setLocationRelativeTo(null);
        frameVista.setVisible(true);
    }

    private class ModificarListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            changeVistaToModifyTeclat();
            showVista();
        }
    }

    private class ConfirmarListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            iCP.modificaTeclat(name, teclat);
            for (int i = 0; i < teclat.length; ++i) {
                for (int j = 0; j < teclat[0].length; ++j) preModifyTeclat[i][j] = teclat[i][j];
            }
            initComponents();
            showVista();
        }
    }

    private class KeySelectListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            selectKey(e.getActionCommand());
        }
    }

    private class ExitListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            frameVista.dispose();
        }
    }

    private class CancelListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            for (int i = 0; i < teclat.length; ++i) {
                for (int j = 0; j < teclat[0].length; ++j) teclat[i][j] = preModifyTeclat[i][j];
            }
            initComponents();
            showVista();
            iCP.cancelarModificar();
        }
    }
}
