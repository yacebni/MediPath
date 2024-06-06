package fr.student.views.modals;

import fr.student.algo.Sommet;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ListerSommetModal extends JFrame {
    private ArrayList<Sommet> sommets;
    private String type;
    public ListerSommetModal(ArrayList<Sommet> sommets, String type){
        this.sommets = new ArrayList<>(sommets);
        this.type = type;
        setTitle("Liste des sommets");
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
        JLabel title = new JLabel("Liste des sommets " + type);
        title.setFont(new java.awt.Font(title.getFont().getName(), java.awt.Font.PLAIN, 20)); // changer la taille de la police
        title.setHorizontalAlignment(JLabel.CENTER);
        panel.add(title, BorderLayout.NORTH);

        String[] entetes = {"Nom", "Type"};
        HashMap<String, String> dataMap = new HashMap<>();
        if(this.type.equalsIgnoreCase("O")){
            for(Sommet s : sommets){
                if(s.getType().equalsIgnoreCase("O")){
                    dataMap.put(s.getNom(), s.getType());
                }
            }
        }else if(this.type.equalsIgnoreCase("M")){
            for(Sommet s : sommets){
                if(s.getType().equalsIgnoreCase("M")){
                    dataMap.put(s.getNom(), s.getType());
                }
            }
        }else {
            for(Sommet s : sommets){
                if(s.getType().equalsIgnoreCase("N")){
                    dataMap.put(s.getNom(), s.getType());
                }
            }
        }

        String[][] data = new String[dataMap.size()][2];
        int i = 0;
        for(Map.Entry<String, String> entry : dataMap.entrySet()){
            data[i][0] = entry.getKey();
            data[i][1] = entry.getValue();
            i++;
        }
        JTable table = new JTable(data, entetes);
        table.setEnabled(false);
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(new JLabel(dataMap.size() + " sommets"), BorderLayout.SOUTH);
        panel.setBorder( BorderFactory.createEmptyBorder(10,30,10, 30) );
        this.add(panel);


    }

}
