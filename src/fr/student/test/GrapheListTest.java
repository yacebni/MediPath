package fr.student.test;

import fr.student.algo.GrapheList;
import fr.student.algo.Sommet;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class GrapheListTest {

    @Test
    void loadInGraphe() {
        // s'il y a autant de sommets dans la liste chaînée que dans le graphe CSV alors tout s'est bien passé
        GrapheList graphe = new GrapheList();
        graphe.loadInGraphe("common/test.csv");
        int nbSommets = graphe.nbSommet();
        assertEquals(true, nbSommets==8);
        assertEquals(false, nbSommets==5);
    }

    @Test
    void nbSommet() {
        GrapheList graphe = new GrapheList();
        graphe.loadInGraphe("common/test.csv");
        assertEquals(8,graphe.nbSommet());
        assertEquals(false, graphe.nbSommet() == 4);
    }

    @Test
    void addSommet() {
        GrapheList graphe = new GrapheList();
        graphe.loadInGraphe("common/test.csv");
        Sommet sommet = new Sommet("S12","O");
        graphe.addSommet(sommet);

        assertEquals(true, graphe.nbSommet() == 9);
        assertEquals(false, graphe.nbSommet() == 8);
        // System.out.println(graphe);
    }

    @Test
    void supprimerSommet() {
        GrapheList graphe = new GrapheList();
        graphe.loadInGraphe("common/test.csv");
        graphe.supprimerSommet("S1");
        assertEquals(true, graphe.nbSommet() == 7);
        assertEquals(false, graphe.nbSommet() == 8);
    }

    @Test
    void returnSommet() {
        GrapheList graphe = new GrapheList();
        graphe.loadInGraphe("common/test.csv");
        // System.out.println(graphe.returnSommet("S1"));
        assertEquals(true, Objects.equals(graphe.returnSommet("S1").getType(), "M") && graphe.returnSommet("S1").getTeteLien().getBatiment().getNom().equalsIgnoreCase( "S2"));
        assertEquals(false, Objects.equals(graphe.returnSommet("S1").getType(), "O") && graphe.returnSommet("S1").getTeteLien().getBatiment().getNom().equalsIgnoreCase( "S2"));
        assertEquals(false, Objects.equals(graphe.returnSommet("S1").getType(), "M") && graphe.returnSommet("S1").getTeteLien().getBatiment().getNom().equalsIgnoreCase( "S3"));

    }

    @Test
    void getVoisin1distance() {
        GrapheList graphe = new GrapheList();
        graphe.loadInGraphe("common/test.csv");
        List<String> voisin = graphe.getVoisin1distance("S1");
        assertEquals(true,
                            voisin.get(0).equalsIgnoreCase("S2")
                                    &&
                                    voisin.get(1).equalsIgnoreCase("S4")
        );
        assertEquals(false,
                             voisin.get(0).equalsIgnoreCase("S8")
                                    &&
                                    voisin.get(1).equalsIgnoreCase("S1")
        );
    }

    @Test
    void testGetVoisinPdistance(){
        /*GrapheList graphe = new GrapheList();
        graphe.loadInGraphe("common/test.csv");
        Map<Integer,List<String>> voisin = graphe.getVoisinPdistance();
        System.out.println(voisin);

        System.out.println(voisin.get(3));

        System.out.println(voisin.get(2).get(0));

        System.out.println(voisin.get(2).get(1));

        System.out.println(voisin.get(3).get(0));

        System.out.println(voisin.get(3).get(1));

        System.out.println(voisin.get(3).get(2));


        assertEquals(true,
                voisin.get(2).get(0).equals("S3")
                        &&
                        voisin.get(2).get(1).equals("S5")
                        &&
                        voisin.get(3).get(0).equals("S2")
                        &&
                        voisin.get(3).get(1).equals("S4")
                        &&
                        voisin.get(3).get(2).equals("S6")
        );
        assertEquals(false,
                 voisin.get(1).equals("S8")
                        &&
                        voisin.get(1).equals("S1")
        );
        */

    }

    @Test
    void getFinalAttribut() {
    }

    @Test
    void tousLesPlusCourtCheminFiabilite() {
        GrapheList graphe = new GrapheList();
        graphe.loadInGraphe("common/test.csv");
        assertEquals(false, graphe.tousLesPlusCourtCheminFiabilite("S4").get("S2").get("S2").equals(0.6));
        assertEquals(true, graphe.tousLesPlusCourtCheminFiabilite("S5").get("S1").get("S4").equals(1.0));


    }

    @Test
    void tousLesPlusCourtCheminDistance() {
        GrapheList graphe = new GrapheList();
        graphe.loadInGraphe("common/test.csv");
        assertEquals(true, graphe.tousLesPlusCourtCheminDistance("S2").get("S3").get("S3").equals(37.0));
        assertEquals(false, graphe.tousLesPlusCourtCheminDistance("S5").get("S5").get("S5").equals(10.0));
    }

    @Test
    void tousLesPlusCourtCheminTemps() {
        GrapheList graphe = new GrapheList();
        graphe.loadInGraphe("common/test.csv");
        assertEquals(true, graphe.tousLesPlusCourtCheminTemps("S5").get("S1").get("S4").equals(15.0));
        assertEquals(false, graphe.tousLesPlusCourtCheminTemps("S1").get("S6").get("S5").equals(60.0));
    }

    @Test
    void getAllSommetName() {
    }

    @Test
    void getSommetPlusProcheDistance() {
        GrapheList graphe = new GrapheList();
        graphe.loadInGraphe("common/test.csv");
        assertEquals(true, graphe.getPlusCourtDistanceByType("S8", "O") == null);
        assertEquals(true, graphe.getPlusCourtDistanceByType("S3", "M").equalsIgnoreCase("S4"));
        assertEquals(true, graphe.getPlusCourtDistanceByType("S2", "O").equalsIgnoreCase("S1"));
    }

    @Test
    void getSommetPlusProcheFiabilite() {
        GrapheList graphe = new GrapheList();
        graphe.loadInGraphe("common/test.csv");
        assertEquals(true, graphe.getPlusFiableByType("S8", "O") == null);
        assertEquals(true, graphe.getPlusFiableByType("S6", "N").equalsIgnoreCase("S5"));
        assertEquals(true, graphe.getPlusFiableByType("S2", "O").equalsIgnoreCase("S3"));
        assertEquals(true, graphe.getPlusFiableByType("S5", "M").equalsIgnoreCase("S4"));
    }

    @Test
    void getSommetPlusProcheTemps() {
        GrapheList graphe = new GrapheList();
        graphe.loadInGraphe("common/test.csv");
        assertEquals(true, graphe.getPlusCourtTempsByType("S8", "O") == null);
        assertEquals(true, graphe.getPlusCourtTempsByType("S6", "N").equalsIgnoreCase("S5"));
        assertEquals(true, graphe.getPlusCourtTempsByType("S6", "N").equalsIgnoreCase("S3"));
    }
}