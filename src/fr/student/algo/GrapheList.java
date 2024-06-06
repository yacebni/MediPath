package fr.student.algo;

import fr.student.views.LienComponent;

import java.util.*;

/**
 * Structure de donnée de stockage des sommets - Le graphe:
 * teteGraphe: Le premier sommet du graphe
 * nbSommet: Le nombre de sommet dans le graphe
 * addSommet: Ajoute un sommet au graphe
 * supprimerSommet: Supprime un sommet du graphe
 * toString: Affiche le graphe
 * afficherVoisinDansGraphe: Affiche les voisins d'un sommet dans le graphe
 * afficherVoisin: Affiche les voisins d'un sommet
 * returnSommet: Recherche un sommet dans le graphe
 */
public class GrapheList{

    private SommetMaillon teteGraphe;

    public static void main(String[] args){
        GrapheList graphe = new GrapheList();
        System.out.println(graphe.loadInGraphe("common/apagnan.csv"));
        System.out.println(graphe.returnSommet("S1").afficherVoisin("\n"));
        System.out.println(graphe.getVoisin1distance("s1"));
        System.out.println(graphe.getVoisinPdistance( "s1", 3));


        HashMap<String, LinkedHashMap<String, Double>> pccf = graphe.tousLesPlusCourtCheminFiabilite("s1");
        System.out.println(pccf);
        System.out.println();

        HashMap<String, LinkedHashMap<String, Double>> pccd = graphe.tousLesPlusCourtCheminDistance("s1");
        System.out.println(pccd);
        System.out.println();

        HashMap<String, LinkedHashMap<String, Double>> pcct = graphe.tousLesPlusCourtCheminTemps("s1");
        System.out.println(pcct);

        System.out.println(graphe.getFinalAttribut(pccf.get("S3")));

        System.out.println(graphe.getVoisinPdistance( "s1", 3));


    }

    public GrapheList(){
        this.teteGraphe = null;
    }

    /**
     * Charge un graphe à partir d'un fichier
     * Crée une liste chainé de sommet en mettant les sommets dans l'ordre du fichier
     * @param filename: le nom du fichier
     * @return true si le chargement s'est bien passé
     */
    public Boolean loadInGraphe(String filename){
        Boolean res = false;
        ArrayList<Sommet> sommets = Utils.fichierToSommet(filename);
        for(Sommet s : sommets){
            this.addSommet(s);
        }
        if(sommets.get(0).equals(this.teteGraphe.getSommet())){
            res = true;
        }
        return res;
    }

    /**
     * Retourne le nombre de sommet dans le graphe
     *
     * Parcours la liste chainée de sommet et compte le nombre de sommet
     * @return le nombre de sommet
     */
    public int nbSommet(){

        int count = 0;
        SommetMaillon temp = this.teteGraphe;
        // boucle dans la liste chainée de sommet
        while (temp != null){
            count += 1;
            temp = temp.getSuivant();
        }
        return count;
    }

    /**
     * Retourne le nombre de liens dans le graphe
     *
     * Parcours la liste chainée de liens et compte le nombre de liens
     * @return le nombre de liens
     */
    public int nbLiens() {
        Set<String> liens = new HashSet<>();
        SommetMaillon temp = this.teteGraphe;

        // Parcours la liste chaînée de sommets
        while (temp != null) {
            LienMaillon lienTemp = temp.getSommet().getTeteLien();

            // Parcours les liens du sommet actuel
            while (lienTemp != null) {
                // Ajoute le lien à l'ensemble
                liens.add(lienTemp.getNomLien());
                lienTemp = lienTemp.getSuivant();
            }

            temp = temp.getSuivant();
        }

        return liens.size() + 1;
    }


    /**
     * Ajoute un sommet au graphe
     * Parcours la liste chainée de sommet et ajoute le sommet à la fin
     *
     * @param sommet: le sommet à ajouter
     * @return true si l'ajout s'est bien passé
     * @return false si le sommet existe déjà
     * @return false si le sommet n'a pas pu être ajouté
     * @return false si le sommet est null
     */
    public boolean addSommet(Sommet sommet){

        boolean success = false;
        SommetMaillon temp = this.teteGraphe;
        // test si le sommet existe déjà ou si le sommet est null
        if(this.getAllSommetName().contains(sommet.getNom()) || sommet == null) {
            success = false;
        }else {

            if (temp == null) {
                this.teteGraphe = new SommetMaillon(sommet);
                success = true;
            } else {
                while (temp.getSuivant() != null) {
                    temp = temp.getSuivant();
                }
                temp.setSuivant(new SommetMaillon(sommet));
                success = true;
            }
        }
        return success;

    }

    /**
     * Supprime un sommet du graphe
     * @param nomSommet: le nom du sommet à supprimer
     * Parcours la liste chainée de sommet et supprime le sommet
     * @return true si la suppression s'est bien passé
     * @return false si le sommet n'a pas pu être supprimé
     * @return false si le sommet n'existe pas
     */
    public boolean supprimerSommet(String nomSommet){

        SommetMaillon tmp = this.teteGraphe;
        SommetMaillon precedent = this.teteGraphe;

        if(tmp.getSommet().getNom().equalsIgnoreCase(nomSommet)){
            this.teteGraphe = tmp.getSuivant();
            SommetMaillon tmp2 = this.teteGraphe;
            while(tmp2 != null){
                if(tmp2.getSommet().estVoisin(nomSommet)){
                    tmp2.getSommet().supprimerLien(nomSommet);
                }
                tmp2 = tmp2.getSuivant();
            }
            return true;
        }else{
            while(tmp != null){

                if(tmp.getSommet().getNom().equalsIgnoreCase(nomSommet)){

                    precedent.setSuivant(tmp.getSuivant());

                    SommetMaillon tmp2 = this.teteGraphe;
                    while(tmp2 != null){
                        if(tmp2.getSommet().estVoisin(nomSommet)){
                            tmp2.getSommet().supprimerLien(nomSommet);
                        }
                        tmp2 = tmp2.getSuivant();
                    }
                    return true;
                }
                precedent = tmp;
                tmp = tmp.getSuivant();
            }
        }
        return false;
    }

