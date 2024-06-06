package fr.student.algo;

import java.util.ArrayList;
import java.util.List;


/**
 * Objet sommet :
 * nom: Le nom du sommet
 * type: Le type du sommet (O, M, N)
 * teteLien: Le premier lien du sommet - Liste chainée
 */
public class Sommet {


    private String nom;
    private String type;
    private LienMaillon teteLien = null;

    /**
        Constructeur de la classe Sommet
        @param nom: Le nom du sommet
        @param type: Le type du sommet
     */
    public Sommet(String nom, String type){

        this.nom = nom;
        this.type = type;
    }

    public String toString() {
        return "Sommet{" +
                "nom='" + nom + '\'' +
                ", type='" + type + '\'' +
                ", teteLien=" + ((teteLien != null)? teteLien.getBatiment().getNom() : null) +
                '}';
    }

    public String getNom() {
        return nom;
    }

    public String getType() {
        return type;
    }

    public LienMaillon getTeteLien() {
        return teteLien;
    }

    public void setTeteLien(LienMaillon teteLien) {
        this.teteLien = teteLien;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setType(String type) {
        this.type = type;
    }

    /**
     * Retourne vrai si le sommet est voisin du sommet courant
     * @param nomSommet: Le nom du sommet à tester
     * @return boolean: Vrai si le sommet est voisin, faux sinon
     */
    public boolean estVoisin(String nomSommet){

        LienMaillon tmp = this.teteLien;
        while(tmp != null){
            if(tmp.getBatiment().getNom().equalsIgnoreCase(nomSommet)){
                return true;
            }
            tmp = tmp.getSuivant();
        }
        return false;
    }

    /**
     * Retourne la liste des voisins du sommet courant
     * @return List<String>: La liste des voisins
     */
    public List<String> getVoisins(){

        List<String> voisins = new ArrayList<>() ;
        LienMaillon tmp = this.getTeteLien();
        while (tmp != null) {
            voisins.add(tmp.getBatiment().getNom());
            tmp = tmp.getSuivant();
        }
        return voisins;
    }

    /**
     * Retourne le lien vers le sommet passé en paramètre
     * @param nomSommet: Le nom du sommet cible
     * @return LienMaillon: Le lien vers le sommet cible
     * @return null: Si le lien n'existe pas
     */
    public LienMaillon getLien(String nomSommet){

        LienMaillon tmp = this.teteLien;
        while(tmp != null){
            if(tmp.getBatiment().getNom().equalsIgnoreCase(nomSommet)){
                return tmp;
            }
            tmp = tmp.getSuivant();
        }
        return null;
    }

    /**
     * Ajoute un lien vers le sommet passé en paramètre
     * @param sommet: Le sommet cible
     * @param fiabilite: La fiabilité du lien
     * @param distance: La distance du lien
     * @param duree: La durée du lien
     * @param nomLien: Le nom du lien
     * @return boolean: Vrai si le lien a été ajouté, faux sinon
     * @return null: Si le sommet cible est null
     */
    public boolean ajoutLien(Sommet sommet, double fiabilite, double distance, int duree, String nomLien){
        LienMaillon tmp = this.teteLien;
        if(tmp == null){
            this.teteLien = new LienMaillon(sommet, fiabilite, distance, duree, nomLien);
            return true;
        }
        while(tmp != null){

            if(tmp.getSuivant() == null){
                tmp.setSuivant(new LienMaillon(sommet, fiabilite, distance, duree, nomLien));
                return true;
            }
            tmp = tmp.getSuivant();
        }
        return false;
    }

    /**
     * Supprime le lien vers le sommet passé en paramètre
     * @param nomSommet: Le nom du sommet cible
     * @return boolean: Vrai si le lien a été supprimé, faux sinon
     * @return null: Si le sommet cible est null
     */
    public boolean supprimerLien(String nomSommet){
        LienMaillon tmp = this.getTeteLien();
        LienMaillon precedent = this.getTeteLien();

        if(tmp.getBatiment().getNom().equalsIgnoreCase(nomSommet)){
            this.setTeteLien(tmp.getSuivant());
            return true;
        }else{
            while (tmp != null){
                if(tmp.getBatiment().getNom().equalsIgnoreCase(nomSommet)){
                    precedent.setSuivant(tmp.getSuivant());
                    return true;
                }
                precedent = tmp;
                tmp = tmp.getSuivant();
            }
        }
        return false;
    }

    /**
     * Affiche les voisins du sommet courant
     * @param separation: La chaine de caractère de séparation entre les voisins
     * @return String: La chaine de caractère contenant les voisins
     */
    public String afficherVoisin(String separation){

        String voisinString = "";
        LienMaillon tmp = this.getTeteLien();
        while (tmp != null) {
            voisinString += tmp.getBatiment().toString() + " Fiabilité : " + tmp.getFiabilite() + " Distance : " + tmp.getDistance() + " Durée : " + tmp.getDuree() + separation;
            tmp = tmp.getSuivant();
        }
        return voisinString;
    }

    /**
     * Retourne la fiabilité du lien entre le sommet courant et le sommet passé en paramètre
     * @param depart: Le sommet courant
     * @param destination: Le nom du sommet cible
     * @return Double: La fiabilité du lien
     */
    public static Double getFiabiliteBetween(Sommet depart, String destination){

        if(depart.estVoisin(destination)) {
            LienMaillon tmp = depart.getTeteLien();
            while (tmp != null) {
                if (tmp.getBatiment().getNom().equalsIgnoreCase(destination)) {
                    return tmp.getFiabilite();
                }
                tmp = tmp.getSuivant();
            }
        }
        return null;
    }

    /**
     * Retourne la distance du lien entre le sommet courant et le sommet passé en paramètre
     * @param depart: Le sommet courant
     * @param destination: Le nom du sommet cible
     * @return Double: La distance du lien
     */
    public static Double getDistanceBetween(Sommet depart, String destination){

        if(depart.estVoisin(destination)) {
            LienMaillon tmp = depart.getTeteLien();
            while (tmp != null) {
                if (tmp.getBatiment().getNom().equalsIgnoreCase(destination)) {
                    return tmp.getDistance();
                }
                tmp = tmp.getSuivant();
            }
        }
        return null;
    }

    /**
     * Retourne la durée du lien entre le sommet courant et le sommet passé en paramètre
     * @param depart: Le sommet courant
     * @param destination: Le nom du sommet cible
     * @return Integer: La durée du lien
     */
    public static Integer getDureeBetween(Sommet depart, String destination){

        if(depart.estVoisin(destination)) {
            LienMaillon tmp = depart.getTeteLien();
            while (tmp != null) {
                if (tmp.getBatiment().getNom().equalsIgnoreCase(destination)) {
                    return tmp.getDuree();
                }
                tmp = tmp.getSuivant();
            }
        }
        return null;
    }


    /**
     * Retourne vrai si le sommet passé en paramètre est le même que le sommet courant
     * @param sommet: Le sommet à comparer
     * @return Boolean: Vrai si les sommets sont les mêmes, faux sinon
     */
    public Boolean isSame(Sommet sommet){

        if(this.getNom().equalsIgnoreCase(sommet.getNom())){
            return true;
        }
        return false;
    }


}
