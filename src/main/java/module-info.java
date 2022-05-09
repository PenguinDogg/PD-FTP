module com.example.pdftp {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.kordamp.bootstrapfx.core;
    requires org.apache.commons.net;
    opens com.example.pdftp to javafx.fxml;
    exports com.example.pdftp;
    exports com.example.pdftp.ui;
    opens com.example.pdftp.ui to javafx.fxml;
}