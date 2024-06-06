package fr.student.views;

import fr.student.views.popups.CanvaRightClickMenu;

import javax.security.auth.login.AccountExpiredException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

public class GrapheCanva extends JPanel implements MouseListener  {
    ArrayList<SommetComponent> sommets;
    ArrayList<LienComponent> liens;
    Interface parent;
    CanvaRightClickMenu menuRightClick;
    ArrayList<SommetComponent> ctrlPressedSommets;

    public GrapheCanva(Interface parent) {
        this.setLayout(null);
        this.sommets = new ArrayList<>();
        this.liens = new ArrayList<>();
        this.parent = parent;

        addMouseListener(this);
    }

    public void paintComponent(java.awt.Graphics g) {
        super.paintComponent(g);

    }

    /**
     * Ajoute un sommet au graphe
     * @param sommet: Le sommet à ajouter
     */
    public void addSommet(SommetComponent sommet){

        add(sommet);
        this.sommets.add(sommet);
    }

    /**
     * Retourne la liste des sommets du graphe
     * @return ArrayList<SommetComponent>: La liste des sommets du graphe
     */
    public ArrayList<SommetComponent> getSommets() {

        return sommets;
    }

    /**
     * Retourne le sommet du graphe qui a le nom passé en paramètre
     * @param name: Le nom du sommet à retourner
     * @return SommetComponent: Le sommet du graphe qui a le nom passé en paramètre
     */
    public SommetComponent getSommet(String name){

        for (SommetComponent sommet : sommets) {
            if (sommet.getNomSommet().equals(name)){
                return sommet;
            }
        }
        return null;
    }

    public ArrayList<LienComponent> getLiens() {
        return liens;
    }

    /**
     * Ajoute un lien au graphe
     * @param lien: Le lien à ajouter
     */
    public void addLien(LienComponent lien){

        lien.setBounds(lien.getX(), lien.getY(), lien.getWidth(), lien.getHeight());
        add(lien);
        this.liens.add(lien);
    }

    /**
     * Supprime un lien du graphe
     * @param lien: Le lien à supprimer
     */
    public void removeLien(LienComponent lien){

        this.liens.remove(lien);
        remove(lien);
        repaint();
    }

    /**
     * Supprime les liens qui contiennent le sommet passé en paramètre
     * @param removedSommet: Le nom du sommet à supprimer
     */
    public void removeLienOf(String removedSommet){

        // parcours les liens pour trouver les liens qui contiennent le sommet supprimé
        ArrayList<LienComponent> removableLiens = new ArrayList<>();
        for (LienComponent lien : liens) {
            // si le lien contient le sommet supprimé, on l'ajoute dans la liste des liens à supprimer
            if (lien.getsA().getNomSommet().equalsIgnoreCase(removedSommet) || lien.getsB().getNomSommet().equalsIgnoreCase(removedSommet)){
                removableLiens.add(lien);
            }
        }
        // supprimer les liens dans la liste des liens à supprimer
        for (LienComponent lien : removableLiens) {
            removeLien(lien);
        }
    }

    /**
     * Supprime un sommet du graphe
     * @param sommet: Le nom du sommet à supprimer
     */
    public void removeSommet(String sommet){

        // parcours les sommets pour le trouver
        for (SommetComponent s : sommets) {
            if (s.getNomSommet().equalsIgnoreCase(sommet)){
                this.sommets.remove(s); // supprimer le sommet dans la liste local
                removeLienOf(sommet);
                remove(s);
                parent.getGraphe().supprimerSommet(sommet); // supprimer le sommet dans le graphe
                break;
            }
        }
        parent.clickingCanva(sommets, liens); // refresh l'interface pour supprimer une eventuelle mauvaise selection
        repaint();
    }

    /**
     * Prévient d'un déplacement d'un sommet
     * @param sommet: Le sommet qui a été déplacé
     */
    public void movingSommet(SommetComponent sommet){

        repaint();
    }

