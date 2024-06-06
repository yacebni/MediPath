package fr.student.algo;

/**
 * Structure de donnée de stockage des liens:
 * nomLien: Le nom du lien
 * batiment: Le sommet cible du lien
 * distance: La distance du lien
 * fiabilite: La fiabilité du lien
 * duree: La durée du lien
 * suivant: Le maillon lien suivant dans la liste
 */
public class LienMaillon {


    private String nomLien;
    private Sommet batiment;
    private double distance;
    private double fiabilite;
    private int duree;
    private LienMaillon suivant = null;

    /**
     * Constructeur de la classe LienMaillon
     * @param batimentCible: Le sommet cible du lien
     * @param distance: La distance du lien
     * @param fiabilite: La fiabilité du lien
     * @param duree: La durée du lien
     * @param nomLien: Le nom du lien
     */
    public LienMaillon(Sommet batimentCible, double fiabilite, double distance, int duree, String nomLien){

        this.nomLien = nomLien;
        this.batiment = batimentCible;
        this.distance = distance;
        this.fiabilite = fiabilite;
        this.duree = duree;
    }


    public double getDistance() {
        return distance;
    }

    public double getFiabilite() {
        return fiabilite;
    }

    public LienMaillon getSuivant() {
        return suivant;
    }

    public int getDuree() {
        return duree;
    }

    public Sommet getBatiment() {
        return batiment;
    }

    public void setSuivant(LienMaillon suivant) {
        this.suivant = suivant;
    }

    public void setBatiment(Sommet batiment) {
        this.batiment = batiment;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public void setDuree(int duree) {
        this.duree = duree;
    }

    public void setFiabilite(double fiabilite) {
        this.fiabilite = fiabilite;
    }

    public String getNomLien() {
        return nomLien;
    }

    public void setNomLien(String nomLien) {
        this.nomLien = nomLien;
    }
}
