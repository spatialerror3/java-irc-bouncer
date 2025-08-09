/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package net.spatialerror3.jib;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

/**
 *
 * @author spatialerror3
 */
public class JIBIRC implements Runnable {
    private SSLSocket ircServer = null;
    private JIBSocket sock = null;
    //
    private String nick = "";
    private String user = "";
    private String realname = "";
    
    public JIBIRC(String Server, int Port, String nick, String user, String realname) {
        this.nick=nick;
        this.user=user;
        this.realname=realname;
        SSLContext sslctx=null;
        try {
            sslctx = SSLContext.getInstance("TLS");
        } catch (NoSuchAlgorithmException ex) {
            System.getLogger(JIBIRC.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        TrustManager[] tm = new TrustManager[]{new JIBSSLTrustManager()};
        try {
            sslctx.init(null,tm,null);
        } catch (KeyManagementException ex) {
            System.getLogger(JIBIRC.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        SSLSocketFactory factory = (SSLSocketFactory) sslctx.getSocketFactory();
        try {
            ircServer = (SSLSocket) factory.createSocket(Server, Port);
        } catch (IOException ex) {
            System.getLogger(JIBIRC.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        sock = new JIBSocket(ircServer);
        onConnect();
        Thread t3 = new Thread(this);
        t3.start();
    }
    
    public void onConnect() {
        JIBPinger ping1 = new JIBPinger(sock);
        writeLine("CAP \r\n");
        writeLine("NICK "+nick+"\r\n");
        writeLine("USER "+user+" 0 * :"+realname+"\r\n");
        writeLine("CAP END\r\n");
        Thread t4 = new Thread(ping1);
        t4.start();
    }
    
    public void writeLine(String l) {
        sock.writeLine(l);
    }
    
    public void processLine(String l) {
        System.err.println(this+" l="+l);
        JavaIrcBouncer.jibServ.writeAllClients(l);
    }
    
    public void run() {
        while(true) {
            processLine(sock.readLine());
        }
    }
}
