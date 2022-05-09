package com.example.pdftp;

import com.example.pdftp.uiApi.FTPControllerApi;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;

public class FTPController implements FTPControllerApi {

    @FXML
    private Label debugText;

    @FXML
    TextField username;
    @FXML
    PasswordField password;
    @FXML TextField host;
    @FXML TextField port;

    @FXML
    protected void onConnectButtonClick() {

        try {
            FTP_CLIENT.connect(host.getText(), port.getText(),
                    username.getText(), password.getText());
            debugText.setText("Connection successful!");
        } catch (IOException e){
            debugText.setText("Error: " + e.toString());
        }


    }

}