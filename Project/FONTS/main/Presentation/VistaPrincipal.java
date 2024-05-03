package main.Presentation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

public class VistaPrincipal {
    private CtrlPresentacio iCtrlPresentacio;
    private JFrame frameVistaPrincipal = new JFrame("Vista Principal");
    private JTabbedPane tabbedPane = new JTabbedPane();

    // Teclats tab components
    private JPanel panelTeclats = new JPanel();
    private JButton btnCrearTeclat = new JButton("Crear Teclat");
    private JButton btnMostrarInfoTeclat = new JButton("Veure Teclat");
    private JButton btnEliminarTeclat = new JButton("Eliminar Teclat");
    private DefaultListModel<String> teclatsListModel = new DefaultListModel<>();
    private JList<String> listTeclats = new JList<>(teclatsListModel);

    // Documents tab components
    private JPanel panelDocuments = new JPanel();
    private JButton btnCrearDocument = new JButton("Crear Document");
    private JButton btnMostrarInfoDocument = new JButton("Veure Document");
    private JButton btnEliminarDocument = new JButton("Eliminar Document");
    private DefaultListModel<String> documentsListModel = new DefaultListModel<>();
    private JList<String> listDocuments = new JList<>(documentsListModel);

    public VistaPrincipal(CtrlPresentacio iCtrlPresentacio, ArrayList<String> teclatsIds,
            ArrayList<String> documentsIds) {
        this.iCtrlPresentacio = iCtrlPresentacio;
        initComponents(teclatsIds, documentsIds);
        addWindowListener(); // Add the window listener
    }

    public void makeVisible() {
        frameVistaPrincipal.pack();
        frameVistaPrincipal.setMinimumSize(new Dimension(800, 600));
        frameVistaPrincipal.setLocationRelativeTo(null);
        frameVistaPrincipal.setVisible(true);
    }

