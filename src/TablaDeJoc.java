public class TablaDeJoc {
    private Celula[][] grid;
    private int linii;
    private int coloane;
    private int numarTotalBombe;

    public TablaDeJoc(int linii, int coloane, int numarTotalBombe) {
        this.linii = linii;
        this.coloane = coloane;
        this.numarTotalBombe = numarTotalBombe;

        //Alocam memorie pentru grid
        this.grid = new Celula[linii][coloane];

        initializazaTabla();
    }

    private void initializazaTabla() {
        for (int i = 0; i < linii; i++) {
            for (int j = 0; j < coloane; j++) {
                // Apelam constructorul pentru fiecare celula
                grid[i][j] = new Celula();
            }
        }
    }

    public Celula getCelula(int linie, int coloana) {
        return grid[linie][coloana];
    }

    public void plaseazaBombe(int startL, int startC) {
        int bombePlasate = 0;
        while (bombePlasate < numarTotalBombe) {
            int lin = (int) (Math.random() * linii);
            int col = (int) (Math.random() * coloane);

            if(!grid[lin][col].esteMinat() && (lin != startL || col !=startC )){
                grid[lin][col].setMinat(true);
                bombePlasate++;
            }
        }
    }

    public void calculeazaNrVecini() {
        for (int i = 0; i < linii; i++) {
            for (int j = 0; j < coloane; j++){
                if (grid[i][j].esteMinat()) {continue;}

                int bombeGasite = 0;

                // Parcurgem vecinii de la -1 la +1 pe linii și coloane fata de celula din mijloc
                for (int dinamicL = -1; dinamicL <= 1; dinamicL++) {
                    for (int dinamicC = -1; dinamicC <= 1; dinamicC++) {
                        int vecinL = i + dinamicL;
                        int vecinC = j + dinamicC;

                        // 1. Verificam să nu ieșim de pe tabla
                        if (vecinL >= 0 && vecinL < linii && vecinC >= 0 && vecinC < coloane) {
                            // 2. Daca vecinul valid are bomba creștem contorul
                            if (grid[vecinL][vecinC].esteMinat()) {
                                bombeGasite++;
                            }
                        }
                    }
                }

                //Salvam numarul de bombe calculate in celula curenta
                grid[i][j].setBombeVecine(bombeGasite);
            }
        }
    }

    //Functie Flood(descopera mai multe celule daca are vecini care nu sunt minati)
    public void descoperaCelula(int l, int c) {
        // Condiție de oprire: în afara tablei
        if (l < 0 || l >= linii || c < 0 || c >= coloane) {
            return;
        }

        // Condiție de oprire: deja descoperită sau are steag
        if (grid[l][c].esteDescoperit() || grid[l][c].areSteag()) {
            return;
        }

        // Descoperim celula în logica din spate
        grid[l][c].setEsteDescoperit(true);

        // Condiție de oprire a propagării: dacă dăm de un număr (> 0 vecini)
        if (grid[l][c].getBombeVecine() > 0) {
            return;
        }

        // Propagarea recursivă pe cei 8 vecini
        for (int dinamicL = -1; dinamicL <= 1; dinamicL++) {
            for (int dinamicC = -1; dinamicC <= 1; dinamicC++) {
                if (dinamicL != 0 || dinamicC != 0) {
                    descoperaCelula(l + dinamicL, c + dinamicC);
                }
            }
        }
    }

    public boolean verificaVictorie() {
        for (int i = 0; i < linii; i++) {
            for (int j = 0; j < coloane; j++) {
                // Dacă găsim o celulă care NU este minată, dar a rămas NEDESCOPERITĂ,
                // înseamnă că jucătorul nu a terminat încă de curățat tabla.
                if (!grid[i][j].esteMinat() && !grid[i][j].esteDescoperit()) {
                    return false;
                }
            }
        }
        // Dacă am parcurs toată tabla și nu am găsit nicio celulă sigură ascunsă...
        return true;
    }
}