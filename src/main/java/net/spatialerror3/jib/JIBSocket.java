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
public class JIBSocket {

    Socket s = null;
    InputStream IS = null;
    InputStreamReader ISR = null;
    BufferedReader BR = null;
    OutputStream OS = null;
    OutputStreamWriter OSW = null;
    BufferedWriter BW = null;
    //
    Exception e = null;

    public JIBSocket(Socket s) {
        this.s = s;
        try {
            IS = s.getInputStream();
        } catch (IOException ex) {
            if (e == null) {
                e = ex;
            }
            System.getLogger(JIBHandleClient.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        ISR = new InputStreamReader(IS);
        BR = new BufferedReader(ISR);
        try {
            OS = s.getOutputStream();
        } catch (IOException ex) {
            if (e == null) {
                e = ex;
            }
            System.getLogger(JIBHandleClient.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        OSW = new OutputStreamWriter(OS);
        BW = new BufferedWriter(OSW);
    }

    public void writeLine(String l) {
        try {
            BW.write(l);
            BW.flush();
            OSW.flush();
            OS.flush();
        } catch (IOException ex) {
            if (e == null) {
                e = ex;
            }
            System.getLogger(JIBSocket.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        System.err.println(this.s + " writeLine()=" + l);
    }

    public String readLine() {
        String l = null;
        try {
            l = BR.readLine();
            if (l == null) {
                if (e == null) {
                    e = new NullPointerException();
                }
            }
        } catch (IOException ex) {
            if (e == null) {
                e = ex;
            }
            System.getLogger(JIBSocket.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        System.err.println(this.s + " readLine()=" + l);
        return l;
    }

    public Exception getError() {
        return e;
    }

    public boolean connected() {
        return s.isConnected();
    }
}
