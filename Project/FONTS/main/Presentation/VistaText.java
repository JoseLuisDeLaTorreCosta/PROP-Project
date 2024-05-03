package main.Presentation;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

public class VistaText {
    CtrlPresentacio iCP;
    private JFrame frameVista;
    private String nomText;
    private String text;
    private JPanel mainPanel;

    private void initComponents() {
        frameVista = new JFrame(nomText);
        frameVista.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frameVista.setMinimumSize(new Dimension(800, 600));

        Border blackline = BorderFactory.createLineBorder(Color.black);
        Border emptyBorder = BorderFactory.createEmptyBorder(30, 30, 30, 30);

        JTextArea textArea = new JTextArea(30, 40);
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setText(text);
        textArea.setMinimumSize(new Dimension(500, 500));
        textArea.setBorder(blackline);
        JPanel textPanel = new JPanel();
        textPanel.add(textArea);

        JButton exitButton = new JButton("Sortir");
        exitButton.addActionListener(new ExitListener());
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBorder(emptyBorder);
        buttonPanel.add(exitButton);

        mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(textPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        frameVista.getContentPane().add(mainPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
    }

    public VistaText(CtrlPresentacio pCP, String nomText, String text) {
        iCP = pCP;
        this.nomText = nomText;
        this.text = text;
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
