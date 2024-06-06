package fr.student.test;

import fr.student.algo.Sommet;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static fr.student.algo.Utils.fichierToSommet;
import static org.junit.jupiter.api.Assertions.*;

class SommetTest {

    @Test
    public void testEstVoisin() {
        ArrayList<Sommet> testFichierToSommet = fichierToSommet("common/test.csv");
        assertEquals(true, testFichierToSommet.get(0).estVoisin("S2") );
        assertEquals(false, testFichierToSommet.get(2).estVoisin("S5"));
    }

    @Test
    public void testGetVoisins() {
        ArrayList<Sommet> testFichierToSommet = fichierToSommet("common/test.csv");
        assertEquals(true, testFichierToSommet.get(0).getVoisins().contains("S2"));
        assertEquals(true, testFichierToSommet.get(0).getVoisins().contains("S4"));
        assertEquals(false, testFichierToSommet.get(0).getVoisins().contains("S3"));

    }

    @Test
    public void testAjoutLien() {
        ArrayList<Sommet> testFichierToSommet = fichierToSommet("common/test.csv");
        assertEquals(true, testFichierToSommet.get(0).ajoutLien(new Sommet("S12","O"),9,12,21,"L12"));
        assertEquals(true, testFichierToSommet.get(0).getVoisins().contains("S12"));
        assertEquals(false, testFichierToSommet.get(0).getVoisins().contains("S3"));
    }

    @Test
    public void testSupprimerLien() {
        ArrayList<Sommet> testFichierToSommet = fichierToSommet("common/test.csv");
        assertEquals(true, testFichierToSommet.get(0).getVoisins().contains("S2"));
        assertEquals(true, testFichierToSommet.get(0).supprimerLien("S2"));
        assertEquals(false, testFichierToSommet.get(0).getVoisins().contains("S2"));
    }

    @Test
    public void testIsSame() {
        ArrayList<Sommet> testFichierToSommet = fichierToSommet("common/test.csv");
        assertEquals(true, testFichierToSommet.get(0).getNom().equals("S1"));
        assertEquals(false, testFichierToSommet.get(0).getNom().equals("S2"));
    }
}