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
import java.net.SocketException;

/**
 *
 * @author spatialerror3
 */
public class JIBSocket {

    private static boolean SOCKETDEBUGGING = false;
    //
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
        JIBSocket.SOCKETDEBUGGING = (JavaIrcBouncer.jibConfig.getValue("SOCKETDEBUGGING") != null ? true : false);
        this.s = s;
        if (this.s == null) {
            if (e == null) {
                e = new NullPointerException();
            }
            return;
        }
        try {
            IS = s.getInputStream();
        } catch (SocketException ex) {
            if (e == null) {
                e = ex;
            }
            System.getLogger(JIBSocket.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        } catch (IOException ex) {
            if (e == null) {
                e = ex;
            }
            System.getLogger(JIBHandleClient.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        if (IS != null) {
            ISR = new InputStreamReader(IS);
            BR = new BufferedReader(ISR);
        }
        try {
            OS = s.getOutputStream();
        } catch (SocketException ex) {
            if (e == null) {
                e = ex;
            }
            System.getLogger(JIBSocket.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        } catch (IOException ex) {
            if (e == null) {
                e = ex;
            }
            System.getLogger(JIBHandleClient.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        if (OS != null) {
            OSW = new OutputStreamWriter(OS);
            BW = new BufferedWriter(OSW);
        }
    }

    public JIBSocket writeLineNoEOL(String l) {
        return writeLine(JIBStringUtil.remEOL2(l) + "\r\n");
    }

    public JIBSocket writeLine(String l) {
        if (getError() != null) {
            return null;
        }
        try {
            BW.write(l);
            BW.flush();
            OSW.flush();
            OS.flush();
        } catch (SocketException ex) {
            if (e == null) {
                e = ex;
            }
            System.getLogger(JIBSocket.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            return null;
        } catch (IOException ex) {
            if (e == null) {
                e = ex;
            }
            System.getLogger(JIBSocket.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            return null;
        }
        if (JIBSocket.SOCKETDEBUGGING) {
            System.err.println(this.s + " writeLine()=" + JIBStringUtil.remEOL(l));
        }
        return this;
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
        } catch (SocketException ex) {
            if (e == null) {
                e = ex;
            }
            System.getLogger(JIBSocket.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        } catch (IOException ex) {
            if (e == null) {
                e = ex;
            }
            System.getLogger(JIBSocket.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        if (JIBSocket.SOCKETDEBUGGING) {
            System.err.println(this.s + " readLine()=" + JIBStringUtil.remEOL(l));
        }
        return l;
    }

    public Exception getError() {
        return e;
    }

    public boolean connected() {
        if (e != null) {
            return false;
        }
        return s.isConnected();
    }
}
