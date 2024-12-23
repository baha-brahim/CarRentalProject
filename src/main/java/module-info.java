module com.example.carrentalproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires de.jensd.fx.glyphs.fontawesome;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires java.sql;
    requires mysql.connector.j;
    requires java.desktop;

    opens com.example.carrentalproject.Controllers to javafx.fxml;
    opens com.example.carrentalproject to javafx.fxml;
    opens com.example.carrentalproject.Classes to javafx.base;

    exports com.example.carrentalproject;
    exports com.example.carrentalproject.Controllers;
}