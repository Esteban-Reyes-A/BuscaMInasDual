package com.learntodroid.androidminesweeper;

import java.util.ArrayList;
import java.util.List;

public class JuegoBuscaMinas {
    private MinaCuadricula minaCuadricula;
    private boolean juegoTerminado;
    private boolean bandera;
    private boolean reiniciar;
    private int conteoBandera;
    private int conteoBomba;
    private boolean tiempoTerminado;

    public JuegoBuscaMinas(int size, int numberBombs) {
        this.juegoTerminado = false;
        this.bandera = false;
        this.reiniciar = true;
        this.tiempoTerminado = false;
        this.conteoBandera = 0;
        this.conteoBomba = numberBombs;
        minaCuadricula = new MinaCuadricula(size);
        minaCuadricula.generateGrid(numberBombs);
    }

    public void handleCellClick(Celda celda) {
        if (!juegoTerminado && !isGameWon() && !tiempoTerminado && !celda.isRevealed()) {
            if (reiniciar) {
                clear(celda);
            } else if (bandera) {
                flag(celda);
            }
        }
    }

    public void clear(Celda celda) {
        int index = getMineGrid().getCells().indexOf(celda);
        getMineGrid().getCells().get(index).setRevealed(true);

        if (celda.getValue() == Celda.BOMB) {
            juegoTerminado = true;
        } else if (celda.getValue() == Celda.BLANK) {
            List<Celda> toClear = new ArrayList<>();
            List<Celda> toCheckAdjacents = new ArrayList<>();

            toCheckAdjacents.add(celda);

            while (toCheckAdjacents.size() > 0) {
                Celda c = toCheckAdjacents.get(0);
                int cellIndex = getMineGrid().getCells().indexOf(c);
                int[] cellPos = getMineGrid().toXY(cellIndex);
                for (Celda adjacent: getMineGrid().adjacentCells(cellPos[0], cellPos[1])) {
                    if (adjacent.getValue() == Celda.BLANK) {
                        if (!toClear.contains(adjacent)) {
                            if (!toCheckAdjacents.contains(adjacent)) {
                                toCheckAdjacents.add(adjacent);
                            }
                        }
                    } else {
                        if (!toClear.contains(adjacent)) {
                            toClear.add(adjacent);
                        }
                    }
                }
                toCheckAdjacents.remove(c);
                toClear.add(c);
            }

            for (Celda c: toClear) {
                c.setRevealed(true);
            }
        }
    }

    public void flag(Celda celda) {
        celda.setFlagged(!celda.isFlagged());
        int count = 0;
        for (Celda c: getMineGrid().getCells()) {
            if (c.isFlagged()) {
                count++;
            }
        }
        conteoBandera = count;
    }

    public boolean isGameWon() {
        int numbersUnrevealed = 0;
        for (Celda c: getMineGrid().getCells()) {
            if (c.getValue() != Celda.BOMB && c.getValue() != Celda.BLANK && !c.isRevealed()) {
                numbersUnrevealed++;
            }
        }

        if (numbersUnrevealed == 0) {
            return true;
        } else {
            return false;
        }
    }

    public void toggleMode() {
        reiniciar = !reiniciar;
        bandera = !bandera;
    }

    public void outOfTime() {
        tiempoTerminado = true;
    }

    public boolean isJuegoTerminado() {
        return juegoTerminado;
    }

    public MinaCuadricula getMineGrid() {
        return minaCuadricula;
    }

    public boolean isBandera() {
        return bandera;
    }

    public boolean isReiniciar() {
        return reiniciar;
    }

    public int getConteoBandera() {
        return conteoBandera;
    }

    public int getConteoBomba() {
        return conteoBomba;
    }
}
