package fr.student.views.popups;

import fr.student.algo.GrapheList;
import fr.student.algo.Sommet;
import fr.student.views.GrapheCanva;
import fr.student.views.SommetComponent;
import fr.student.views.modals.AddSommetModal;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class CanvaRightClickMenu extends JPopupMenu {
    GrapheCanva parent;
    JMenuItem addMenu;

    /**
     * Création du menu contextuel le canva
     * @param parent: Le parent du menu
     */
    public CanvaRightClickMenu(GrapheCanva parent, GrapheList graphe) {
        this.parent = parent;
        if(!parent.getInterface().getFiltred()){
            addMenu = new JMenuItem("Créer un sommet");
            addMenu.addActionListener(e -> {
                AddSommetModal addSommetModal = new AddSommetModal(graphe.getAllSommetName());
                addSommetModal.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowDeactivated(WindowEvent e) {
                        super.windowDeactivated(e);
                        if(addSommetModal.getNomSommet() == null || addSommetModal.getNomSommet().equals("")){
                            return;
                        }else{
                            graphe.addSommet(new Sommet(addSommetModal.getNomSommet(), addSommetModal.getTypeSommet()));
                            SommetComponent sommet = new SommetComponent(addSommetModal.getNomSommet(), (int) (Math.random()*parent.getWidth()), (int) (Math.random()*parent.getHeight()), parent.getSize(), parent);
                            parent.addSommet(sommet);
                            parent.refresh(null);
                        }
                    }
                });
            });
            add(addMenu);

            JMenuItem highlight = new JMenuItem("Highlight");
            highlight.addActionListener(e -> {
                String name = JOptionPane.showInputDialog(parent, "Choisissez un nom de sommet", "Choisissez un nom", JOptionPane.PLAIN_MESSAGE);
                for (SommetComponent sommet : parent.getSommets()) {
                    if(sommet.getNomSommet().equalsIgnoreCase(name)){
                        sommet.setColor(Color.RED);
                        return;
                    }
                }
            });
            add(highlight);
        }
    }


}
