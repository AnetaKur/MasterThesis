module com.example.masterthesis {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;

    opens com.example.masterthesis to javafx.fxml;
    exports com.example.masterthesis;
    exports com.example.masterthesis.controller;
    opens com.example.masterthesis.controller to javafx.fxml;
}