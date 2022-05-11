package com.example.pdftp.data;

import com.example.pdftp.core.LogStream;
import com.example.pdftp.dataApi.FTP;
import com.example.pdftp.ui.FTPController;
import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import java.io.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

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
    public void connect(String host, String port, String user, String password, FTPController ui) throws IOException {
        this.host = host;
        this.port = Integer.parseInt(port);
        this.user = user;
        this.password = password;

        ftp = new FTPClient();

        ftp.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(new LogStream(ui))));
        ftp.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));
        ftp.connect(this.host, this.port);
        int reply = ftp.getReplyCode();
        if (!FTPReply.isPositiveCompletion(reply)) {
            ftp.disconnect();
            throw new IOException("Exception in connecting to FTP Server");
        }
        ui.put("Attempting to log in with user credentials\n");
        ftp.login(user, password);
        ui.put("Successfully logged in!\n");

        listFiles("/", ui);
    }

    public void listFiles(String path, FTPController ui) throws IOException {
        FTPFile[] files = ftp.listFiles(path);
        ui.setRoot(path);
        for (FTPFile file : files) {
            // Loop over each file
            ui.addFileToTree(file);
        }
        ui.setFilesAscending();
    }
}
