package fr.student.views.modals;

import javax.swing.*;
import java.awt.*;

public class AddLienModal extends JFrame {
    private Double fiabilite;
    private Double distance;
    private Integer duree;
    private String s1;
    private String s2;
    public AddLienModal(String s1, String s2, String title) {
        super(title);
        this.s1 = s1;
        this.s2 = s2;
        this.setSize(300, 300);
        this.setPreferredSize(new Dimension(300, 300));
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(this.getParent());
        this.setResizable(false);
        this.setAlwaysOnTop(true);
        this.setVisible(true);
        this.initComponent();
    }

    private void initComponent() {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel title = new JLabel("Lien entre " + s1 + " et " + s2 );
        title.setHorizontalAlignment(JLabel.CENTER);
        title.setFont(new Font(title.getFont().getName(), Font.PLAIN, 20)); // changer la taille de la police
        panel.add(title, BorderLayout.NORTH);

        JPanel content = new JPanel(new GridLayout(4, 1));

        JPanel fiabilitePanel = new JPanel(new BorderLayout());
        JLabel fiabiliteLabel = new JLabel("Fiabilité du lien");
        JTextField fiabiliteField = new JTextField();
        fiabilitePanel.add(fiabiliteLabel, BorderLayout.NORTH);
        fiabilitePanel.add(fiabiliteField, BorderLayout.CENTER);
        content.add(fiabilitePanel);

        JPanel distancePanel = new JPanel(new BorderLayout());
        JLabel distanceLabel = new JLabel("Distance du lien");
        JTextField distanceField = new JTextField();
        distancePanel.add(distanceLabel, BorderLayout.NORTH);
        distancePanel.add(distanceField, BorderLayout.CENTER);
        content.add(distancePanel);

        JPanel dureePanel = new JPanel(new BorderLayout());
        JLabel dureeLabel = new JLabel("Durée du lien");
        JTextField dureeField = new JTextField();
        dureePanel.add(dureeLabel, BorderLayout.NORTH);
        dureePanel.add(dureeField, BorderLayout.CENTER);
        content.add(dureePanel);

        JButton valider = new JButton("Valider");
        valider.addActionListener(e -> {
            this.fiabilite = Double.parseDouble(fiabiliteField.getText());
            this.distance = Double.parseDouble(distanceField.getText());
            this.duree = Integer.parseInt(dureeField.getText());
            this.dispose();
        });
        content.add(valider);

        panel.add(content, BorderLayout.CENTER);

        panel.setBorder( BorderFactory.createEmptyBorder(10,30,10, 30) );
        this.add(panel);
    }

    public Double getDistance() {
        return distance;
    }

    public Double getFiabilite() {
        return fiabilite;
    }

    public Integer getDuree() {
        return duree;
    }
}