    /**
     * Retourne une chaine de caractère représentant le graphe
     * Parcours la liste chainée de sommet et ajoute le toString de chaque sommet à la chaine de caractère
     * @return la chaine de caractère
     */
    public String toString() {
        String display = "";
        SommetMaillon temp = this.teteGraphe;
        while(temp != null){
            display += temp.getSommet().toString() + "\n";
            temp = temp.getSuivant();
        }

        return display;
    }

    /**
     * Affiche les voisins d'un sommet
     * @param sommet: le sommet dont on veut afficher les voisins
     * Parcours la liste chainée de sommet et affiche les voisins du sommet
     */
    public void afficherVoisinDansGraphe(Sommet sommet) {

        LienMaillon tmp = sommet.getTeteLien();
        if(tmp != null){
            while (tmp.getSuivant() != null) {
                System.out.println(tmp.getBatiment().toString());
                tmp = tmp.getSuivant();
            }
        }

    }

    /**
     * Retourne un sommet
     * @param nomSommet: le nom du sommet à retourner
     * Parcours la liste chainée de sommet et retourne le sommet
     * @return null si le sommet n'existe pas
     * @return le sommet si le sommet existe
     * @return null si le sommet est null
     */
    public Sommet returnSommet(String nomSommet){

        SommetMaillon tmp = this.teteGraphe;
        if(tmp != null){
            if(tmp.getSommet().getNom().equalsIgnoreCase(nomSommet)){
                return tmp.getSommet();
            }else {
                while (tmp.getSuivant() != null) {
                    if (tmp.getSuivant().getSommet().getNom().equalsIgnoreCase(nomSommet)) {
                        return tmp.getSuivant().getSommet();
                    }
                    tmp = tmp.getSuivant();

                }
            }
        }
        return null;
    }

    /**
     * Retourne les voisins d'un sommet à une distance de 1
     * @param nomSommet: le nom du sommet dont on veut les voisins
     * Parcours la liste chainée de sommet et retourne les voisins direct du sommet
     * @return null si le sommet n'existe pas
     * @return la liste des voisins si le sommet existe
     * @return null si le sommet est null
     */
    public List<String> getVoisin1distance(String nomSommet){

        if(this.returnSommet(nomSommet) != null){
            return this.returnSommet(nomSommet).getVoisins();
        }
        return null;
    }


    /**
     * Retourne les voisins d'un sommet à une distance de P
     * @param nomSommet: le nom du sommet dont on veut les voisins
     * @parm distance: la distance à laquelle on veut les voisins
     * Parcours la liste chainée de sommet et retourne les voisins à la distance demandée
     * @return null si le sommet n'existe pas
     * @return la liste des voisins si le sommet existe
     * @return null si le sommet est null
     */
    public Map<Integer, List<String>> getVoisinPdistance(String nomSommet, Integer distance){

        if(this.returnSommet(nomSommet) != null){
            Integer i = 1;
            Map<Integer, List<String>> sommets = new TreeMap<>();
            sommets.put(i, this.returnSommet(nomSommet).getVoisins());
            // pour chaque distance
            for(int j = 0; j < distance; j++){
                List<String> returnArray = new ArrayList<>();
                // pour chaque sommet du voisin d'avant
                for(String s : sommets.get(i)){
                    // eviter la redondance
                    for(String s2 : this.returnSommet(s).getVoisins()) {
                        // s'il n'est pas deja dans la liste ou si ce n'est pas le sommet de depart
                        if(!returnArray.contains(s2) && !s2.equalsIgnoreCase(nomSommet)) {
                            returnArray.add(s2);
                        }
                    }
                }
                sommets.put(i+1, returnArray);
                i++;
            }

            return sommets;
        }
        return null;

    }

    /**
     * Retourne un sommetMaillon
     * @param nomSommet: le nom du sommet à retourner
     * Parcours la liste chainée de sommet et retourne le sommetMaillon
     * @return null si le sommet n'existe pas
     * @return le sommetMaillon si le sommet existe
     * @return null si le sommet est null
     */
    private SommetMaillon returnSommetMaillon(String nomSommet) {

        SommetMaillon tmp = this.teteGraphe;
        if (tmp != null) {
            if (tmp.getSommet().getNom().equalsIgnoreCase(nomSommet)) {
                return tmp;
            } else {
                while (tmp.getSuivant() != null) {
                    if (tmp.getSuivant().getSommet().getNom().equalsIgnoreCase(nomSommet)) {
                        return tmp.getSuivant();
                    }
                    tmp = tmp.getSuivant();
                }
            }
        }
        return null;
    }

    /**
     * Retourne la taille d'un chemin
     * @param chemin: le chemin dont on veut la taille
     * @return null si le chemin n'existe pas
     */
    public static Integer getTailleChemin(LinkedHashMap<String, Double> chemin){

        if(chemin != null){
            return chemin.size() -1;
        }else{
            return null;
        }
    }

    /**
     * Retourne tous les sommets
     * Parcours la liste chainée de sommet et retourne les sommets
     * @return null si le sommet n'existe pas
     * @return la liste des sommets si le sommet existe
     * @return null si le sommet est null
     */
    public ArrayList<SommetMaillon> getAllSommetMaillons(){

        ArrayList<SommetMaillon> sommets = new ArrayList<>();
        SommetMaillon maillon = teteGraphe;
        while (maillon != null) {
            sommets.add(maillon);
            maillon = maillon.getSuivant();
        }
        return sommets;
    }

    /**
     * Retourne tous les noms des sommets
     * Parcours la liste chainée de sommet et retourne les noms des sommets
     * @return null si le sommet n'existe pas
     * @return la liste des noms des sommets si le sommet existe
     * @return null si le sommet est null
     */
    public ArrayList<String> getAllSommetName(){

        ArrayList<String> sommets = new ArrayList<>();
        SommetMaillon maillon = teteGraphe;
        while (maillon != null) {
            sommets.add(maillon.getSommet().getNom());
            maillon = maillon.getSuivant();
        }
        return sommets;
    }