    private void addWindowListener() {
        // Add a WindowListener to the frame to handle the window closing event
        frameVistaPrincipal.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // Call the tancarSessio function in CtrlPresentacio when the window is closing
                iCtrlPresentacio.tancarSessio();
            }
        });
    }

    public void tancarVista() {
        frameVistaPrincipal.setVisible(false);
    }

    private void initComponents(ArrayList<String> teclatsIds, ArrayList<String> documentsIds) {
        frameVistaPrincipal.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Teclats tab
        initTeclatsTab();

        // Documents tab
        initDocumentsTab();

        // Add tabs to the tabbedPane
        tabbedPane.addTab("Teclats", panelTeclats);
        tabbedPane.addTab("Documents", panelDocuments);

        // Initialize Teclats list with the obtained IDs
        for (String teclatId : teclatsIds) {
            teclatsListModel.addElement(teclatId);
        }

        // Initialize Documents list with the obtained IDs
        for (String documentId : documentsIds) {
            documentsListModel.addElement(documentId);
        }

        // Add tabbedPane to the contentPane
        frameVistaPrincipal.getContentPane().add(tabbedPane);

        customizeList(listTeclats);
        customizeList(listDocuments);
    }

    private void initTeclatsTab() {
        // Teclats tab layout
        panelTeclats.setLayout(new BorderLayout());

        // Create Teclat button action listener
        btnCrearTeclat.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Check if there are any documentIds
                if (documentsListModel.isEmpty()) {
                    JOptionPane.showMessageDialog(frameVistaPrincipal,
                            "Has de tenir algun document creat primer.", "Warning", JOptionPane.WARNING_MESSAGE);
                } else {
                    // Call a function to create a Teclat (implement as needed)
                    iCtrlPresentacio.showVistaCrearTeclat();
                }
            }
        });

        // Show Information button action listener for Teclats
        btnMostrarInfoTeclat.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Check if there is a selected value
                if (listTeclats.isSelectionEmpty()) {
                    // If no selection, show a dialog
                    JOptionPane.showMessageDialog(null, "Has de sel·leccionar un teclat", "Error",
                            JOptionPane.ERROR_MESSAGE);
                } else {
                    // Call a function to show information about the selected Teclat
                    iCtrlPresentacio.mostrarTeclat(listTeclats.getSelectedValue());
                }
            }
        });

        // Eliminar Teclat button action listener
        btnEliminarTeclat.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Check if there is a selected value
                if (listTeclats.isSelectionEmpty()) {
                    // Dialog de error
                    JOptionPane.showMessageDialog(frameVistaPrincipal,
                            "Has de sel·leccionar un teclat", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    String selectedTeclatId = listTeclats.getSelectedValue();

                    // Dialog de confirmació
                    int dialogResult = JOptionPane.showConfirmDialog(frameVistaPrincipal,
                            "Estàs segur que vols eliminar el teclat " + selectedTeclatId + "?",
                            "Confirmation", JOptionPane.YES_NO_OPTION);

                    if (dialogResult == JOptionPane.YES_OPTION) {
                        // Esborrar-ho del domini
                        iCtrlPresentacio.removeTeclat(selectedTeclatId);
                    }
                }
            }
        });

        // Create a panel for the buttons with FlowLayout
        JPanel buttonPanelTeclats = new JPanel(new FlowLayout());
        buttonPanelTeclats.add(btnCrearTeclat);
        buttonPanelTeclats.add(btnMostrarInfoTeclat);
        buttonPanelTeclats.add(btnEliminarTeclat);

        // Add Teclats components to the panel
        panelTeclats.add(buttonPanelTeclats, BorderLayout.NORTH);
        panelTeclats.add(new JScrollPane(listTeclats), BorderLayout.CENTER);
    }

    private void initDocumentsTab() {
        // Documents tab layout
        panelDocuments.setLayout(new BorderLayout());

        // Create Document button action listener
        btnCrearDocument.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Call a function to create a Document (implement as needed)
                iCtrlPresentacio.crearDocument();
            }
        });

        // Show Information button action listener for Documents
        btnMostrarInfoDocument.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Check if there is a selected value
                if (listDocuments.isSelectionEmpty()) {
                    // If no selection, show a dialog
                    JOptionPane.showMessageDialog(frameVistaPrincipal,
                            "Has de sel·leccionar un document", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    // Call a function to show information about the selected Document
                    iCtrlPresentacio.mostrarDocument(listDocuments.getSelectedValue());
                }
            }
        });

        // Remove Document button action listener
        btnEliminarDocument.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Check if there is a selected value
                if (listDocuments.isSelectionEmpty()) {
                    // Dialog de error
                    JOptionPane.showMessageDialog(frameVistaPrincipal,
                            "Has de sel·leccionar un document", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    String selectedDocumentId = listDocuments.getSelectedValue();

                    // Dialog de confirmació
                    int dialogResult = JOptionPane.showConfirmDialog(frameVistaPrincipal,
                            "Estàs segur que vols eliminar el document " + selectedDocumentId + "?",
                            "Confirmation", JOptionPane.YES_NO_OPTION);

                    if (dialogResult == JOptionPane.YES_OPTION) {
                        // Esborrar-ho del domini
                        iCtrlPresentacio.removeDocument(selectedDocumentId);
                    }
                }
            }
        });

        // Create a panel for the buttons with FlowLayout
        JPanel buttonPanelDocuments = new JPanel(new FlowLayout());
        buttonPanelDocuments.add(btnCrearDocument);
        buttonPanelDocuments.add(btnMostrarInfoDocument);
        buttonPanelDocuments.add(btnEliminarDocument);

        // Add Documents components to the panel
        panelDocuments.add(buttonPanelDocuments, BorderLayout.NORTH);
        panelDocuments.add(new JScrollPane(listDocuments), BorderLayout.CENTER);
    }

    private void customizeList(JList<String> list) {
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setLayoutOrientation(JList.VERTICAL);
        list.setVisibleRowCount(-1);
        list.setFixedCellHeight(40); // Set the height of each list item
        list.setFixedCellWidth(200); // Set the width of each list item

        // Set a custom cell renderer to center the text
        DefaultListCellRenderer renderer = new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                    boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected,
                        cellHasFocus);
                label.setHorizontalAlignment(JLabel.CENTER); // Set text alignment to center
                return label;
            }
        };

        list.setCellRenderer(renderer);

        // Add some space between elements
        list.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }
}
