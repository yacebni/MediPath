package fr.student.views;

import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import com.formdev.flatlaf.themes.FlatMacLightLaf;
import fr.student.algo.GrapheList;
import fr.student.algo.LienMaillon;
import fr.student.algo.Sommet;
import fr.student.algo.Utils;
import fr.student.views.modals.ListerAreteModal;
import fr.student.views.modals.ListerSommetModal;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class Interface extends JFrame implements ComponentListener {
    public static int WIDTH = 1280;
    public static int HEIGHT = 720;

    private static String[] TYPE = {"O", "M", "N"};
    public static String[] MODES = {"Fiabilite", "Temps", "Distance"};

    private GrapheList graphe;
    private GrapheCanva canva;
    private JPanel ecran;
    private String selectedFile;
    private String selectedFileName;
    private String selectedMode;
    private String selectedSommetDep;
    private String selectedSommetArr;
    private String selectedSommetDep2;
    private JToolBar toolbar;
    private JScrollPane cheminScrollPane;
    private JComboBox modeDropDown;
    private JLabel cheminLabel;
    private Integer pVoisin;
    private JScrollPane voisinScrollPane;
    private Color currentSommetsColor;
    private JComboBox toolbar2TypeDropdown;
    private JComboBox toolbar2ModeDropdown;
    private String selectedSommet;
    private String selectedType2;
    private String selectedMode2;
    private JScrollPane cheminScrollPane2;
    private String theme;
    public JPanel interactPanel;
    private Boolean filtred = false;
    private JRadioButtonMenuItem filtrerAucun;

    public Interface(GrapheList graphe){
        if(graphe != null){
            this.graphe = graphe;
        }else{
            this.graphe = graphe;
        }
        Utils.saveGrapheInFile(graphe, ".save-sae/fullyGraphe.csv");

        // set window properties
        super.setTitle("Graphe - IHM");
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        getContentPane().addComponentListener(this);
        this.setSize(WIDTH, HEIGHT);
        this.setMinimumSize(new Dimension(WIDTH, HEIGHT));
        this.setLocationRelativeTo(null); // centrer la fenetre
        this.setResizable(true);
        this.setIconImage(new ImageIcon("common/imgs/icon.png").getImage());
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE); // fermer le processus quand on ferme les fenetres

        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                setPreferredSize(new Dimension(getWidth(), getHeight()));
                Interface.WIDTH = getWidth();
                Interface.HEIGHT = getHeight();
                for (SommetComponent s : canva.getSommets()) {
                    HashMap<String, Integer> newCoords = s.tooBigCoords(s.getX(), s.getY(), canva.getSize());
                    s.setCoords(newCoords.get("x"), newCoords.get("y"));
                }
                canva.repaint();
            }
        });

        selectedFile = null;
        this.currentSommetsColor = Color.decode("#0094FF");
        initComponents();
        initMenu();

        // theme
        try {
            UIManager.setLookAndFeel(new FlatMacLightLaf());
            SwingUtilities.updateComponentTreeUI(this);
        } catch (UnsupportedLookAndFeelException ex) {
            try {
                UIManager.setLookAndFeel(new NimbusLookAndFeel());
            } catch (UnsupportedLookAndFeelException exc) {
                System.out.println("NimbusLookAndFeel is not supported");
            }
            System.out.println("FlatMacLightLaf is not supported");
        }
        this.theme = "light";
    }

    /**
     * Initialisation des composants de la fenetre
     */
    private void initComponents(){

        this.ecran = (JPanel) this.getContentPane();
        ecran.setLayout(new BorderLayout());

        JPanel graphePanel = GraphePanel(); // Panel d'affichage du graphe
        this.interactPanel = interactPanel(); // Panel d'affichage des boutons et des menus

        // Mettre les boutons et tout dans une toolbar pour le dock
        this.toolbar = new JToolBar("Toolbar");
        toolbar.setFloatable(true);
        toolbar.add(interactPanel);

        ecran.add(graphePanel, BorderLayout.CENTER);
        ecran.add(toolbar, BorderLayout.EAST);
    }

    /**
     * Panel d'affichage du graphe
     * @return JPanel le canva ou sera dessine le graphe
     */
    private JPanel GraphePanel(){

        JPanel panel = new JPanel(new BorderLayout());
        this.canva = new GrapheCanva(this);
        String title;
        if(selectedFile != null && selectedFileName != null){
            title = "Graphe - " + selectedFileName;
        }else{
            title = "Graphe - IHM";
        }

        JPanel centerTitle = generateCenterLabel(title, 20);
        panel.setSize(new Dimension(WIDTH-WIDTH/4, HEIGHT));
        this.canva.setSize(new Dimension(panel.getWidth(), panel.getHeight()));

        // remplissage du graphe
        this.graphe.getAllSommetName().forEach(sommetName -> {
            // creation des sommets
            SommetComponent sommet = new SommetComponent(sommetName, (int) (Math.random()*panel.getWidth()), (int) (Math.random()*panel.getHeight()), panel.getSize(), canva);
            sommet.setColor(sommet.getDefaultLightModeColor());
            this.canva.addSommet(sommet);
        });
        createLiens(); // creation des liens
        repaint();


        panel.add(centerTitle, BorderLayout.NORTH);
        panel.add(this.canva, BorderLayout.CENTER);
        return panel;
    }

    /**
     * Methode appelee lors d'un clic sur le canva
     * @param sommets la liste des sommets du graphe
     * @param liens la liste des liens du graphe
     */
    public void clickingCanva(List<SommetComponent> sommets, List<LienComponent> liens){

        this.toolbar.removeAll();
        this.toolbar.add(interactPanel());
        this.toolbar.repaint();
        this.selectedSommet = null;

        for (SommetComponent sommetComponent : sommets) {
            if(theme.equalsIgnoreCase("light")){
                sommetComponent.setColor(sommetComponent.defaultLightModeColor);
            }else{
                sommetComponent.setColor(sommetComponent.defaultDarkModeColor);
            }
        }
        for (LienComponent lienComponent : liens) {
            if(theme.equalsIgnoreCase("light")){
                lienComponent.setColor(lienComponent.selectedLightModeColor);
            }else{
                lienComponent.setColor(lienComponent.selectedDarkModeColor);
            }
        }

        repaint();
        pack();

    }

    /**
     * Methode appelee lors d'un clic sur un sommet - panel interactif avec le sommet
     * @param sname le nom du sommet clique
     * @param sommets la liste des sommets du graphe
     * @param liens la liste des liens du graphe
     * @return JPanel le panel interactif avec le sommet
     */
    public void clickingSommetInterface(String sname, List<SommetComponent> sommets, List<LienComponent> liens){

        JPanel panel = sommetInteractPanel(sname);
        this.toolbar.removeAll();
        this.toolbar.add(panel);

        // colorier le sommet cliqué en fonction du theme
        for (SommetComponent sommetComponent : sommets) {
           if(theme.equalsIgnoreCase("light")){
               if(sommetComponent.getNomSommet().equals(sname)){
                   sommetComponent.setColor(sommetComponent.defaultSelectedLightModeColor);
               }else{
                   sommetComponent.setColor(sommetComponent.defaultUnselectedLightMode);
               }
           }else{
               if(sommetComponent.getNomSommet().equals(sname)){
                   sommetComponent.setColor(sommetComponent.defaultSelectedDarkModeColor);
               }else{
                   sommetComponent.setColor(sommetComponent.defaultUnselectedDarkMode);
               }

           }
        }
        canva.repaint();
        for(LienComponent lien : liens){
            if(theme.equalsIgnoreCase("dark")){
                if(lien.getsA().getNomSommet().equalsIgnoreCase(sname)){
                    lien.setColor(lien.selectedDarkModeColor);
                    lien.getsB().setColor(lien.getsB().defaultVoisinColorDarkMode);
                }
                else if(lien.getsB().getNomSommet().equalsIgnoreCase(sname)){
                    lien.setColor(lien.selectedDarkModeColor);
                    lien.getsA().setColor(lien.getsB().defaultVoisinColorDarkMode);
                }else{
                    lien.setColor(lien.unselectedDarkModeColor);
                }
            }else{
                if(lien.getsA().getNomSommet().equalsIgnoreCase(sname)){
                    lien.setColor(lien.selectedLightModeColor);
                    lien.getsB().setColor(lien.getsB().defaultVoisinColorLightMode);
                }
                else if(lien.getsB().getNomSommet().equalsIgnoreCase(sname)){
                    lien.setColor(lien.selectedLightModeColor);
                    lien.getsA().setColor(lien.getsB().defaultVoisinColorLightMode);
                }else{
                    lien.setColor(lien.unselectedLightModeColor);
                }
            }
        }

        pack();
    }

    /**
     * Methode qui genere le panel interactif avec le sommet
     * @param sname le nom du sommet
     * @return JPanel le panel interactif avec le sommet
     */
    public JPanel sommetInteractPanel(String sname){

        this.selectedSommet = sname;
        JPanel panel = new JPanel(new BorderLayout());

        // Top of the toolbar
        JPanel title = generateCenterLabel(sname, 20);

        JPanel content = new JPanel(new GridLayout(2, 1, 5, 5));

        JPanel render = new JPanel(new BorderLayout());
        JPanel choose = new JPanel(new GridLayout(2, 2));
        JPanel modeLabelTitle = generateCenterLabel("Mode : ", 16);
        JPanel typeLabelTitle = generateCenterLabel("Type : ", 16);

        // dropdowns / combobox
        this.toolbar2TypeDropdown = new JComboBox(TYPE);
        this.toolbar2TypeDropdown.setSelectedItem(TYPE[0]);
        this.toolbar2TypeDropdown.setSize(new Dimension(((WIDTH/4)-20)/2, 30));
        this.toolbar2TypeDropdown.setPreferredSize(new Dimension(((WIDTH/4)-20)/2, 30));
        this.toolbar2ModeDropdown = new JComboBox(MODES);
        this.toolbar2ModeDropdown.setSelectedItem(MODES[0]);
        this.toolbar2ModeDropdown.setSize(new Dimension(((WIDTH/4)-20)/2, 30));
        this.toolbar2ModeDropdown.setPreferredSize(new Dimension(((WIDTH/4)-20)/2, 30));

        this.cheminScrollPane2 = new JScrollPane();
        this.cheminScrollPane2.setPreferredSize(new Dimension((WIDTH/4)-40, 100));
        this.cheminScrollPane2.setSize(new Dimension((WIDTH/4)-40, 100));

        choose.add(typeLabelTitle);
        choose.add(modeLabelTitle);
        choose.add(this.toolbar2TypeDropdown);
        choose.add(this.toolbar2ModeDropdown);
        render.add(choose, BorderLayout.NORTH);
        render.add(this.cheminScrollPane2, BorderLayout.CENTER);
        content.add(render);
        panel.add(title, BorderLayout.NORTH);
        panel.add(content, BorderLayout.CENTER);

        initToolbar2ModeListener();

        // Bottom of the toolbar

        panel.setBorder( BorderFactory.createEmptyBorder(10,20,10, 20) );
        return panel;

    }

    /**
     * Methode qui initialise les listeners sur les dropdowns du panel interactif avec le sommet
     */
    private void initToolbar2ModeListener() {

        this.toolbar2TypeDropdown.addActionListener(e -> {
            String nomSommet;
            this.selectedType2 = toolbar2TypeDropdown.getSelectedItem().toString();
            if(selectedMode2 == MODES[0]){
                nomSommet = graphe.getPlusFiableByType(this.selectedSommet, this.selectedType2);
                LinkedHashMap<String, Double> chemin = this.graphe.tousLesPlusCourtCheminFiabilite(this.selectedSommet).get(nomSommet);
                this.loadInScrollPanelChemin(chemin, true, this.cheminScrollPane2);
            }
            if(selectedMode2 == MODES[1]){
                nomSommet = graphe.getPlusCourtTempsByType(this.selectedSommet, this.selectedType2);
                LinkedHashMap<String, Double> chemin = this.graphe.tousLesPlusCourtCheminTemps(this.selectedSommet).get(nomSommet);
                this.loadInScrollPanelChemin(chemin, false, this.cheminScrollPane2);
            }
            if(selectedMode2 == MODES[2]){
                nomSommet = graphe.getPlusCourtDistanceByType(this.selectedSommet, this.selectedType2);
                LinkedHashMap<String, Double> chemin = this.graphe.tousLesPlusCourtCheminDistance(this.selectedSommet).get(nomSommet);
                this.loadInScrollPanelChemin(chemin, null, this.cheminScrollPane2);
            }

        });
        this.toolbar2ModeDropdown.addActionListener(e -> {
            String nomSommet;
            this.selectedMode2 = toolbar2ModeDropdown.getSelectedItem().toString();
            if(selectedMode2 == MODES[0]){
                nomSommet = graphe.getPlusFiableByType(this.selectedSommet, this.selectedType2);
                LinkedHashMap<String, Double> chemin = this.graphe.tousLesPlusCourtCheminFiabilite(this.selectedSommet).get(nomSommet);
                this.loadInScrollPanelChemin(chemin, true, this.cheminScrollPane2);
            }
            if(selectedMode2 == MODES[1]){
                nomSommet = graphe.getPlusCourtTempsByType(this.selectedSommet, this.selectedType2);
                LinkedHashMap<String, Double> chemin = this.graphe.tousLesPlusCourtCheminTemps(this.selectedSommet).get(nomSommet);
                this.loadInScrollPanelChemin(chemin, false, this.cheminScrollPane2);

            }
            if(selectedMode2 == MODES[2]){
                nomSommet = graphe.getPlusCourtDistanceByType(this.selectedSommet, this.selectedType2);
                LinkedHashMap<String, Double> chemin = this.graphe.tousLesPlusCourtCheminDistance(this.selectedSommet).get(nomSommet);
                this.loadInScrollPanelChemin(chemin, null, this.cheminScrollPane2);

            }
        });
    }

    // panel interactif avec le graphe
    public JPanel interactPanel(){
        /**
        * Methode qui initialise le panel interactif avec le graphe
        * @return JPanel
         */
        JPanel panel = new JPanel(new GridLayout(2, 1, 5, 5));

        // Top of the toolbar
        JPanel topPanel = new JPanel(new BorderLayout());
        JPanel selectPanel = new JPanel(new BorderLayout());

        // dropdown / combobox mode
        this.modeDropDown = new JComboBox(MODES);
        modeDropDown.setSelectedItem(MODES[0]);
        modeDropDown.setPreferredSize(new Dimension((WIDTH/4)-40, 30)); // calculer le chemin
        modeDropDown.setSize(new Dimension((WIDTH/4)-40, 30));
        selectPanel.add(modeDropDown, BorderLayout.NORTH);

        // dropdown / combobox sommet
        ArrayList<String> sommets = this.graphe.getAllSommetName();
        JComboBox sommetDepDropDown = new JComboBox(this.graphe.getAllSommetName().toArray());
        sommetDepDropDown.setSelectedItem(sommets.get(0));
        this.selectedSommetDep = sommets.get(0); // sommet de depart par defaut
        JComboBox sommetArrDropDown = new JComboBox(this.graphe.getAllSommetName().toArray());
        sommetArrDropDown.setSelectedItem(sommets.get(sommets.size()-1));
        this.selectedSommetArr = sommets.get(sommets.size()-1); // sommet d'arrivee par defaut

        // taille des dropdown
        sommetDepDropDown.setPreferredSize(new Dimension(((WIDTH/4)-20)/2, 30));
        sommetArrDropDown.setPreferredSize(new Dimension(((WIDTH/4)-20)/2, 30));
        sommetDepDropDown.setSize(new Dimension(((WIDTH/4)-20)/2, 30));
        sommetArrDropDown.setSize(new Dimension(((WIDTH/4)-20)/2, 30));

        initSommetChoiceDropdownTop(sommetDepDropDown, true);
        initSommetChoiceDropdownTop(sommetArrDropDown, false);

        // grille pour mettre les labels au dessus
        JPanel choseSommetPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        JPanel depLabel = generateCenterLabel("Sommet de départ", 15);
        JPanel arrLabel = generateCenterLabel("Sommet d'arrivée", 15);
        choseSommetPanel.add(depLabel);
        choseSommetPanel.add(arrLabel);
        choseSommetPanel.add(sommetDepDropDown);
        choseSommetPanel.add(sommetArrDropDown);
        selectPanel.add(choseSommetPanel, BorderLayout.CENTER);
        topPanel.add(selectPanel, BorderLayout.NORTH);

        // Scroll pane pour affichage du chemin
        JPanel cheminPanel = new JPanel(new BorderLayout());
        this.cheminLabel = new JLabel("Chemin par " + MODES[0] + " : ");
        cheminPanel.add(cheminLabel, BorderLayout.NORTH);
        Font labelFontChemin = this.cheminLabel.getFont();
        this.cheminLabel.setFont(new Font(labelFontChemin.getName(), Font.PLAIN, 15)); // changer la taille de la police

        this.cheminScrollPane = new JScrollPane();
        cheminScrollPane.setPreferredSize(new Dimension((WIDTH/4)-40, (int) (HEIGHT/4.5)));
        cheminScrollPane.setSize(new Dimension((WIDTH/4)-40, (int) (HEIGHT/4.5)));
        cheminPanel.add(cheminScrollPane, BorderLayout.CENTER);
        topPanel.add(cheminPanel, BorderLayout.CENTER);
        initModeEventListener();


        // Bottom of the toolbar
        JPanel bottomPanel = new JPanel(new BorderLayout());
        JPanel bottomTitleLabel =  generateCenterLabel("Afficher les voisins d'un sommet", 16);
        JPanel topOfBottomPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        topOfBottomPanel.add(bottomTitleLabel);

        // grille pour mettre les labels au dessus
        JPanel labelGrid = new JPanel(new GridLayout(1, 2, 5, 5));
        JPanel sommetVoisinLabel = generateCenterLabel("Sommet", 15);
        JPanel numberVoisinLabel = generateCenterLabel("Max distance", 15);

        // dropdown / combobox sommet
        JPanel dropDownPanel = new JPanel(new GridLayout(1, 2, 0, 0));
        JComboBox sommetVoisinDropDown = new JComboBox(this.graphe.getAllSommetName().toArray());
        sommetVoisinDropDown.setSelectedItem(this.graphe.getAllSommetName().toArray()[0]);
        sommetVoisinDropDown.setPreferredSize(new Dimension(((WIDTH/4)-40)/2, 30)); // calculer le chemin
        sommetVoisinDropDown.setSize(new Dimension(((WIDTH/4)-40)/2, 30));
        initSommetChoiceDropdownBottom(sommetVoisinDropDown); // event listener

        // Number text field
        JTextField numberTextField = new JTextField();
        numberTextField.setPreferredSize(new Dimension(((WIDTH/4)-40)/2, 30)); // calculer le chemin
        numberTextField.setSize(new Dimension(((WIDTH/4)-40)/2, 30));
        this.pVoisin = 1;
        numberTextField.setText(String.valueOf(this.pVoisin));

        // Gestion des changements
        numberTextField.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent ke) {
                String value = numberTextField.getText();
                int l = value.length();
                // si le caractere est un chiffre ou une suppression
                if (ke.getKeyChar() >= '0' && ke.getKeyChar() <= '9' || ke.getKeyChar() == KeyEvent.VK_BACK_SPACE || ke.getKeyChar() == KeyEvent.VK_DELETE) {
                    numberTextField.setEditable(true);
                } else if(ke.getKeyChar() == KeyEvent.VK_ENTER){ // si il appuye sur Entrée / Enter
                    // si la valeur est pas null et superieur à 0
                    if(l != 0){
                        if(Integer.parseInt(value) > 0){
                            pVoisin = Integer.parseInt(value);
                        }
                    }
                    // coloriage des sommets voisins
                    Map<Integer, List<String>> voisins =  graphe.getVoisinPdistance(sommetVoisinDropDown.getSelectedItem().toString(), pVoisin-1);
                    ArrayList<String> sommets = new ArrayList<String>();
                    for (String entry : voisins.get(voisins.size())) {
                        sommets.add(entry);
                    }
                    for (SommetComponent s : canva.getSommets()){
                        if(sommets.contains(s.getNomSommet())){
                            s.setColor(Color.GREEN);
                        }else{
                            if(theme.equalsIgnoreCase("light")){
                                s.setColor(s.defaultLightModeColor);
                            }else{
                                s.setColor(s.defaultDarkModeColor);
                            }
                        }
                    }
                    // Affichage des voisins dans le scroll pane
                    canva.returnSommetComponent(sommetVoisinDropDown.getSelectedItem().toString()).setColor(Color.ORANGE);

                    loadInScrollPanelVoisin(voisins);
                }else{
                    numberTextField.setEditable(false);

                }
            }
        });

        // ajout de l'affichage
        this.voisinScrollPane = new JScrollPane();
        this.voisinScrollPane.setPreferredSize(new Dimension((WIDTH/4)-40, 100));
        this.voisinScrollPane.setSize(new Dimension((WIDTH/4)-40, 100));

        labelGrid.add(sommetVoisinLabel);
        labelGrid.add(numberVoisinLabel);
        dropDownPanel.add(sommetVoisinDropDown);
        dropDownPanel.add(numberTextField);
        topOfBottomPanel.add(labelGrid);
        topOfBottomPanel.add(dropDownPanel);
        bottomPanel.add(topOfBottomPanel, BorderLayout.NORTH);
        bottomPanel.add(voisinScrollPane, BorderLayout.CENTER);

        panel.add(topPanel);
        panel.add(bottomPanel);
        panel.setBorder( BorderFactory.createEmptyBorder(10,20,10, 20) );
        return panel;
    }

    /**
     * Génère un panel avec un label centré
     * @param content : contenu du label
     * @param size : taille de la police
     * @return JPanel le panel avec le label centré
     */
    public JPanel generateCenterLabel(String content, Integer size){

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel label = new JLabel(content);
        Font labelFont = label.getFont();
        label.setFont(new Font(labelFont.getName(), Font.PLAIN, size)); // changer la taille de la police
        panel.add(label);
        return panel;
    }

    /**
     * Initialise le menu
     */
    public void initMenu() {

        JMenuBar menuBar = new JMenuBar();
        JMenu file = new JMenu("File");
        JMenu appearance = new JMenu("Appearance");
        JMenu info = new JMenu("Info");


        JMenuItem open = new JMenuItem("Open");
        file.add(open);
        JMenuItem refresh = new JMenuItem("Refresh");
        file.add(refresh);
        JMenuItem save = new JMenuItem("Save");
        file.add(save);

        // Theme
        JMenu changeTheme = new JMenu("Change theme");
        ButtonGroup themeGroup = new ButtonGroup();
        JRadioButtonMenuItem lightTheme = new JRadioButtonMenuItem("Light");
        lightTheme.setSelected(true);

        themeGroup.add(lightTheme);
        JRadioButtonMenuItem darkTheme = new JRadioButtonMenuItem("Dark");
        themeGroup.add(darkTheme);

        changeTheme.add(lightTheme);
        changeTheme.add(darkTheme);
        appearance.add(changeTheme);

        JMenu filtrer = new JMenu("Filtrer");
        ButtonGroup filtreGroup = new ButtonGroup();
        JRadioButtonMenuItem filtrerM = new JRadioButtonMenuItem("M");
        filtreGroup.add(filtrerM);
        JRadioButtonMenuItem filtrerO = new JRadioButtonMenuItem("O");
        filtreGroup.add(filtrerO);
        JRadioButtonMenuItem filtrerN = new JRadioButtonMenuItem("N");
        filtreGroup.add(filtrerN);
        this.filtrerAucun = new JRadioButtonMenuItem("Aucun");
        filtreGroup.add(filtrerAucun);
        filtrerAucun.setSelected(true);
        filtrer.add(filtrerM);
        filtrer.add(filtrerO);
        filtrer.add(filtrerN);
        filtrer.add(filtrerAucun);
        appearance.add(filtrer);

        // info
        JMenu infoSommet = new JMenu("Sommets");
        JMenuItem infoO = new JMenuItem("Bloc Operatoire");
        JMenuItem infoM = new JMenuItem("Maternite");
        JMenuItem infoN = new JMenuItem("Centre de nutrition");
        infoSommet.add(infoO);
        infoSommet.add(infoM);
        infoSommet.add(infoN);
        info.add(infoSommet);

        JMenuItem infoArete = new JMenuItem("Aretes");
        info.add(infoArete);

        menuBar.add(file);
        menuBar.add(appearance);
        menuBar.add(info);
        this.setJMenuBar(menuBar);

        // complexite djikstra
        JMenuItem complexite = new JMenuItem("Complexite djikstra");
        info.add(complexite);

        // Action listeners
        complexite.addActionListener(e -> {
            // recuperation de la complexite
            String complexiteString = graphe.ComplexiteFiab(graphe);
            // ecriture dans un joptionpane
            JOptionPane complexitePain = new JOptionPane();
            complexitePain.showMessageDialog(this, complexiteString, "Complexite djikstra fiabilite", JOptionPane.PLAIN_MESSAGE);
        });

        // Open file
        open.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setCurrentDirectory(new java.io.File("./common"));
            fileChooser.setDialogTitle("Select a csv");
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            fileChooser.setAcceptAllFileFilterUsed(false);
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Graphe file", "txt", "text", "csv");
            fileChooser.setFileFilter(filter);
            if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                selectedFile = fileChooser.getSelectedFile().getAbsolutePath();
                selectedFileName = fileChooser.getSelectedFile().getName();
                // Clear the screen and graphe
                ecran.removeAll();
                ecran.revalidate();
                ecran.repaint();
                this.pack();
                // nouveau graphe
                this.graphe = new GrapheList();
                this.graphe.loadInGraphe(selectedFile);
                // reecriture de l'écran
                this.initComponents();
                System.out.println("Selected file: " + selectedFile);
                Utils.saveGrapheInFile(graphe, ".save-sae/fullyGraphe.csv");
                System.out.println("Sauvegarde du graphe bien effectué");
            }
            this.filtrerAucun.setSelected(true);
            this.filtred = false;
        });

        // refresh graphe
        refresh.addActionListener(e -> {
            if(selectedFile == null){
                return;
            }
            // Clear the screen and graphe
            ecran.removeAll();
            ecran.revalidate();
            ecran.repaint();
            this.pack();
            // nouveau graphe
            this.graphe = new GrapheList();
            this.graphe.loadInGraphe(selectedFile);
            // reecriture de l'écran
            this.initComponents();
            this.filtrerAucun.setSelected(true);
            this.filtred = false;
        });
        save.addActionListener(e -> {
            // recuperation du dossier ou telecharger le fichier
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int returnVal = fileChooser.showOpenDialog(this);
            if(returnVal == JFileChooser.APPROVE_OPTION){
                String location = fileChooser.getCurrentDirectory().toString();
                if(selectedFileName != null){
                    // sauvegarde du graphe
                    Utils.saveGrapheInFile(graphe, location + "/" + selectedFileName);
                }else{
                    Utils.saveGrapheInFile(graphe, location + "/grapheSaved.csv");
                }
            }
        });

        // Theme
        // mettre le theme claire
        lightTheme.addActionListener(e -> {
            try {
                UIManager.setLookAndFeel(new FlatMacLightLaf());
                SwingUtilities.updateComponentTreeUI(this);
            } catch (UnsupportedLookAndFeelException ex) {
                try {
                    UIManager.setLookAndFeel(new NimbusLookAndFeel());
                } catch (UnsupportedLookAndFeelException exc) {
                    System.out.println("NimbusLookAndFeel is not supported");
                }
                System.out.println("FlatMacLightLaf is not supported");
            }
            this.currentSommetsColor = Color.decode("#FF9FDF");
            this.canva.getSommets().forEach(sommet -> {
                sommet.setColor(this.currentSommetsColor);
            });
            this.theme = "light";
            canva.refresh(selectedSommet);
        });

        // mettre le theme sombre
        darkTheme.addActionListener(e -> {
            try {
                UIManager.setLookAndFeel(new FlatMacDarkLaf());
                SwingUtilities.updateComponentTreeUI(this);
            } catch (UnsupportedLookAndFeelException ex) {
                try {
                    UIManager.setLookAndFeel(new NimbusLookAndFeel());
                } catch (UnsupportedLookAndFeelException exc) {
                    System.out.println("NimbusLookAndFeel is not supported");
                }
                System.out.println("FlatMacLightLaf is not supported");
            }
            this.currentSommetsColor = Color.decode("#DE00DE");
            this.canva.getSommets().forEach(sommet -> {
                sommet.setColor(this.currentSommetsColor);
            });
            this.theme = "dark";
            canva.refresh(selectedSommet);
        });

        // info sommet
        infoO.addActionListener(e -> {
            ListerSommetModal listerSommetModal = new ListerSommetModal(graphe.getAllSommet(), "O");
        });
        infoM.addActionListener(e -> {
            ListerSommetModal listerSommetModal = new ListerSommetModal(graphe.getAllSommet(), "M");
        });
        infoN.addActionListener(e -> {
            ListerSommetModal listerSommetModal = new ListerSommetModal(graphe.getAllSommet(), "N");
        });

        // info arete
        infoArete.addActionListener(e -> {
            ListerAreteModal listerAreteModal = new ListerAreteModal(graphe.getAllLiens());
        });

        // filtrer
        filtrerO.addActionListener(e -> {
            this.filtrerBy("O");
            this.filtred = true;
        });
        filtrerM.addActionListener(e -> {
            this.filtrerBy("M");
            this.filtred = true;

        });
        filtrerN.addActionListener(e -> {
            this.filtrerBy("N");
            this.filtred = true;
        });
        filtrerAucun.addActionListener(e -> {
            this.filtrerBy(null);
            this.filtred = false;
        });

    }

    public void filtrerBy(String type) {
        // remise de l'ancien graphe
        ecran.removeAll();
        ecran.revalidate();
        ecran.repaint();
        this.pack();
        if(!this.filtred){
            Utils.saveGrapheInFile(this.graphe, ".save-sae/fullyGraphe.csv");
        }
        this.graphe = new GrapheList();
        if(type != null){
            GrapheList newGraphe = this.graphe.supprimerAllByType(type, ".save-sae/fullyGraphe.csv");
            Utils.saveGrapheInFile(newGraphe, ".save-sae/"+type+"filtredGraphe.csv");
            this.graphe.loadInGraphe(".save-sae/"+type+"filtredGraphe.csv");
        }else{
            this.graphe.loadInGraphe(".save-sae/fullyGraphe.csv");
        }

        this.initComponents();

    }


    /**
     * Initialise le dropdown de selection du sommet de depart
     * @param dropdown : le dropdown a initialiser
     */
    public void initSommetChoiceDropdownBottom(JComboBox dropdown){

        dropdown.addActionListener(e -> {
            this.selectedSommetDep2 = dropdown.getSelectedItem().toString();
        });
    }

    /**
     * Listener pour le dropdown de selection du mode
     * On affiche le chemin correspondant au mode selectionne
     */
    public void initModeEventListener(){

        modeDropDown.addActionListener(e -> {
            // on recupere le mode selectionne
            this.selectedMode = modeDropDown.getSelectedItem().toString();
            // on affiche le chemin correspondant au mode
            if(selectedMode.equals(MODES[0])){
                LinkedHashMap<String, Double> chemin = this.graphe.tousLesPlusCourtCheminFiabilite(this.selectedSommetDep).get(this.selectedSommetArr);
                cheminLabel.setText("Chemin par " + MODES[0] + ", taille " + GrapheList.getTailleChemin(chemin)  + " : ");
                this.loadInScrollPanelChemin(chemin, true, this.cheminScrollPane);
            }
            else if(selectedMode.equals(MODES[1])){
                LinkedHashMap<String, Double> chemin = this.graphe.tousLesPlusCourtCheminTemps(this.selectedSommetDep).get(this.selectedSommetArr);
                cheminLabel.setText("Chemin par " + MODES[1] + ", taille " + GrapheList.getTailleChemin(chemin) + " : ");
                this.loadInScrollPanelChemin(chemin, false, this.cheminScrollPane);
            }
            else if(selectedMode.equals(MODES[2])){
                LinkedHashMap<String, Double> chemin = this.graphe.tousLesPlusCourtCheminDistance(this.selectedSommetDep).get(this.selectedSommetArr);
                cheminLabel.setText("Chemin par " + MODES[2] + ", taille " + GrapheList.getTailleChemin(chemin) + " : ");
                this.loadInScrollPanelChemin(chemin, null, this.cheminScrollPane);
            }
            pack();

        });
    }

    /**
     * Initialise le dropdown pour le choix du sommet de depart ou d'arrivee
     * @param dropdown : le dropdown a initialiser
     * @param depOuArr: Boolean -> true: depart, false: arrivee
     */
     public void initSommetChoiceDropdownTop(JComboBox dropdown, Boolean depOuArr){

        dropdown.addActionListener(e -> {
            if(depOuArr){
                this.selectedSommetDep = dropdown.getSelectedItem().toString();
            }
            else{
                this.selectedSommetArr = dropdown.getSelectedItem().toString();
            }
            for (LienComponent lien : this.canva.getLiens()){
                if(theme.equalsIgnoreCase("light")){
                    lien.setColor(lien.defaultLightModeColor);
                }else{
                    lien.setColor(lien.defaultDarkModeColor);
                }
            }
            for (SommetComponent sommet : this.canva.getSommets()){
                if(theme.equalsIgnoreCase("light")){
                    sommet.setColor(sommet.defaultLightModeColor);
                }else{
                    sommet.setColor(sommet.defaultDarkModeColor);
                }
            }
            // Calcule du chemin avec nouvelle valeur de depart ou d'arrivee
            // on recupere le mode selectionne
            this.selectedMode = modeDropDown.getSelectedItem().toString();
            // on affiche le chemin correspondant au mode
            if(selectedMode.equals(MODES[0])){
                LinkedHashMap<String, Double> chemin = this.graphe.tousLesPlusCourtCheminFiabilite(this.selectedSommetDep).get(this.selectedSommetArr);
                cheminLabel.setText("Chemin par " + MODES[0] + ", taille " + GrapheList.getTailleChemin(chemin) + " : ");
                // color djikstra
                AtomicReference<String> pastSommet = new AtomicReference<>(this.selectedSommetDep);
                if(chemin != null){
                    // boucler sur tout les sommets pour tracer le chemin
                    chemin.forEach((sommet, fiabValue) -> {
                        // changer la couleur de tout les liens entre les sommets
                        this.canva.getLien(this.canva.returnSommetComponent(pastSommet.get()), canva.returnSommetComponent(sommet)).forEach(lien -> {
                            lien.setColor(Color.RED);
                            lien.repaint();
                            lien.getsA().setColor(Color.RED);

                        });
                        pastSommet.set(sommet);
                    });
                }
                this.loadInScrollPanelChemin(chemin, true, this.cheminScrollPane);
            }
            else if(selectedMode.equals(MODES[1])){
                LinkedHashMap<String, Double> chemin = this.graphe.tousLesPlusCourtCheminTemps(this.selectedSommetDep).get(this.selectedSommetArr);
                cheminLabel.setText("Chemin par " + MODES[1] + ", taille " + GrapheList.getTailleChemin(chemin) + " : ");
                // color djikstra
                AtomicReference<String> pastSommet = new AtomicReference<>(this.selectedSommetDep);
                if(chemin != null){
                    // boucler sur tout les sommets pour tracer le chemin
                    chemin.forEach((sommet, fiabValue) -> {
                        // changer la couleur de tout les liens entre les sommets
                        this.canva.getLien(this.canva.returnSommetComponent(pastSommet.get()), canva.returnSommetComponent(sommet)).forEach(lien -> {
                            lien.setColor(Color.RED);
                            lien.repaint();
                            lien.getsA().setColor(Color.RED);

                        });
                        pastSommet.set(sommet);
                    });
                }

                this.loadInScrollPanelChemin(chemin, false, this.cheminScrollPane);
            }
            else if(selectedMode.equals(MODES[2])){
                LinkedHashMap<String, Double> chemin = this.graphe.tousLesPlusCourtCheminDistance(this.selectedSommetDep).get(this.selectedSommetArr);
                cheminLabel.setText("Chemin par " + MODES[2] + ", taille " + GrapheList.getTailleChemin(chemin) + " : ");
                // color djikstra
                AtomicReference<String> pastSommet = new AtomicReference<>(this.selectedSommetDep);
                if(chemin != null){
                    // boucler sur tout les sommets pour tracer le chemin
                    chemin.forEach((sommet, fiabValue) -> {
                        // changer la couleur de tout les liens entre les sommets
                        this.canva.getLien(this.canva.returnSommetComponent(pastSommet.get()), canva.returnSommetComponent(sommet)).forEach(lien -> {
                            lien.setColor(Color.RED);
                            lien.repaint();
                            lien.getsA().setColor(Color.RED);

                        });
                        pastSommet.set(sommet);
                    });
                }

                this.loadInScrollPanelChemin(chemin, null, this.cheminScrollPane);
            }
            pack();
        });
    }

    /**
     * Affiche le chemin dans le scroll panel
     * @param chemin: LinkedHashMap<String, Double> -> String: nom du sommet, Double: distance
     * @param mode: Boolean -> true: affiche le chemin en fonction les fiabilites, false: affiche le chemin en fonction les durées, null: affiche le chemin en fonction les distances
     * @param scrollPane: JScrollPane -> le scrollPane dans lequel afficher le chemin
     */
    public void loadInScrollPanelChemin(LinkedHashMap<String, Double> chemin, Boolean mode, JScrollPane scrollPane){


        // si il y a un chemin entre les 2 sommets
        if(chemin != null){
            // Setup du panel d'affichage
            JPanel panel = new JPanel(new GridLayout(chemin.size(), 1));
            // Premier sommet
            Sommet s = this.graphe.returnSommet(this.selectedSommetDep);
            Boolean first = true;
            // Boucler dans le chemin sur les sommets
            for (Map.Entry<String, Double> entry : chemin.entrySet()) {
                // Si c'est le premier sommet ne pas traiter la distance
                if(first){
                    first = false;
                    JLabel label = new JLabel(entry.getKey() + " : " + entry.getValue());
                    Font labelFont = label.getFont();
                    label.setFont(new Font(labelFont.getName(), Font.PLAIN, 17)); // changer la taille de la police
                    panel.add(label);
                }else{
                    JLabel label = new JLabel();
                    Font labelFont = label.getFont();
                    label.setFont(new Font(labelFont.getName(), Font.PLAIN, 17)); // changer la taille de la police
                    if(mode == null){
                        // Afficher le sommet : la distance, la fiabilité, la durée
                        label.setText(s.getNom() + " => " + entry.getKey() + " : " + entry.getValue() + " km | " + Math.round(Sommet.getFiabiliteBetween(s, entry.getKey())*100)/10 + " %" + " | " + Sommet.getDureeBetween(s, entry.getKey()) + " min");
                    }else{
                        // Affiche le sommet le plus fiable
                        if(mode == true){
                            // Afficher le sommet : la fiabilité, la distance, la durée
                            label.setText(s.getNom() + " => " + entry.getKey() + " : " + Math.round(entry.getValue()*1000)/10 + " % | " + Sommet.getDistanceBetween(s, entry.getKey()) + " km" + " | " + Sommet.getDureeBetween(s, entry.getKey()) + " min");
                        } else {
                            // Afficher le sommet : la durée, la fiabilité, la distance
                            label.setText(s.getNom() + " => " + entry.getKey() + " : " + entry.getValue() + "min | " + Math.round(Sommet.getFiabiliteBetween(s, entry.getKey())*100)/10 + " %" + " | " + Sommet.getDistanceBetween(s, entry.getKey()) + " km");
                        }
                    }

                    panel.add(label);
                }
                s = this.graphe.returnSommet(entry.getKey()); // changer le sommet précédent
            }
            /*JLabel label = new JLabel(); //A FAIRE TRAITER BON CHEMIN YACINE
            Font labelFont = label.getFont();
            label.setFont(new Font(labelFont.getName(), Font.PLAIN, 17)); // changer la taille de la police
            label.setText(chemin.entrySet());*/

            panel.setBorder( BorderFactory.createEmptyBorder(5,5,5, 5) );
            scrollPane.setViewportView(panel);
        }else{
            // Si il n'y a pas de chemin
            JPanel panel = new JPanel(new GridLayout(1, 1));
            JLabel label = new JLabel("Pas de chemin entre " + this.selectedSommetDep + " et " + this.selectedSommetArr);
            Font labelFont = label.getFont();
            label.setFont(new Font(labelFont.getName(), Font.PLAIN, 17)); // changer la taille de la police
            panel.add(label);
            panel.setBorder( BorderFactory.createEmptyBorder(5,5,5, 5) );
            scrollPane.setViewportView(panel);
        }
    }

    public void loadInScrollPanelVoisin(Map<Integer, List<String>> voisins) {
        /**
        * Affiche les voisins dans le scroll panel
        * @param voisins: Map<Integer, List<String>> -> Integer: distance, List<String>: liste des sommets        *
         */
        // Setup du panel d'affichage
        JPanel panel = new JPanel(new GridLayout(voisins.size(), 1));
        // Boucler dans les voisins sur les distances
        List<List<String>> alreadyTreated = new ArrayList<>(); // liste des sommets déjà traité
        for (Map.Entry<Integer, List<String>> entry : voisins.entrySet()) {
            if (!alreadyTreated.contains(entry.getValue())) {
                JLabel label = new JLabel();
                Font labelFont = label.getFont();
                label.setFont(new Font(labelFont.getName(), Font.PLAIN, 17)); // changer la taille de la police
                label.setText(entry.getKey() + " distance : " + entry.getValue()); // afficher les sommet et la distance
                panel.add(label);
                alreadyTreated.add(entry.getValue());
            }
        }
        panel.setLayout(new GridLayout(alreadyTreated.size(), 1)); // changer le nombre de ligne en fonction du nombre d'élément affiché
        panel.setBorder( BorderFactory.createEmptyBorder(5,5,5, 5) );
        this.voisinScrollPane.setViewportView(panel);
    }

    /**
     * Créer les liens entre les sommets
     */
    public void createLiens(){

        ArrayList<SommetComponent> sommetsComponent = this.canva.getSommets();
        // recuperation des sommets
        for (SommetComponent sommetComponent : sommetsComponent){
            String nomSommet = sommetComponent.getNomSommet();
            // recuperation du sommet en question
            Sommet sommet = graphe.returnSommet(nomSommet);
            List<String> voisins = sommet.getVoisins();
            // recuperation des voisins
            for (String voisin : voisins ){
                LienMaillon lienMaillon = sommet.getLien(voisin);
                SommetComponent voisinComponent = canva.getSommet(voisin);
                // creation du lien
                LienComponent lien = new LienComponent(sommetComponent, voisinComponent, lienMaillon.getNomLien());
                lien.setBounds(0, 0, canva.getWidth()*2, canva.getHeight()*2);
                this.canva.addLien(lien);
            }
        }
    }

    @Override
    public void componentResized(ComponentEvent e) {
        // Resize des scroll panel
    }

    @Override
    public void componentMoved(ComponentEvent e) {

    }

    @Override
    public void componentShown(ComponentEvent e) {

    }

    @Override
    public void componentHidden(ComponentEvent e) {

    }

    public GrapheList getGraphe() {
        return graphe;
    }

    public String getTheme() {
        return theme;
    }

    public Color getCurrentSommetsColor() {
        return currentSommetsColor;
    }

    public Boolean getFiltred() {
        return filtred;
    }
}

