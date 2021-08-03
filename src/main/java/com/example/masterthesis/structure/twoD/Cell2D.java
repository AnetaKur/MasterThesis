package com.example.masterthesis.structure.twoD;

import javafx.scene.paint.Color;

public class Cell2D {
    int state;
    int x, y;
    Color cellColor;
    int colorCode;

    public Cell2D() {
        state = 0;
        colorCode = 1;
        cellColor = Color.WHITE;
    }

    public Cell2D(int state, int x, int y, Color cellColor) {
        this.state = state;
        this.x = x;
        this.y = y;
        this.cellColor = cellColor;
        colorCode = 1;

    }

    public Cell2D(int state, int x, int y, Color cellColor, int colorCode) {
        this.state = state;
        this.x = x;
        this.y = y;
        this.cellColor = cellColor;
        this.colorCode = colorCode;
    }

    public int getColorCode() {
        return colorCode;
    }

    public void setColorCode(int colorCode) {
        this.colorCode = colorCode;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Color getCellColor() {
        return cellColor;
    }

    public void setCellColor(Color cellColor) {
        this.cellColor = cellColor;
    }

    public void setPosition(int x, int y) {
        setX(x);
        setY(y);
    }

    public double getDistance(int x1, int y1) {
        double dist = 0;
        dist = Math.sqrt(Math.pow(this.x - x1, 2) + Math.pow(this.y - y1, 2));

        return dist;
    }

    @Override
    public String toString() {
        return "Cell{" +
                "x=" + x +
                ", y=" + y +
                ", state = " + state +
                "}\n";
    }

}
