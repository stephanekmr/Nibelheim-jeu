package Model;

// Thread pour mettre a jour la position des tirs toutes les 50ms
public class Avancer_tir extends Thread {

    Tir tir;
    // Attributs et constantes
    private int DELAY = 30; // Délai de 30ms entre chaque mise à jour

    // constructeur de la classe
    public Avancer_tir(Tir t) {
        this.tir = t;
    }

    // Modifier la méthode run() pour mettre à jour la position des tirs toutes les 50ms
    @Override
    public void run() {
        while (true) {
            // Mettre à jour les tirs
            tir.updateTirs();
            tir.removeTirObstacle();
            try {
                Thread.sleep(DELAY);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
