package com.example.pdftp.data;

import com.example.pdftp.core.LogStream;
import com.example.pdftp.dataApi.FTP;
import com.example.pdftp.ui.FTPController;
import javafx.scene.control.TextArea;
import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import java.io.*;
import java.util.*;

public class FTPConnection extends FTP {

    @Override
    public String getUrl() {
        return "ftp://" + host;
    }

    @Override
    public void close() throws IOException {
        ftp.disconnect();
    }

    @Override
    public void connect(String host, String port, String user, String password, FTPController ui, TextArea log) throws IOException {
        this.host = host;
        this.port = Integer.parseInt(port);
        this.user = user;
        this.password = password;

        ftp = new FTPClient();
        ftp.addProtocolCommandListener(new PrintCommandListener(new PrintStream(new LogStream(log))));
        ftp.connect(this.host, this.port);
        int reply = ftp.getReplyCode();
        if (!FTPReply.isPositiveCompletion(reply)) {
            ftp.disconnect();
            throw new IOException("Exception in connecting to FTP Server");
        }
        ftp.login(user, password);

        listFiles("/", ui);
    }

    public void listFiles(String path, FTPController ui) throws IOException {
        FTPFile[] files = ftp.listFiles(path);
        Queue<FTPFile> directories = new LinkedList<FTPFile>();
        ui.setRootRemoteTree(path);
        for (FTPFile file : files) {
            if(file.isDirectory()){ directories.add(file); }
            else { ui.addFileToTree(file); }
        }

        for(FTPFile file : directories){
            if(path != "/"){ ui.addDirectoryToTree(file, path); }
            else { ui.addDirectoryToTree(file); }
        }

        ui.setFilesAscending();
    }
}
