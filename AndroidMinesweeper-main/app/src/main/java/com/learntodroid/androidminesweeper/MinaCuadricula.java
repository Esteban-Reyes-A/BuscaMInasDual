package com.learntodroid.androidminesweeper;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MinaCuadricula {
    private List<Celda> celdas;
    private int Tamanio;

    public MinaCuadricula(int size) {
        this.Tamanio = size;
        this.celdas = new ArrayList<>();
        for (int i = 0; i < size * size; i++) {
            celdas.add(new Celda(Celda.BLANK));
        }
    }

    public void generateGrid(int totalBombs) {
        int bombsPlaced = 0;
        while (bombsPlaced < totalBombs) {
            int x = new Random().nextInt(Tamanio);
            int y = new Random().nextInt(Tamanio);

            if (cellAt(x, y).getValue() == Celda.BLANK) {
                celdas.set(x + (y* Tamanio), new Celda(Celda.BOMB));
                bombsPlaced++;
            }
        }

        for (int x = 0; x < Tamanio; x++) {
            for (int y = 0; y < Tamanio; y++) {
                if (cellAt(x, y).getValue() != Celda.BOMB) {
                    List<Celda> adjacentCeldas = adjacentCells(x, y);
                    int countBombs = 0;
                    for (Celda celda : adjacentCeldas) {
                        if (celda.getValue() == Celda.BOMB) {
                            countBombs++;
                        }
                    }
                    if (countBombs > 0) {
                        celdas.set(x + (y* Tamanio), new Celda(countBombs));
                    }
                }
            }
        }
    }

    public Celda cellAt(int x, int y) {
        if (x < 0 || x >= Tamanio || y < 0 || y >= Tamanio) {
            return null;
        }
        return celdas.get(x + (y* Tamanio));
    }

    public List<Celda> adjacentCells(int x, int y) {
        List<Celda> adjacentCeldas = new ArrayList<>();

        List<Celda> cellsList = new ArrayList<>();
        cellsList.add(cellAt(x-1, y));
        cellsList.add(cellAt(x+1, y));
        cellsList.add(cellAt(x-1, y-1));
        cellsList.add(cellAt(x, y-1));
        cellsList.add(cellAt(x+1, y-1));
        cellsList.add(cellAt(x-1, y+1));
        cellsList.add(cellAt(x, y+1));
        cellsList.add(cellAt(x+1, y+1));

        for (Celda celda : cellsList) {
            if (celda != null) {
                adjacentCeldas.add(celda);
            }
        }

        return adjacentCeldas;
    }

    public int toIndex(int x, int y) {
        return x + (y* Tamanio);
    }

    public int[] toXY(int index) {
        int y = index / Tamanio;
        int x = index - (y* Tamanio);
        return new int[]{x, y};
    }

    public void revealAllBombs() {
        for (Celda c: celdas) {
            if (c.getValue() == Celda.BOMB) {
                c.setRevealed(true);
            }
        }
    }

    public List<Celda> getCells() {
        return celdas;
    }
}
