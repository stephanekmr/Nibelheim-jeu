package Model;

import java.awt.Image;
import java.awt.Point;
import javax.swing.ImageIcon;

public class Goules extends Ennemis {
    // Points de vie des goules
    private static final int HEALTH_MAX = 10;

    // Vitesse des projectiles des goules
    public static final int SPEED_PROJECTILE = 12;

    // Taille du sprite du goule
    public static final int WIDTH = 52;
    public static final int HEIGHT = 64;
    // Indique si on doit demarrer le mouvement ou pas
    private volatile boolean isRunning = true;

    // Taille de la fenêtre de jeu pour gerer la position de l'ennemi
    private static final int WIDTH_fenetre = 1920;
    private static final int HEIGHT_fenetre = 1080;

    // Classe Character
    Character c;

    // distance max entre le joueur et la goule pour qu'elle tire
    private final int DISTANCE_MAX_X = 300;
    private static final int DISTANCE_MAX_Y = 300;

    // Projectile unique pour chaque goule
    public Projectile projectile ;

    // direction fixe du projectile
    private double projectileDirX = 0;
    private double projectileDirY = 0;

    // Image de l'ennemie
    public static final Image sprite = new ImageIcon("src/Images/goule.gif").getImage().getScaledInstance(WIDTH,HEIGHT, Image.SCALE_DEFAULT);

    // constructeur
    public Goules(Character c, int speed, int bonusAmount, Point pos, Bonus b) {
        super(c, HEALTH_MAX, speed, WIDTH, HEIGHT, bonusAmount, pos, sprite, b);
        this.c = c;
    }

    // Méthode pour arrèter le mouvement des goules
    public void stopMovement() {
        isRunning = false;
    }

    // Méthode pour déplacer la goule vers le joueur mais a une certaine distance
    public void goToCharacter() {
        // Si la goule est morte, supprimer le projectile
        if (this.health <= 0) {
            projectile = null;
            return;
        }

        // on recupere la position du joueur (son centre)
        Point playerPosition = new Point(
            (int) c.getCurrent_x() + Character.WIDTH / 2, 
            (int) c.getCurrent_y() + Character.HEIGHT / 2
        );
        // on recupere la position de la goule
        Point goulePosition = getPosition();
        // on met a jour la hitbox de l'ennemie
        this.hitboxEnnemie.x = this.position.x;
        this.hitboxEnnemie.y = this.position.y;
        // on calcule la direction entre le joueur et la goule
        int dx = playerPosition.x - goulePosition.x;
        int dy = playerPosition.y - goulePosition.y;
        // on calcule la distance entre le joueur et la goule
        double distance = Math.sqrt(dx * dx + dy * dy);
        // on evite la division par zero
        if (distance > 0) {
            // on normalise la direction
            double directionX = dx / distance;
            double directionY = dy / distance;
            // si la distance est superieure ou égale au seuil, on bouge
            if (distance >= DISTANCE_MAX_X && distance >= DISTANCE_MAX_Y) {
                goulePosition.x += directionX * getSpeed();
                goulePosition.y += directionY * getSpeed();
                setPosition(goulePosition);
            } else {
                // si pas de projectile actif, on tire
                synchronized (this) {
                    TirGoule(goulePosition, directionX, directionY);
                }
            }
        }
        // si un projectile est actif, on le fait avancer
        synchronized (this) {
            if (projectile != null) {
                updateProjectile();
            }
        }
    }

    // Méthode pour tirer un projectile (direction calculée une seule fois)
    public void TirGoule(Point goulePosition, double directionX, double directionY) {
        if (projectile == null) {
            // on stocke la direction une fois pour toutes
            projectileDirX = directionX;
            projectileDirY = directionY;
            // on crée un nouveau projectile
            projectile = new Projectile(new Point(goulePosition.x, goulePosition.y),
                new Point((int) directionX, (int) directionY));
            // on met a jour la hitbox du projectile pour qu'elle soit au bon endroit
            projectile.hitboxProjectile.x = projectile.getPosition().x;
            projectile.hitboxProjectile.y = projectile.getPosition().y;
        }
    }

    // Mettre à jour la position du projectile (déplacement linéaire)
    public synchronized void updateProjectile() {
        synchronized (this) {  // On synchronise l'accès à la méthode pour eviter une NullPointerException
            // on verifie si le projectile est null
            if (projectile == null) {
                System.out.println("Le projectile est null.");
                return;
            }
            // On check les collisions
            collisionProjectile();
            // On calcule la direction du projectile
            double moveX = projectileDirX * SPEED_PROJECTILE;
            double moveY = projectileDirY * SPEED_PROJECTILE;
            // On calcule la nouvelle position du projectile
            int newX = projectile.getPosition().x + (int) Math.round(moveX);
            int newY = projectile.getPosition().y + (int) Math.round(moveY);
            // Si le projectile sort de l'écran, on le supprime
            if (newX < 0 || newX > WIDTH_fenetre || newY < 0 || newY > HEIGHT_fenetre) {
                projectile = null;
                return;
            }
            // on met à jour la position du projectile
            projectile.setPosition(new Point(newX, newY));
            projectile.hitboxProjectile.x = newX;
            projectile.hitboxProjectile.y = newY;
        
            // System.out.println("Position du projectile : " + newX + " " + newY);
        }
    }

    // Méthode pour gerer la collision entre le projectile et le joueur
    public void collisionProjectile() {
        if (projectile != null) {
            if (projectile.hitboxProjectile.intersects(c.hitboxC)) {
                c.setVie(c.getVie() - 1);
                projectile = null; // Supprimer le projectile
                // Relancer le tir immédiatement après la collision
                Point goulePosition = getPosition();
                Point playerPosition = new Point((int) c.getCurrent_x(), (int) c.getCurrent_y());
                // Recalculer la direction vers le joueur
                int dx = playerPosition.x - goulePosition.x;
                int dy = playerPosition.y - goulePosition.y;
                double distance = Math.sqrt(dx * dx + dy * dy);
                // Normaliser la direction
                if (distance > 0) {
                    double directionX = dx / distance;
                    double directionY = dy / distance;
                    TirGoule(goulePosition, directionX, directionY); // Tirer s'il est assez proche
                }
            }
        }
    }

    // Thread pour démarrer le déplacement de l'ennemie
    public void startMovement() {
        Thread movementThread = new Thread(() -> {
            while (isRunning) {
                goToCharacter();
                try {
                    Thread.sleep(50); // Attendre 50 ms entre chaque déplacement
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        movementThread.start();
    }

}