    /**
     * Retourne tous les sommets
     * Parcours la liste chainée de sommet et retourne les sommets
     * @return la liste des sommets
     */
    public ArrayList<Sommet> getAllSommet(){
            ArrayList<Sommet> sommets = new ArrayList<>();
            SommetMaillon maillon = teteGraphe;
            while (maillon != null) {
                sommets.add(maillon.getSommet());
                maillon = maillon.getSuivant();
            }
            return sommets;
    }

    /**
     * Retourne toutes les aretes
     * Parcours la liste chainée de sommet et retourne les aretes
     * @return La liste des aretes
     */
    public Map<String, ArrayList<LienMaillon>> getAllLiens(){

            Map<String, ArrayList<LienMaillon>> aretes = new HashMap<>();
            SommetMaillon maillon = teteGraphe;
            while (maillon != null) {
                ArrayList<LienMaillon> liens = new ArrayList<>();
                LienMaillon lien = maillon.getSommet().getTeteLien();
                while (lien != null) {
                    liens.add(lien);
                    lien = lien.getSuivant();
                }
                aretes.put(maillon.getSommet().getNom(), liens);
                maillon = maillon.getSuivant();
            }
            return aretes;
    }

    /**
     * @param chemin: le chemin dont on veut l'attribut
     * @return l'attribut Durée, Distance ou Fiabilite du chemin
     */
    public Double getFinalAttribut(LinkedHashMap<String, Double> chemin){

        Double res = chemin.get(chemin.keySet().toArray()[chemin.size()-1]);
        return res;
    }

    public LienMaillon getLien(String s1, String s2){
        SommetMaillon sommet1 = this.returnSommetMaillon(s1);
        SommetMaillon sommet2 = this.returnSommetMaillon(s2);
        if(sommet1 != null && sommet2 != null){
            LienMaillon lien = sommet1.getSommet().getTeteLien();
            while(lien != null){
                if(lien.getBatiment().getNom().equalsIgnoreCase(s2)){
                    return lien;
                }
                lien = lien.getSuivant();
            }
        }
        return null;
    }

    // ************************************************************************

    /**
     * Retourne tous les plus courts chemins en fonction de la fiabilité
     * @param nomSommet: le nom du sommet de départ
     * Parcours la liste chainée de sommet et retourne les plus courts chemins
     * @return null si le sommet n'existe pas
     * @return les plus courts chemins si le sommet existe
     * @return null si le sommet est null
     */
    public HashMap<String, LinkedHashMap<String, Double>> tousLesPlusCourtCheminFiabilite(String nomSommet){

        HashMap<String, LinkedHashMap<String, Double>> res = new HashMap<>();//Hash map qui permet de stocker les chemins les plus courts
        Map<String, Boolean> sommetsTraites = new HashMap<>();//Hash map permettant de marquer les sommet traiter
        res.put(nomSommet, new LinkedHashMap<>());
        res.get(nomSommet).put(nomSommet, 1.0);//le premier centre(sommet) a une fiabilite de 1
        //on itere sur tout les centres afin de les mettre a false -> les sommets ne sont pas encore marquer
        this.getAllSommetMaillons().forEach(Centre -> {
            sommetsTraites.put(Centre.getSommet().getNom(), false);
        });
        sommetsTraites.put(nomSommet, true);//le premier centre est le debut donc il traiter -> true
        List<String[]> fileAttente = new ArrayList<>();//creation d'une liste permettant d'ajouter les sommet traiter
        fileAttente.add(new String[]{nomSommet, "1"});//ajoute le premier sommet avec comme fiabilite 1
        String[] donnee;
        while (!(fileAttente.isEmpty())) {//tant que cette liste n'est pas vide
            donnee = fileAttente.get(fileAttente.size() - 1);//sauvegarde la fiabilite et le centre de la fin de la file dans la variable donnee
            fileAttente.remove(fileAttente.size() - 1);//supprime ces cet element
            String centre = donnee[0];//donne[0] vaut le nom du centre
            double fiab = Double.parseDouble(donnee[1]);//donne[1] vaut la fiabilite de type String convertie en double

            LienMaillon voisin = this.returnSommet(centre).getTeteLien();
            while (voisin != null) {
                String nomVoisin = voisin.getBatiment().getNom();
                if (!sommetsTraites.get(nomVoisin)) {
                    if (!res.containsKey(nomVoisin)) {
                        // On arrive à cette endroit si nomVoisin n'a pas encore de chemin
                        res.put(nomVoisin, new LinkedHashMap<>(res.get(centre)));
                        res.get(nomVoisin).put(nomVoisin, (voisin.getFiabilite() / 10) * fiab);
                        fileAttente.add(new String[]{nomVoisin, String.valueOf((voisin.getFiabilite() / 10) * fiab)});

                    } else {
                        // si nomVoisin a déjà un chemin
                        LinkedHashMap<String, Double> chemin = res.get(nomVoisin);
                        Double lastFiabCentreDansChemin = null;
                        String lastNomCentreDansChemin = null;
                        for (Map.Entry<String, Double> centreChemin : chemin.entrySet()) {
                            // Dans cette boucle on récupère l'extrémité du chemin <centre1, nomVoisin>
                            lastNomCentreDansChemin = centreChemin.getKey(); // le nom de l'extrémité
                            lastFiabCentreDansChemin = centreChemin.getValue(); // la fiabilité de l'extrémité
                        }
                        if (lastFiabCentreDansChemin < (voisin.getFiabilite() / 10) * fiab) {
                            // Si on arrive ici, ca veut dire que le chemin initial entre le centre1 et nomVoisin n'est pas le plus fiable a ce moment la
                            res.put(nomVoisin, new LinkedHashMap<>(res.get(donnee[0])));
                            res.get(nomVoisin).put(lastNomCentreDansChemin, (voisin.getFiabilite() / 10) * fiab);
                            // Donc on créer le nouveau chemin le plus fiable entre centre1 et nomVoisin

                            // Comme il y a un nouveau chemin, ca veut dire que la fileAttente est obsolete car elle n'est pas à jour
                            // Grace a la boucle ci dessous, on retire l'ancien nomVoisin puis on le remplace par le nouveau avec sa nouvelle fiabilité
                            int i = 0;
                            boolean check = false;
                            while (!check && i < fileAttente.size()) {
                                if (fileAttente.get(i)[0].equals(nomVoisin)) {
                                    check = true;
                                    fileAttente.remove(i);
                                    fileAttente.add(new String[]{nomVoisin, String.valueOf((voisin.getFiabilite() / 10) * fiab)});
                                }
                                i++;
                            }
                        }

                    }
                }
                voisin = voisin.getSuivant();
            }
            if (fileAttente.size() >= 2) {
                // si on arrive ca veut dire qu'il faut mettre le sommetTraite le plus fiable tout en haut de la liste pour la traité en premier
                int maxiFiabIndice = 0;
                for (int i = 1; i < fileAttente.size(); i++) {
                    if (Double.parseDouble(fileAttente.get(i)[1]) > Double.parseDouble(fileAttente.get(maxiFiabIndice)[1])) {
                        // si on arrive ici, ca veut dire qu'on a trouvé le plus fiable au ieme moment
                        maxiFiabIndice = i;
                    } else if (Double.parseDouble(fileAttente.get(i)[1]) == Double.parseDouble(fileAttente.get(maxiFiabIndice)[1])) {
                        // si on arrive ici, ca veut dire que deux sommets ont la même fiabilité donc je met en priorité le plus petit sommet (ex: D'abord S9 puis S15, et pas S15 puis S9)
                        String[] listNomCentre = fileAttente.get(i)[0].split("");
                        String[] listMaxiNomCentre = fileAttente.get(maxiFiabIndice)[0].split("");
                        int nombre;
                        int maxiNombre;
                        if (listNomCentre.length == 2) {
                            nombre = Integer.parseInt(listNomCentre[1]);
                        } else {
                            nombre = Integer.parseInt(listNomCentre[1] + listNomCentre[2]);
                        }
                        if (listMaxiNomCentre.length == 2) {
                            maxiNombre = Integer.parseInt(listMaxiNomCentre[1]);
                        } else {
                            maxiNombre = Integer.parseInt(listMaxiNomCentre[1] + listMaxiNomCentre[2]);
                        }
                        if (maxiNombre > nombre) {
                            maxiFiabIndice = i;
                        }
                    }
                }
                sommetsTraites.put(fileAttente.get(maxiFiabIndice)[0], true); // je marque le sommet qui va etre traité
                fileAttente.add(new String[]{fileAttente.get(maxiFiabIndice)[0], String.valueOf(fileAttente.get(maxiFiabIndice)[1])});
                fileAttente.remove(maxiFiabIndice);
            }
        }
        return res;
    }



