package fr.student.views.modals;

import fr.student.algo.Sommet;

import javax.swing.*;
import java.awt.*;

public class InfoSommetModal extends JFrame {
    private Sommet sommet;
    public InfoSommetModal(Sommet sommet){
        this.sommet = sommet;
        setTitle("Info Sommet");
        this.setPreferredSize(new java.awt.Dimension(300, 250));
        this.setSize(300, 250);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(this.getParent());
        this.setResizable(false);
        this.setAlwaysOnTop(true);
        this.initComponents();
        this.setVisible(true);
    }

    private void initComponents() {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel title = new JLabel("Sommet " + sommet.getNom());
        title.setFont(new java.awt.Font(title.getFont().getName(), java.awt.Font.PLAIN, 20)); // changer la taille de la police
        title.setHorizontalAlignment(JLabel.CENTER);
        panel.add(title, BorderLayout.NORTH);
        JPanel content = new JPanel(new GridLayout(1, 1));
        JLabel typeLabel = new JLabel("Type : " + sommet.getType());
        typeLabel.setHorizontalAlignment(JLabel.CENTER);
        typeLabel.setFont(new java.awt.Font(typeLabel.getFont().getName(), java.awt.Font.PLAIN, 17)); // changer la taille de la police
        content.add(typeLabel);
        panel.add(content, BorderLayout.CENTER);
        panel.setBorder( BorderFactory.createEmptyBorder(10,20,10, 20) );
        this.add(panel);
    }
}
