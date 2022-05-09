package com.example.pdftp.ui;

import com.example.pdftp.uiApi.FTPControllerApi;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.IOException;

public class FTPController implements FTPControllerApi {
    @FXML
    TextField username;
    @FXML
    PasswordField password;
    @FXML TextField host;
    @FXML TextField port;

    @FXML
    TextArea logger;

    @Override
    public void put(String text) {
        logger.setText(logger.getText() + text);
    }

    @FXML
    protected void onConnectButtonClick() {
        put("Test");
        try {
            FTP_CLIENT.connect(host.getText(), port.getText(),
                    username.getText(), password.getText(), this);
        } catch (IOException e){
            put("Error: " + e.toString());
        }
    }

}