package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;


public class VistaBuscaMinas extends JFrame {
    private int extraFilas = 0;
    private int extraCols  = 0;
    private int extraMinas = 0;


    private Dificultad dificultadActual = Dificultad.MEDIA;
    private JButton[][] botones;
    private Estado estado;
    private JPanel panelTablero;
    private final JComboBox<Dificultad> comboDificultad = new JComboBox<>(Dificultad.values());

    public VistaBuscaMinas() {
        setTitle("Busca Minas");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());


        JPanel arriba = new JPanel(new FlowLayout(FlowLayout.LEFT));
        arriba.add(new JLabel("Dificultad: "));
        comboDificultad.setSelectedItem(dificultadActual);
        comboDificultad.addActionListener(this::cambiarDificultad);
        arriba.add(comboDificultad);
        add(arriba, BorderLayout.NORTH);


        panelTablero = new JPanel();
        panelTablero.setPreferredSize(new Dimension(500, 500));
        add(panelTablero, BorderLayout.CENTER);

        reiniciarJuego();
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }


    private void reiniciarJuego() {
        int filas = dificultadActual.filas + extraFilas;
        int cols = dificultadActual.columnas + extraCols;
        int minas = dificultadActual.minas + extraMinas;


        estado = LogicaFuncional.crearEstadoVacio(filas, cols);
        estado = LogicaFuncional.colocarMinas(estado, minas, System.nanoTime());


        panelTablero.removeAll();
        panelTablero.setLayout(new GridLayout(filas, cols));
        botones = new JButton[filas][cols];

        for (int f = 0; f < filas; f++) {
            for (int c = 0; c < cols; c++) {
                JButton b = new JButton();
                b.setFont(new Font("Arial", Font.BOLD, 14));
                final int ff = f, cc = c;
                b.addActionListener(e -> manejarClick(ff, cc));
                botones[f][c] = b;
                panelTablero.add(b);
            }
        }

        panelTablero.revalidate();
        panelTablero.repaint();
        pack();
    }


    private void manejarClick(int f, int c) {
        LogicaFuncional.ResultadoRevelar r = LogicaFuncional.revelarCelda(estado, f, c);
        estado = r.estado;

        if (r.exploto) {

            botones[f][c].setText("BB");
            botones[f][c].setBackground(Color.RED);

            revelarTodasLasMinasUI();
            int res = JOptionPane.showConfirmDialog(
                    this,
                    "¡BOOM! Perdiste :(\n¿Reiniciar?",
                    "Juego terminado",
                    JOptionPane.DEFAULT_OPTION
            );
            if (res == JOptionPane.OK_OPTION) {
                //extraFilas += 5;
                //extraCols  += 5;
                //extraMinas += 5;
                reiniciarJuego();
            }


            return;
        }


        repintarTableroUI();

        if (LogicaFuncional.esVictoria(estado)) {
            JOptionPane.showMessageDialog(
                    this,
                    "¡Ganaste! :D",
                    "Juego terminado",
                    JOptionPane.INFORMATION_MESSAGE
            );
            reiniciarJuego();

        }
    }


    private void repintarTableroUI() {
        int filas = botones.length;
        int cols = botones[0].length;

        for (int f = 0; f < filas; f++) {
            for (int c = 0; c < cols; c++) {
                JButton b = botones[f][c];
                if (estado.revelado[f][c]) {
                    if (estado.minas[f][c]) {
                        b.setText("BB");
                        b.setBackground(Color.RED);
                        b.setEnabled(false);
                    } else {
                        int n = LogicaFuncional.contarMinasCerca(estado, f, c);
                        b.setText(n > 0 ? String.valueOf(n) : "");
                        b.setEnabled(false);
                    }
                } else {
                    b.setText("");
                    b.setBackground(null);
                    b.setEnabled(true);
                }
            }
        }
    }


    private void revelarTodasLasMinasUI() {
        for (int f = 0; f < botones.length; f++) {
            for (int c = 0; c < botones[0].length; c++) {
                if (estado.minas[f][c]) {
                    botones[f][c].setText("BB");
                    botones[f][c].setEnabled(false);
                }
            }
        }
    }


    private void cambiarDificultad(ActionEvent e) {
        dificultadActual = (Dificultad) comboDificultad.getSelectedItem();
        reiniciarJuego();
    }
}
