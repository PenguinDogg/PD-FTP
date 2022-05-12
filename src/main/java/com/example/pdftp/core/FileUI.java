package com.example.pdftp.core;

import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.io.FilenameUtils;

import java.util.Calendar;
import java.util.Formatter;

public class FileUI {

    private String filename;
    private String filesize;
    private String filetype;
    private String lastModified;
    private String permissions;
    private String owner;

    public boolean isDirectory() {
        return isDirectory;
    }

    public void setDirectory(boolean directory) {
        isDirectory = directory;
    }

    private boolean isDirectory;

    public String getFilesize() {
        return String.valueOf(filesize);
    }

    public void setFilesize(String filesize) {
        this.filesize = filesize;
    }

    public String getFiletype() {
        return filetype;
    }

    public void setFiletype(String filetype) {
        this.filetype = filetype;
    }

    public String getLastModified() {
        return lastModified;
    }

    public void setLastModified(String lastModified) {
        this.lastModified = lastModified;
    }

    public String getPermissions() {
        return permissions;
    }

    public void setPermissions(String permissions) {
        this.permissions = permissions;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    private String formatCalender(Calendar timestamp){
        StringBuilder sb = new StringBuilder();
        Formatter fmt = new Formatter(sb);
        if (timestamp != null) {
            fmt.format(" %1$tY-%1$tm-%1$td", timestamp);
            // Only display time units if they are present
            if (timestamp.isSet(Calendar.HOUR_OF_DAY)) {
                fmt.format(" %1$tH", timestamp);
                if (timestamp.isSet(Calendar.MINUTE)) {
                    fmt.format(":%1$tM", timestamp);
                    if (timestamp.isSet(Calendar.SECOND)) {
                        fmt.format(":%1$tS", timestamp);
                        if (timestamp.isSet(Calendar.MILLISECOND)) {
                            fmt.format(".%1$tL", timestamp);
                        }
                    }
                }
                fmt.format(" %1$tZ", timestamp);
            }
        }
        return sb.toString();
    }

    private String permissionToString(int access, FTPFile file) {
        final StringBuilder sb = new StringBuilder();
        if (file.hasPermission(access, 0)) {
            sb.append('r');
        } else {
            sb.append('-');
        }
        if (file.hasPermission(access, 1)) {
            sb.append('w');
        } else {
            sb.append('-');
        }
        if (file.hasPermission(access, 2)) {
            sb.append('x');
        } else {
            sb.append('-');
        }
        return sb.toString();
    }

    private String formatPermissions(FTPFile file){
        StringBuilder sb = new StringBuilder();
        final Formatter fmt = new Formatter(sb);
        sb.append(permissionToString(0, file));
        sb.append(permissionToString(1, file));
        sb.append(permissionToString(2, file));

        return sb.toString();
    }

    private String formatOwner(FTPFile file){
        StringBuilder sb = new StringBuilder();
        final Formatter fmt = new Formatter(sb);
        fmt.format(" %-8s %-8s", file.getUser(), file.getGroup());

        return sb.toString();
    }

    private String formatFileSize(FTPFile file){
        StringBuilder sb = new StringBuilder();
        final Formatter fmt = new Formatter(sb);
        fmt.format(" %8d", Long.valueOf(file.getSize()));

        return sb.toString();
    }

    public FileUI(FTPFile file){
        this.filename = file.getName();
        this.filesize = formatFileSize(file) + " B";
        this.filetype = FilenameUtils.getExtension(this.filename);
        this.lastModified = formatCalender(file.getTimestamp());
        this.permissions = formatPermissions(file);
        this.owner = formatOwner(file);
        this.isDirectory = file.isDirectory();
    }

    public FileUI(String directory){
        this.filename = directory;
        this.filetype = "";
        this.filesize = "";
        this.lastModified = "";
        this.permissions = "";
        this.owner = "";
        this.isDirectory = true;
    }


}