    /**
     * Retourne tous les plus courts chemins en fonction du temps
     * @param nomSommet: le nom du sommet de départ
     * Parcours la liste chainée de sommet et retourne les plus courts chemins
     * @return null si le sommet n'existe pas
     * @return les plus courts chemins si le sommet existe
     * @return null si le sommet est null
     */
    public HashMap<String, LinkedHashMap<String, Double>> tousLesPlusCourtCheminTemps(String nomSommet){

        HashMap<String, LinkedHashMap<String, Double>> res = new HashMap<>();//Hash map qui permet de stocker les chemins les plus courts
        Map<String, Boolean> sommetsTraites = new HashMap<>();//Hash map permettant de marquer les sommet traiter
        res.put(nomSommet, new LinkedHashMap<>());
        res.get(nomSommet).put(nomSommet, 0.0);//le premier centre(sommet) a une temps de 0
        //on itere sur tout les centres afin de les mettre a false -> les sommets ne sont pas encore marquer
        this.getAllSommetMaillons().forEach(Centre -> {
            sommetsTraites.put(Centre.getSommet().getNom(), false);
        });
        sommetsTraites.put(nomSommet, true);//le premier centre est le debut donc il traiter -> true
        List<String[]> fileAttente = new ArrayList<>();//creation d'une liste permettant d'ajouter les sommet traiter
        fileAttente.add(new String[]{nomSommet, "0"});//ajoute le premier sommet avec comme temps 0
        String[] donnee;
        while (!(fileAttente.isEmpty())) {//tant que cette liste n'est pas vide
            donnee = fileAttente.get(fileAttente.size() - 1);//sauvegarde la temps et le centre de la fin de la file dans la variable donnee
            fileAttente.remove(fileAttente.size() - 1);//supprime ces cet element
            String centre = donnee[0];//donne[0] vaut le nom du centre
            double temps = Double.parseDouble(donnee[1]);//donne[1] vaut la temps de type String convertie en double

            LienMaillon voisin = this.returnSommet(centre).getTeteLien();
            while (voisin != null) {
                String nomVoisin = voisin.getBatiment().getNom();
                if (!sommetsTraites.get(nomVoisin)) {
                    if (!res.containsKey(nomVoisin)) {
                        // On arrive à cette endroit si nomVoisin n'a pas encore de chemin
                        res.put(nomVoisin, new LinkedHashMap<>(res.get(centre)));
                        res.get(nomVoisin).put(nomVoisin, (voisin.getDuree()) + temps);
                        fileAttente.add(new String[]{nomVoisin, String.valueOf((voisin.getDuree()) + temps)});

                    } else {
                        // si nomVoisin a déjà un chemin
                        LinkedHashMap<String, Double> chemin = res.get(nomVoisin);
                        Double lastFiabCentreDansChemin = null;
                        String lastNomCentreDansChemin = null;
                        for (Map.Entry<String, Double> centreChemin : chemin.entrySet()) {
                            // Dans cette boucle on récupère l'extrémité du chemin <centre1, nomVoisin>
                            lastNomCentreDansChemin = centreChemin.getKey(); // le nom de l'extrémité
                            lastFiabCentreDansChemin = centreChemin.getValue(); // la fiabilité de l'extrémité
                        }
                        if (lastFiabCentreDansChemin < (voisin.getDuree()) + temps) {
                            // Si on arrive ici, ca veut dire que le chemin initial entre le centre1 et nomVoisin n'est pas le plus fiable a ce moment la
                            res.put(nomVoisin, new LinkedHashMap<>(res.get(donnee[0])));
                            res.get(nomVoisin).put(lastNomCentreDansChemin, (voisin.getDuree()) + temps);
                            // Donc on créer le nouveau chemin le plus fiable entre centre1 et nomVoisin

                            // Comme il y a un nouveau chemin, ca veut dire que la fileAttente est obsolete car elle n'est pas à jour
                            // Grace a la boucle ci dessous, on retire l'ancien nomVoisin puis on le remplace par le nouveau avec sa nouvelle fiabilité
                            int i = 0;
                            boolean check = false;
                            while (!check && i < fileAttente.size()) {
                                if (fileAttente.get(i)[0].equals(nomVoisin)) {
                                    check = true;
                                    fileAttente.remove(i);
                                    fileAttente.add(new String[]{nomVoisin, String.valueOf((voisin.getDuree()) + temps)});
                                }
                                i++;
                            }
                        }

                    }
                }
                voisin = voisin.getSuivant();
            }
            if (fileAttente.size() >= 2) {
                // si on arrive ca veut dire qu'il faut mettre le sommetTraite le plus fiable tout en haut de la liste pour la traité en premier
                int maxiTempsIndice = 0;
                for (int i = 1; i < fileAttente.size(); i++) {
                    if (Double.parseDouble(fileAttente.get(i)[1]) < Double.parseDouble(fileAttente.get(maxiTempsIndice)[1])) {
                        // si on arrive ici, ca veut dire qu'on a trouvé le plus long au ieme moment
                        maxiTempsIndice = i;
                    } else if (Double.parseDouble(fileAttente.get(i)[1]) == Double.parseDouble(fileAttente.get(maxiTempsIndice)[1])) {
                        // si on arrive ici, ca veut dire que deux sommets ont la même long donc je met en priorité le plus petit sommet (ex: D'abord S9 puis S15, et pas S15 puis S9)
                        String[] listNomCentre = fileAttente.get(i)[0].split("");
                        String[] listMaxiNomCentre = fileAttente.get(maxiTempsIndice)[0].split("");
                        int nombre;
                        int maxiNombre;
                        if (listNomCentre.length == 2) {
                            nombre = Integer.parseInt(listNomCentre[1]);
                        } else {
                            nombre = Integer.parseInt(listNomCentre[1] + listNomCentre[2]);
                        }
                        if (listMaxiNomCentre.length == 2) {
                            maxiNombre = Integer.parseInt(listMaxiNomCentre[1]);
                        } else {
                            maxiNombre = Integer.parseInt(listMaxiNomCentre[1] + listMaxiNomCentre[2]);
                        }
                        if (maxiNombre > nombre) {
                            maxiTempsIndice = i;
                        }
                    }
                }
                sommetsTraites.put(fileAttente.get(maxiTempsIndice)[0], true); // je marque le sommet qui va etre traité
                fileAttente.add(new String[]{fileAttente.get(maxiTempsIndice)[0], String.valueOf(fileAttente.get(maxiTempsIndice)[1])});
                fileAttente.remove(maxiTempsIndice);
            }
        }
        return res;
    }

