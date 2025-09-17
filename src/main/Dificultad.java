package main;

public enum Dificultad {
    FACIL(10, 10, 10),
    MEDIA(12, 12, 15),
    DIFICIL(14, 14, 20);

    public final int filas;
    public final int columnas;
    public final int minas;

    Dificultad(int filas, int columnas, int minas) {
        this.filas = filas;
        this.columnas = columnas;
        this.minas = minas;
    }

    @Override
    public String toString() {
        return name().charAt(0) + name().substring(1).toLowerCase()
                + " (" + filas + "x" + columnas + ", " + minas + " minas)";
    }
}
