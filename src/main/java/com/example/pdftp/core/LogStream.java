package com.example.pdftp.core;


import com.example.pdftp.ui.FTPController;

import java.io.IOException;
import java.io.OutputStream;

public class LogStream extends OutputStream {

    public FTPController ftp;

    public LogStream(FTPController ftp){
        this.ftp = ftp;
    }

    @Override
    public void write(int b) throws IOException {
        ftp.put((char) b);
    }
}