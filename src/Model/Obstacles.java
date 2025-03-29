package Model;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;


public class Obstacles {
    //nombre d'obstacle valeur aleartoire entre 7 et 10
    public static int nbObstacle = 7 + (int) (Math.random() * 4);
    //dimension de l'obstacle
    public static final int HEIGHT_O = 50;
    public static final int WIDTH_O = 50;
    //liste d'obstacle
    public ArrayList<Point> obstacles= new ArrayList<Point>();
    //generer un nombre aleatoire
    Random rand = new Random();
    // Distance minimale entre deux obstacles
    private static final int DISTANCE_MIN = 150;

    //constructeur de la classe Obstacles
    public Obstacles() {
        genererObstacle();
    }
    //generer une liste d'obstacle
    public void genererObstacle() {
        int x;
        int y;
        for (int i = 0; i < nbObstacle; i++) {
            //generer un obstacle
            x = rand.nextInt(1000);
            y = rand.nextInt(1000);
           
            //verifier que les obstacles ne sont pas a la position du joueur
            if(x>800 && x<900 && y>500 && y<600){
                x = rand.nextInt(1500);
                y = rand.nextInt(1000);
            } 
            Point p = new Point(x, y);
            //ajouter l'obstacle a la liste si la distance entre les obstacles est suffisante
            if (verifierDistanceObstacle(p)){
                obstacles.add(p);

            }
            //sinon on decremente i pour generer un autre obstacle
            else{
                i--;
            }
            
        }
    }
    //retourner la liste d'obstacle
    public ArrayList<Point> getObstacles() {
        return this.obstacles;
    }
    //methode qui verifie que les obstacles ne sont pas trop proches
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
