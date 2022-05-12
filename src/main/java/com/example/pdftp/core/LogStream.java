package com.example.pdftp.core;

import javafx.scene.control.TextArea;
import java.io.IOException;
import java.io.OutputStream;

public class LogStream extends OutputStream {

    private TextArea textArea;

    public LogStream(TextArea textArea){
        this.textArea = textArea;
    }

    @Override
    public void write(int b) throws IOException {
        textArea.appendText(String.valueOf((char)b));
        textArea.positionCaret(textArea.getLength());
    }

}