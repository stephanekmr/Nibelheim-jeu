package Controler;

import javax.swing.ImageIcon;
import java.awt.*;
import Model.Bonus;
import Model.Obstacles;




//Classe du personnage principal du jeu. Celui contrôlé par le joueur.
//Le joueur utilise les touches Z,Q,S,D pour se déplacer.
//Il peut se déplacer librement sur toute la fenêre.
//Il peut également tirer des projectiles.
//Il a une barre de vie qui diminue lorsqu'il est touché par un ennemi.
//Il peut ramasser des bonus pour augmenter sa vie ou sa puissance de tir.
public class Character extends Thread {

    // Instance de la classe bonus
    private Bonus b ;
    // Instance de la classe obstacles
    private Obstacles o;
    // Hitbox du personnage
    public Rectangle hitboxC;

    // Attributs pour la position du joueur
    private double current_x = 820;
    private double current_y = 540;
    
    private double vx = 0, vy = 0;  // Vitesse horizontale et verticale
    private double acceleration = 2; // Accélération progressive
    private double friction = 0.9;  // Décélération (simule l’inertie)
    private double maxSpeed = 8;   // Vitesse maximale

    
    private int vie = 100; // Points de vie du joueur
    public static final int maxVie = 100; // Points de vie maximum du joueur
    private Inputs inputs; // Gestion des entrées clavier

    private int nombreBonus = 0; // Nombre de bonus ramassés
    private boolean paused = false; // Booléen pour mettre en pause le jeu

    // Dimensions du personnage
    public static final int WIDTH = 90;
    public static final int HEIGHT = 90;

    //creer des getters pour vx et vy
    public double getVx() {
        return vx;
    }
    public double getVy() {
        return vy;
    }

    // Getters pour la position du joueur
    public double getCurrent_x() {
        return current_x;
    }
    public double getCurrent_y() {
        return current_y;
    }

    // Constructeur pour la classe Character
    public Character(Bonus bonus, Inputs i, Obstacles o) {
        this.o = o;
        this.b = bonus;
        this.inputs = i;
        this.hitboxC = new Rectangle((int)current_x, (int)current_y, WIDTH, HEIGHT);
    }

    // Setters pour la position du joueur
    public void setCurrent_x(double current_x) {
        this.current_x = current_x;
    }
    public void setCurrent_y(double current_y) {
        this.current_y = current_y;
    }

    // Getteur et setteur pour le nombre de bonus
    public int getNombreBonus() {
        return nombreBonus;
    }
    public void setNombreBonus(int nombreBonus) {
        this.nombreBonus = nombreBonus;
    }

    // Methode pour reinitialiser le KeyListener
    public void resetInput(Inputs i) {
        this.inputs = i;
    }


    // Sprite du personnage
    public static final Image characterSprite = new ImageIcon("src/Images/character.png")
            .getImage().getScaledInstance(WIDTH, HEIGHT, Image.SCALE_DEFAULT);



    public synchronized void pauseGame() {
        paused = true;
    }

    public synchronized void resumeGame() {
        paused = false;
        notify(); // Réveille le thread s'il est en attente
    }
        
    public void run() {
        while (vie >= 0) {
            synchronized (this) {
                while (paused) {
                    try {
                        wait(); // Met en pause l'exécution du thread
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }

            hitboxC.x = (int) current_x;
            hitboxC.y = (int) current_y;
            updateMovement(); // Mise à jour des déplacements

            try {
                Thread.sleep(15);
            } catch (InterruptedException e) {
                //Thread.currentThread().interrupt();
            }
        }
    }

    // Gestion du déplacement avec inertie
    private void updateMovement() {
        // Accélération selon les touches pressées
        if (inputs.up) { vy -= acceleration; }
        if (inputs.down) { vy += acceleration; }
        if (inputs.left) { vx -= acceleration; }
        if (inputs.right) { vx += acceleration; }
        

        // Limite la vitesse maximale
        vx = Math.max(-maxSpeed, Math.min(maxSpeed, vx));
        vy = Math.max(-maxSpeed, Math.min(maxSpeed, vy));

        // Appliquer la friction (simule une glisse après l'arrêt des touches)
        vx *= friction;
        vy *= friction;

        // Appliquer le mouvement
        // Vérifier si le mouvement horizontal du joueur l'amène en collision avec un obstacle
        if (parcoursObstacle(current_x + vx, current_y) == false) {
            current_x += vx;
        } else {
            vx = 0; // Arrêt du mouvement horizontal en cas de collision
        }
        // Vérifier si le mouvement vertical du joueur l'amène en collision avec un obstacle
        if (parcoursObstacle(current_x, current_y + vy) ==false) {
            current_y += vy;
        } else {
            vy = 0; // Arrêt du mouvement vertical en cas de collision
        }

        // Garder la sorcière dans l'écran
        current_x = Math.max(0, Math.min(1800, current_x));
        current_y = Math.max(0, Math.min(1080, current_y));
    }

    // Getter et setter pour les points de vie
    public int getVie() {
        return vie;
    }

    public void setVie(int vie) {
        this.vie = vie;
    }

    // Méthode pour vérifier si le joueur est proche d'un bonus et récupérer ce bonus
    public void checkBonusProche() {
        for (int i = 0; i < b.getPointBonus().size(); i++) {
            if (hitboxC.intersects(b.getPointBonus().get(i))) {
                nombreBonus++;
                b.removeBonus(b.getPointBonus().get(i).getLocation());
                break;
            }
        }
    }


    //methode pour verifier si il y a collision entre le joueur et un obstacle
    public boolean collisionObstacleJoueur( double next_x, double next_y, int ox, int oy) {
        Rectangle r1 = new Rectangle((int) next_x, (int) next_y,WIDTH, HEIGHT);
        Rectangle r2 = new Rectangle(ox, oy, Obstacles.WIDTH_O, Obstacles.HEIGHT_O);
        return r1.intersects(r2);
         
     }
     //methode pour parcourir la liste des obstacles et verifier si il y a collision
     public boolean parcoursObstacle(double next_x, double next_y) {
         boolean collision = false;
         for (Point obstacle : o.getObstacles()) {
             if (collisionObstacleJoueur(next_x, next_y, obstacle.x, obstacle.y)) {
                 // System.out.println("Collision avec un obstacle");
                 collision=true;
                 break;
             }
         }
         return collision;
         
     }

    // Méthode pour réinitialiser le personnage (Quand on relance une partie)
    public void restartPlayer() {
        pauseGame();
        current_x = 820;
        current_y = 540;
        vie = maxVie;
        vx = 0;
        vy = 0;
        nombreBonus = 0;
        inputs.resetKeys();
        resumeGame();
    }

}