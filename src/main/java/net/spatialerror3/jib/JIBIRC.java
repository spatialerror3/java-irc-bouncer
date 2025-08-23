/*
 * Copyright (C) 2025 spatialerror3
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author spatialerror3
 */
public class JIBIRC implements Runnable {

    private static final Logger log = LogManager.getLogger(JIBIRC.class);
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
    private JIBIRCLog ircLog = null;
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
    //
    private boolean keepDisconnected = false;

    public JIBIRC(JIBUser u, JIBIRCServer serv) {
        JIBIRC.DEBUGGING = JavaIrcBouncer.jibDebug.debug();
        this.u = u;
        //
        if (serv == null) {
            this.serv = u.getIrcServer();
            //serv.setServer(Server);
            //serv.setPort(Port);
            //serv.setSsl(!noSsl);
            //
            if (this.serv != null) {
                this.serv.resolve();
            }
            serv = this.serv;
        } else {
            this.serv = serv;
        }
    }

    public void init() {
        //
        this.Server = serv.getServer();
        this.Port = serv.getPort();
        //
        this.nick = serv.getUserInfo().getNick();
        this.user = serv.getUserInfo().getUser();
        this.realname = serv.getUserInfo().getRealname();
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
        ircLog = new JIBIRCLog(this.u);
        //FIXME: 
        //connect(null);
    }

