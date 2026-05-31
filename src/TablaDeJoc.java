public class TablaDeJoc {
    private Celula[][] grid;
    private int linii;
    private int coloane;
    private int numarTotalBombe;

    public TablaDeJoc(int linii, int coloane, int numarTotalBombe){
        this.linii = linii;
        this.coloane = coloane;
        this.numarTotalBombe = numarTotalBombe;

        //Alocam memorie pentru grid
        this.grid = new Celula[linii][coloane];

        initializazaTabla();
    }

    private void initializazaTabla(){
        for (int i = 0; i < linii; i++) {
            for (int j = 0; j < coloane; j++) {
                // Instanțiem fiecare celulă în parte
                grid[i][j] = new Celula();
            }
        }
    }
}
