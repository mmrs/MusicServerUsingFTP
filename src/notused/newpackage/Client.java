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
import java.util.Scanner;
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
public class Client {
    
    Client(String serverIP, int port) throws IOException {
        try {
            initializeAudioLines();
            client = new Socket(serverIP, port);
            System.out.println("Client is connected to : " + client.getInetAddress());
            processProcessing();
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
            System.out.println(e + "\nException in initAudio");
        }
    }

    void processProcessing() {
        try {
            fromClient = new ObjectInputStream(client.getInputStream());
            toClient = new ObjectOutputStream(client.getOutputStream());
            toClient.flush();

            captureAndSend();
            receiveAndPlay();
        } catch (IOException e) {
            System.out.println(e + "\nException in processServer()");
        }
    }

    void receiveAndPlay() {
        receive = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true) {
                        if (client.getInputStream().available() > 0) {
                            Data data = (Data) fromClient.readObject();
                            sourceLine.write(data.getData(), 0, dataLength);
                        } else {
                            try {
                                Thread.sleep(10);
                            } catch (InterruptedException e) {
                                System.out.println(e + "\nException in audioReceiveAndPlay()");
                            }
                        }
                    }
                } catch (Exception e) {
                    System.out.println(e + "\nException in audioReceiveAndPlay()");
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
//                        System.out.println("Currently available to mic : " + targetLine.available());
                        if (targetLine.available() > dataLength) {
                            byte[] buff = new byte[dataLength];
                            while (targetLine.available() >= dataLength) { //flush old data from mic to reduce lag, and read most recent data
                                targetLine.read(buff, 0, buff.length); //read from microphone
                            }
                            toClient.writeObject(new Data(buff));
                        } else {
                            try {
                                Thread.sleep(10);
                            } catch (InterruptedException e) {
                                System.out.println(e + "\nException in audioReceiveAndPlay()");
                            }
                        }
                    }
                } catch (IOException e) {
                    System.out.println(e + "\nException in audioReceiveAndPlay()");
                }
            }
        });
        send.start();
    }

    public static void main(String[] args) {
        try {
            Scanner inScanner = new Scanner(System.in);
            System.out.println("print sever ip : ");
            Client client = new Client(inScanner.nextLine(), 1234);
        } catch (IOException e) {
            System.out.println(e + "\nException in main method()");
        }
    }
    
    
      Socket client;

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
