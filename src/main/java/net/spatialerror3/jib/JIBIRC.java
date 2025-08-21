/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package net.spatialerror3.jib;

import java.io.IOException;
import java.net.ConnectException;
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

    private static boolean DEBUGGING = false;
    private SSLSocket ircServer = null;
    private Socket ircServerNoSsl = null;
    private JIBSocket sock = null;
    //
    private String Server = null;
    private int Port = -1;
    private JIBIRCServer serv = null;
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
    private JIBIRCPerform perform = null;
    //
    private int errorCounter = 0;
    //
    private JIBIRCLog log = null;
    //
    private long connects = 0;
    private boolean connected = false;
    private boolean connecting = false;
    //
    private JIBUser u = null;
    //
    private Thread t3 = null;
    //
    private Exception connectError = null;

    public JIBIRC(JIBUser u, JIBIRCServer serv, String Server, int Port, String nick, String user, String realname) {
        JIBIRC.DEBUGGING = JavaIrcBouncer.jibDebug.debug();
        this.u = u;
        //
        this.Server = Server;
        this.Port = Port;
        if (serv == null) {
            serv = new JIBIRCServer();
            serv.setServer(Server);
            serv.setPort(Port);
            serv.setSsl(!noSsl);
            //
            serv.resolve();
        } else {
            this.serv = serv;
        }
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
        //
        if (u.getIRCUserInfo() != null) {
            myInfo = u.getIRCUserInfo();
        }
        //
        log = new JIBIRCLog();
        connect();
    }

    public boolean connected() {
        if (connecting == true) {
            return true;
        }
        if (getConnectError() != null) {
            return false;
        }
        if (getError() != null) {
            return false;
        }
        if (connected == true) {
            return true;
        }

        return false;
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
        connects++;
        preLogon = true;
        JIBIRCServer tmpServ = u.getIrcServer();
        tmpServ.resolve();
        ns = new JIBIRCNickServ(u, serv);
        ns.init();
        perform = new JIBIRCPerform(u, serv);
        if (serv.getChannels().length() > 0) {
            perform.performListAdd("JOIN :" + serv.getChannels() + "\r\n");
        }
        //u.writeAllClients(":JIB.jib NOTICE " + myInfo.nick + " :Connecting to " + this.Server + " :" + this.Port);
        u.writeAllClients(":JIB.jib NOTICE " + myInfo.nick + " :Connecting (#" + connects + ") [SSL=" + tmpServ.getSsl() + "] to " + tmpServ.getServer() + " :" + tmpServ.getPort() + " (RESOLVED: " + tmpServ.getResolved() + ")");
        u.writeAllClients(":JIB.jib NOTICE " + myInfo.nick + " :USER= " + myInfo.user);
        u.writeAllClients(":JIB.jib NOTICE " + myInfo.nick + " :REALNAME= " + myInfo.realname);
        u.writeAllClients(":JIB.jib NOTICE " + myInfo.nick + " :NICK= " + myInfo.nick);
        u.writeAllClients(":JIB.jib NOTICE " + myInfo.nick + " :NICKSERV " + (tmpServ.getNickServUser() != null) + " " + (tmpServ.getNickServPass() != null));
        u.writeAllClients(":JIB.jib NOTICE " + myInfo.nick + " :CHANNELS " + tmpServ.getChannels());
        InetAddress clientBind = null;
        if (JavaIrcBouncer.jibConfig.getValue("ClientNoSSL") != null) {
            noSsl = true;
        }
        noSsl = !tmpServ.getSsl();
        try {
            if (JavaIrcBouncer.jibConfig.getValue("ClientBind") != null) {
                clientBind = InetAddress.getByName(JavaIrcBouncer.jibConfig.getValue("ClientBind"));
            }
            if (this.serv.getClientBind() != null) {
                clientBind = InetAddress.getByName(this.serv.getClientBind());
            }
        } catch (UnknownHostException ex) {
            System.getLogger(JIBIRC.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            connecting = false;
        }
        InetAddress dst = null;
        int dstport = -1;
        try {
            dst = InetAddress.getByName(this.Server);
            dstport = Port;
        } catch (UnknownHostException ex) {
            System.getLogger(JIBIRC.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            connecting = false;
        }
        if (tmpServ != null) {
            dst = tmpServ.getResolved();
            dstport = tmpServ.getPort();
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
                ircServer = (SSLSocket) factory.createSocket(dst, dstport, clientBind, 0);
            } catch (ConnectException ce) {
                connectError = ce;
            } catch (IOException ex) {
                System.getLogger(JIBIRC.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            }
            sock = new JIBSocket(ircServer);
        } else {
            try {
                ircServerNoSsl = new Socket(dst, dstport, clientBind, 0);
            } catch (ConnectException ce) {
                connectError = ce;
            } catch (IOException ex) {
                System.getLogger(JIBIRC.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            }
            sock = new JIBSocket(ircServerNoSsl);
        }
        if (sock != null && sock.getError() == null) {
            connected = true;
            onConnect();
        }
        if (t3 == null) {
            t3 = new Thread(this);
            t3.start();
        }
        connecting = false;
    }

    public void reconnect() {
        if (DEBUGGING) {
            System.err.println("reconnect() called...");
        }
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
        if (DEBUGGING) {
            System.err.println("onLogon()");
        }
        try {
            this.ns.identify();
        } catch (Exception e) {
            System.getLogger(JIBIRC.class.getName()).log(System.Logger.Level.ERROR, (String) null, e);
        }
        try {
            this.perform.perform();
        } catch (Exception e) {
            System.getLogger(JIBIRC.class.getName()).log(System.Logger.Level.ERROR, (String) null, e);
        }
        try {
            String[] chans = JavaIrcBouncer.jibDbUtil.getChannels(u);
            for (int i = 0; i < chans.length; i++) {
                if (chans[i] != null) {
                    writeLine("JOIN " + chans[i] + "\r\n");
                }
            }
        } catch (Exception e) {
            System.getLogger(JIBIRC.class.getName()).log(System.Logger.Level.ERROR, (String) null, e);
        }
        if (DEBUGGING) {
            System.err.println("preLogon=false");
        }
        preLogon = false;
    }

    public void writeLine(String l) {
        if (DEBUGGING) {
            System.err.println(this + " writeLine()=" + JIBStringUtil.remEOL2(l));
        }
        sock.writeLine(l);
    }

    public void processLine(String l) {
        if (DEBUGGING) {
            System.err.println(this + " l=" + l);
        }
        if (l == null || l.startsWith("ERROR")) {
            errorCounter++;
        } else {
            if (preLogon) {
                String[] sp5 = l.split(" ", 3);
                if (sp5.length > 1 && sp5[1].equals("005")) {
                    onLogon();
                }
                if (sp5.length > 1 && sp5[1].equals("255")) {
                    onLogon();
                }
                if (sp5.length > 1 && sp5[1].equals("376")) {
                    onLogon();
                }
            }
        }
        if (log != null) {
            log.processLine(l);
        }
        u.writeAllClients(l);
    }

    public String simulateNick(String oldnick, String newnick) {
        if (newnick == null) {
            newnick = myInfo.nick;
        }
        String nickSim = ":" + oldnick + " NICK " + newnick;
        u.writeAllClients(nickSim + "\r\n");
        return newnick;
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
            u.writeAllClients(":JIB.jib 353 " + nick + " = " + chan + " :" + nick + "\r\n");
            u.writeAllClients(":JIB.jib 366 " + nick + " " + chan + " :End of /NAMES list.\r\n");
            writeLine("NAMES " + chan + "\r\n");
            u.writeAllClients(":JIB.jib 324 " + nick + " " + chan + " +nt\r\n");
            u.writeAllClients(":JIB.jib 329 " + nick + " " + chan + " 0\r\n");
            u.writeAllClients(":JIB.jib 315 " + nick + " " + chan + " :End of /WHO list.\r\n");
        }
    }

    public void simulatePART(String chan, JIBUserInfo who) {
        String partSim = ":" + who.nuh() + " PART " + chan;
        u.writeAllClients(partSim + "\r\n");
    }

    public void simulatePART(JIBHandleClient skip, String chan, JIBUserInfo who) {
        String partSim = ":" + who.nuh() + " PART " + chan;
        u.writeAllClients(skip, partSim + "\r\n");
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

    public Exception getConnectError() {
        return this.connectError;
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
            if (DEBUGGING) {
                System.err.println("connecting=" + connecting + " connected=" + connected + " connected()=" + connected());
            }
            if (sock.connected() == false || connected == false || connected() == false) {
                reconnect();
            }
            try {
                Thread.sleep(30000);
            } catch (InterruptedException ex) {
                System.getLogger(JIBIRC.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            }
        }
        if (t3 != null) {
            t3 = null;
        }
    }
}
