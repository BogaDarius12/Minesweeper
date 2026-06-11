import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class FereastraJoc extends JFrame {
    private JPanel panouPrincipal;
    private JPanel panouGrid;
    private JLabel etichetaTimer;
    private Timer timerGrafic;
    private int secundeTrecute = 0;

    // 1. Dimensiunile tabelei si numarul de bombe
    private final int NR_LINII = 10;
    private final int NR_COLOANE = 10;
    private final int NR_BOMBE = 10;

    // Primul click trebuie sa fie safe, porneste ca true
    private boolean primulClick = true;

    // 2. Matricea de butoane pentru a le accesa din orice metoda
    private JButton[][] matriceButoane = new JButton[NR_LINII][NR_COLOANE];

    // 3. Legatura catre logica jocului
    private TablaDeJoc tablaLogica;

    public FereastraJoc() {
        panouPrincipal = new JPanel(new BorderLayout());
        panouGrid = new JPanel();


        JPanel panouSus = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        etichetaTimer = new JLabel("Timp: 0s");
        etichetaTimer.setFont(new Font("Arial", Font.BOLD, 18));
        panouSus.add(etichetaTimer);

        panouPrincipal.add(panouSus, BorderLayout.NORTH);
        panouPrincipal.add(panouGrid, BorderLayout.CENTER);

        setContentPane(panouPrincipal);
        setTitle("Minesweeper");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        tablaLogica = new TablaDeJoc(NR_LINII, NR_COLOANE, NR_BOMBE);
        panouGrid.setLayout(new GridLayout(NR_LINII, NR_COLOANE));

        genereazaButoane();
        configureazaTimer(); // Initializam mecanismul de timer

        pack();
        setSize(650, 700);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void configureazaTimer() {
        // Ii spunem sa se activeze la fiecare 1000ms (1 secunda)
        timerGrafic = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                secundeTrecute++;
                etichetaTimer.setText("Timp: " + secundeTrecute + "s");
            }
        });
    }

    private void genereazaButoane() {
        for (int i = 0; i < NR_LINII; i++) {
            for (int j = 0; j < NR_COLOANE; j++) {
                JButton buton = new JButton();

                // Salvam butonul In matricea noastra de butoane
                matriceButoane[i][j] = buton;

                final int linieCurenta = i;
                final int coloanaCurenta = j;

                // --- CLICK STANGA (DESCOPERIRE) ---
                buton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        JButton butonApasat = (JButton) e.getSource();

                        // Luam celula logica corespunzatoare coordonatelor
                        Celula celulaLogica = tablaLogica.getCelula(linieCurenta, coloanaCurenta);

                        // Daca celula este deja descoperita sau are steag, ignoram click-ul complet
                        if (celulaLogica.esteDescoperit() || celulaLogica.areSteag()) {
                            return;
                        }

                        //Logica ca primul click sa fie safe
                        if (primulClick) {
                            tablaLogica.plaseazaBombe(linieCurenta, coloanaCurenta);
                            tablaLogica.calculeazaNrVecini();
                            primulClick = false;

                            timerGrafic.start();
                        }

                        // Verificam daca celula pe care s-a apasat este MINATa
                        if (celulaLogica.esteMinat()) {
                            butonApasat.setText("💣");
                            butonApasat.setBackground(Color.RED);
                            System.out.println("Game Over! Ai calcat pe o mina.");

                            // Dezactivam tot si aratam restul bombelor
                            dezactiveazaToateButoanele();

                            // Intrebam utilizatorul daca vrea Try Again
                            int raspuns = JOptionPane.showConfirmDialog(
                                    FereastraJoc.this,
                                    "Ai calcat pe o mina! Vrei sa Incerci din nou?",
                                    "Game Over",
                                    JOptionPane.YES_NO_OPTION,
                                    JOptionPane.QUESTION_MESSAGE
                            );

                            if (raspuns == JOptionPane.YES_OPTION) {
                                reseteazaJocul();
                            }
                            if (raspuns == JOptionPane.NO_OPTION) {
                                System.exit(0);
                            }
                        }
                        // Daca este o celula sigura
                        else {
                            // Trimitem comanda de Flood Fill In logica jocului
                            tablaLogica.descoperaCelula(linieCurenta, coloanaCurenta);

                            // Actualizam toata interfata grafica pe ecran
                            actualizeazaInterfata();

                            // Verificam daca aceasta miscare a adus victoria
                            if (tablaLogica.verificaVictorie()) {
                                System.out.println("Felicitari! Ai castigat!");
                                dezactiveazaToateButoanele();

                                int raspuns = JOptionPane.showConfirmDialog(
                                        FereastraJoc.this,
                                        "Felicitari, ai castigat! Vrei sa mai joci o runda?",
                                        "Victorie!",
                                        JOptionPane.YES_NO_OPTION,
                                        JOptionPane.INFORMATION_MESSAGE
                                );

                                if (raspuns == JOptionPane.YES_OPTION) {
                                    reseteazaJocul();
                                }
                                if (raspuns == JOptionPane.NO_OPTION){
                                    System.exit(0);
                                }
                            }
                        }
                    }
                });

                // --- CLICK DREAPTA (STEAGURI) ---
                buton.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        if (SwingUtilities.isRightMouseButton(e)) {
                            Celula celulaLogica = tablaLogica.getCelula(linieCurenta, coloanaCurenta);

                            // Nu putem pune steag pe o celula deja descoperita
                            if (!celulaLogica.esteDescoperit()) {
                                boolean stareaNoua = !celulaLogica.areSteag();
                                celulaLogica.setAreSteag(stareaNoua);

                                // Actualizam vizual textul butonului
                                if (stareaNoua) {
                                    buton.setText("🚩");
                                    buton.setForeground(Color.RED);
                                } else {
                                    buton.setText("");
                                }
                            }
                        }
                    }
                });

                panouGrid.add(buton);
            }
        }
    }

    // Metoda care parcurge logica din spate si sincronizeaza butoanele de pe ecran fara sa le piarda culorile
    private void actualizeazaInterfata() {
        for (int i = 0; i < NR_LINII; i++) {
            for (int j = 0; j < NR_COLOANE; j++) {
                Celula celulaLogica = tablaLogica.getCelula(i, j);
                JButton butonGrafic = matriceButoane[i][j];

                if (celulaLogica.esteDescoperit()) {
                    // Blocam interactiunea pastrand culorile vii
                    butonGrafic.setFocusable(false);
                    butonGrafic.setBackground(new Color(235, 235, 235)); // Fundal plat gri deschis
                    butonGrafic.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
                    butonGrafic.setFont(new Font("Arial", Font.BOLD, 16));

                    int vecini = celulaLogica.getBombeVecine();
                    if (vecini > 0) {
                        butonGrafic.setText(String.valueOf(vecini));

                        // Paleta completa de culori Minesweeper (1-8)
                        switch (vecini) {
                            case 1: butonGrafic.setForeground(Color.BLUE); break;
                            case 2: butonGrafic.setForeground(new Color(0, 128, 0)); break; // Verde Inchis
                            case 3: butonGrafic.setForeground(Color.RED); break;
                            case 4: butonGrafic.setForeground(new Color(0, 0, 128)); break;  // Albastru Inchis
                            case 5: butonGrafic.setForeground(new Color(128, 0, 0)); break;  // Maro
                            case 6: butonGrafic.setForeground(new Color(0, 128, 128)); break; // Albastru Deschis
                            case 7: butonGrafic.setForeground(Color.BLACK); break;
                            case 8: butonGrafic.setForeground(Color.GRAY); break;
                        }
                    } else {
                        butonGrafic.setText(""); // 0 vecini ramane complet gol
                    }
                }
            }
        }
    }

    // Oprim posibilitatea de a mai juca dupa un Game Over / Victorie
    private void dezactiveazaToateButoanele() {
        timerGrafic.stop(); // Aici chiar opreste Timer-ul
        for (int i = 0; i < NR_LINII; i++) {
            for (int j = 0; j < NR_COLOANE; j++) {
                Celula celulaLogica = tablaLogica.getCelula(i, j);

                // Dezactivam complet butoanele grafice
                matriceButoane[i][j].setEnabled(false);

                // Daca am pierdut, dezvaluim unde erau restul bombelor ascunse
                if (celulaLogica.esteMinat()) {
                    matriceButoane[i][j].setText("💣");
                }
            }
        }
    }

    // Reseteaza interfata si logica la starea initiala
    private void reseteazaJocul() {
        tablaLogica = new TablaDeJoc(NR_LINII, NR_COLOANE, NR_BOMBE);
        primulClick = true;

        timerGrafic.stop();
        secundeTrecute = 0;
        etichetaTimer.setText("Timp: 0s");

        for (int i = 0; i < NR_LINII; i++) {
            for (int j = 0; j < NR_COLOANE; j++) {
                JButton buton = matriceButoane[i][j];
                buton.setText("");
                buton.setEnabled(true);
                buton.setFocusable(true);
                buton.setBackground(null);
                buton.setForeground(null);
                buton.setBorder(UIManager.getBorder("Button.border"));
            }
        }
        System.out.println("Jocul a fost resetat cu succes!");
    }
}