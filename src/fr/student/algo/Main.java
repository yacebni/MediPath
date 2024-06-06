package fr.student.algo;

public class Main {
    public static void main(String[] args){
        /*Sommet s1 = new Sommet("s1", "H");
        Sommet s2 = new Sommet("s2", "O");
        Sommet s3 = new Sommet("s3", "M");

        GrapheList graphe = new GrapheList();
        graphe.addSommet(s1);
        graphe.addSommet(s2);
        graphe.addSommet(s3);
        s1.setTeteLien(new LienMaillon(s2, 30, 3, 500, "l1"));
        s1.ajoutLien(s3, 20, 90, 1000, "l2");
        System.out.println(graphe.toString());

        System.out.println(graphe.toString());
        System.out.println("quoicoubeh");

        System.out.println(s1.afficherVoisin());
        System.out.println("quoicoubeh");
        graphe.supprimerSommet("s3");
        System.out.println(s1.afficherVoisin());
        System.out.println("quoicoubeh");

        System.out.println(graphe.toString());
         */
        String filename = "common/liste-adjacence-jeuEssai.csv";
        GrapheList graphe = new GrapheList();
        graphe.loadInGraphe(filename);

        System.out.println(graphe.toString());
        System.out.println();
        //System.out.println(graphe.returnSommet("s12").afficherVoisin("\n"));

        //System.out.println(graphe.nbSommet());
        System.out.println();


        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();

        /*if(graphe.returnSommet("s2").estVoisin("s4")){
            System.out.println("s4");
            System.out.println(graphe.returnSommet("s4").afficherVoisin("\n"));
        }*/

        //System.out.println(graphe.tousLesPlusCourtCheminDistance("S1"));
        //System.out.println(graphe.trouverRouteAvecDispensaires("S1", "S10").get("S10"));
        System.out.println(graphe.trouverRouteAvecDispensaires2("S1", "S10", 2, 3, 7).get("S10"));
        //System.out.println(graphe.ComplexiteFiab(graphe));
    }
}