    public String getNick() {
        return this.nick;
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

    public void connect2(JIBIRCServer serv) {
        if (serv == null) {
            this.serv = u.getIrcServer();
        } else {
            this.serv = serv;
        }
        this.serv.setUserInfo(u.getIRCUserInfo());
        init();
        connect(serv);
    }

    private void connect(JIBIRCServer serv) {
        SSLContext sslctx = null;
        if (connected == true) {
            return;
        }
        if (errorCounter >= 15) {
            return;
        }
        connecting = true;
        keepDisconnected = false;
        connects++;
        preLogon = true;
        JIBIRCServer tmpServ = null;
        if (serv == null) {
            tmpServ = u.getIrcServer();
        } else {
            tmpServ = serv;
        }
        if (tmpServ != null) {
            tmpServ.resolve();
            ns = new JIBIRCNickServ(u, tmpServ);
            ns.init();
            perform = new JIBIRCPerform(u, tmpServ);
            if (tmpServ.getChannels().length() > 0) {
                perform.performListAdd("JOIN :" + tmpServ.getChannels() + "\r\n");
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
                log.error((String) null, ex);
                connecting = false;
            }
            InetAddress dst = null;
            int dstport = -1;
            try {
                dst = InetAddress.getByName(this.Server);
                dstport = Port;
            } catch (UnknownHostException ex) {
                log.error((String) null, ex);
                connecting = false;
            }
            if (tmpServ != null) {
                dst = tmpServ.getResolved();
                dstport = tmpServ.getPort();
            }
            if (dst == null) {
                connecting = false;
                return;
            }
            if (!noSsl) {
                try {
                    sslctx = SSLContext.getInstance("TLS");
                } catch (NoSuchAlgorithmException ex) {
                    log.error((String) null, ex);
                }
                TrustManager[] tm = new TrustManager[]{new JIBSSLTrustManager()};
                try {
                    sslctx.init(null, tm, null);
                } catch (KeyManagementException ex) {
                    log.error((String) null, ex);
                }
                SSLSocketFactory factory = (SSLSocketFactory) sslctx.getSocketFactory();
                try {
                    ircServer = (SSLSocket) factory.createSocket(dst, dstport, clientBind, 0);
                } catch (ConnectException ce) {
                    connectError = ce;
                } catch (IOException ex) {
                    log.error((String) null, ex);
                }
                sock = new JIBSocket(ircServer);
            } else {
                try {
                    ircServerNoSsl = new Socket(dst, dstport, clientBind, 0);
                } catch (ConnectException ce) {
                    connectError = ce;
                } catch (IOException ex) {
                    log.error((String) null, ex);
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
        }
        connecting = false;
    }

    public void disconnect(JIBIRCServer serv) {
        if (this.serv.equals(serv)) {
            // FIXME: disconnect from server
            this.keepDisconnected = true;
        }
    }

    public void reconnect() {
        if (DEBUGGING) {
            log.debug("reconnect() called...");
        }
        if (this.keepDisconnected == true) {
            return;
        }
        if (sock == null || sock.connected() == false || connected == false) {
            errorCounter = 0;
            connecting = false;
            connected = false;
            preLogon = true;
            connect(null);
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
            log.debug("onLogon()");
        }
        try {
            this.ns.identify();
        } catch (Exception e) {
            log.error((String) null, e);
        }
        try {
            this.perform.perform();
        } catch (Exception e) {
            log.error((String) null, e);
        }
        try {
            String[] chans = JavaIrcBouncer.jibDbUtil.getChannels(u);
            for (int i = 0; i < chans.length; i++) {
                if (chans[i] != null) {
                    writeLine("JOIN " + chans[i] + "\r\n");
                }
            }
        } catch (Exception e) {
            log.error((String) null, e);
        }
        if (DEBUGGING) {
            log.debug("preLogon=false");
        }
        preLogon = false;
    }

    public void writeLine(String l) {
        if (DEBUGGING) {
            log.debug(this + " writeLine()=" + JIBStringUtil.remEOL2(l));
        }
        sock.writeLine(l);
    }

    public void processLine(String l) {
        if (DEBUGGING) {
            log.debug(this + " l=" + l);
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
                if (sp5.length > 1 && sp5[1].equals("431")) {
                    writeLine("NICK C" + JIBStringUtil.randHexString().substring(0, 8) + "\r\n");
                }
                if (sp5.length > 1 && sp5[1].equals("432")) {
                    writeLine("NICK C" + JIBStringUtil.randHexString().substring(0, 8) + "\r\n");
                }
                if (sp5.length > 1 && sp5[1].equals("433")) {
                    writeLine("NICK C" + JIBStringUtil.randHexString().substring(0, 8) + "\r\n");
                }
                if (sp5.length > 1 && sp5[1].equals("436")) {
                    writeLine("NICK C" + JIBStringUtil.randHexString().substring(0, 8) + "\r\n");
                }
            }
        }
        String[] lsp = l.split(" ");
        if (lsp.length > 2) {
            if (lsp[1].equals("NICK")) {
                JIBUserInfo src = JIBUserInfo.parseNUH(lsp[0]);
                String srcNewNick = JIBStringUtil.remDD(lsp[2]);
                if (src.getNick().equals(this.nick)) {
                    this.nick = srcNewNick;
                }
            }
        }
        if (ircLog != null) {
            ircLog.processLine(l);
        }
        u.writeAllClients(l);
    }

    public String simulateNick(String oldnick, String newnick) {
        if (newnick == null) {
            if (myInfo != null && myInfo.nick != null && myInfo.nick.length() > 0) {
                newnick = myInfo.nick;
            } else {
                newnick = "u" + Long.toString(u.getUserId());
            }
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
        if (sock != null && sock.getError() != null) {
            connected = false;
        }
        if (sock == null) {
            return null;
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
                    try {
                        ping1.processLine(l);
                    } catch (Exception ex1) {
                        log.error("ping1.processLine(...)", ex1);
                    }
                    try {
                        processLine(l);
                    } catch (Exception ex2) {
                        log.error("processLine(...)", ex2);
                    }
                } else {
                    connected = false;
                }
            }
            if (DEBUGGING) {
                log.debug("connecting=" + connecting + " connected=" + connected + " connected()=" + connected());
            }
            if (sock.connected() == false || connected == false || connected() == false) {
                reconnect();
            }
            try {
                Thread.sleep(30000);
            } catch (InterruptedException ex) {
                log.error((String) null, ex);
            }
        }
        if (t3 != null) {
            t3 = null;
        }
    }
}