    public HashMap<String, LinkedHashMap<String, Double>> tousLesPlusCourtCheminDistance(String nomSommet){

        HashMap<String, LinkedHashMap<String, Double>> res = new HashMap<>();//Hash map qui permet de stocker les chemins les plus courts
        Map<String, Boolean> sommetsTraites = new HashMap<>();//Hash map permettant de marquer les sommet traiter
        res.put(nomSommet, new LinkedHashMap<>());
        res.get(nomSommet).put(nomSommet, 0.0);//le premier centre(sommet) a une temps de 0
        //on itere sur tout les centres afin de les mettre a false -> les sommets ne sont pas encore marquer
        this.getAllSommetMaillons().forEach(Centre -> {
            sommetsTraites.put(Centre.getSommet().getNom(), false);
        });
        sommetsTraites.put(nomSommet, true);//le premier centre est le debut donc il traiter -> true
        List<String[]> fileAttente = new ArrayList<>();//creation d'une liste permettant d'ajouter les sommet traiter
        fileAttente.add(new String[]{nomSommet, "0"});//ajoute le premier sommet avec comme temps 0
        String[] donnee;
        while (!(fileAttente.isEmpty())) {//tant que cette liste n'est pas vide
            donnee = fileAttente.get(fileAttente.size() - 1);//sauvegarde la temps et le centre de la fin de la file dans la variable donnee
            fileAttente.remove(fileAttente.size() - 1);//supprime ces cet element
            String centre = donnee[0];//donne[0] vaut le nom du centre
            double dist = Double.parseDouble(donnee[1]);//donne[1] vaut la temps de type String convertie en double

            LienMaillon voisin = this.returnSommet(centre).getTeteLien();
            while (voisin != null) {
                String nomVoisin = voisin.getBatiment().getNom();
                if (!sommetsTraites.get(nomVoisin)) {
                    if (!res.containsKey(nomVoisin)) {
                        // On arrive à cette endroit si nomVoisin n'a pas encore de chemin
                        res.put(nomVoisin, new LinkedHashMap<>(res.get(centre)));
                        res.get(nomVoisin).put(nomVoisin, (voisin.getDistance()) + dist);
                        fileAttente.add(new String[]{nomVoisin, String.valueOf((voisin.getDistance()) + dist)});

                    } else {
                        // si nomVoisin a déjà un chemin
                        LinkedHashMap<String, Double> chemin = res.get(nomVoisin);
                        Double lastFiabCentreDansChemin = null;
                        String lastNomCentreDansChemin = null;
                        for (Map.Entry<String, Double> centreChemin : chemin.entrySet()) {
                            // Dans cette boucle on récupère l'extrémité du chemin <centre1, nomVoisin>
                            lastNomCentreDansChemin = centreChemin.getKey(); // le nom de l'extrémité
                            lastFiabCentreDansChemin = centreChemin.getValue(); // la fiabilité de l'extrémité
                        }
                        if (lastFiabCentreDansChemin < (voisin.getDistance()) + dist) {
                            // Si on arrive ici, ca veut dire que le chemin initial entre le centre1 et nomVoisin n'est pas le plus fiable a ce moment la
                            res.put(nomVoisin, new LinkedHashMap<>(res.get(donnee[0])));
                            res.get(nomVoisin).put(lastNomCentreDansChemin, (voisin.getDistance()) + dist);
                            // Donc on créer le nouveau chemin le plus fiable entre centre1 et nomVoisin

                            // Comme il y a un nouveau chemin, ca veut dire que la fileAttente est obsolete car elle n'est pas à jour
                            // Grace a la boucle ci dessous, on retire l'ancien nomVoisin puis on le remplace par le nouveau avec sa nouvelle fiabilité
                            int i = 0;
                            boolean check = false;
                            while (!check && i < fileAttente.size()) {
                                if (fileAttente.get(i)[0].equals(nomVoisin)) {
                                    check = true;
                                    fileAttente.remove(i);
                                    fileAttente.add(new String[]{nomVoisin, String.valueOf((voisin.getDistance()) + dist)});
                                }
                                i++;
                            }
                        }

                    }
                }
                voisin = voisin.getSuivant();
            }
            if (fileAttente.size() >= 2) {
                // si on arrive ca veut dire qu'il faut mettre le sommetTraite le plus fiable tout en haut de la liste pour la traité en premier
                int maxiDistIncide = 0;
                for (int i = 1; i < fileAttente.size(); i++) {
                    if (Double.parseDouble(fileAttente.get(i)[1]) < Double.parseDouble(fileAttente.get(maxiDistIncide)[1])) {
                        // si on arrive ici, ca veut dire qu'on a trouvé le plus long au ieme moment
                        maxiDistIncide = i;
                    } else if (Double.parseDouble(fileAttente.get(i)[1]) == Double.parseDouble(fileAttente.get(maxiDistIncide)[1])) {
                        // si on arrive ici, ca veut dire que deux sommets ont la même long donc je met en priorité le plus petit sommet (ex: D'abord S9 puis S15, et pas S15 puis S9)
                        String[] listNomCentre = fileAttente.get(i)[0].split("");
                        String[] listMaxiNomCentre = fileAttente.get(maxiDistIncide)[0].split("");
                        int nombre;
                        int maxiNombre;
                        if (listNomCentre.length == 2) {
                            nombre = Integer.parseInt(listNomCentre[1]);
                        } else {
                            nombre = Integer.parseInt(listNomCentre[1] + listNomCentre[2]);
                        }
                        if (listMaxiNomCentre.length == 2) {
                            maxiNombre = Integer.parseInt(listMaxiNomCentre[1]);
                        } else {
                            maxiNombre = Integer.parseInt(listMaxiNomCentre[1] + listMaxiNomCentre[2]);
                        }
                        if (maxiNombre > nombre) {
                            maxiDistIncide = i;
                        }
                    }
                }
                sommetsTraites.put(fileAttente.get(maxiDistIncide)[0], true); // je marque le sommet qui va etre traité
                fileAttente.add(new String[]{fileAttente.get(maxiDistIncide)[0], String.valueOf(fileAttente.get(maxiDistIncide)[1])});
                fileAttente.remove(maxiDistIncide);
            }
        }
        return res;
    }
    /**
     * Retourne le sommet le plus court en distance du type donné en paramètre
     * @param nomSommet : le nom du sommet de départ
     * @param type : le type de sommet que l'on recherche
     * @return le nom du sommet le plus court en distance du type donné en paramètre
     */
    public String getPlusCourtDistanceByType(String nomSommet, String type) {

        HashMap<String, LinkedHashMap<String, Double>> plusCourtsChemins = tousLesPlusCourtCheminDistance(nomSommet);
        //appel de la fonction du dijkstra
        double distanceMin = Double.MAX_VALUE;
        String sommetPlusProche = null;

        for (String sommet : plusCourtsChemins.keySet()) {
            //on parcours tous les plus courts chemins
            LinkedHashMap<String, Double> chemin = plusCourtsChemins.get(sommet);
            double distance = chemin.get(nomSommet);

            if (distance < distanceMin) {
                //on parcours les deux distances
                Sommet sommetActuel = returnSommet(sommet);
                if (sommetActuel != null && sommetActuel.getType().equals(type)) {//on vérifie que le sommet correspond bien au type
                    distanceMin = distance;
                    sommetPlusProche = sommet;
                }
            }
        }

        return sommetPlusProche;
    }

