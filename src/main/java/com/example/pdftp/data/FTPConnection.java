package com.example.pdftp.data;

import com.example.pdftp.core.LogStream;
import com.example.pdftp.dataApi.FTP;
import com.example.pdftp.ui.FTPController;
import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.*;

public class FTPConnection extends FTP {

    @Override
    public String getUrl(){
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
        LogStream.FTP_CONTROLLER = ui;
        ftp.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(new LogStream())));

        ftp.connect(this.host, this.port);
        int reply = ftp.getReplyCode();
        if(!FTPReply.isPositiveCompletion(reply)){
            ftp.disconnect();
            throw new IOException("Exception in connecting to FTP Server");
        }

        ftp.login(user, password);
    }
}