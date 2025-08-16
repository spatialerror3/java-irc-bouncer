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
    //
    private boolean preLogon = true;
    private JIBIRCNickServ ns = null;
    //
    private int errorCounter = 0;
    //
    private JIBIRCLog log = null;
    //
    private boolean connected = false;
    private boolean connecting = false;
    //
    private JIBUser u = null;
    private JIBIRCServer serv = null;

    public JIBIRC(JIBUser u, String Server, int Port, String nick, String user, String realname) {
        this.u = u;
        //
        this.Server = Server;
        this.Port = Port;
        serv = new JIBIRCServer();
        serv.setServer(Server);
        serv.setPort(Port);
        serv.setSsl(!noSsl);
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
        ns = new JIBIRCNickServ();
        ns.init();
        log = new JIBIRCLog();
        connect();
    }

    private void connect() {
        SSLContext sslctx = null;
        if (connected == true) {
            return;
        }
        if (errorCounter >= 15) {
            return;
        }
        connecting = true;
        preLogon = true;
        u.writeAllClients(":JIB.jib NOTICE " + myInfo.nick + " :Connecting to " + this.Server + " :" + this.Port);
        u.writeAllClients(":JIB.jib NOTICE " + myInfo.nick + " :USER= " + myInfo.user);
        u.writeAllClients(":JIB.jib NOTICE " + myInfo.nick + " :REALNAME= " + myInfo.realname);
        u.writeAllClients(":JIB.jib NOTICE " + myInfo.nick + " :NICK= " + myInfo.nick);
        InetAddress clientBind = null;
        if (JavaIrcBouncer.jibConfig.getValue("ClientNoSSL") != null) {
            noSsl = true;
        }
        try {
            if (JavaIrcBouncer.jibConfig.getValue("ClientBind") != null) {
                clientBind = InetAddress.getByName(JavaIrcBouncer.jibConfig.getValue("ClientBind"));
            }
        } catch (UnknownHostException ex) {
            System.getLogger(JIBIRC.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            connecting = false;
        }
        InetAddress dst = null;
        try {
            dst = InetAddress.getByName(this.Server);
        } catch (UnknownHostException ex) {
            System.getLogger(JIBIRC.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            connecting = false;
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
        if (sock != null && sock.getError() == null) {
            connected = true;
        }
        onConnect();
        Thread t3 = new Thread(this);
        t3.start();
        connecting = false;
    }

    public void reconnect() {
        System.err.println("reconnect() called...");
        if (sock.connected() == false || connected == false) {
            errorCounter = 0;
            connecting = false;
            connected = false;
            preLogon = true;
            connect();
        }
    }

    public void onConnect() {
        ping1 = new JIBPinger(sock);
        ping1.setPingStr();
        writeLine("CAP LS 302\r\n");
        writeLine("NICK " + nick + "\r\n");
        writeLine("USER " + user + " 0 * :" + realname + "\r\n");
        writeLine("CAP END\r\n");
        Thread t4 = new Thread(ping1);
        t4.start();
        ping1.doPing();
    }

    public void onLogon() {
        this.ns.identify();
        String[] chans = JavaIrcBouncer.jibDbUtil.getChannels();
        for (int i = 0; i < chans.length; i++) {
            if (chans[i] != null) {
                writeLine("JOIN " + chans[i] + "\r\n");
            }
        }
        preLogon = false;
    }

    public void writeLine(String l) {
        sock.writeLine(l);
    }

    public void processLine(String l) {
        System.err.println(this + " l=" + l);
        if (preLogon) {
            String[] sp5 = l.split(" ", 3);
            if (sp5.length > 1 && sp5[1].equals("005")) {
                onLogon();
            }
        }
        if (l == null || l.startsWith("ERROR")) {
            errorCounter++;
        }
        if (log != null) {
            log.processLine(l);
        }
        u.writeAllClients(l);
    }

    public void simulateNick(String oldnick, String newnick) {
        if (newnick == null) {
            newnick = myInfo.nick;
        }
        String nickSim = ":" + oldnick + " NICK " + newnick;
        u.writeAllClients(nickSim + "\r\n");
    }

    public void simulateJoin(String chan) {
        String joinSim = ":" + myInfo.nuh() + " JOIN " + chan + " * :" + realname;
        String joinSim2 = ":" + myInfo.nuh() + " JOIN :" + chan;
        if (ircv32 == true) {
            u.writeAllClients(joinSim + "\r\n");
        } else {
            u.writeAllClients(joinSim2 + "\r\n");
        }
    }

    public void simulateJoin(String chan, String nick) {
        String joinSim = ":" + nick + " JOIN " + chan + " * :" + realname;
        String joinSim2 = ":" + nick + " JOIN :" + chan;
        if (ircv32 == true) {
            u.writeAllClients(joinSim + "\r\n");
        } else {
            u.writeAllClients(joinSim2 + "\r\n");
            u.writeAllClients(":JIB.jib MODE " + chan + " +nt\r\n");
            u.writeAllClients(":JIB.jib 366 " + nick + " " + chan + " :End of /NAMES list.\r\n");
            u.writeAllClients(":JIB.jib 324 " + nick + " " + chan + " +nt\r\n");
            u.writeAllClients(":JIB.jib 329 " + nick + " " + chan + " 0\r\n");
            u.writeAllClients(":JIB.jib 315 " + nick + " " + chan + " :End of /WHO list.\r\n");
        }
    }

    public void simulatePRIVMSG(String chan, String msg) {
        String msgSim = ":" + myInfo.nuh() + " PRIVMSG " + chan + " :" + msg;
        u.writeAllClients(msgSim + "\r\n");
    }

    public void simulatePRIVMSG(JIBHandleClient skip, String chan, String msg) {
        String msgSim = ":" + myInfo.nuh() + " PRIVMSG " + chan + " :" + msg;
        u.writeAllClients(skip, msgSim + "\r\n");
    }

    public Exception getError() {
        if (sock.getError() != null) {
            connected = false;
        }
        return sock.getError();
    }

    public void run() {
        String l = null;
        while (errorCounter < 15) {
            while (getError() == null && sock.connected() == true) {
                l = sock.readLine();
                if (l != null) {
                    ping1.processLine(l);
                    processLine(l);
                } else {
                    connected = false;
                }
            }
            System.err.println("connecting=" + connecting + " connected=" + connected);
            if (sock.connected() == false || connected == false) {
                reconnect();
            }
            try {
                Thread.sleep(30000);
            } catch (InterruptedException ex) {
                System.getLogger(JIBIRC.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            }
        }
    }
}
