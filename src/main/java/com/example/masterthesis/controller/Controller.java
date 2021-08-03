package com.example.masterthesis.controller;

import com.example.masterthesis.automat.CABuilder;
import com.example.masterthesis.automat.CelluralAutomata2D;
import com.example.masterthesis.structure.twoD.BoundaryCondition;
import com.example.masterthesis.structure.twoD.Neighbourhood2D;
import javafx.animation.AnimationTimer;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;

import static com.example.masterthesis.structure.twoD.BoundaryCondition.*;
import static com.example.masterthesis.structure.twoD.Neighbourhood2D.*;
import static com.example.masterthesis.structure.twoD.Nucleation2D.*;


public class Controller implements Initializable {

    public Canvas matrix;
    public ChoiceBox BC;
    public ChoiceBox type;
    public TextField h;
    public TextField w;
    public TextField rowAmount;
    public TextField columnAmount;
    public TextField radius;
    public Button start;
    public TextField amount;

    public CelluralAutomata2D automat;
    public CABuilder builder;
    public GridPane gridPane;
    public javafx.scene.layout.Pane Pane;
    public ScrollPane scroll;
    public ChoiceBox squareSize;
    public ChoiceBox neighbourhood;
    public Button Allgrowth;
    public Button step;
    public Button monteCarlo;
    public TextField MCcounter;
    public Button stopMC;

    AnimationTimer animationTimer;
    AnimationTimer monteCarloAnimation;


    // int size;//wielkość jednej komórki
    GraphicsContext gc;
    Random r;
    double k;
    int height, width;
    int c = 0;
    float time = 0;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        BC.setItems(FXCollections.observableArrayList(
                periodic, absorbic));
        BC.setValue(periodic);
        type.setItems(FXCollections.observableArrayList(
                homogeneous, withRadius, random, clicking));
        type.setValue(random);
        squareSize.setItems(FXCollections.observableArrayList(1, 2, 4, 10));
        squareSize.setValue(4);
        neighbourhood.setItems(FXCollections.observableArrayList(
                vonNeumann, Moore, PentagonalDown, PentagonalLeft, PentagonalRandom, PentagonalRight,
                PentagonalUp, HeksagonalLeft, HeksagonalRight, HeksagonalRandom));
        neighbourhood.setValue(vonNeumann);
        w.setText("100");
        h.setText("100");
        amount.setText("10");

        height = Integer.parseInt(h.getText()) * (int) squareSize.getValue();
        width = Integer.parseInt(w.getText()) * (int) squareSize.getValue();


        gc = matrix.getGraphicsContext2D();
        r = new Random();
        Pane.setPrefWidth(gridPane.getPrefWidth());

        Pane.setPrefHeight(gridPane.getPrefHeight());
        MCcounter.setEditable(false);
        monteCarlo.setDisable(true);
        stopMC.setDisable(true);


        animationTimer = new AnimationTimer() {
            @Override
            public void handle(long l) {

                monteCarlo.setDisable(true);
                stopMC.setDisable(true);

                simulation((Neighbourhood2D) neighbourhood.getValue());

                try {
                    //slowing down for better animation
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (builder.isOver()) {
                    stop();
                    monteCarlo.setDisable(false);
                    stopMC.setDisable(false);

                }
                ;
            }
        };


    }


