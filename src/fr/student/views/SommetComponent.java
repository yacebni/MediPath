package fr.student.views;

import fr.student.views.popups.SommetClickMenu;

import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import javax.swing.*;

public class SommetComponent extends JComponent implements MouseListener, MouseMotionListener {
    private GrapheCanva parent;
    private Color color;
    public Color defaultSelectedLightModeColor = Color.decode("#61BDFF");
    public Color defaultDarkModeColor = Color.decode("#8A005C");
    public Color defaultSelectedDarkModeColor = Color.decode("#FF00AA");
    public Color defaultLightModeColor = Color.decode("#438EC5");
    public Color defaultVoisinColorDarkMode = Color.decode("#B24C90");
    public Color defaultVoisinColorLightMode = Color.decode("#0094FF");
    public Color defaultUnselectedLightMode = new Color(255, 255, 255, 10);
    public Color defaultUnselectedDarkMode = new Color(0, 0, 0, 10);

    private static int COMPONENT_WIDTH = 50;
    private static int COMPONENT_HEIGHT = 50;
    private static int FONT_SIZE = 20;
    private int parentWidth;
    private int parentHeight;
    private String name;
    private volatile int screenX = 0;
    private volatile int screenY = 0;
    private volatile int myX = 0;
    private volatile int myY = 0;

    /**
     * Création d'un sommet
     * @param name: Le nom du sommet
     * @param xCoord: La coordonnée x du sommet
     * @param yCoord: La coordonnée y du sommet
     * @param size: La taille du sommet
     * @param parent: Le parent du sommet
     */
    public SommetComponent(String name, int xCoord, int yCoord, Dimension size, GrapheCanva parent) {
        this.parent = parent;
        this.name = name;
        HashMap<String, Integer> coords = tooBigCoords(xCoord, yCoord, new Dimension((int) (size.getWidth()), (int) (size.getHeight()-30)));
        this.myX = coords.get("x");
        this.myY = coords.get("y");
        setOpaque(false);
        setPreferredSize(size); // Définir les dimensions du composant
        setBounds(this.myX, this.myY, COMPONENT_WIDTH, COMPONENT_HEIGHT);
        setLocation(this.myX, this.myY);

        addMouseListener(this);
        addMouseMotionListener(this);
    }

    /**
     * Vérifie si les coordonnées sont trop grandes pour le parent
     * @param x: La coordonnée x
     * @param y: La coordonnée y
     * @param size: La taille du parent
     * @return HashMap<String, Integer>: Les nouvelles coordonnées
     */
    public HashMap<String, Integer> tooBigCoords(int x, int y, Dimension size){

        HashMap<String, Integer> coords = new HashMap<String, Integer>() {{
            put("x", x);
            put("y", y);
        }};
        // si une des taille dépasse la taille du parent
        if(x>size.getWidth()- COMPONENT_WIDTH *2 || y>size.getHeight()- COMPONENT_HEIGHT *2){
            // si x dépasse la largeur du parent
            if(x>size.getWidth()- COMPONENT_WIDTH *2){
                coords.put("x", (int) size.getWidth() - COMPONENT_WIDTH *2);
            }
            // si y dépasse la hauteur du parent
            if(y>size.getHeight()- COMPONENT_HEIGHT *2){
                coords.put("y", (int) size.getHeight() - COMPONENT_HEIGHT *2);
            }
        } else if(x<0 || y<0){
            if (x<0){
                coords.put("x", 0);
            } else{
                coords.put("x", x);
            }
            if(y<0){
                coords.put("y", 0);
            } else{
                coords.put("y", y);
            }
        }else{
            coords.put("x", x);
            coords.put("y", y);
        }
        return coords;

    }
    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);

        Graphics2D g2d = (Graphics2D) graphics;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        setOpaque(false);
        if(color == null){
            color = Color.RED;
        }
        g2d.setColor(color);
        defineCoordsInfo();

        g2d.fillOval(0, 0, COMPONENT_WIDTH, COMPONENT_HEIGHT);
        Color txtColor = new JLabel().getForeground();
        Font txtFont = new JLabel().getFont();
        g2d.setColor(txtColor);


        g2d.setFont(new Font(txtFont.getName(), Font.BOLD, FONT_SIZE));
        FontMetrics fm = g2d.getFontMetrics();
        int textWidth = fm.stringWidth(this.name);
        int textHeight = fm.getHeight();
        int x = (COMPONENT_WIDTH - textWidth) / 2;
        int y = (COMPONENT_HEIGHT - textHeight) / 2 + fm.getAscent();
        g2d.drawString(this.name, x, y);


    }

    /**
     * Définir les coordonnées du parent
     */
    public void defineCoordsInfo(){

        this.parentWidth = getParent().getSize().width;
        this.parentHeight = getParent().getSize().height;
    }

    public void setColor(Color color) {
        this.color = color;
        repaint();
    }

    /**
     * Définir les coordonnées du sommet
     * @param x: La coordonnée x
     * @param y: La coordonnée y
     */
    public void setCoords(int x, int y){

        HashMap<String, Integer> coords = tooBigCoords(x, y, this.getParent().getSize());
        this.myX = coords.get("x");
        this.myY = coords.get("y");
        setLocation(this.myX, this.myY);
        repaint();
    }

    // MouseListener
    @Override
    public void mouseClicked(MouseEvent e) {
        // right click
        if (SwingUtilities.isRightMouseButton(e)) {
            SommetClickMenu menu = new SommetClickMenu(getNomSommet(), parent);
            menu.show(e.getComponent(), e.getX(), e.getY());

        } else {
            if (e.isControlDown()) {
                // ctrl click
                parent.setCtrlPressedSommet(this);
            }else{
                // left click
                parent.clickingSommet(name);
            }

        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        screenX = e.getXOnScreen();
        screenY = e.getYOnScreen();

        myX = getX();
        myY = getY();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) { }

    @Override
    public void mouseExited(MouseEvent e) { }

    // MouseMotionListener
    @Override
    public void mouseDragged(MouseEvent e) {
        int deltaX = e.getXOnScreen() - screenX;
        int deltaY = e.getYOnScreen() - screenY;

        HashMap<String, Integer> coords = tooBigCoords(myX+ deltaX, myY+ deltaY, new Dimension(getParent().getWidth()+50, getParent().getHeight()+50));
        Integer new_coordX = coords.get("x");
        Integer new_coordY = coords.get("y");
        if(new_coordX<0 || new_coordY<0){
            if(new_coordX < 0){
                setLocation(0, new_coordY);
            }
            if(new_coordY < 0){
                setLocation(new_coordX, 0);
            }
        }
        else{
            setLocation(new_coordX, new_coordY);
        }
        this.parent.movingSommet(SommetComponent.this);

    }

    @Override
    public void mouseMoved(MouseEvent e) { }

    public String getNomSommet(){
        return this.name;
    }
    public int getMyX() {
        return myX;
    }

    public int getMyY() {
        return myY;
    }

    @Override
    public int getWidth() {
        return this.COMPONENT_WIDTH;
    }

    @Override
    public int getHeight() {
        return this.COMPONENT_HEIGHT;
    }

    public Color getColor() {
        return color;
    }

    public Color getDefaultLightModeColor() {
        return defaultLightModeColor;
    }
}

