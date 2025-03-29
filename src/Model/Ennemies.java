package Model;

import Controler.Character;

import java.awt.*;
import java.util.List;
import javax.swing.ImageIcon;
import java.util.concurrent.CopyOnWriteArrayList;

//Classe représentant un ennemie quelconque. Ses sous-classes sont les ennemies spécifiques
public class Ennemies {
    /* VARIABLES */
    int health, speed, bonusAmount;
    boolean randomSpawn;
    Point position;
    Rectangle hitboxEnnemie;

    // Variables static afin de savoir quels sont les ennemis présents à l'écran
    public static List<Ennemies> ListEnnemies = new CopyOnWriteArrayList<>();

    // Taille du sprite de l'ennemi
    public static final int WIDTH = 45;
    public static final int HEIGHT = 45;
    // Image de l'ennemi
    public Image img = new ImageIcon("src/Images/character.png").getImage().getScaledInstance(WIDTH, HEIGHT,Image.SCALE_DEFAULT);

    /* CONSTRUCTEUR */
    public Ennemies(Character c, int health, int speed, int bonusAmount, Point position, Image sprite) {
        this.speed = speed;
        this.health = health;
        this.bonusAmount = bonusAmount;
        this.img = sprite;
        this.position = position;
        this.hitboxEnnemie = new Rectangle(position.x, position.y,WIDTH, HEIGHT);
        // System.out.println("On insère dans liste d'ennemies : " + speed + "
        // vitesse");
        ListEnnemies.add(this);
    }

    /* GETTERS & SETTERS */
    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int v) {
        this.speed = v;
    }

    public static List<Ennemies> getListEnnemies() {
        return ListEnnemies;
    }

    public Point getPosition() {
        return position;
    }

    public void setPosition(Point p) {
        this.position = p;
    }

    /* METHODES */

    //Lorsque l'ennemie touche le joueur
    public static void allCollisions(Character c, Tir t) {
        List<Projectile> tirs = t.getTirs();
        for (Ennemies ennemi : ListEnnemies) {
            //Collision entre la soricère et les ennemies (contact entre les deux hitboxes)
            if (c.hitboxC.intersects(ennemi.hitboxEnnemie)) {
                ennemi.kill();
            }
            
            //Collision entre les tirs et les ennemies
            for (Projectile proj : tirs){
                if (proj.hitboxProjectile.intersects(ennemi.hitboxEnnemie)){
                    ennemi.isHit(1);     
                    tirs.remove(proj);
                }
            }
        }        
    }

    // Losque l'ennemi est touché, il perd de la vie
    public void isHit(int damage) {
        health -= damage;
        System.out.println("Point de vie : "+this.health);
        if (health == 0) {
            System.out.println("Normalement je meurs la !");
            this.kill();
        }
    }

    // L'ennemi meurt quand sa vie arrive à 0
    public void kill() {
        ListEnnemies.remove(this);
    }

    // Thread pour démarrer le déplacement de l'ennemie
    public static void startCollision(Character c, Tir t) {
        Thread collisionThread = new Thread(() -> {
            while (true) {
                allCollisions(c, t);
                try {
                    Thread.sleep(50); // Attendre 50 ms entre chaque déplacement
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        collisionThread.start();
    }
}
