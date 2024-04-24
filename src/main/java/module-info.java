module com.david.gestiontfg {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires java.sql;
    requires org.apache.poi.poi;
    requires org.apache.poi.ooxml;
    requires icepdf.viewer;
    requires org.apache.pdfbox;
    requires com.google.gson;
    requires java.desktop;
    requires org.kohsuke.github.api;

    exports com.david.gestiontfg;
    opens com.david.gestiontfg to javafx.fxml, javafx.controls;
}