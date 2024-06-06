package fr.student.views.modals;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class AddSommetModal extends JFrame {
    private static String[] TYPE = {"O", "M", "N"};
    private String nomSommet;
    private String typeSommet;
    private JLabel errLabel;
    private ArrayList<String> sommets;


    public AddSommetModal(ArrayList<String> sommets){
        super("Ajouter un sommet");
        this.sommets = sommets;
        this.setPreferredSize(new java.awt.Dimension(300, 450));
        this.setSize(300, 400);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(this.getParent());
        this.setResizable(false);
        this.setAlwaysOnTop(true);
        this.setVisible(true);
        this.nomSommet = null;
        this.typeSommet = null;
        this.initComponents();
    }

    public void initComponents(){
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        JLabel title = new JLabel("Ajouter un sommet");
        title.setHorizontalAlignment(JLabel.CENTER);
        title.setFont(new Font(title.getFont().getName(), Font.PLAIN, 20)); // changer la taille de la police

        JPanel content = new JPanel();
        content.setLayout(new GridLayout(5, 1, 10, 10));
        JLabel nomLabel = new JLabel("Nom du sommet");
        nomLabel.setHorizontalAlignment(JLabel.CENTER);
        JTextField nomField = new JTextField();
        JLabel typeLabel = new JLabel("Type du sommet");
        typeLabel.setHorizontalAlignment(JLabel.CENTER);
        JComboBox<String> typeBox = new JComboBox<>(TYPE);
        JButton valider = new JButton("Valider");
        this.errLabel = new JLabel();
        this.errLabel.setFont(new Font(title.getFont().getName(), Font.PLAIN, 15)); // changer la taille de la police
        this.errLabel.setForeground(Color.RED);

        valider.addActionListener(e -> {
            if (nomField.getText().equals("") || typeBox.getSelectedItem().equals("") || typeBox.getSelectedItem() == null){
                this.errLabel.setText("Veuillez remplir tous les champs");
                return;
            }
            else if (nomField.getText().contains(" ")){
                this.errLabel.setText("Le nom du sommet ne doit pas contenir d'espace");
                return;
            } else if (this.sommets.contains(nomField.getText())){
                this.errLabel.setText("Un sommet avec ce nom existe déjà");
                return;
            }else{
                this.nomSommet = nomField.getText();
                this.typeSommet = (String) typeBox.getSelectedItem();
                this.dispose();
            }
        });
        content.add(nomLabel);
        content.add(nomField);
        content.add(typeLabel);
        content.add(typeBox);
        content.add(valider);
        panel.add(title, BorderLayout.NORTH);
        panel.add(content, BorderLayout.CENTER);
        panel.add(this.errLabel, BorderLayout.SOUTH);
        panel.setBorder( BorderFactory.createEmptyBorder(10,30,10, 30) );
        this.add(panel);
    }

    public String getNomSommet() {
        return nomSommet;
    }

    public String getTypeSommet() {
        return typeSommet;
    }
}
