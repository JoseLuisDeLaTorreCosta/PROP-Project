package main.Presentation;

import java.util.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

public class VistaAlfabet {
    CtrlPresentacio iCP;
    private JFrame frameVista;
    private String nomAlfabet;
    private ArrayList<Character> alfabet;
    private int[][] taulaMarkov;
    private JPanel mainPanel;

    private void initComponents() {
        frameVista = new JFrame(nomAlfabet);
        frameVista.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frameVista.setMinimumSize(new Dimension(800, 600));

        int files = taulaMarkov.length + 1;
        int columnes = taulaMarkov[0].length + 1;
        JPanel alfabetPanel = new JPanel(new GridLayout(files, columnes));

        Border blackline = BorderFactory.createLineBorder(Color.black);
        Border emptyBorder = BorderFactory.createEmptyBorder(30, 30, 30, 30);

        for (int i = 0; i < files; ++i) {
            for (int j = 0; j < columnes; ++j) {
                JLabel label = new JLabel();
                label.setHorizontalAlignment(SwingConstants.CENTER);
                label.setVerticalAlignment(SwingConstants.CENTER);
                label.setPreferredSize(new Dimension(10, 10));
                label.setBorder(blackline);
                if (i == 0 && j == 0) label.setText(String.valueOf(' '));
                else if (i == 0) label.setText(String.valueOf(alfabet.get(j - 1)));
                else if (j == 0) label.setText(String.valueOf(alfabet.get(i - 1)));
                else label.setText(String.valueOf(taulaMarkov[i - 1][j - 1]));
                alfabetPanel.add(label);
            }
        }

        JButton exitButton = new JButton("Sortir");
        exitButton.addActionListener(new ExitListener());
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBorder(emptyBorder);
        buttonPanel.add(exitButton);

        mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(alfabetPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        frameVista.getContentPane().add(mainPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
    }

    public VistaAlfabet(CtrlPresentacio pCP, String nomAlfabet, ArrayList<Character> alfabet, int[][] taulaMarkov) {
        iCP = pCP;
        this.nomAlfabet = nomAlfabet;
        this.alfabet = alfabet;
        this.taulaMarkov = taulaMarkov;
        initComponents();
    }

    public void showVista() {
        frameVista.pack();
        frameVista.setLocationRelativeTo(null);
        frameVista.setVisible(true);
    }

    private class ExitListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            frameVista.dispose();
        }
    }
}
