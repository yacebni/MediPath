package fr.student.test;

import org.junit.jupiter.api.Test;

import static fr.student.algo.Utils.fichierToSommet;
import static fr.student.algo.Utils.lienValeur;
import static org.junit.jupiter.api.Assertions.*;

public class UtilsTest {
    @Test
    public void testLienValeur() {

        String ligne1 = "S1;M;4, 25, 50;10, 31, 37;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;5, 26, 35;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;10, 25, 35;0;0;8, 18, 23;0;0;0;0;0;0;0;0;0;0;0;0";
        String ligne2 = "S2;M;4, 25, 50;0;9, 28, 37;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;6, 12, 24;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0";
        String ligne26 = "S26;O;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;7, 12, 18;6, 20, 38;9, 7, 15;4, 12, 19;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0";

        assertEquals(4, lienValeur(ligne1.split(";"), ligne2.split(";"))[0]);
        assertEquals(25, lienValeur(ligne1.split(";"), ligne2.split(";"))[1]);
        assertEquals(50, lienValeur(ligne1.split(";"), ligne2.split(";"))[2]);

        assertEquals(null, lienValeur(ligne1.split(";"), ligne26.split(";")));
    }
    @Test
    public void testFichierToSommet(){
        System.out.println(fichierToSommet("common/test.csv"));
        assertEquals("S1",fichierToSommet("common/test.csv").get(0).getNom());
        assertEquals("M", fichierToSommet("common/test.csv").get(7).getType());
    }
}