package fr.student.algo;

import fr.student.test.*;
import fr.student.views.GrapheCanva;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Utils {

    public static void main(String[] args){

        // utiliser junit pour tester les fonctions
        UtilsTest test = new UtilsTest();
        try{
            test.testLienValeur();

        }catch (Exception e){
            System.err.println("Erreur lors du test : \n" + e.getMessage());
        }
    }


    /**
     * Converti un fichier en liste de sommet
     * @param filename: Le nom du fichier à convertir
     * @return ArrayList<Sommet>: La liste des sommets
     */
    public static ArrayList<Sommet> fichierToSommet(String filename){

        ArrayList<Sommet> sommets = new ArrayList<>();
        ArrayList<String> lines = new ArrayList<>();
        try{
            Scanner file = new Scanner(new FileReader(filename));
            // conversion du fichier en tableau pour mieux le manipuler
            while(file.hasNextLine()){
                String s = file.nextLine();
                lines.add(s);
            }

            // lecture du tableau
            // On lock une ligne puis on lis les lignes en bas
            for(int i = 0; i<lines.size(); i++){
                String[] lineSplited = lines.get(i).split(";"); // decoupage de la ligne
                Sommet sommet = new Sommet( lineSplited[0], lineSplited[1]); // creation du sommet;

                // lock chaque ligne du dessous
                for(int j = 0; j<lines.size(); j++){
                    String[] line2Splited = lines.get(j).split(";");
                    Integer[] lien = lienValeur(lineSplited, line2Splited);

                    if(lien != null && j != i){
                        sommet.ajoutLien(new Sommet(line2Splited[0], line2Splited[1]), lien[0], lien[1], lien[2],  lien[1] + "KM");
                    }
                }
                sommets.add(sommet); // ajout du sommet dans la liste
            }
            file.close();
            return sommets;
        }catch (FileNotFoundException e){
            System.out.println("Le fichier n'est pas trouvé !");
            return null;
        }
    }


    /**
     * Retourne les valeurs du lien entre 2 sommets
     * @param lineSplited1: La ligne du sommet 1
     * @param lineSplited2: La ligne du sommet 2
     * @return Integer[]: Les valeurs du lien
     */
    public static Integer[] lienValeur(String[] lineSplited1, String[] lineSplited2){
        try{
            for(int i = 2; i<lineSplited1.length; i++){
                if(lineSplited1[i].equals(lineSplited2[i]) && lineSplited1[i].split(",").length == 3 ){ // si les 2 sommets ont le meme lien
                    String[] linkSplited = lineSplited1[i].split(",");
                    Integer[] link = new Integer[linkSplited.length];
                    link[0] = Integer.parseInt(linkSplited[0].trim());
                    link[1] = Integer.parseInt(linkSplited[1].trim());
                    link[2] = Integer.parseInt(linkSplited[2].trim());
                    return link;
                }
            }
        }catch (ArrayIndexOutOfBoundsException e){
                System.err.println("Erreur lors de la lecture du fichier : \n" + e.getMessage());
                System.err.println("Vérifiez que le fichier choisis correspond bien au pattern utilisé !");

        }
        return null;
    }

    public static void saveGrapheInFile(GrapheList graphe, String filename){
        ArrayList<ArrayList<String>> lines = new ArrayList<>();
        // remplissage sommet
        for(Sommet sommet : graphe.getAllSommet()){
            ArrayList<String> line = new ArrayList<>();
            line.add(sommet.getNom());
            line.add(sommet.getType());
            lines.add(line);
        }

        // creation liste de lien déjà traité
        ArrayList<ArrayList<String>> dejaTraite = new ArrayList<>();

        // boucler dans toutes les lignes
        for(int i = 0; i<lines.size(); i++){
            String nomSommet = lines.get(i).get(0);
            // on recupere les liens du sommet de la ligne i
            LienMaillon tmp = graphe.returnSommet(nomSommet).getTeteLien();
            // on boucle dans les liens
            while(tmp != null){
                // verification de si on a deja traité le sqommet
                Boolean in = false;
                for (ArrayList<String> traiteCouple : dejaTraite){
                    if(traiteCouple.contains(nomSommet) && traiteCouple.contains(tmp.getBatiment().getNom())){
                        in = true;
                    }
                }
                // si le lien n'est pas traité
                if(!in){
                    // on boucle dans les autres lignes
                    ArrayList<String> deuxSommet = new ArrayList<>();
                    deuxSommet.add(nomSommet);

                    for (int j = 0; j<lines.size(); j++){
                        String nomSommet2 = lines.get(j).get(0);

                        // si le sommet de cette ligne est celui du lien avec le sommet de la ligne i
                        if(graphe.returnSommet(nomSommet2).getNom().equalsIgnoreCase(tmp.getBatiment().getNom())){
                            deuxSommet.add(nomSommet2);
                            // on écrit qu'il y a un lien
                            lines.get(j).add((int)tmp.getFiabilite() + "," + (int)tmp.getDistance() + "," + tmp.getDuree());
                        } else if (nomSommet2.equalsIgnoreCase(nomSommet)) {
                            lines.get(j).add((int)tmp.getFiabilite() + "," + (int)tmp.getDistance() + "," + tmp.getDuree());
                        } else{
                            // sinon on écrit 0
                            lines.get(j).add("0");
                        }
                    }
                    dejaTraite.add(deuxSommet);
                }
                tmp = tmp.getSuivant();
            }
        }

        try {
            File dir = new File(".save-sae");
            if(!dir.exists()){
                dir.mkdirs();
            }
            PrintWriter writer = new PrintWriter(filename, "UTF-8");
            for (ArrayList<String> line : lines){
                String ligne = "";
                for (String s : line){
                    ligne += s + ";";
                }
                writer.println(ligne);
            }
            writer.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

}

