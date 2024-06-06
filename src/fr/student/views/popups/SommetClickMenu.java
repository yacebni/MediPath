package fr.student.views.popups;

import fr.student.algo.GrapheList;
import fr.student.algo.LienMaillon;
import fr.student.algo.Sommet;
import fr.student.algo.Utils;
import fr.student.views.GrapheCanva;
import fr.student.views.LienComponent;
import fr.student.views.SommetComponent;
import fr.student.views.modals.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.concurrent.atomic.AtomicReference;

public class SommetClickMenu extends JPopupMenu {
    GrapheCanva parent;
    private String nom;
    JMenuItem remove;
    JMenuItem infoSommet;
    JMenuItem infoLien;
    JMenuItem removeLien;
    JMenuItem addLien;
    JMenuItem infoDistance;
    JMenuItem editSommet;
    JMenuItem editLien;
    private JMenuItem comparaison;

    private Integer nbNutri;
    private Integer nbBloc;
    private Integer nbMater;

    /**
     * Création du menu contextuel pour les sommets
     * @param nom: Le nom du sommet
     * @param parent: Le parent du menu
     */
    public SommetClickMenu(String nom, GrapheCanva parent) {

        this.nom = nom;
        this.parent = parent;
        initComponent();
    }
    public void initComponent() {
        remove = new JMenuItem("Supprimer");
        remove.addActionListener(e -> {
            parent.removeSommet(nom);
        });

        infoSommet = new JMenuItem("Info Sommet");
        infoSommet.addActionListener(e -> {
            GrapheList grapheList = parent.getInterface().getGraphe();
            Sommet sommet = grapheList.returnSommet(nom);

            InfoSommetModal infoSommetModal = new InfoSommetModal(sommet);
        });

        editSommet = new JMenuItem("Modifier type");
        editSommet.addActionListener(e -> {
            Object[] options = { "O", "M",
                    "N" };
            int result = JOptionPane.showOptionDialog(null, "Modifier le type de " + nom, "Enter a Number",
                    JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE,
                    null, options, null);
            if(result == JOptionPane.YES_OPTION){
                parent.getInterface().getGraphe().returnSommet(nom).setType("O");
            } else if (result == JOptionPane.NO_OPTION){
                parent.getInterface().getGraphe().returnSommet(nom).setType("M");
            } else {
                parent.getInterface().getGraphe().returnSommet(nom).setType("N");
            }
        });

        // si on a plusieurs sommets selectionnés + de choses
        if(parent.getCtrlPressedSommets() != null){

            if(parent.getCtrlPressedSommets().contains(parent.getSommet(nom))) {
                if(parent.getCtrlPressedSommets().size() > 1) {
                    // afficher les infos du lien
                    infoLien = new JMenuItem("Info Lien");
                    infoLien.addActionListener(e -> {
                        GrapheList grapheList = parent.getInterface().getGraphe();
                        String s1 = parent.getCtrlPressedSommets().get(0).getNomSommet();
                        String s2 = parent.getCtrlPressedSommets().get(1).getNomSommet();
                        InfoLienModal infoLienModal = new InfoLienModal(grapheList, s1, s2);
                    });
                    add(infoLien);

                    // remove / add lien
                    GrapheList grapheList = parent.getInterface().getGraphe();
                    SommetComponent sc1 = parent.getCtrlPressedSommets().get(0);
                    SommetComponent sc2 = parent.getCtrlPressedSommets().get(1);

                    // s'ils sont voisin, on peut supprimer le lien
                    if(grapheList.returnSommet(sc1.getNomSommet()).estVoisin(sc2.getNomSommet())) {
                        removeLien = new JMenuItem("Supprimer Lien");
                        removeLien.addActionListener(e -> {
                            String s1 = sc1.getNomSommet();
                            String s2 = sc2.getNomSommet();
                            grapheList.returnSommet(s1).supprimerLien(s2);
                            grapheList.returnSommet(s2).supprimerLien(s1);
                            ArrayList<LienComponent> liens = parent.getLien(parent.getCtrlPressedSommets().get(0), parent.getCtrlPressedSommets().get(1));
                            for(LienComponent lien : liens){
                                parent.removeLien(lien);
                            }
                            parent.refresh(null);
                        });

                        // edit lien
                        editLien = new JMenuItem("Modifier Lien");
                        editLien.addActionListener(e -> {
                            String s1 = sc1.getNomSommet();
                            String s2 = sc2.getNomSommet();

                            AddLienModal addLienModal = new AddLienModal(s1, s2, "Modifier Lien entre " + s1 + " et " + s2);

                            addLienModal.addWindowListener(new WindowAdapter() {
                                @Override
                                public void windowDeactivated(WindowEvent e) {
                                    super.windowDeactivated(e);
                                    Double fiabilite = addLienModal.getFiabilite();
                                    Double distance = addLienModal.getDistance();
                                    Integer duree = addLienModal.getDuree();

                                    if(nom != null && nom != "" && fiabilite != null && distance != null && duree != null){
                                        LienMaillon lien1to2 = grapheList.returnSommet(s1).getLien(s2);
                                        LienMaillon lien2to1 = grapheList.returnSommet(s2).getLien(s1);
                                        lien1to2.setDistance(distance);
                                        lien1to2.setFiabilite(fiabilite);
                                        lien1to2.setDuree(duree);
                                        lien1to2.setNomLien(distance + "KM");
                                        lien2to1.setDistance(distance);
                                        lien2to1.setFiabilite(fiabilite);
                                        lien2to1.setDuree(duree);
                                        lien2to1.setNomLien(distance + "KM");

                                        LienComponent lien1 = new LienComponent(sc1, sc2, distance + "KM");
                                        lien1.setBounds(0, 0, parent.getWidth()*2, parent.getHeight()*2);
                                        parent.addLien(lien1);
                                        LienComponent lien2 = new LienComponent(sc2, sc1, distance + "KM");
                                        lien1.setBounds(0, 0, parent.getWidth()*2, parent.getHeight()*2);
                                        parent.addLien(lien1);

                                        parent.addLien(lien2);
                                        parent.refresh(null);
                                    }
                                }
                            });
                        });

                        if(!parent.getInterface().getFiltred()){
                            add(editLien);
                            add(removeLien);
                        }
                    }else{
                        // s'ils sont pas voisin on peut ajouter un lien
                        addLien = new JMenuItem("Ajouter Lien");
                        addLien.addActionListener(e -> {
                            String s1 = sc1.getNomSommet();
                            String s2 = sc2.getNomSommet();

                            AddLienModal addLienModal = new AddLienModal(s1, s2, "Ajouter Lien entre " + s1 + " et " + s2);

                            addLienModal.addWindowListener(new WindowAdapter() {
                                @Override
                                public void windowDeactivated(WindowEvent e) {
                                    super.windowDeactivated(e);
                                    Double fiabilite = addLienModal.getFiabilite();
                                    Double distance = addLienModal.getDistance();
                                    Integer duree = addLienModal.getDuree();

                                    if(nom != null && nom != "" && fiabilite != null && distance != null && duree != null){
                                        grapheList.returnSommet(s1).ajoutLien(grapheList.returnSommet(s2), fiabilite, distance, duree, nom);
                                        grapheList.returnSommet(s2).ajoutLien(grapheList.returnSommet(s1), fiabilite, distance, duree, nom);

                                        LienComponent lien1 = new LienComponent(sc1, sc2, distance + "KM");
                                        lien1.setBounds(0, 0, parent.getWidth()*2, parent.getHeight()*2);
                                        parent.addLien(lien1);
                                        LienComponent lien2 = new LienComponent(sc2, sc1, distance + "KM");
                                        lien1.setBounds(0, 0, parent.getWidth()*2, parent.getHeight()*2);
                                        parent.addLien(lien1);

                                        parent.addLien(lien2);
                                        parent.refresh(null);
                                    }
                                }
                            });
                        });
                        if(!parent.getInterface().getFiltred()){
                            add(addLien);
                        }
                    }

                    infoDistance = new JMenuItem("Info 2 Distance");
                    infoDistance.addActionListener(e -> {
                        String s1 = parent.getCtrlPressedSommets().get(0).getNomSommet();
                        String s2 = parent.getCtrlPressedSommets().get(1).getNomSommet();
                        if(grapheList.getVoisinPdistance(s1, 2).get(2).contains(s2)){
                            JOptionPane.showMessageDialog(parent, "Les deux sommets sont à une distance de 2");
                        }else{
                            JOptionPane.showMessageDialog(parent, "Les deux sommets ne sont pas à une distance de 2");
                        }
                    });
                    add(infoDistance);

                    // djikstra & chemin passe partout
                    if(parent.getCtrlPressedSommets().size() == 2){
                        JMenu djikstra = new JMenu("Djikstra");
                        JMenuItem fiabilite = new JMenuItem("Fiabilite");
                        JMenuItem distance = new JMenuItem("Distance");
                        JMenuItem duree = new JMenuItem("Duree");
                        String s1 = parent.getCtrlPressedSommets().get(0).getNomSommet();
                        String s2 = parent.getCtrlPressedSommets().get(1).getNomSommet();
                        fiabilite.addActionListener(e -> {
                            for (LienComponent lien : parent.getLiens()){
                                if(parent.getInterface().getTheme().equalsIgnoreCase("light")){
                                    lien.setColor(lien.defaultLightModeColor);
                                }else{
                                    lien.setColor(lien.defaultDarkModeColor);
                                }
                            }
                            for (SommetComponent sommet : parent.getSommets()){
                                if(parent.getInterface().getTheme().equalsIgnoreCase("light")){
                                    sommet.setColor(sommet.defaultLightModeColor);
                                }else{
                                    sommet.setColor(sommet.defaultDarkModeColor);
                                }
                            }
                            // on recupere le chemin
                            HashMap<String, LinkedHashMap<String, Double>> chemin = parent.getInterface().getGraphe().tousLesPlusCourtCheminFiabilite(s1);
                            // string builder prcq le string de base marche pas jsp pk
                            AtomicReference<String> pastSommet = new AtomicReference<>(s1);
                            if(chemin.get(s2) != null){
                                // boucler sur tout les sommets pour tracer le chemin
                                chemin.get(s2).forEach((sommet, fiabValue) -> {
                                    // changer la couleur de tout les liens entre les sommets
                                    parent.getLien(parent.returnSommetComponent(pastSommet.get()), parent.returnSommetComponent(sommet)).forEach(lien -> {
                                        lien.setColor(Color.RED);
                                        lien.repaint();
                                        lien.getsA().setColor(Color.RED);

                                    });
                                    pastSommet.set(sommet);
                                });
                            }
                        });
                        distance.addActionListener(e -> {
                            for (LienComponent lien : parent.getLiens()){
                                if(parent.getInterface().getTheme().equalsIgnoreCase("light")){
                                    lien.setColor(lien.defaultLightModeColor);
                                }else{
                                    lien.setColor(lien.defaultDarkModeColor);
                                }
                            }
                            for (SommetComponent sommet : parent.getSommets()){
                                if(parent.getInterface().getTheme().equalsIgnoreCase("light")){
                                    sommet.setColor(sommet.defaultLightModeColor);
                                }else{
                                    sommet.setColor(sommet.defaultDarkModeColor);
                                }
                            }
                            HashMap<String, LinkedHashMap<String, Double>> chemin = parent.getInterface().getGraphe().tousLesPlusCourtCheminDistance(s1);
                            // string builder prcq le string de base marche pas jsp pk
                            AtomicReference<String> pastSommet = new AtomicReference<>(s1);
                            if(chemin.get(s2) != null){
                                chemin.get(s2).forEach((sommet, distValue) -> {
                                    // changer la couleur de tout les liens entre les sommets
                                    parent.getLien(parent.returnSommetComponent(pastSommet.get()), parent.returnSommetComponent(sommet)).forEach(lien -> {
                                        lien.setColor(Color.RED);
                                        lien.repaint();
                                        lien.getsA().setColor(Color.RED);

                                    });
                                    pastSommet.set(sommet);
                                });
                            }
                        });
                        duree.addActionListener(e -> {
                            for (LienComponent lien : parent.getLiens()){
                                if(parent.getInterface().getTheme().equalsIgnoreCase("light")){
                                    lien.setColor(lien.defaultLightModeColor);
                                }else{
                                    lien.setColor(lien.defaultDarkModeColor);
                                }
                            }
                            for (SommetComponent sommet : parent.getSommets()){
                                if(parent.getInterface().getTheme().equalsIgnoreCase("light")){
                                    sommet.setColor(sommet.defaultLightModeColor);
                                }else{
                                    sommet.setColor(sommet.defaultDarkModeColor);
                                }
                            }
                            HashMap<String, LinkedHashMap<String, Double>> chemin = parent.getInterface().getGraphe().tousLesPlusCourtCheminTemps(s1);
                            // string builder prcq le string de base marche pas jsp pk
                            AtomicReference<String> pastSommet = new AtomicReference<>(s1);
                            if(chemin.get(s2) != null){
                                chemin.get(s2).forEach((sommet, tmpValue) -> {
                                    parent.getLien(parent.returnSommetComponent(pastSommet.get()), parent.returnSommetComponent(sommet)).forEach(lien -> {
                                        lien.setColor(Color.RED);
                                        lien.repaint();
                                        lien.getsA().setColor(Color.RED);

                                    });
                                    pastSommet.set(sommet);
                                });
                            }
                        });

                        djikstra.add(fiabilite);
                        djikstra.add(distance);
                        djikstra.add(duree);
                        add(djikstra);

                        // F19
                        JMenuItem passePartout = new JMenuItem("Route passe partout");
                        passePartout.addActionListener(e -> {
                            // dessiner les aretes
                            HashMap<String, LinkedHashMap<String, Double>> chemin = grapheList.trouverRouteAvecDispensaires(s1, s2);
                            AtomicReference<String> pastSommet = new AtomicReference<>(s1);
                            if(chemin != null){
                                if(chemin.get(s2) != null){
                                    String cheminString = chemin.get(s2).keySet().toString();
                                    chemin.get(s2).forEach((sommet, tmpValue) -> {
                                        parent.getLien(parent.returnSommetComponent(pastSommet.get()), parent.returnSommetComponent(sommet)).forEach(lien -> {
                                            lien.setColor(Color.RED);
                                            lien.repaint();
                                            lien.getsA().setColor(Color.RED);

                                        });
                                        pastSommet.set(sommet);
                                    });
                                    JOptionPane cheminPane = new JOptionPane();
                                    cheminPane.showMessageDialog(parent, cheminString, "Chemin", JOptionPane.INFORMATION_MESSAGE);

                                }
                            }else{
                                JOptionPane erreurPane = new JOptionPane();
                                erreurPane.showMessageDialog(parent, "Il n'y a pas de route de " + s1 + " à " + s2 + " passant par un dispensaire de chaque categorie", "Pas de route", JOptionPane.ERROR_MESSAGE);

                            }
                        });

                        add(passePartout);

                        // F20
                        JMenuItem passePar = new JMenuItem("Passe par : ");
                        passePar.addActionListener(e -> {

                            CheminCustomModal cheminCustomModal = new CheminCustomModal();
                            cheminCustomModal.addWindowListener(new WindowAdapter(){
                                    @Override
                                    public void windowDeactivated(WindowEvent e) {
                                        super.windowDeactivated(e);
                                        if(cheminCustomModal.getMaternite() != null && cheminCustomModal.getBloc() != null && cheminCustomModal.getNutrition() != null){
                                            nbNutri = cheminCustomModal.getNutrition();
                                            nbMater = cheminCustomModal.getMaternite();
                                            nbBloc = cheminCustomModal.getBloc();

                                            HashMap<String, LinkedHashMap<String, Double>> chemin = grapheList.trouverRouteAvecDispensaires2(s1, s2, nbNutri, nbBloc, nbMater);
                                            AtomicReference<String> pastSommet = new AtomicReference<>(s1);
                                            if (chemin != null) {
                                                if (chemin.get(s2) != null) {
                                                    String cheminString = chemin.get(s2).keySet().toString();
                                                    chemin.get(s2).forEach((sommet, tmpValue) -> {
                                                        parent.getLien(parent.returnSommetComponent(pastSommet.get()), parent.returnSommetComponent(sommet)).forEach(lien -> {
                                                            lien.setColor(Color.RED);
                                                            lien.repaint();
                                                            lien.getsA().setColor(Color.RED);

                                                        });
                                                        pastSommet.set(sommet);
                                                    });
                                                    JOptionPane cheminPane = new JOptionPane();
                                                    cheminPane.showMessageDialog(parent, cheminString, "Chemin", JOptionPane.INFORMATION_MESSAGE);

                                                }
                                            } else {
                                                JOptionPane erreurPane = new JOptionPane();
                                                erreurPane.showMessageDialog(parent, "Il n'y a pas de route de " + s1 + " à " + s2 + " passant par " + nbNutri + " centre de nutrition, " + nbBloc + " bloc operatoire et " + nbMater + " maternité.", "Pas de route", JOptionPane.ERROR_MESSAGE);
                                            }
                                        }

                                    }
                            });


                        });
                        add(passePar);

                    }

                    comparaison = new JMenuItem("Comparaison");
                    comparaison.addActionListener(e -> {
                        ComparaisonSommetModal compSommetModal = new ComparaisonSommetModal(sc1.getNomSommet(), sc2.getNomSommet(), grapheList);
                    });
                    add(comparaison);
                }
            }
        }

        if(!parent.getInterface().getFiltred()){
            add(remove);
            add(editSommet);
        }
        add(infoSommet);
    }

    public String getNom() {
        return nom;
    }

}
