package com.example.pdftp.ui;

import com.example.pdftp.core.DirUI;
import com.example.pdftp.core.FileUI;
import com.example.pdftp.core.LogStream;
import com.example.pdftp.uiApi.FTPControllerApi;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.input.MouseEvent;
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

    TreeItem<FileUI> rootRemoteTree;
    TreeItem<DirUI> rootRemoteTreeDir;
    TreeTableColumn<FileUI, String> fileColumn;
    @FXML
    TreeTableView remoteTree;

    @FXML
    TreeTableView remoteTreeDir;

    FTPController uiInstance;

    LogStream log;

    private TreeItem itemInTree(TreeItem search){
        for(TreeItem item : rootRemoteTreeDir.getChildren()){
            DirUI itemDirUI = (DirUI) item.getValue();
            DirUI searchDirUI = (DirUI) search.getValue();
            if(itemDirUI.getPath().equals(searchDirUI.getPath())){ return item; }
        }
        return null;
    }

    public void addDirectoryToTree(FTPFile file){
        if(remoteTreeDir.getRoot() == null){ initRemoteTreeDir(); }
        addDirectoryToTree(file, "/");
    }

    /*TODO: Bug: Adds duplicate directories if not root, doesn't nest further
    TODO: than 2*/

    public void addDirectoryToTree(FTPFile file, String path){
        if(!rootRemoteTreeDir.isExpanded()){ rootRemoteTreeDir.setExpanded(true); }
        file.setName("/" + file.getName());
        addFileToTree(file);
        TreeItem<DirUI> item = new TreeItem<>(new DirUI(file.getName()));
        if(itemInTree(item) == null){
            TreeItem<DirUI> pathItem = itemInTree(new TreeItem<>(new DirUI(path)));
            if(pathItem != null) {
                pathItem.getChildren().add(item);
            } else {
                rootRemoteTreeDir.getChildren().add(item);
                item.setExpanded(true);
            }
        }
    }

    public void addFileToTree(FTPFile file){
        if(remoteTree.getRoot() == null){ initRemoteTree(); }
        addFileToTree(file, rootRemoteTree);
        if(!rootRemoteTree.isExpanded()) rootRemoteTree.setExpanded(true);
    }

    private void addFileToTree(FTPFile file, TreeItem atItem){
        atItem.getChildren().add(new TreeItem(new FileUI(file)));
    }

    public void clearTreeFiles(String newName){
        rootRemoteTree = new TreeItem(new FileUI(newName));
        remoteTree.setRoot(rootRemoteTree);
        rootRemoteTree.setExpanded(true);
    }

    public void setRootRemoteTree(String directory){

    }

    public void setFilesAscending(){ fileColumn.setSortType(TreeTableColumn.SortType.DESCENDING); }

    private void createColumn(String text, String propertyName, int size, TreeTableView tree){
        fileColumn = new TreeTableColumn<>(text);
        fileColumn.setPrefWidth(size);
        fileColumn.setCellValueFactory(new TreeItemPropertyValueFactory<>(propertyName));
        tree.getColumns().add(fileColumn);
    }

    private void initRemoteTree(){
        createColumn("Filename", "filename", 200, remoteTree);
        createColumn("File size", "filesize", 75, remoteTree);
        createColumn("File type", "filetype", 75, remoteTree);
        createColumn("Last modified", "lastModified", 115, remoteTree);
        createColumn("Permissions", "permissions", 100, remoteTree);
        createColumn("Owner/Group", "owner", 115, remoteTree);
        rootRemoteTree = new TreeItem(new FileUI(".."));
        remoteTree.setRoot(rootRemoteTree);

        remoteTree.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(event.getClickCount() == 2){
                    TreeItem<FileUI> item =
                            (TreeItem<FileUI>)
                                    remoteTree.getSelectionModel().getSelectedItem();
                    if (item.getValue().isDirectory()){
                        clearTreeFiles("..");
                        try {
                            FTP_CLIENT.listFiles(item.getValue().getFilename(), uiInstance);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        });
    }

    private void initRemoteTreeDir(){
        createColumn("Remote site:", "path", 200, remoteTreeDir);
        rootRemoteTreeDir = new TreeItem(new DirUI("/"));
        rootRemoteTreeDir.setExpanded(true);
        remoteTreeDir.setRoot(rootRemoteTreeDir);

        remoteTreeDir.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(event.getClickCount() == 2){
                    TreeItem<DirUI> item =
                            (TreeItem<DirUI>)
                                    remoteTreeDir.getSelectionModel().getSelectedItem();
                    clearTreeFiles("..");
                    try {
                        FTP_CLIENT.listFiles(item.getValue().getPath(), uiInstance);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
    }

    public void start(){
        uiInstance = this;
        remoteTree.setPlaceholder(new Label("Not connected to any server"));
        remoteTreeDir.setPlaceholder(new Label(""));
    }

    @FXML
    protected void onConnectButtonClick() {
        try {
            FTP_CLIENT.connect(host.getText(), port.getText(),
                    username.getText(), password.getText(), this, logger);
        } catch (IOException e){

        }
    }
}