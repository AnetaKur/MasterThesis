package com.example.masterthesis.automat;

import com.example.masterthesis.structure.twoD.Cell2D;
import javafx.scene.paint.Color;

public class CelluralAutomata2D {
    int width;
    int height;
    Cell2D[][] matrix;

    public CelluralAutomata2D(int width, int height) {
        this.width = width;
        this.height = height;
        matrix = new Cell2D[this.height][this.width];
        for (int i = 0; i < (this.height); i++) {
            for (int j = 0; j < (this.width); j++) {
                matrix[i][j] = new Cell2D();
                matrix[i][j].setPosition(i, j);
                matrix[i][j].setState(0);
                matrix[i][j].setCellColor(Color.WHITE);
            }
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Cell2D[][] getMatrix() {
        return matrix;
    }

    public Cell2D getCell(int i, int j) {
        return matrix[i][j];
    }
}