    /**
     * Retourne le sommet le plus fiable du type donné en paramètre
     * @param nomSommet : le nom du sommet de départ
     * @param type : le type de sommet que l'on recherche
     * @return le nom du sommet le plus fiable du type donné en paramètre
     */
    public String getPlusFiableByType(String nomSommet, String type) {

        HashMap<String, LinkedHashMap<String, Double>> plusCourtsChemins = tousLesPlusCourtCheminFiabilite(nomSommet);
        //appel de la fonction du dijkstra
        double fiabiliteMax = 0.0;
        double distanceMin = Double.MAX_VALUE;
        String sommetPlusProche = null;

        for (String sommet : plusCourtsChemins.keySet()) {
            //on parcours tous les plus chemins les plus fiables
            LinkedHashMap<String, Double> chemin = plusCourtsChemins.get(sommet);
            double distance = chemin.get(nomSommet);
            double fiabilite = getFinalAttribut(chemin);

            if (fiabilite > fiabiliteMax || (fiabilite == fiabiliteMax && distance < distanceMin)) {
                //on parcours les fiabilités
                Sommet sommetActuel = returnSommet(sommet);
                if (sommetActuel != null && sommetActuel.getType().equals(type)) {//on vérifie que le sommet correspond bien au type
                    fiabiliteMax = fiabilite;
                    distanceMin = distance;
                    sommetPlusProche = sommet;
                }
            }
        }

        return sommetPlusProche;
    }

