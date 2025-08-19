/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package net.spatialerror3.jib;

import java.net.Socket;
import java.util.Iterator;

/**
 *
 * @author spatialerror3
 */
public class JIBHandleClient implements Runnable {

    JIBSocket sock = null;
    private boolean inAuth = true;
    private String authUser = null;
    private String authPass = null;
    private boolean authOk = false;
    //
    private JIBUser authed = null;
    //
    private String trackNick = null;

    public JIBHandleClient(Socket cs) {
        sock = new JIBSocket(cs);
        onConnect();
    }

    public void onConnect() {
        sendLine("");
        sendLine(":JIB.jib NOTICE " + "*" + " :AUTHENTICATION MANDATORY\r\n");
    }

    public void onAuthDone() {
        String[] channels = JavaIrcBouncer.jibDbUtil.getChannels(authed);
        getSingleJIBIRC().simulateNick(trackNick, null);
        for (int j = 0; j < channels.length; j++) {
            if (channels[j] != null) {
                getSingleJIBIRC().simulateJoin(channels[j]);
                getSingleJIBIRC().simulateJoin(channels[j], trackNick);
            }
        }
        sendLine(":JIB.jib NOTICE " + trackNick + " :IDENTIFIED AS " + authed.getUUID().toString() + "\r\n");
    }

    public Exception getError() {
        return sock.getError();
    }

    public boolean getConnected() {
        return sock.connected();
    }

    public JIBIRC getSingleJIBIRC() {
        if (authOk == false) {
            return null;
        }
        int dstPort = -1;
        if (JavaIrcBouncer.jibConfig.getValue("Port") != null) {
            dstPort = Integer.valueOf(JavaIrcBouncer.jibConfig.getValue("Port")).intValue();
        }
        JIBIRC jibIRC = null;
        if (authed.getJibIRC() == null) {
            if (JavaIrcBouncer.jibConfig.getValue("Server") != null) {
                JIBIRCServer tmpServ = null;
                if (authed.getIrcServer() == null) {
                    tmpServ = new JIBIRCServer();
                    tmpServ.setServer(JavaIrcBouncer.jibConfig.getValue("Server"));
                    tmpServ.setPort(dstPort);
                    tmpServ.setSsl(JavaIrcBouncer.jibConfig.getValue("ClientNoSSL") == null);
                    tmpServ.setClientBind(JavaIrcBouncer.jibConfig.getValue("ClientBind"));
                    authed.addIrcServer(tmpServ);
                } else {
                    tmpServ = authed.getIrcServer();
                }
                if (authed.getIRCUserInfo() == null) {
                    authed.setNick(JavaIrcBouncer.jibConfig.getValue("Nick"));
                    authed.setUser(JavaIrcBouncer.jibConfig.getValue("User"));
                    authed.setRealname(JavaIrcBouncer.jibConfig.getValue("Realname"));
                } else {
                    if (authed.getIRCUserInfo().nick == null) {
                        authed.setNick(JavaIrcBouncer.jibConfig.getValue("Nick"));
                    }
                    if (authed.getIRCUserInfo().user == null) {
                        authed.setUser(JavaIrcBouncer.jibConfig.getValue("User"));
                    }
                    if (authed.getIRCUserInfo().realname == null) {
                        authed.setRealname(JavaIrcBouncer.jibConfig.getValue("Realname"));
                    }
                }
                if (authed.admin() && authed.getUserId() == 0) {
                    jibIRC = new JIBIRC(authed, tmpServ, JavaIrcBouncer.jibConfig.getValue("Server"), dstPort, JavaIrcBouncer.jibConfig.getValue("Nick"), JavaIrcBouncer.jibConfig.getValue("User"), JavaIrcBouncer.jibConfig.getValue("Realname"));
                    JavaIrcBouncer.jibIRC = jibIRC;
                } else {
                    tmpServ.resolve();
                    jibIRC = new JIBIRC(authed, tmpServ, tmpServ.getServer(), tmpServ.getPort(), authed.getIRCUserInfo().nick, authed.getIRCUserInfo().user, authed.getIRCUserInfo().realname);
                }
                authed.setJibIRC(jibIRC);
            } else {
                System.exit(255);
            }
        }
        return authed.getJibIRC();
    }

    public void processError() {
        if (JavaIrcBouncer.jibIRC != null) {
            if (JavaIrcBouncer.jibIRC.getError() != null) {
                JavaIrcBouncer.jibIRC = null;
            }
        }
    }

    public void sendLine(String l) {
        sock.writeLine(l + "\r\n");
    }

    public void checkUserPass() {
        if (this.authUser == null || this.authPass == null) {
            return;
        }
        String cnfUserChk = JavaIrcBouncer.jibConfig.getValue("AUTHUSER");
        String cnfPassChk = JavaIrcBouncer.jibConfig.getValue("AUTHPASS");
        if ((this.authUser.equals(cnfUserChk) && this.authPass.equals(cnfPassChk)) || (JavaIrcBouncer.jibCore.authUser(this.authUser, this.authPass) != null)) {
            authed = JavaIrcBouncer.jibCore.authUser(this.authUser, this.authPass);
            authed.addClient(this);
            this.authOk = true;
            onAuthDone();
        } else {
            sendLine("ERROR :Auth failed\r\n");
        }
        this.inAuth = false;
    }

