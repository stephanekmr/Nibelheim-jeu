package Model;

import java.awt.*;
import java.util.concurrent.CopyOnWriteArrayList;

import Controler.Character;

public class Tir {

    // Attributs et constantes
    public static final int speed = 25;

    // Liste de balles tirées
    private CopyOnWriteArrayList<Projectile> tirs = new CopyOnWriteArrayList<>();
    ///private ArrayList<Point> directions; // Nouvel attribut pour stocker la direction de chaque tir

    // Instances de classe
    private Point mousePosition; // Position de la souris
    private Character c;  // Le joueur
    private Obstacles o; // Les obstacles


    // Constructeur pour initialiser la liste de tirs
    public Tir(Character c, Obstacles obs) {
        this.o = obs;
        this.c = c;
        tirs = new CopyOnWriteArrayList<>();
        mousePosition = new Point(0, 0); // Initialiser la position de la souris

        // Démarrer le thread pour mettre à jour la position de la souris
        startMousePositionThread();
    }

    // Getteur pour récupérer les tirs
    public CopyOnWriteArrayList<Projectile> getTirs() {
        return tirs;
    }

    // Méthode pour ajouter un tir (tirer une nouvelle balle)
    public void addTir() {
        Point startPoint = new Point((int) (c.getCurrent_x() + 50), (int) (c.getCurrent_y() + 50)); // Point de départ du tir (position du joueur)
        Point direction = new Point(mousePosition.x - startPoint.x, mousePosition.y - startPoint.y); // Direction du tir
        // Créer un nouveau projectile avec la position et la direction
        Projectile projectile = new Projectile(startPoint, direction);
        // Ajouter le projectile à la liste des projectiles
        tirs.add(projectile); 
    }

    // Méthode pour supprimer les tirs qui sont sortis de la fenêtre
    public void removeTir(int index) {
        tirs.remove(index);
    }

    // Méthode pour mettre à jour les tirs et les faire avancer
    public void updateTirs() {
        for (int i = 0; i < tirs.size(); i++) {
            Projectile tir = tirs.get(i);
            Point direction = tir.getDirection(); // Récupérer la direction du tir
            // Vérifier que la direction n'est pas nulle ou invalide
            if (direction.x == 0 && direction.y == 0) {
                removeTir(i);
                continue;
            }
            // Normaliser la direction pour obtenir un vecteur unitaire
            double length = Math.sqrt(direction.x * direction.x + direction.y * direction.y);
            double dirX = direction.x / length;
            double dirY = direction.y / length;
            // Appliquer une vitesse constante au tir
            int newX = (int) (tir.getPosition().x + dirX * speed);
            int newY = (int) (tir.getPosition().y + dirY * speed);
            // Déplacer le tir dans la direction normalisée
            tir.setPosition(new Point(newX, newY));
            // Mettre à jour la hitbox du tir
            tir.hitboxProjectile.x = newX;
            tir.hitboxProjectile.y = newY;
            // Vérifier si le tir est sorti de la fenêtre et le supprimer
            if (newX < 0 || newX > 1920 || newY < 0 || newY > 1080) {
                removeTir(i);
            }
        }
    }

        // methode pour verifier si le tir touche un obstacle
        public boolean collisionTirObstacle(Projectile tir, Point obstacle) {
            Rectangle r1 = new Rectangle(tir.getPosition().x, tir.getPosition().y, 10, 10);
            Rectangle r2 = new Rectangle(obstacle.x, obstacle.y, Obstacles.WIDTH_O, Obstacles.HEIGHT_O);
            return r1.intersects(r2);
        }
    
        // Méthode pour vérifier si un tir touche un obstacle et le supprimer
        public void removeTirObstacle() {
            for (int i = 0; i < tirs.size(); i++) {
                for (Point obstacle : o.getObstacles()) {
                    if (collisionTirObstacle(tirs.get(i), obstacle)) {
                        tirs.remove(i);
                        break;
                    }
                }
            }
        }

    // Méthode pour démarrer le thread de mise à jour de la position de la souris
    private void startMousePositionThread() {
        Thread mousePositionThread = new Thread(() -> {
            while (true) {
                PointerInfo mouseInfo = MouseInfo.getPointerInfo();
                if (mouseInfo != null) {
                    Point mousePoint = mouseInfo.getLocation();
                    mousePosition.setLocation(mousePoint);
                    removeTirObstacle(); // Vérifier les collisions avec les obstacles
                }

                try {
                    Thread.sleep(50); // Délai de mise à jour de la position de la souris
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        mousePositionThread.start();
    }


    // Methode pour réinitialiser la liste des projectiles 
    public void resetTirs() {
        tirs.clear();
    }
}