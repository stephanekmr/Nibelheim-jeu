package Model;

import java.awt.*;
import java.io.File;
import java.util.List;
import java.util.Random;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

import javax.sound.sampled.Clip;

import java.util.concurrent.CopyOnWriteArrayList;

//Classe représentant un ennemie quelconque. Ses sous-classes sont les ennemies spécifiques
public class Ennemis {
    /* VARIABLES */
    int health, speed, bonusAmount;
    boolean randomSpawn;
    Point position;
    Rectangle hitboxEnnemie;
    private Bonus b;
    private boolean isMoving = false;

    // Variables static afin de savoir quels sont les ennemis présents à l'écran
    public static List<Ennemis> ListEnnemies = new CopyOnWriteArrayList<>();

    // Taille de la fenêtre de jeu pour gerer la position de l'ennemi
    private static final int WIDTH_fentre = 1920;
    private static final int HEIGHT_fentre = 1080;

    // Generateur d'entier aléartoire
    private static final Random rand = new Random();

    // Taille du sprite de l'ennemi
    private int width;
    private int height;

    // Clip audio pour l'ennemi étant détruit
    Clip audioKill = null;

    // Pour quand le joueur est touché
    static Clip audioIsHit = null;

    // Image de l'ennemi
    public Image img;

    /* CONSTRUCTEUR */
    public Ennemis(Character c, int health, int speed, int width, int height, int bonusAmount, Point position,
            Image sprite, Bonus b) {
        this.speed = speed;
        this.health = health;
        this.bonusAmount = bonusAmount;
        this.img = sprite;
        this.position = position;
        this.width = width;
        this.height = height;
        this.hitboxEnnemie = new Rectangle(position.x, position.y, width, height);
        // System.out.println("On insère dans liste d'ennemies : " + speed + "vitesse");
        this.b = b;
        ListEnnemies.add(this);
    }

    // Constructeur par défaut
    public Ennemis() {
    }

    /* GETTERS & SETTERS */
    //getter et setter pour la vitesse des ennemis
    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int v) {
        this.speed = v;
    }

    public static List<Ennemis> getListEnnemies() {
        return ListEnnemies;
    }
    // getters et setters pour la position des ennemis 
    public Point getPosition() {
        return position;
    }

    public void setPosition(Point p) {
        this.position = p;
    }

    // getteurs pour la taille de l'ennemi
    public int getWidth(Ennemis e) {
        return e.width;
    }

    public int getHeight(Ennemis e) {
        return e.height;
    }

    // Getteur pôur isMoving
    public boolean getIsMoving() {
        return isMoving;
    }

    // Méthode pour ajouter un ennemi
    public void addEnnemie(Ennemis e) {
        ListEnnemies.add(e);
    }

    // Métode qui génère une position aleartoire pour l'ennemi
    public static Point genererPositionAleatoire() {
        int x = new int[] { -50, 100, 250, 500, 750, 1000, 1500, WIDTH_fentre + 50 }[rand.nextInt(8)];
        int y;
        if (x == -50 || x == WIDTH_fentre + 50) {
            y = rand.nextInt(HEIGHT_fentre - (-50) + 1) + (-50);
        } else {
            y = new int[] { -100, HEIGHT_fentre + 50 }[rand.nextInt(2)];
        }
        return new Point(x, y);
    }

    /* METHODES */

    // Lorsque l'ennemie touche le joueur
    public static void allCollisions(Character c, Tir t) {
        List<Projectile> tirs = t.getTirs();
        for (Ennemis ennemi : ListEnnemies) {
            // Si l'ennemi est déja en mouvement
            if (ennemi != null && ennemi.isMoving) {
                // Collision entre la soricère et les ennemies (contact entre les deux hitboxes)
                if (c.hitboxC.intersects(ennemi.hitboxEnnemie)) {

                    try {
                        AudioInputStream audioIn = AudioSystem.getAudioInputStream(new File("src/Audios/se_item00.wav"));
                        audioIsHit = AudioSystem.getClip();
                        audioIsHit.open(audioIn);
                        audioIsHit.start();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    c.setVie(c.getVie() - 1);
                    ennemi.kill();
                }
                // Collision entre les tirs et les ennemies
                for (Projectile proj : tirs) {
                    if (proj.hitboxProjectile.intersects(ennemi.hitboxEnnemie)) {
                        ennemi.isHit(1);
                        tirs.remove(proj);
                    }
                }
            }
        }
    }

    // Losque l'ennemi est touché, il perd de la vie
    public void isHit(int damage) {
        health -= damage;
        // Lorsque l'ennemi n'a plus de vie, il meurt et lâche des bonus
        if (health == 0) {
            for (int i = 0; i < bonusAmount; i++) {
                b.addBonus(position);
            }
            this.kill();
        }
    }

    // L'ennemi meurt quand sa vie arrive à 0
    public void kill() {
        try {
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(new File("src/Audios/se_damage01.wav"));
            audioKill = AudioSystem.getClip();
            audioKill.open(audioIn);
            audioKill.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
        ListEnnemies.remove(this);
    }

    // Méthode pour lancer le déplacement de l'ennemi peut importe son type
    public void startMouvement() {
        // if c'est un fantome, on le démarre
        if (this instanceof Fantome) {
            ((Fantome) this).startMovement();
            this.isMoving = true;
        }
        // if c'est une araignee, on le démarre
        else if (this instanceof Araignee) {
            ((Araignee) this).startMovement();
            this.isMoving = true;
        }
        // if c'est une goule, on le démarre
        else if (this instanceof Goules) {
            ((Goules) this).startMovement();
            this.isMoving = true;
        }
    }

}
