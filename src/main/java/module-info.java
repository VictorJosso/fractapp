module fr.josso.fractales {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.swing;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires java.desktop;
    requires java.logging;
    requires commons.cli;
    requires progressbar;

    opens fr.josso.fractales to javafx.fxml;
    exports fr.josso.fractales;
    exports fr.josso.fractales.Graphics;
    opens fr.josso.fractales.Graphics to javafx.fxml, javafx.graphics;
}