    void build(Object t, Object b) {


        if (homogeneous.equals(t) && Integer.parseInt(rowAmount.getText()) > 1 &&
                Integer.parseInt(rowAmount.getText()) < (height / (2 * (int) squareSize.getValue())) && Integer.parseInt(columnAmount.getText()) > 1 &&
                Integer.parseInt(columnAmount.getText()) < (width / (2 * (int) squareSize.getValue()))) {
//            && height%Integer.parseInt(rowAmount.getText()) == 0 &&
//                    width%Integer.parseInt(columnAmount.getText()) == 0
            //sprawdzam by w w r i c nie było ciągiem komórek mają być przerwy
            //sprawdzam by dlugosc byla podzielna przez ilosc

            //   setBC(b);
            builder.buildHomogenous(Integer.parseInt(columnAmount.getText()), Integer.parseInt(rowAmount.getText()));
            draw(gc);
        } else if (withRadius.equals(t)) {

            if (Integer.parseInt(radius.getText()) >= Integer.parseInt(h.getText()) || Integer.parseInt(radius.getText()) >= Integer.parseInt(w.getText())) {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setHeaderText("Error");
                errorAlert.setContentText("Radius is too big");
                errorAlert.showAndWait();

            } else {
                int r = Integer.parseInt(radius.getText());
                builder.buildWithRadius(Integer.parseInt(amount.getText()), r);
                draw(gc);
            }

        } else if (random.equals(t) && Integer.parseInt(amount.getText()) > 0) {

            //sprawdzam by zajęte było max 1/100 przestrzeni

            builder.buildRandom(Integer.parseInt(amount.getText()));
            // setBC(b);
            draw(gc);
        } else if (clicking.equals(t)) {

            matrix.addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {

                    int x = (int) event.getX();
                    int y = (int) event.getY();

                    builder.setCAState(x / (int) squareSize.getValue(), y / (int) squareSize.getValue());

                    draw(gc);
                }
            });
            // setBC(b);

        }

    }

    public void setBC(Object b) {
        if (b == periodic) {
            builder.setPeriodic();
        } else if (b == absorbic) {
            builder.setAbsorbic();
        }
    }

    public void simulation(Neighbourhood2D n) {


        builder.simulation(n, (BoundaryCondition) BC.getValue());
        draw(gc);


    }

    public void begin(ActionEvent actionEvent) {

        if (limitSpace()) {

//            monteCarloAnimation.stop();
//            monteCarlo.setDisable(true);
//            stopMC.setDisable(true);
            time = 0;
            c = 0;
            MCcounter.setText(String.valueOf(c));
            height = Integer.parseInt(h.getText()) * (int) squareSize.getValue();
            width = Integer.parseInt(w.getText()) * (int) squareSize.getValue();
            matrix.setHeight(height);
            matrix.setWidth((width));
            automat = new CelluralAutomata2D((int) matrix.getWidth() / (int) squareSize.getValue(), (int) matrix.getHeight() / (int) squareSize.getValue());
            builder = new CABuilder(automat);


            Pane.setPrefWidth(gridPane.getPrefWidth());
            Pane.setPrefHeight(gridPane.getPrefHeight());


            build(type.getValue(), BC.getValue());

        }
    }


    public void draw(GraphicsContext g) {

        g.setFill(Color.WHITE);
        g.fillRect(0, 0, matrix.getWidth(), matrix.getHeight());


        for (int y = 0; y < automat.getHeight(); y++) {
            for (int x = 0; x < automat.getWidth(); x++) {
                if (builder.getState(y, x) != 0) {

                    g.setFill(builder.getColor(y, x));
                    g.fillRect(x * (int) squareSize.getValue(), y * (int) squareSize.getValue(), (int) squareSize.getValue(), (int) squareSize.getValue());


                }
            }
        }


//        //creating grid on board
//        // vertical lines
//        int size = (int) squareSize.getValue();
//        g.setStroke(Color.GRAY);
//        for(int i = 0 ; i <= matrix.getWidth() ; i+=size){
//            g.strokeLine(i, 0, i, matrix.getHeight() );
//        }
//
//        // horizontal lines
//        g.setStroke(Color.GRAY);
//        for(int i = 0 ; i <= matrix.getHeight() ; i+=size){
//            g.strokeLine(0, i, matrix.getWidth(), i);
//        }


    }


    public boolean limitSpace() {
        if (Integer.parseInt(w.getText()) >= 10 && Integer.parseInt(h.getText()) >= 10
                && Integer.parseInt(w.getText()) <= 1000 && Integer.parseInt(h.getText()) <= 1000) return true;
        else return false;
    }


    public void StartGrowth(ActionEvent actionEvent) {

        animationTimer.stop();
        simulation((Neighbourhood2D) neighbourhood.getValue());

    }

    public void Growth(ActionEvent actionEvent) {

        animationTimer.start();
    }


}