    /**
     * Prévient d'un clic sur un sommet
     * @param sommet: Le sommet qui a été cliqué
     */
    public void clickingSommet(String sommet){

        parent.clickingSommetInterface(sommet, sommets, liens);

    }

    /**
     * Rafraichit l'interface
     * @param sommet: Le sommet qui a été cliqué
     */
    public void refresh(String sommet){
        this.ctrlPressedSommets = null;
        if(sommet != null){
            parent.clickingSommetInterface(sommet, sommets, liens);
        }else{
            parent.clickingCanva(sommets, liens);
        }
        repaint();
    }

    public void setCtrlPressedSommet(SommetComponent sommet) {
        if(this.ctrlPressedSommets == null){
            // ajout du sommet si la liste est vide et set couleur
            this.ctrlPressedSommets = new ArrayList<>();
            this.ctrlPressedSommets.add(sommet);
            sommet.setColor(Color.RED);
        }else{
            if(!this.ctrlPressedSommets.contains(sommet)){
                // si la liste ne contient pas le sommet
                // si il n'y a pas deja 2 sommet dans la liste
                if(this.ctrlPressedSommets.size() < 2){
                    this.ctrlPressedSommets.add(sommet);
                    sommet.setColor(Color.RED);
                }else{
                    SommetComponent unselectedSommet = this.ctrlPressedSommets.get(0);
                    // on supprime le premier sommet de la liste
                    this.ctrlPressedSommets.remove(0);
                    // on remplace le sommet par le nouveau
                    this.ctrlPressedSommets.add(sommet);
                    // on remet la couleur par default au sommet supprimé
                    if (parent.getTheme().equalsIgnoreCase("light")){
                        unselectedSommet.setColor(unselectedSommet.defaultLightModeColor);
                    }else{
                        unselectedSommet.setColor(unselectedSommet.defaultDarkModeColor);
                    }
                    // on met la couleur rouge au nouveau sommet
                    sommet.setColor(Color.RED);
                }
            }else{
                // si la liste contient le sommet
                this.ctrlPressedSommets.remove(sommet);
                // couleur par default en fonction du theme
                if (parent.getTheme().equalsIgnoreCase("light")){
                    sommet.setColor(sommet.defaultLightModeColor);
                }else{
                    sommet.setColor(sommet.defaultDarkModeColor);
                }
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        // right click
        if (SwingUtilities.isRightMouseButton(e)) {
            this.menuRightClick = new CanvaRightClickMenu(this, parent.getGraphe());
            this.menuRightClick.show(e.getComponent(), e.getX(), e.getY());


        } else {
            // left click
            this.ctrlPressedSommets = null;
            parent.clickingCanva(sommets, liens);

        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }
    @Override
    public void mouseExited(MouseEvent e) {

    }

    public Interface getInterface() {
        return parent;
    }

    public ArrayList<SommetComponent> getCtrlPressedSommets() {
        return ctrlPressedSommets;
    }
    public ArrayList<LienComponent> getLien(SommetComponent sA, SommetComponent sB){
        // parcours les liens pour trouver les liens à supprimer
        ArrayList<LienComponent> delLien = new ArrayList<>();
        for (LienComponent lien : liens) {
            if(lien.getsA().getNomSommet().equalsIgnoreCase(sA.getNomSommet()) && lien.getsB().getNomSommet().equalsIgnoreCase(sB.getNomSommet())){
                delLien.add(lien);
            }else if(lien.getsA().getNomSommet().equalsIgnoreCase(sB.getNomSommet()) && lien.getsB().getNomSommet().equalsIgnoreCase(sA.getNomSommet())){
                delLien.add(lien);
            }
        }
        return delLien;
    }
    public LienComponent getLienByName(String nom){
        for (LienComponent lien : liens) {
            if(lien.getNom().equalsIgnoreCase(nom)){
                return lien;
            }
        }
        return null;
    }

    public SommetComponent returnSommetComponent(String nom){
        for (SommetComponent sommet : sommets) {
            if(sommet.getNomSommet().equalsIgnoreCase(nom)){
                return sommet;
            }
        }
        return null;
    }
}
