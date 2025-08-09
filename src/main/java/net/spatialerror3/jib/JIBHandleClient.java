/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package net.spatialerror3.jib;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;

/**
 *
 * @author spatialerror3
 */
public class JIBHandleClient implements Runnable {

    InputStream IS = null;
    InputStreamReader ISR = null;
    BufferedReader BR = null;
    OutputStream OS = null;
    OutputStreamWriter OSW = null;
    BufferedWriter BW = null;
    JIBSocket sock = null;

    public JIBHandleClient(Socket cs) {
        try {
            IS = cs.getInputStream();
        } catch (IOException ex) {
            System.getLogger(JIBHandleClient.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        ISR = new InputStreamReader(IS);
        BR = new BufferedReader(ISR);
        try {
            OS = cs.getOutputStream();
        } catch (IOException ex) {
            System.getLogger(JIBHandleClient.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        OSW = new OutputStreamWriter(OS);
        BW = new BufferedWriter(OSW);
        sock=new JIBSocket(cs);
        onConnect();
    }

    public void onConnect() {
        sendLine("");
        JavaIrcBouncer.jibIRC=new JIBIRC("192.168.179.3",6697,"test7667","test1","testing...");
    }

    public void sendLine(String l) {
        try {
            BW.write(l + "\r\n");
            BW.flush();
            OSW.flush();
            OS.flush();
        } catch (IOException ex) {
            System.getLogger(JIBHandleClient.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }

    public void processLine(String l) {
        System.err.println("l=" + l);
        if (l.startsWith("CAP")) {
            sendLine(l);
        }
        if (l.startsWith("NICK")) {
            sendLine(l);
        }
        if (l.startsWith("USER")) {
            sendLine(l);
        }
        if (l.startsWith("PASS")) {
            sendLine(l);
        }
        if (l.startsWith("QUIT")) {

        }
        JavaIrcBouncer.jibIRC.writeLine(l+"\r\n");
    }

    public void run() {
        String l = null;
        while (true) {
            try {
                l = BR.readLine();
            } catch (IOException ex) {
                System.getLogger(JIBHandleClient.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            }
            processLine(l);
        }
    }
}
