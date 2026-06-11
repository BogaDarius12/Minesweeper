import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        // În Swing, este recomandat ca interfața grafică să fie pornită pe un fir de execuție special (Event Dispatch Thread)
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // Această linie pornește fizic jocul tău!
                new FereastraJoc();
            }
        });
    }
}