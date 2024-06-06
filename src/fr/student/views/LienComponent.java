package fr.student.views;

import javax.swing.*;
import java.awt.*;

public class LienComponent extends JComponent {
    private Point pointA;
    private Point pointB;
    private SommetComponent sA;
    private SommetComponent sB;
    private String nom;
    private Color color;
    public static Color defaultLightModeColor = Color.decode("#E8E9FF");
    public static Color defaultDarkModeColor = Color.decode("#00046B");
    public static Color selectedLightModeColor = Color.decode("#000000");
    public static Color selectedDarkModeColor = Color.decode("#FFFFFF");

    public static Color unselectedDarkModeColor = new Color(0, 0, 0, 10);
    public static Color unselectedLightModeColor = new Color(255, 255, 255, 10);

    /**
     * Constructeur de la classe LienComponent
     * @param a: Sommet A
     * @param b: Sommet B
     * @param nom: Nom du lien
     */
    public LienComponent(SommetComponent a, SommetComponent b, String nom) {

        this.sA = a;
        this.sB = b;
        this.nom = nom;
        linkBetween(a, b);
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        g2d.setColor(this.color);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        this.pointA = new Point(sA.getX() + 25, sA.getY() + 25);
        this.pointB = new Point(sB.getX() + 25, sB.getY() + 25);


        // nom du sommet
        Font txtFont = new JLabel().getFont();
        g2d.setFont(new Font(txtFont.getName(), Font.BOLD, 15));
        // Tracer la ligne
        g2d.drawLine(pointA.x, pointA.y, pointB.x, pointB.y);
        //g2d.drawString(nom, (pointA.x + pointB.x) / 2, (pointA.y + pointB.y) / 2);
    }

    @Override
    public Dimension getPreferredSize() {
        int maxX = Math.max(pointA.x, pointB.x);
        int maxY = Math.max(pointA.y, pointB.y);
        return new Dimension(maxX, maxY);
    }

    public void setPointA(Point pointA) {
        this.pointA = pointA;
    }

    public void setPointB(Point pointB) {
        this.pointB = pointB;
    }

    public Point getPointA() {
        return pointA;
    }

    public Point getPointB() {
        return pointB;
    }

    /**
     * Crée un lien entre deux sommets
     * @param a: Le premier sommet
     * @param b: Le deuxième sommet
     */
    public void linkBetween(SommetComponent a, SommetComponent b) {

        this.pointA = new Point(a.getX() + 25, a.getY() + 25); // 25 is the size of the circle
        this.pointB = new Point(b.getX() + 25, b.getY() + 25);
        this.sA = a;
        this.sB = b;
    }

    /**
     * Change la couleur du lien
     * @param color: La nouvelle couleur du lien
     */
    public void setColor(Color color) {
        this.color = color;
    }
    public SommetComponent getsA() {
        return sA;
    }

    public SommetComponent getsB() {
        return sB;
    }

    public String getNom() {
        return nom;
    }
}