    /**
     * Retourne le sommet le plus rapide du type donné en paramètre
     * @param nomSommet : le nom du sommet de départ
     * @param type : le type de sommet que l'on recherche
     * @return le nom du sommet le plus rapide du type donné en paramètre
     */
    public String getPlusCourtTempsByType(String nomSommet, String type) {

        HashMap<String, LinkedHashMap<String, Double>> plusCourtsChemins = tousLesPlusCourtCheminTemps(nomSommet);
        //appel de la fonction du dijkstra
        double tempsMin = Double.MAX_VALUE;
        double distanceMin = Double.MAX_VALUE;
        String sommetPlusProche = null;

        for (String sommet : plusCourtsChemins.keySet()) {
            //on parcours tous les chemins les plus rapides
            LinkedHashMap<String, Double> chemin = plusCourtsChemins.get(sommet);
            double temps = getFinalAttribut(chemin);
            double distance = chemin.get(nomSommet);

            if (temps < tempsMin || (temps == tempsMin && distance < distanceMin)) {
                //on compare les temps
                Sommet sommetActuel = returnSommet(sommet);
                if (sommetActuel != null && sommetActuel.getType().equals(type)) {//on vérifie que le sommet correspond bien au type
                    tempsMin = temps;
                    distanceMin = distance;
                    sommetPlusProche = sommet;
                }
            }
        }

        return sommetPlusProche;
    }

    /**
     * Recherche une route entre un site de départ et un site d'arrivée en passant par des dispensaires spécifiques.
     * @param siteDepart Le site de départ.
     * @param siteArrivee Le site d'arrivée.
     * @return Un HashMap représentant les routes possibles avec les distances entre les sites.
     *         Retourne null si le site de départ ou d'arrivée n'existe pas dans le graphe.
     */
    public HashMap<String, LinkedHashMap<String, Double>> trouverRouteAvecDispensaires(String siteDepart, String siteArrivee) {
        // Initialisation des variables et structures de données
        HashMap<String, LinkedHashMap<String, Double>> routes = new HashMap<>();
        Sommet sommetDepart = returnSommet(siteDepart);
        Sommet sommetArrivee = returnSommet(siteArrivee);

        if (sommetDepart == null || sommetArrivee == null) {
            System.out.println("Le site de départ ou d'arrivée n'existe pas dans le graphe.");
            return null;
        }

        Set<String> visited = new HashSet<>();
        visited.add(siteDepart);

        boolean dispensaireNutritionTrouve = false;
        boolean dispensaireBlocTrouve = false;
        boolean dispensaireMaterniteTrouve = false;

        LinkedHashMap<String, Double> chemin = new LinkedHashMap<>();
        chemin.put(siteDepart, 0.0);
        routes.put(siteDepart, chemin);

        return trouverRouteAvecDispensairesRecursif(siteDepart, siteArrivee, visited, dispensaireNutritionTrouve, dispensaireBlocTrouve, dispensaireMaterniteTrouve, routes);
    }

    /**
     * Méthode récursive pour trouver une route avec des dispensaires spécifiques.
     * @param sommetActuel Le sommet actuel lors de la récursion.
     * @param siteArrivee Le site d'arrivée.
     * @param visited Les sommets visités lors de la recherche.
     * @param dispensaireNutritionTrouve Indique si un dispensaire de nutrition a été trouvé sur la route.
     * @param dispensaireBlocTrouve Indique si un dispensaire de bloc a été trouvé sur la route.
     * @param dispensaireMaterniteTrouve Indique si un dispensaire de maternité a été trouvé sur la route.
     * @param routes Les routes actuelles avec leurs distances.
     * @return Un HashMap représentant les routes possibles avec les distances entre les sites.
     *         Retourne null si aucune route n'a été trouvée.
     */
    private HashMap<String, LinkedHashMap<String, Double>> trouverRouteAvecDispensairesRecursif(String sommetActuel, String siteArrivee, Set<String> visited, boolean dispensaireNutritionTrouve, boolean dispensaireBlocTrouve, boolean dispensaireMaterniteTrouve, HashMap<String, LinkedHashMap<String, Double>> routes) {
        // Condition de fin : si le sommet actuel est le site d'arrivée et tous les dispensaires ont été trouvés
        if (sommetActuel.equals(siteArrivee) && dispensaireNutritionTrouve && dispensaireBlocTrouve && dispensaireMaterniteTrouve) {
            return routes;
        }

        // Parcours des liens du sommet actuel
        LienMaillon lienCourant = returnSommet(sommetActuel).getTeteLien();
        while (lienCourant != null) {
            String nomVoisin = lienCourant.getBatiment().getNom();

            if (!visited.contains(nomVoisin)) {
                visited.add(nomVoisin);

                // Vérification du type de bâtiment voisin
                boolean estDispensaireNutrition = lienCourant.getBatiment().getType().equalsIgnoreCase("N");
                boolean estDispensaireBloc = lienCourant.getBatiment().getType().equalsIgnoreCase("O");
                boolean estDispensaireMaternite = lienCourant.getBatiment().getType().equalsIgnoreCase("M");

                // Mise à jour de la route actuelle avec le voisin
                LinkedHashMap<String, Double> chemin = new LinkedHashMap<>(routes.get(sommetActuel));
                chemin.put(nomVoisin, lienCourant.getDistance());
                routes.put(nomVoisin, chemin);


                // Appel récursif avec le voisin comme sommet actuel
                HashMap<String, LinkedHashMap<String, Double>> result = trouverRouteAvecDispensairesRecursif(nomVoisin, siteArrivee, visited, dispensaireNutritionTrouve || estDispensaireNutrition, dispensaireBlocTrouve || estDispensaireBloc, dispensaireMaterniteTrouve || estDispensaireMaternite, routes);

                // Si une route a été trouvée, la retourner
                if (result != null) {
                    return result;
                }

                // Retour en arrière : suppression du voisin de la liste des sommets visités et des routes
                visited.remove(nomVoisin);
                routes.remove(nomVoisin);
            }

            lienCourant = lienCourant.getSuivant();
        }

        return null;
    }
    /**
     * Trouve une route traversant exactement un nombre donné de dispensaires de chaque catégorie,
     * entre deux sites spécifiés par l'utilisateur.
     *
     * @param siteDepart     Le site de départ de la route.
     * @param siteArrivee    Le site d'arrivée de la route.
     * @param nbNutrition    Le nombre de dispensaires de nutrition requis dans la route.
     * @param nbBloc         Le nombre de dispensaires de bloc requis dans la route.
     * @param nbMaternite    Le nombre de dispensaires de maternité requis dans la route.
     * @return               Les routes trouvées avec les dispensaires de chaque catégorie.
     */
    public HashMap<String, LinkedHashMap<String, Double>> trouverRouteAvecDispensaires2(String siteDepart, String siteArrivee, int nbNutrition, int nbBloc, int nbMaternite) {
        HashMap<String, LinkedHashMap<String, Double>> routes = new HashMap<>();
        Sommet sommetDepart = returnSommet(siteDepart);
        Sommet sommetArrivee = returnSommet(siteArrivee);

        if (sommetDepart == null || sommetArrivee == null) {
            System.out.println("Le site de départ ou d'arrivée n'existe pas dans le graphe.");
            return null;
        }

        Set<String> visited = new HashSet<>();
        visited.add(siteDepart);

        LinkedHashMap<String, Double> chemin = new LinkedHashMap<>();
        chemin.put(siteDepart, 0.0);
        routes.put(siteDepart, chemin);

        return trouverRouteAvecDispensairesRecursif2(siteDepart, siteArrivee, visited, 0, 0, 0, routes, nbNutrition, nbBloc, nbMaternite);
    }