    public String trackNick1() {
        if (trackNick == null) {
            return "*";
        }
        return trackNick;
    }

    public void processLine(String l) {
        boolean passthrough = true;
        if (l == null) {
            // FIXME: HANDLE ERROR
            return;
        }
        String[] sp = l.split(" ");
        System.err.println("l=" + l);
        if (l.startsWith("CAP")) {
            passthrough = false;
            //sendLine(l); FIXME: IRSSI
            if (l.startsWith("CAP LS")) {
                sendLine("CAP " + trackNick1() + " LS :\r\n");
            }
            if (l.startsWith("CAP REQ")) {
                String capabs = l.substring(l.indexOf(':') + 1);
                sendLine("CAP " + trackNick1() + " ACK :" + capabs + "\r\n");
            }
            if (l.startsWith("CAP END")) {
                sendLine("001 " + trackNick + " :JIB\r\n");
            }
        }
        if (l.startsWith("NICK")) {
            passthrough = false;
            trackNick = l.substring(5);
            sendLine(l);
        }
        if (l.startsWith("USER")) {
            passthrough = false;
            this.authUser = sp[1];
            checkUserPass();
            sendLine(l);
        }
        if (l.startsWith("PASS")) {
            passthrough = false;
            this.authPass = l.substring(5);
            checkUserPass();
            sendLine(l);
        }
        if (l.startsWith("QUIT")) {
            passthrough = false;
            return;
        }
        if (l.startsWith("CAP END")) {
            getSingleJIBIRC();
        }
        if (l.startsWith("JOIN")) {
            if (!l.equals("JOIN :")) {
                String[] channels = l.substring(5).split(",");
                for (int j = 0; j < channels.length; j++) {
                    getSingleJIBIRC().simulateJoin(channels[j]);
                    JavaIrcBouncer.jibDbUtil.addChannel(authed, channels[j]);
                }
            } else {
                passthrough = false;
            }
        }
        if (l.startsWith("PRIVMSG")) {
            String[] msgextract = l.split(" ", 3);
            getSingleJIBIRC().simulatePRIVMSG(this, sp[1], msgextract[2].substring(1));
            if (msgextract[1].equals("*jib")) {
                passthrough = false;
                sendLine(":*jib!jib@JIB.jib PRIVMSG " + trackNick + " :" + "YOU ARE " + authed.getUUID() + "\r\n");
                sendLine(":*jib!jib@JIB.jib PRIVMSG " + trackNick + " :" + "REPLAY" + "\r\n");
                sendLine(":*jib!jib@JIB.jib PRIVMSG " + trackNick + " :" + msgextract[2].substring(1) + "\r\n");
                if (msgextract[2].substring(1).startsWith("REPLAY")) {
                    Iterator<String> logReplay = JavaIrcBouncer.jibDbUtil.replayLog().iterator();
                    while (logReplay.hasNext()) {
                        sendLine(logReplay.next() + "\r\n");
                    }
                }
                if (msgextract[2].substring(1).startsWith("CONNECT")) {

                }
                if (msgextract[2].substring(1).startsWith("DISCONNECT")) {

                }
                if (msgextract[2].substring(1).startsWith("RECONNECT")) {

                }
                if (msgextract[2].substring(1).startsWith("SET")) {

                }
                if (msgextract[2].substring(1).startsWith("SERVER")) {
                    String mtp = msgextract[2].substring(1);
                    String[] mtps = mtp.split(" ");
                    if (mtps[0].equals("SERVER") && mtps.length >= 4) {
                        String mtpHost = mtps[1];
                        String mtpPort = mtps[2];
                        String mtpSSL = mtps[3];
                        int mtpPortInt = Integer.valueOf(mtpPort);
                        boolean mtpSslBool = Boolean.valueOf(mtpSSL);
                        JIBIRCServer tmpServ2 = new JIBIRCServer();
                        tmpServ2.setServer(mtpHost);
                        tmpServ2.setPort(mtpPortInt);
                        tmpServ2.setSsl(mtpSslBool);
                        tmpServ2.resolve();
                        authed.addIrcServer(tmpServ2);
                    }
                }
            }
        }
        if (l.startsWith("PART")) {
            String[] sp4 = l.split(" ", 3);
            JIBUserInfo tmpWho = new JIBUserInfo();
            tmpWho.nick = trackNick1();
            JavaIrcBouncer.jibDbUtil.removeChannel(authed, sp4[1]);
            getSingleJIBIRC().simulatePART(this, sp4[1], tmpWho);
        }
        if (l.startsWith("RAW")) {
            passthrough = false;
            if (l.length() >= 4) {
                getSingleJIBIRC().writeLine(l.substring(4) + "\r\n");
            }
        }
        if (l.startsWith("PING")) {
            String pong = l.substring(5);
            sendLine("PONG " + pong + "\r\n");
            sendLine(":JIB.jib PONG JIB.jib :" + JIBStringUtil.remDD(pong) + "\r\n");
        }
        if (passthrough) {
            getSingleJIBIRC().writeLine(l + "\r\n");
        }
    }

    public void run() {
        String l = null;
        String pl = "";
        while (pl != null) {
            processError();
            l = sock.readLine();
            processLine(l);
            pl = l;
        }
    }
}
