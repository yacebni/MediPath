package fr.student.views.modals;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class CheminCustomModal extends JFrame {
    private Integer bloc = 1;
    private Integer blocRes;
    private Integer maternite = 1;
    private Integer materniteRes;
    private Integer nutrition = 1;
    private Integer nutritionRes;
    public CheminCustomModal(){
        setTitle("Chemin custom");
        this.setPreferredSize(new java.awt.Dimension(300, 400));
        this.setSize(300, 400);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(this.getParent());
        this.setResizable(false);
        this.setAlwaysOnTop(true);
        this.initComponents();
        this.setVisible(true);
    }

    private void initComponents() {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel title = new JLabel("Chemin custom");

        JPanel content = new JPanel(new GridLayout(4, 1));
        JPanel maternitePanel = new JPanel(new BorderLayout());
        JLabel materniteLabel = new JLabel("Maternité");
        JTextField textFieldMaternite = new JTextField();
        textFieldMaternite.setText("1");
        textFieldMaternite.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                String text = textFieldMaternite.getText();
                if (text.matches("[0-9]+")) {
                    maternite = Integer.valueOf(textFieldMaternite.getText());
                }
            };
        });
        maternitePanel.add(materniteLabel, BorderLayout.NORTH);
        maternitePanel.add(textFieldMaternite, BorderLayout.CENTER);
        content.add(maternitePanel);

        JPanel nutritionPanel = new JPanel(new BorderLayout());
        JLabel nutritionLabel = new JLabel("Centre nutrition");
        JTextField textFieldNutrition = new JTextField();
        textFieldNutrition.setText("1");
        textFieldNutrition.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                String text = textFieldNutrition.getText();
                if (text.matches("[0-9]+")) {
                    nutrition = Integer.valueOf(textFieldNutrition.getText());
                }
            };
        });
        nutritionPanel.add(nutritionLabel, BorderLayout.NORTH);
        nutritionPanel.add(textFieldNutrition, BorderLayout.CENTER);
        content.add(nutritionPanel);

        JPanel blocPanel = new JPanel(new BorderLayout());
        JLabel blocLabel = new JLabel("Bloc operatoire");
        JTextField textFieldBloc = new JTextField();
        textFieldBloc.setText("1");
        textFieldBloc.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                String text = textFieldBloc.getText();
                if (text.matches("[0-9]+")) {
                    bloc = Integer.valueOf(textFieldBloc.getText());
                }
            };
        });
        blocPanel.add(blocLabel, BorderLayout.NORTH);
        blocPanel.add(textFieldBloc, BorderLayout.CENTER);
        content.add(blocPanel);

        JButton valider = new JButton("Valider");
        content.add(valider);

        valider.addActionListener(e -> {
            this.blocRes = this.bloc;
            this.materniteRes = this.maternite;
            this.nutritionRes = this.nutrition;
            this.dispose();
        });

        panel.add(title, BorderLayout.NORTH);
        panel.add(content, BorderLayout.CENTER);
        panel.setBorder( BorderFactory.createEmptyBorder(10,30,10, 30) );
        add(panel);
    }

    public Integer getBloc() {
        return blocRes;
    }

    public Integer getMaternite() {
        return materniteRes;
    }

    public Integer getNutrition() {
        return nutritionRes;
    }
}
