package com.example.pdftp.dataApi;
import com.example.pdftp.ui.FTPController;
import org.apache.commons.net.ftp.FTPClient;

import java.io.IOException;

public abstract class FTP {

    public String host;
    public int port;
    public String user;
    public String password;
    public String ftpUrl;

    public FTPClient ftp;

    public abstract String getUrl();

    public abstract void close() throws IOException;

    public abstract void connect(String host, String port, String user, String password, FTPController ui) throws IOException;
}