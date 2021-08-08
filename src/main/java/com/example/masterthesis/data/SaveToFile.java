package com.example.masterthesis.data;

import com.example.masterthesis.automat.CABuilder;
import com.example.masterthesis.structure.twoD.Cell2D;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class SaveToFile {
    public void saveData(CABuilder cellural) throws IOException {
        Random r = new Random();

        BufferedWriter writer = new BufferedWriter(new FileWriter("data.txt"));
        int atoms = cellural.CA.getHeight() * cellural.CA.getWidth();

        writer.append("\n\n" + atoms + " atoms\n");
        writer.append(cellural.getTypeSize() + " atom types\n");
        writer.append("0.0 " + cellural.CA.getHeight() + " xlo xhi\n");
        writer.append("0.0 " + cellural.CA.getWidth() + " ylo yhi\n");
        writer.append("0.0 0.0 zlo zhi\n");
        writer.append("\n");
        int k = 1;
        writer.append("Atoms\n\n");
        for (int i = 0; i < cellural.CA.getHeight(); i++) {
            for (int j = 0; j < cellural.CA.getWidth(); j++) {
                double z = 0.0+(1.0 - 0.0)*r.nextDouble();
                Cell2D c = cellural.CA.getCell(i, j);
                writer.append(k + " " + c.getColorCode() + " " + c.getX() + " " + c.getY() +" "+z+ "\n");
                k++;
            }


        }
        writer.close();

    }
}
