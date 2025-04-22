package Model;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Random;

public class Obstacles {
    // dimension de l'obstacle
    public static final int HEIGHT_O = 50;
    public static final int WIDTH_O = 50;
    // liste d'obstacle
    public ArrayList<Point> obstacles = new ArrayList<Point>();
    // generer un nombre aleatoire
    Random rand = new Random();
    // Distance minimale entre deux obstacles
    private static final int DISTANCE_MIN = 150;

    // constructeur de la classe Obstacles
    public Obstacles() {
        // genererObstacle();
    }
    
    // retourner la liste d'obstacle
    public ArrayList<Point> getObstacles() {
        return this.obstacles;
    }

    // generer une liste d'obstacle
    public void genererObstacle(int nbObstacle) {
        obstacles.clear(); // Vider la liste d'obstacles avant de générer de nouveaux obstacles

        while (obstacles.size() < nbObstacle) {
            // Générer une position aléatoire pour l'obstacle
            int x = rand.nextInt(1920 - WIDTH_O);
            int y = rand.nextInt(1080 - HEIGHT_O);
            Point obs = new Point(x, y); // Créer un point pour l'obstacle
            // Vérifier que l'obstacle respecte les conditions
            if (verifierDistanceObstacle(obs) && !Character.collisionObstacleJoueur(x, y)) {
                obstacles.add(obs); // Ajouter l'obstacle à la liste
            }

        }
    }

    // methode qui verifie que les obstacles ne sont pas trop proches
    public boolean verifierDistanceObstacle(Point p) {
        for (Point o : obstacles) {
            // Distance entre les obstacles
            double distance = Math.sqrt(Math.pow(o.x - p.x, 2) + Math.pow(o.y - p.y, 2));
            // Si la distance est inférieure à la distance minimale
            if (distance < DISTANCE_MIN) {
                return false;
            }
        }
        return true;
    }

}
