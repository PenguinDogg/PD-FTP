module com.example.pdftp {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.kordamp.bootstrapfx.core;
    requires org.apache.commons.net;
    opens com.example.pdftp to javafx.fxml;
    exports com.example.pdftp;
}