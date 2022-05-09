package com.example.pdftp.core;


import com.example.pdftp.ui.FTPController;

import java.io.IOException;
import java.io.OutputStream;

public class LogStream extends OutputStream {

    public static FTPController FTP_CONTROLLER;
    @Override
    public void write(int b) throws IOException {
        FTP_CONTROLLER.put(String.valueOf((char) b));
    }
}