package fr.student.algo;

/**
 * Structure de donnée de stockage des sommets:
 * val: Le sommet stocké
 * suivant: Le maillon sommet suivant dans la liste
 */
public class SommetMaillon {


    private Sommet val;
    private SommetMaillon suivant = null;
    public SommetMaillon(Sommet refere){
        this.val = refere;
    }

    public SommetMaillon getSuivant() {
        return suivant;
    }

    public Sommet getSommet() {
        return val;
    }

    public void setSuivant(SommetMaillon suivant) {
        this.suivant = suivant;
    }

    public void setSommet(Sommet val) {
        this.val = val;
    }
}
