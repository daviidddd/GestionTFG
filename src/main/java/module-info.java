module com.david.gestiontfg {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires java.sql;
    requires org.apache.poi.poi;
    requires org.apache.poi.ooxml;
    requires icepdf.viewer;
    requires org.apache.pdfbox;
    requires com.google.gson;

    exports com.david.gestiontfg;
    opens com.david.gestiontfg to javafx.fxml;
}