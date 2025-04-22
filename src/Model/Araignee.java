package Model;

import java.awt.Image;
import java.awt.Point;
import javax.swing.ImageIcon;

public class Araignee extends Ennemis {

    // Points de vie de l'ennemie
    private static final int HEALTH_MAX = 2;

    // Taille du sprite du fantôme
    public static final int WIDTH = 64;
    public static final int HEIGHT = 40;

    // Classe Character
    Character c;

    // Image de l'ennemie
    public static final Image sprite = new ImageIcon("src/Images/araignee.gif").getImage().getScaledInstance(WIDTH,HEIGHT, Image.SCALE_DEFAULT);

    // Constructeur
    public Araignee(Character c, int speed, int bonusAmount, Point pos, Bonus b) {
        super(c, HEALTH_MAX, speed, WIDTH, HEIGHT, bonusAmount, pos, sprite, b);
        this.c = c;
    }

    // Méthode pour déplacer le fantôme vers le joueur
    public void goToCharacter() {
        // Récupérer la position actuelle du joueur
        Point playerPosition = new Point((int) c.getCurrent_x(), (int) c.getCurrent_y());

        // Mettre à jour la hitbox de l'ennemie
        this.hitboxEnnemie.x = this.position.x;
        this.hitboxEnnemie.y = this.position.y;

        // Récupérer la position actuelle du fantôme
        Point ghostPosition = getPosition();

        // Calculer la direction vers le joueur
        int dx = playerPosition.x - ghostPosition.x;
        int dy = playerPosition.y - ghostPosition.y;

        // Calculer la distance entre le joueur et le fantôme
        double distance = Math.sqrt(dx * dx + dy * dy);

        // Éviter la division par zéro
        if (distance > 0) {
            // Normaliser la direction
            double directionX = dx / distance;
            double directionY = dy / distance;

            // Déplacer le fantôme dans la direction du joueur
            ghostPosition.x += directionX * getSpeed();
            ghostPosition.y += directionY * getSpeed();

            // Mettre à jour la position du fantôme
            setPosition(ghostPosition);
        } else {
            // Le fantôme est déjà sur le joueur
            // System.out.println("Le fantôme a atteint le joueur !");
        }
    }

    // Thread pour démarrer le déplacement de l'ennemie
    public void startMovement() {
        Thread movementThread = new Thread(() -> {
            while (true) {
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
