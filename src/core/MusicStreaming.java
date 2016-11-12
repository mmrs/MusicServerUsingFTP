/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

import com.sun.org.apache.bcel.internal.generic.AASTORE;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;
import jdk.nashorn.internal.parser.Lexer;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPListParseEngine;

/**
 *
 * @author Moshiur
 */
public class MusicStreaming {

    String server = "192.168.0.101";
    int port = 21;
    String user = "mmrs";
    String pass = "mmrs";
    FTPFile[] files;
    FTPListParseEngine engine;
    public Player mp3player = null;
    public BufferedInputStream in = null;
    public FTPClient ftpClient ;
    String song = null;
    Boolean isFirst = true;
    public void play() throws JavaLayerException, IOException {
        
        ftpClient = new FTPClient();
        ftpClient.connect(server,port);
        ftpClient.login(user, pass);
        ftpClient.enterLocalPassiveMode();
        ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
        System.out.println(ftpClient.getListHiddenFiles());
        engine = ftpClient.initiateListParsing();
        
        files = engine.getFiles();
       
         for(int i=0;i<files.length;i++){
             System.out.println(i +"  "+files[i].getName());
         }
        
         Scanner scanner = new Scanner(System.in);
         while(scanner.hasNext()){
            
             int i = scanner.nextInt();
             song = "/" + files[i].getName();
             System.out.println("curently playing " + i);
//        song = "/Bondhu Tomar Chokher Majhe.mp3";
        
            if(!isFirst){
                System.out.println("trying to stop");
                mp3player.close();
                mp3player.getPosition();
                in.close();
            }
            
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        isFirst = false;
                        in = new BufferedInputStream(ftpClient.retrieveFileStream(song));
                        mp3player = new Player(in);
                        mp3player.play();
                    } catch (Exception ex) {
                        Logger.getLogger(MusicStreaming.class.getName()).log(Level.SEVERE, null, ex);
                    }
   
                }
            }).start();
        
    }

    }

    public static void main(String[] args) throws JavaLayerException, IOException {
        
        new MusicStreaming().play();
      //  new JFrame().setVisible(true);
    }

}
