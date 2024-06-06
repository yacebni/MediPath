package fr.student.views.modals;

import fr.student.algo.GrapheList;
import fr.student.algo.Sommet;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class InfoLienModal extends JFrame {
    private GrapheList graphe;
    private Sommet s1;
    private Sommet s2;

    public InfoLienModal(GrapheList graphe, String s1, String s2){
        super("Information sur un lien");
        this.setPreferredSize(new java.awt.Dimension(300, 250));
        this.setSize(300, 250);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(this.getParent());
        this.setResizable(false);
        this.setAlwaysOnTop(true);
        this.graphe = graphe;
        this.s1 = graphe.returnSommet(s1);
        this.s2 = graphe.returnSommet(s2);
        this.initComponents();
    }

    private void initComponents(){
        if(s1.getLien(s2.getNom()) == null){
            JOptionPane.showMessageDialog(null, "Ce lien n'existe pas", "Erreur", JOptionPane.ERROR_MESSAGE);
            this.dispose();
            return;
        }
        Double fiab = s1.getLien(s2.getNom()).getFiabilite();
        Double distance = s1.getLien(s2.getNom()).getDistance();
        Integer duree = s1.getLien(s2.getNom()).getDuree();
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel title = new JLabel("Lien " + s1.getNom() + " - " + s2.getNom());
        title.setFont(new java.awt.Font(title.getFont().getName(), java.awt.Font.PLAIN, 20)); // changer la taille de la police
        titlePanel.add(title);
        panel.add(titlePanel, BorderLayout.NORTH);
        JPanel content = new JPanel(new GridLayout(3, 1));
        JLabel fiabLabel = new JLabel("Fiabilité : " + fiab*10 + "%");
        fiabLabel.setHorizontalAlignment(JLabel.CENTER);
        fiabLabel.setFont(new java.awt.Font(fiabLabel.getFont().getName(), java.awt.Font.PLAIN, 15)); // changer la taille de la police
        content.add(fiabLabel);
        JLabel distanceLabel = new JLabel("Distance : " + distance + " km");
        distanceLabel.setHorizontalAlignment(JLabel.CENTER);
        distanceLabel.setFont(new java.awt.Font(distanceLabel.getFont().getName(), java.awt.Font.PLAIN, 15)); // changer la taille de la police
        content.add(distanceLabel);
        JLabel dureeLabel = new JLabel("Durée : " + duree + " minutes");
        dureeLabel.setHorizontalAlignment(JLabel.CENTER);
        dureeLabel.setFont(new java.awt.Font(dureeLabel.getFont().getName(), java.awt.Font.PLAIN, 15)); // changer la taille de la police
        content.add(dureeLabel);
        panel.setBorder( BorderFactory.createEmptyBorder(10,30,10, 30) );
        panel.add(content, BorderLayout.CENTER);
        this.add(panel);
        this.setVisible(true);
    }
}
