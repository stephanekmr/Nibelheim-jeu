package Model;

import java.awt.Point;
import java.awt.Rectangle;

// Classe pour les projectiles (position et direction)
public class Projectile {

    // Taille du projectile
    public static final int WIDTH_PROJECTILE = 12;
    public static final int HEIGHT_PROJECTILE = 12;

    // Attributs de position et direction
    private Point position;
    private Point direction;
    public Rectangle hitboxProjectile;
    
    // Constructeur pour initialiser la position et la direction
    public Projectile(Point position, Point direction) {
        this.position = position;
        this.direction = direction;
        hitboxProjectile = new Rectangle(position.x, position.y, WIDTH_PROJECTILE,HEIGHT_PROJECTILE);
    }

    // Getteurs pour récupérer la position et la direction
    public Point getPosition() {
        return position;
    }

    public Point getDirection() {
        return direction;
    }

    // Setteus pour modifier la position et la direction
    public void setPosition(Point position) {
        this.position = position;
    }

    public void setDirection(Point direction) {
        this.direction = direction;
    }

}
