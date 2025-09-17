package main;

import java.util.Random;

public final class LogicaFuncional {

    private LogicaFuncional() {}

    public static Estado crearEstadoVacio(int filas, int columnas) {
        return new Estado(new boolean[filas][columnas], new boolean[filas][columnas]);
    }

    public static Estado colocarMinas(Estado estado, int totalMinas, long semilla) {
        boolean[][] minas = Estado.copiarMatriz(estado.minas);
        int filas = minas.length;
        int cols = minas[0].length;

        Random rand = new Random(semilla);
        int colocadas = 0;
        while (colocadas < totalMinas) {
            int f = rand.nextInt(filas);
            int c = rand.nextInt(cols);
            if (!minas[f][c]) {
                minas[f][c] = true;
                colocadas++;
            }
        }
        return new Estado(minas, Estado.copiarMatriz(estado.revelado));
    }

    public static int contarMinasCerca(Estado estado, int f, int c) {
        int filas = estado.minas.length;
        int cols = estado.minas[0].length;
        int cuenta = 0;
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                int nf = f + i;
                int nc = c + j;
                if (nf >= 0 && nf < filas && nc >= 0 && nc < cols) {
                    if (estado.minas[nf][nc]) cuenta++;
                }
            }
        }
        return cuenta;
    }

    public static ResultadoRevelar revelarCelda(Estado estado, int f, int c) {
        int filas = estado.minas.length;
        int cols = estado.minas[0].length;


        if (f < 0 || f >= filas || c < 0 || c >= cols || estado.revelado[f][c]) {
            return new ResultadoRevelar(estado, false);
        }

        Estado nuevo = estado.copiar();
        nuevo.revelado[f][c] = true;

        if (nuevo.minas[f][c]) {
            return new ResultadoRevelar(nuevo, true);
        }

        int alrededor = contarMinasCerca(nuevo, f, c);
        if (alrededor == 0) {
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    if (i != 0 || j != 0) {
                        nuevo = revelarCelda(nuevo, f + i, c + j).estado;
                    }
                }
            }
        }
        return new ResultadoRevelar(nuevo, false);
    }

    public static boolean esVictoria(Estado estado) {
        for (int f = 0; f < estado.minas.length; f++) {
            for (int c = 0; c < estado.minas[0].length; c++) {
                if (!estado.minas[f][c] && !estado.revelado[f][c]) {
                    return false;
                }
            }
        }
        return true;
    }

    public static final class ResultadoRevelar {
        public final Estado estado;
        public final boolean exploto;

        public ResultadoRevelar(Estado estado, boolean exploto) {
            this.estado = estado;
            this.exploto = exploto;
        }
    }
}
