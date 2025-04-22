package View;

public class Redessine extends Thread{
    // Constante pour le délai de rafraichissement
    public static final int DELAY = 25;

    // Instances de classe utiles
    private Affichage monAffichage;

    public Redessine(Affichage aff) {
        this.monAffichage = aff;
    }

    //Redessine l'écran 
    @Override
    public void run() {
      while (true) {
        // Redessiner l'écran
        monAffichage.revalidate();
        monAffichage.repaint();
        try { Thread.sleep(DELAY); }
        catch (Exception e) { e.printStackTrace(); }
      }
    }

}