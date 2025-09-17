package main;

import java.util.Arrays;

public final class Estado {
    public final boolean[][] minas;
    public final boolean[][] revelado;

    public Estado(boolean[][] minas, boolean[][] revelado) {
        this.minas = minas;
        this.revelado = revelado;
    }

    public static boolean[][] copiarMatriz(boolean[][] m) {
        boolean[][] copia = new boolean[m.length][m[0].length];
        for (int i = 0; i < m.length; i++) {
            copia[i] = Arrays.copyOf(m[i], m[i].length);
        }
        return copia;
    }

    public Estado copiar() {
        return new Estado(copiarMatriz(minas), copiarMatriz(revelado));
    }
}

