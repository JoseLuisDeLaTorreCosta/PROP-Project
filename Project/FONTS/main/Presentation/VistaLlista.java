package main.Presentation;

import java.util.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

public class VistaLlista {
    CtrlPresentacio iCP;
    private JFrame frameVista;
    private String nomLlista;
    private ArrayList<String> llista;
    private ArrayList<Integer> frequencies;
    private JPanel mainPanel;

    private void initComponents() {
        frameVista = new JFrame(nomLlista);
        frameVista.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frameVista.setMinimumSize(new Dimension(800, 600));

        int files = llista.size();
        int columnes = 2;
        JPanel llistaPanel = new JPanel(new GridLayout(files, columnes));

        Border blackline = BorderFactory.createLineBorder(Color.black);
        Border emptyBorder = BorderFactory.createEmptyBorder(30, 30, 30, 30);

        for (int i = 0; i < files; ++i) {
            JLabel paraula = new JLabel(llista.get(i));
            paraula.setHorizontalAlignment(SwingConstants.CENTER);
            paraula.setVerticalAlignment((SwingConstants.CENTER));
            paraula.setBorder(blackline);
            llistaPanel.add(paraula);
            JLabel freq = new JLabel(String.valueOf(frequencies.get(i)));
            freq.setHorizontalAlignment(SwingConstants.CENTER);
            freq.setVerticalAlignment((SwingConstants.CENTER));
            freq.setBorder(blackline);
            llistaPanel.add(freq);
        }

        JButton exitButton = new JButton("Sortir");
        exitButton.addActionListener(new ExitListener());
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBorder(emptyBorder);
        buttonPanel.add(exitButton);

        mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(llistaPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        frameVista.getContentPane().add(mainPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
    }

    public VistaLlista(CtrlPresentacio pCP, String nomLlista, ArrayList<String> llista, ArrayList<Integer> frequencies) {
        iCP = pCP;
        this.nomLlista = nomLlista;
        this.llista = llista;
        this.frequencies = frequencies;
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
