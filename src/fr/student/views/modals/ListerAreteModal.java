package fr.student.views.modals;

import fr.student.algo.LienMaillon;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Map;

public class ListerAreteModal extends JFrame {
    private Map<String, ArrayList<LienMaillon>> aretes;
    public ListerAreteModal(Map<String, ArrayList<LienMaillon>> aretes){
        this.aretes = aretes;
        setTitle("Liste des arêtes");
        this.setPreferredSize(new java.awt.Dimension(500, 450));
        this.setSize(500, 450);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(this.getParent());
        this.setResizable(false);
        this.setAlwaysOnTop(true);
        this.initComponents();
        this.setVisible(true);
    }

    private void initComponents() {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel title = new JLabel("Liste des aretes");
        title.setFont(new java.awt.Font(title.getFont().getName(), java.awt.Font.PLAIN, 20)); // changer la taille de la police
        title.setHorizontalAlignment(JLabel.CENTER);
        panel.add(title, BorderLayout.NORTH);

        String[] entetes = {"Sommet Depart", "Sommet arrive", "Fiabilite", "Distance", "Duree"};
        ArrayList<String> liensCounter = new ArrayList<>();
        for (Map.Entry<String, ArrayList<LienMaillon>> entry : aretes.entrySet()) {
            for(LienMaillon lien : entry.getValue()){
                liensCounter.add(entry.getKey());
            }
        }
        String[][] data = new String[liensCounter.size()][5];
        int i = 0;
        for (Map.Entry<String, ArrayList<LienMaillon>> entry : aretes.entrySet()) {
            for(LienMaillon lien : entry.getValue()){
                data[i][0] = entry.getKey();
                data[i][1] = lien.getBatiment().getNom();
                data[i][2] = String.valueOf(lien.getFiabilite());
                data[i][3] = String.valueOf(lien.getDistance());
                data[i][4] = String.valueOf(lien.getDuree());
                i++;
            }
        }

        JTable table = new JTable(data, entetes);
        table.setEnabled(false);
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(new JLabel(liensCounter.size() / 2 + " liens"), BorderLayout.SOUTH);
        panel.setBorder( BorderFactory.createEmptyBorder(10,30,10, 30) );
        this.add(panel);
    }
}
