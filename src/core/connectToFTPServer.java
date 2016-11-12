/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

import java.io.BufferedInputStream;
import java.io.IOException;
import javazoom.jl.player.Player;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPListParseEngine;

/**
 *
 * @author Moshiur
 */
public class connectToFTPServer {

 
    public FTPFile[] getFiles() throws IOException {
        return listParseEnfine.getFiles();
    }

    public FTPClient getFtpClient() {
        return ftpClient;
    }
    
    
    FTPListParseEngine listParseEnfine;
    public Player mp3player = null;
    public BufferedInputStream in = null;
    public FTPClient ftpClient ;
    public connectToFTPServer(String ip, int port,String user,String password) throws IOException {
        
        ftpClient = new FTPClient();
        ftpClient.connect(ip,port);
        ftpClient.login(user, password);
        ftpClient.enterLocalPassiveMode();
        ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
        listParseEnfine = ftpClient.initiateListParsing();
        System.out.println("connected to FTP server");
    } 
}
