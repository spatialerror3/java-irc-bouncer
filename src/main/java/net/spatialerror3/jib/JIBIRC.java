/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package net.spatialerror3.jib;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
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
    private Socket ircServerNoSsl = null;
    private JIBSocket sock = null;
    //
    private String Server = null;
    private int Port = -1;
    //
    private String nick = "";
    private String user = "";
    private String realname = "";
    //
    private JIBUserInfo myInfo = null;
    private JIBPinger ping1 = null;
    //
    private boolean ircv32 = false;
    private boolean noSsl = false;

    public JIBIRC(String Server, int Port, String nick, String user, String realname) {
        this.Server = Server;
        this.Port = Port;
        //
        this.nick = nick;
        this.user = user;
        this.realname = realname;
        //
        myInfo = new JIBUserInfo();
        myInfo.nick = this.nick;
        myInfo.user = this.user;
        myInfo.host = "localhost";
        myInfo.realname = this.realname;
        connect();
    }

    private void connect() {
        SSLContext sslctx = null;
        JavaIrcBouncer.jibServ.writeAllClients(":JIB.jib NOTICE " + myInfo.nick + " :Connecting to " + this.Server + " :" + this.Port + "\r\n");
        JavaIrcBouncer.jibServ.writeAllClients(":JIB.jib NOTICE " + myInfo.nick + " :USER= " + myInfo.user + "\r\n");
        JavaIrcBouncer.jibServ.writeAllClients(":JIB.jib NOTICE " + myInfo.nick + " :REALNAME= " + myInfo.realname + "\r\n");
        JavaIrcBouncer.jibServ.writeAllClients(":JIB.jib NOTICE " + myInfo.nick + " :NICK= " + myInfo.nick + "\r\n");
        InetAddress clientBind = null;
        if (JavaIrcBouncer.jibConfig.getValue("ClientNoSSL") != null) {
            noSsl=true;
        }
        try {
            if (JavaIrcBouncer.jibConfig.getValue("ClientBind") != null) {
                clientBind = InetAddress.getByName(JavaIrcBouncer.jibConfig.getValue("ClientBind"));
            }
        } catch (UnknownHostException ex) {
            System.getLogger(JIBIRC.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        InetAddress dst = null;
        try {
            dst = InetAddress.getByName(this.Server);
        } catch (UnknownHostException ex) {
            System.getLogger(JIBIRC.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        if (!noSsl) {
            try {
                sslctx = SSLContext.getInstance("TLS");
            } catch (NoSuchAlgorithmException ex) {
                System.getLogger(JIBIRC.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            }
            TrustManager[] tm = new TrustManager[]{new JIBSSLTrustManager()};
            try {
                sslctx.init(null, tm, null);
            } catch (KeyManagementException ex) {
                System.getLogger(JIBIRC.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            }
            SSLSocketFactory factory = (SSLSocketFactory) sslctx.getSocketFactory();
            try {
                ircServer = (SSLSocket) factory.createSocket(dst, Port, clientBind, 0);
            } catch (IOException ex) {
                System.getLogger(JIBIRC.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            }
            sock = new JIBSocket(ircServer);
        } else {
            try {
                ircServerNoSsl = new Socket(dst, Port, clientBind, 0);
            } catch (IOException ex) {
                System.getLogger(JIBIRC.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            }
            sock = new JIBSocket(ircServerNoSsl);
        }
        onConnect();
        Thread t3 = new Thread(this);
        t3.start();
    }

    public void onConnect() {
        ping1 = new JIBPinger(sock);
        ping1.setPingStr();
        writeLine("CAP \r\n");
        writeLine("NICK " + nick + "\r\n");
        writeLine("USER " + user + " 0 * :" + realname + "\r\n");
        writeLine("CAP END\r\n");
        Thread t4 = new Thread(ping1);
        t4.start();
        ping1.doPing();
    }

    public void writeLine(String l) {
        sock.writeLine(l);
    }

    public void processLine(String l) {
        System.err.println(this + " l=" + l);
        JavaIrcBouncer.jibServ.writeAllClients(l);
    }

    public void simulateJoin(String chan) {
        String joinSim = ":" + myInfo.nuh() + " JOIN " + chan + " * :" + realname;
        String joinSim2 = ":" + myInfo.nuh() + " JOIN :" + chan;
        if (ircv32 == true) {
            JavaIrcBouncer.jibServ.writeAllClients(joinSim + "\r\n");
        } else {
            JavaIrcBouncer.jibServ.writeAllClients(joinSim2 + "\r\n");
        }
    }

    public void simulateJoin(String chan, String nick) {
        String joinSim = ":" + nick + " JOIN " + chan + " * :" + realname;
        String joinSim2 = ":" + nick + " JOIN :" + chan;
        if (ircv32 == true) {
            JavaIrcBouncer.jibServ.writeAllClients(joinSim + "\r\n");
        } else {
            JavaIrcBouncer.jibServ.writeAllClients(joinSim2 + "\r\n");
            JavaIrcBouncer.jibServ.writeAllClients(":JIB.jib MODE " + chan + " +nt\r\n");
            JavaIrcBouncer.jibServ.writeAllClients(":JIB.jib 366 " + nick + " " + chan + " :End of /NAMES list.\r\n");
            JavaIrcBouncer.jibServ.writeAllClients(":JIB.jib 324 " + nick + " " + chan + " +nt\r\n");
            JavaIrcBouncer.jibServ.writeAllClients(":JIB.jib 329 " + nick + " " + chan + " 0\r\n");
            JavaIrcBouncer.jibServ.writeAllClients(":JIB.jib 315 " + nick + " " + chan + " :End of /WHO list.\r\n");
        }
    }

    public void simulatePRIVMSG(String chan, String msg) {
        String msgSim = ":" + myInfo.nuh() + " PRIVMSG " + chan + " :" + msg;
        JavaIrcBouncer.jibServ.writeAllClients(msgSim + "\r\n");
    }

    public Exception getError() {
        return sock.getError();
    }

    public void run() {
        String l = null;
        while (getError() == null) {
            l = sock.readLine();
            ping1.processLine(l);
            processLine(l);
        }
    }
}