    /**
     * Méthode récursive pour trouver une route traversant exactement un nombre donné de dispensaires de chaque catégorie.
     *
     * @param sommetActuel       Le sommet actuel lors de l'exploration récursive.
     * @param siteArrivee        Le site d'arrivée de la route.
     * @param visited            Les sites déjà visités dans la route.
     * @param nbNutrition        Le nombre de dispensaires de nutrition déjà rencontrés dans la route.
     * @param nbBloc             Le nombre de dispensaires de bloc déjà rencontrés dans la route.
     * @param nbMaternite        Le nombre de dispensaires de maternité déjà rencontrés dans la route.
     * @param routes             Les routes trouvées jusqu'à présent.
     * @return                   Les routes trouvées avec les dispensaires de chaque catégorie.
     */
    private HashMap<String, LinkedHashMap<String, Double>> trouverRouteAvecDispensairesRecursif2(String sommetActuel, String siteArrivee, Set<String> visited, int nbNutrition, int nbBloc, int nbMaternite, HashMap<String, LinkedHashMap<String, Double>> routes, int nbNutritionDemande, int nbBlocDemande, int nbMaterniteDemande) {
        if (sommetActuel.equals(siteArrivee)) {
            if (nbNutrition == nbNutritionDemande && nbBloc == nbBlocDemande && nbMaternite == nbMaterniteDemande) {
                return routes;
            }
            return null;
        }

        LienMaillon lienCourant = returnSommet(sommetActuel).getTeteLien();
        while (lienCourant != null) {
            String nomVoisin = lienCourant.getBatiment().getNom();

            if (!visited.contains(nomVoisin)) {
                visited.add(nomVoisin);

                boolean estDispensaireNutrition = lienCourant.getBatiment().getType().equalsIgnoreCase("N");
                boolean estDispensaireBloc = lienCourant.getBatiment().getType().equalsIgnoreCase("O");
                boolean estDispensaireMaternite = lienCourant.getBatiment().getType().equalsIgnoreCase("M");

                int nouveauNbNutrition = nbNutrition + (estDispensaireNutrition ? 1 : 0);
                int nouveauNbBloc = nbBloc + (estDispensaireBloc ? 1 : 0);
                int nouveauNbMaternite = nbMaternite + (estDispensaireMaternite ? 1 : 0);

                if (nouveauNbNutrition <= nbNutritionDemande && nouveauNbBloc <= nbBlocDemande && nouveauNbMaternite <= nbMaterniteDemande) {
                    LinkedHashMap<String, Double> chemin = routes.get(sommetActuel);
                    chemin.put(nomVoisin, lienCourant.getDistance());
                    routes.put(nomVoisin, new LinkedHashMap<>(chemin));

                    HashMap<String, LinkedHashMap<String, Double>> result = trouverRouteAvecDispensairesRecursif2(nomVoisin, siteArrivee, visited, nouveauNbNutrition, nouveauNbBloc, nouveauNbMaternite, routes, nbNutritionDemande, nbBlocDemande, nbMaterniteDemande);

                    if (result != null) {
                        return result;
                    }

                    visited.remove(nomVoisin);
                    routes.get(sommetActuel).remove(nomVoisin);
                }
            }

            lienCourant = lienCourant.getSuivant();
        }

        return null;
    }

    /**
     * Retourne la complexité de l'algorithme du chemin le plus fiable
     * @return la complexité de l'algorithme du chemin le plus fiable
     */
    public String ComplexiteFiab(GrapheList graphe) {
        int V = graphe.nbSommet();
        int E = graphe.nbLiens();
        String complexite = "O((|"+V+"| + |"+E+"|)² log |"+V+"|)";
        return complexite;
    }

    public GrapheList supprimerAllByType(String type, String filename){
        GrapheList newGraphe = new GrapheList();
        newGraphe.loadInGraphe(filename);
        SommetMaillon tmp = newGraphe.teteGraphe;
        while(tmp != null){
            if(!tmp.getSommet().getType().equalsIgnoreCase(type)){
                newGraphe.supprimerSommet(tmp.getSommet().getNom());
            }
            tmp = tmp.getSuivant();
        }
        return newGraphe;
    }

}
