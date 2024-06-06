package fr.student.views.modals;

import fr.student.algo.GrapheList;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ComparaisonSommetModal extends JFrame {
    private String s1;
    private String s2;
    private GrapheList graphe;
    private Integer distance = 1;
    private JTable tableS1;
    private JTable tableS2;
    private JLabel bottomLabel;
    private JLabel bottomLabel2;
    private JLabel titleLabel;

    // penser à rendre le code propre (function, implement)
    public ComparaisonSommetModal(String s1, String s2, GrapheList graphe){
        setTitle("Comparaison de " + s1 + " et " + s2);
        this.setPreferredSize(new java.awt.Dimension(700, 500));
        this.setSize(700, 500);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(this.getParent());
        this.setResizable(false);
        this.setAlwaysOnTop(true);
        this.s1 = s1;
        this.s2 = s2;
        this.graphe = graphe;
        this.initComponents();
        this.setVisible(true);
    }

    private void initComponents() {
        this.distance = 1;
        Map<Integer, List<String>> voisins1 = graphe.getVoisinPdistance(s1, this.distance-1);
        Map<Integer, List<String>> voisins2 = graphe.getVoisinPdistance(s2, this.distance-1);

        // Nom sommet -> [Type voisin, liste des voisins]
        Map<String, Map<String, ArrayList<String>>> voisinsSorted = new HashMap<>();

        // creation des voisins de s1
        voisinsSorted.put(s1, new HashMap<>());
        voisinsSorted.get(s1).put("M", new ArrayList<>());
        voisinsSorted.get(s1).put("O", new ArrayList<>());
        voisinsSorted.get(s1).put("N", new ArrayList<>());
        // on boucle sur les voisins a p distance de s1 pour savoir s'ils sont M O ou N
        for(String sommet : voisins1.get(distance)) {
            if(graphe.returnSommet(sommet).getType().equalsIgnoreCase("M")){
                voisinsSorted.get(s1).get("M").add(sommet);
            } else if(graphe.returnSommet(sommet).getType().equalsIgnoreCase("O")){
                voisinsSorted.get(s1).get("O").add(sommet);
            } else if(graphe.returnSommet(sommet).getType().equalsIgnoreCase("N")){
                voisinsSorted.get(s1).get("N").add(sommet);
            }
        }

        voisinsSorted.put(s2, new HashMap<>());
        voisinsSorted.get(s2).put("M", new ArrayList<>());
        voisinsSorted.get(s2).put("O", new ArrayList<>());
        voisinsSorted.get(s2).put("N", new ArrayList<>());
        for(String sommet : voisins2.get(distance)) {
            if(graphe.returnSommet(sommet).getType().equalsIgnoreCase("M")){
                voisinsSorted.get(s2).get("M").add(sommet);
            } else if(graphe.returnSommet(sommet).getType().equalsIgnoreCase("O")){
                voisinsSorted.get(s2).get("O").add(sommet);
            } else if(graphe.returnSommet(sommet).getType().equalsIgnoreCase("N")){
                voisinsSorted.get(s2).get("N").add(sommet);
            }
        }


        // Interface
        JPanel panel = new JPanel(new BorderLayout());

        // haut
        JPanel top = new JPanel(new GridLayout(2, 1));
        JPanel title = new JPanel(new FlowLayout(FlowLayout.CENTER));
        this.titleLabel = new JLabel("Comparaison de " + s1 + " et " + s2 + " à " + distance + " distance");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        title.add(titleLabel);
        JPanel textFieldContainer = new JPanel();
        textFieldContainer.setLayout(new FlowLayout(FlowLayout.CENTER));
        JTextField textField = new JTextField();
        textField.setPreferredSize(new Dimension(50, 30));
        textField.setText(distance.toString());
        textFieldContainer.add(textField);
        top.add(title);
        top.add(textFieldContainer);
        panel.add(top, BorderLayout.NORTH);

        // content
        JPanel content = new JPanel(new GridLayout(1, 2));
        JPanel contentLeft = new JPanel(new BorderLayout());
        JPanel contentRight = new JPanel(new BorderLayout());
        JLabel labelS1 = new JLabel(s1);
        JLabel labelS2 = new JLabel(s2);
        labelS1.setFont(new Font("Arial", Font.BOLD, 16));
        labelS2.setFont(new Font("Arial", Font.BOLD, 16));
        contentLeft.add(labelS1, BorderLayout.NORTH);
        contentRight.add(labelS2, BorderLayout.NORTH);
        String[] entetes = {"M", "O", "N"};
        Integer maxLen = 0;
        for(String type : voisinsSorted.get(s1).keySet()) {
            if(voisinsSorted.get(s1).get(type).size() > maxLen) {
                maxLen = voisinsSorted.get(s1).get(type).size();
            }
        }

        Integer maxLen2 = 0;
        for(String type : voisinsSorted.get(s2).keySet()) {
            if(voisinsSorted.get(s2).get(type).size() > maxLen2) {
                maxLen2 = voisinsSorted.get(s2).get(type).size();
            }
        }
        String[][] data = new String[maxLen][3];
        for(int i = 0; i < maxLen; i++) {
            for(int j = 0; j < 3; j++) {
                if(voisinsSorted.get(s1).get(entetes[j]).size() > i) {
                    data[i][j] = voisinsSorted.get(s1).get(entetes[j]).get(i);
                } else {
                    data[i][j] = "";
                }
            }
        }
        String[][] data2 = new String[maxLen2][3];
        for(int i = 0; i < maxLen2; i++) {
            for(int j = 0; j < 3; j++) {
                if(voisinsSorted.get(s2).get(entetes[j]).size() > i) {
                    data2[i][j] = voisinsSorted.get(s2).get(entetes[j]).get(i);
                } else {
                    data2[i][j] = "";
                }
            }
        }

        tableS1 = new JTable(data, entetes);
        tableS2 = new JTable(data2, entetes);
        JScrollPane scrollPaneS1 = new JScrollPane(tableS1);
        JScrollPane scrollPaneS2 = new JScrollPane(tableS2);
        contentLeft.add(scrollPaneS1, BorderLayout.CENTER);
        contentRight.add(scrollPaneS2, BorderLayout.CENTER);


        // bas
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER));
        this.bottomLabel = new JLabel(s1 + " a " + voisinsSorted.get(s1).get("M").size() + " voisins M, " + voisinsSorted.get(s1).get("O").size() + " voisins O et " + voisinsSorted.get(s1).get("N").size() + " voisins N");
        bottom.add(this.bottomLabel);
        contentLeft.add(bottom, BorderLayout.SOUTH);
        JPanel bottom2 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        this.bottomLabel2 = new JLabel(s2 + " a " + voisinsSorted.get(s2).get("M").size() + " voisins M, " + voisinsSorted.get(s2).get("O").size() + " voisins O et " + voisinsSorted.get(s2).get("N").size() + " voisins N");
        bottom2.add(this.bottomLabel2);
        contentRight.add(bottom2, BorderLayout.SOUTH);

        content.add(contentLeft);
        content.add(contentRight);
        panel.add(content, BorderLayout.CENTER);

        panel.setBorder( BorderFactory.createEmptyBorder(10,30,10, 30) );
        this.add(panel);

        // changement valeur distance
        textField.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                String text = textField.getText();
                if (text.matches("[2-9]+")) {
                    Integer newDistance = Integer.parseInt(text);
                    if (newDistance > 1) {
                        distance = newDistance;
                        Map<Integer, List<String>> newVoisin1 = graphe.getVoisinPdistance(s1, distance-1);
                        Map<Integer, List<String>> newVoisin2 = graphe.getVoisinPdistance(s2, distance-1);
                        voisinsSorted.put(s1, new HashMap<>());
                        voisinsSorted.get(s1).put("M", new ArrayList<>());
                        voisinsSorted.get(s1).put("O", new ArrayList<>());
                        voisinsSorted.get(s1).put("N", new ArrayList<>());
                        for (String sommet : newVoisin1.get(newVoisin1.size()-1)) {
                            if (graphe.returnSommet(sommet).getType().equalsIgnoreCase("M")) {
                                voisinsSorted.get(s1).get("M").add(sommet);
                            } else if (graphe.returnSommet(sommet).getType().equalsIgnoreCase("O")) {
                                voisinsSorted.get(s1).get("O").add(sommet);
                            } else if (graphe.returnSommet(sommet).getType().equalsIgnoreCase("N")) {
                                voisinsSorted.get(s1).get("N").add(sommet);
                            }
                        }

                        voisinsSorted.put(s2, new HashMap<>());
                        voisinsSorted.get(s2).put("M", new ArrayList<>());
                        voisinsSorted.get(s2).put("O", new ArrayList<>());
                        voisinsSorted.get(s2).put("N", new ArrayList<>());
                        for (String sommet : newVoisin2.get(newVoisin2.size()-1)) {
                            if (graphe.returnSommet(sommet).getType().equalsIgnoreCase("M")) {
                                voisinsSorted.get(s2).get("M").add(sommet);
                            } else if (graphe.returnSommet(sommet).getType().equalsIgnoreCase("O")) {
                                voisinsSorted.get(s2).get("O").add(sommet);
                            } else if (graphe.returnSommet(sommet).getType().equalsIgnoreCase("N")) {
                                voisinsSorted.get(s2).get("N").add(sommet);
                            }
                        }

                        String[] entetes = {"M", "O", "N"};
                        Integer maxLen = 0;
                        for (String type : voisinsSorted.get(s1).keySet()) {
                            if (voisinsSorted.get(s1).get(type).size() > maxLen) {
                                maxLen = voisinsSorted.get(s1).get(type).size();
                            }
                        }
                        String[][] data = new String[maxLen][3];
                        for (int i = 0; i < maxLen; i++) {
                            for (int j = 0; j < 3; j++) {
                                if (voisinsSorted.get(s1).get(entetes[j]).size() > i) {
                                    data[i][j] = voisinsSorted.get(s1).get(entetes[j]).get(i);
                                } else {
                                    data[i][j] = "";
                                }
                            }
                        }
                        String[][] data2 = new String[maxLen][3];
                        for (int i = 0; i < maxLen; i++) {
                            for (int j = 0; j < 3; j++) {
                                if (voisinsSorted.get(s2).get(entetes[j]).size() > i) {
                                    data2[i][j] = voisinsSorted.get(s2).get(entetes[j]).get(i);
                                } else {
                                    data2[i][j] = "";
                                }
                            }
                        }
                        titleLabel.setText("Comparaison de " + s1 + " et " + s2 + " à " + distance + " distance");
                        tableS1.setModel(new DefaultTableModel(data, entetes));
                        tableS2.setModel(new DefaultTableModel(data2, entetes));
                        bottomLabel.setText(s1 + " a " + voisinsSorted.get(s1).get("M").size() + " voisins M, " + voisinsSorted.get(s1).get("O").size() + " voisins O et " + voisinsSorted.get(s1).get("N").size() + " voisins N");
                        bottomLabel2.setText(s2 + " a " + voisinsSorted.get(s2).get("M").size() + " voisins M, " + voisinsSorted.get(s2).get("O").size() + " voisins O et " + voisinsSorted.get(s2).get("N").size() + " voisins N");
                        pack();
                    }
                }
            }
        });
    }
}
