package com.example.masterthesis.structure.threeD;

import javafx.scene.paint.Color;

public class Cell3D {
    int x, y, z;
    int state;
    Color cellColor;
    int colorCode;

    public Cell3D() {
    }

    public Cell3D(int x, int y, int z, int state, Color cellColor, int colorCode) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.state = state;
        this.cellColor = cellColor;
        this.colorCode = colorCode;
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

    public int getZ() {
        return z;
    }

    public void setZ(int z) {
        this.z = z;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public Color getCellColor() {
        return cellColor;
    }

    public void setCellColor(Color cellColor) {
        this.cellColor = cellColor;
    }

    public int getColorCode() {
        return colorCode;
    }

    public void setColorCode(int colorCode) {
        this.colorCode = colorCode;
    }
}
