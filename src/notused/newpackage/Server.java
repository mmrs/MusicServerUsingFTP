/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package notused.newpackage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;

/**
 *
 * @author siyam
 */
public class Server {

    Server(int port) throws IOException {
        try {
            initializeAudioLines();
            serverSocket = new ServerSocket(port);
            System.out.println("Server Running");
            startProcessing();
        } catch (IOException e) {
            System.out.println(e + "\nException in Server Constructor");
        }
    }

    void initializeAudioLines() {

        try {
            format = new AudioFormat(11025f, 8, 1, true, true);

            targetInfo = new DataLine.Info(TargetDataLine.class, format);
            sourceInfo = new DataLine.Info(SourceDataLine.class, format);

            targetLine = (TargetDataLine) AudioSystem.getLine(targetInfo);
            targetLine.open(format);
            targetLine.start();

            sourceLine = (SourceDataLine) AudioSystem.getLine(sourceInfo);
            sourceLine.open(format);
            sourceLine.start();

        } catch (LineUnavailableException e) {
            System.out.println(e + "\nException in initialization");
        }
    }

    void startProcessing() {
        try {
            server = serverSocket.accept();

            System.out.println("Server is connected to : " + server.getInetAddress());

            toClient = new ObjectOutputStream(server.getOutputStream());
            toClient.flush();
            fromClient = new ObjectInputStream(server.getInputStream());

            captureAndSend();
            receiveAndPlay();
        } catch (Exception e) {
            System.out.println(e + "\nException in Processing");
        }
    }

    void receiveAndPlay() {
        receive = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true) {
                        if (server.getInputStream().available() > 0) {
                            Data data = (Data) fromClient.readObject();
                            sourceLine.write(data.getData(), 0, dataLength);
                        } else {
                            try {
                                Thread.sleep(10);
                            } catch (InterruptedException e) {
                                System.out.println(e + "\nException in audioReceiving");
                            }
                        }
                    }
                } catch (Exception e) {
                    System.out.println(e + "\nException in audioReceiving");
                }
            }
        });
        receive.start();
    }

    void captureAndSend() {
        send = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true) {
                        if (targetLine.available() > dataLength) {
                            byte[] buff = new byte[dataLength];
                            while (targetLine.available()>= dataLength) {
                                targetLine.read(buff, 0, buff.length); //read from microphone
                            }
                            toClient.writeObject(new Data(buff));
                        } else {
                            try {
                                Thread.sleep(10);
                            } catch (InterruptedException e) {
                                System.out.println(e + "\nException in capturing");
                            }
                        }
                    }
                } catch (IOException e) {
                    System.out.println(e + "\nException in capturing");
                }
            }
        });
        send.start();
    }

    public static void main(String[] args) {
        try {
            int port = 1234;
            Server server = new Server(port);
        } catch (IOException e) {
            System.out.println(e + "\nException in main");
        }
    }

    ServerSocket serverSocket;
    Socket server;

    ObjectInputStream fromClient;
    ObjectOutputStream toClient;

    AudioFormat format;

    DataLine.Info targetInfo;
    DataLine.Info sourceInfo;

    TargetDataLine targetLine;
    SourceDataLine sourceLine;

    Thread receive, send;
    int dataLength = 1200;
}
