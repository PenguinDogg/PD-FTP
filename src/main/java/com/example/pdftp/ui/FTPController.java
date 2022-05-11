package com.example.pdftp.ui;

import com.example.pdftp.core.FileUI;
import com.example.pdftp.uiApi.FTPControllerApi;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import org.apache.commons.net.ftp.FTPFile;

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

    TreeItem root;
    TreeTableColumn<FileUI, String> fileColumn;
    @FXML
    TreeTableView remoteTree;
    @Override
    public void put(String text) {
        logger.appendText(text);
    }

    public void put(char c){
        logger.appendText(String.valueOf(c));
    }

    public void addFileToTree(FTPFile file){
        root.getChildren().add(new TreeItem(new FileUI(file)));
        if(!root.isExpanded()) root.setExpanded(true);
    }

    public void setRoot(String directory){

    }

    public void setFilesAscending(){ fileColumn.setSortType(TreeTableColumn.SortType.DESCENDING); }

    private void createColumn(String text, String propertyName, int size){
        fileColumn = new TreeTableColumn<>(text);
        fileColumn.setPrefWidth(size);
        fileColumn.setCellValueFactory(new TreeItemPropertyValueFactory<>(propertyName));
        remoteTree.getColumns().add(fileColumn);
    }

    public void start(){
        createColumn("Filename", "filename", 200);
        createColumn("File size", "filesize", 75);
        createColumn("File type", "filetype", 75);
        createColumn("Last modified", "lastModified", 115);
        createColumn("Permissions", "permissions", 100);
        createColumn("Owner/Group", "owner", 115);
        root = root = new TreeItem(new FileUI(".."));
        remoteTree.setRoot(root);
    }

    @FXML
    protected void onConnectButtonClick() {
        put("Attempting connection with host, please wait...\n");
        try {
            FTP_CLIENT.connect(host.getText(), port.getText(),
                    username.getText(), password.getText(), this);
        } catch (IOException e){
            put("Error: " + e.toString());
        }
    }

}