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

import java.net.Socket;
import java.util.Iterator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author spatialerror3
 */
public class JIBHandleClient implements Runnable {

    private static final Logger log = LogManager.getLogger(JIBHandleClient.class);
    private static boolean DEBUGGING = false;
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
        JIBHandleClient.DEBUGGING = JavaIrcBouncer.jibDebug.debug();
        sock = new JIBSocket(cs);
        onConnect();
    }

    public void onConnect() {
        //sendLine("");
        sendLine(":JIB.jib NOTICE " + "*" + " :AUTHENTICATION MANDATORY\r\n");
    }

    public void refreshNick() {
        String nickInIRC = null;
        if (inAuth == false && getSingleJIBIRC() != null) {
            nickInIRC = getSingleJIBIRC().getNick();
            if (!nickInIRC.equals(trackNick) && nickInIRC != null && nickInIRC.length() > 0) {
                String setTrackNick = getSingleJIBIRC().simulateNick(trackNick, nickInIRC);
                trackNick = setTrackNick;
            }
        }
    }

    public void onAuthDone() {
        String[] channels = JavaIrcBouncer.jibDbUtil.getChannels(authed);
        String setTrackNick = getSingleJIBIRC().simulateNick(trackNick, null);
        trackNick = setTrackNick;
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
                if (authed.getIrcServer() == null && authed.getUserId() == 0 && authed.admin() == true) {
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
                    if (tmpServ != null) {
                        tmpServ.setNickServUser(JavaIrcBouncer.jibConfig.getValue("NICKSERVUSER"));
                        tmpServ.setNickServPass(JavaIrcBouncer.jibConfig.getValue("NICKSERVPASS"));
                    }
                    tmpServ.setUserInfo(authed.getIRCUserInfo());
                    jibIRC = new JIBIRC(authed, tmpServ);
                    JavaIrcBouncer.jibIRC = jibIRC;
                } else {
                    if (tmpServ != null) {
                        tmpServ.resolve();
                    }
                    JIBUserInfo tmpui2 = new JIBUserInfo();
                    tmpui2.setNick(authed.getIRCUserInfo().getNick());
                    tmpui2.setUser(authed.getIRCUserInfo().getUser());
                    tmpui2.setRealname(authed.getIRCUserInfo().getRealname());
                    if (tmpServ != null) {
                        tmpServ.setUserInfo(tmpui2);
                    }
                    jibIRC = new JIBIRC(authed, tmpServ);
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
        sock.writeLineNoEOL(l);
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
            log.warn("AUTH FAILED FOR USER " + this.authUser);
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
        if (DEBUGGING) {
            System.err.println(this + " l=" + l);
        }
        if (l.startsWith("CAP")) {
            passthrough = false;
            //sendLine(l); FIXME: IRSSI
            if (l.startsWith("CAP LS")) {
                sendLine(":JIB.jib CAP " + trackNick1() + " LS :\r\n");
            }
            if (l.startsWith("CAP REQ")) {
                String capabs = l.substring(l.indexOf(':') + 1);
                sendLine(":JIB.jib CAP " + trackNick1() + " ACK :" + capabs + "\r\n");
            }
            if (l.startsWith("CAP END")) {
                sendLine(":JIB.jib 001 " + trackNick + " :JIB\r\n");
            }
        }
        if (l.startsWith("NICK")) {
            passthrough = false;
            trackNick = l.substring(5);
            //sendLine(l);
        }
        if (l.startsWith("USER")) {
            passthrough = false;
            this.authUser = sp[1];
            checkUserPass();
            //sendLine(l);
        }
        if (l.startsWith("PASS")) {
            passthrough = false;
            this.authPass = l.substring(5);
            checkUserPass();
            //sendLine(l);
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
            try {
                getSingleJIBIRC().simulatePRIVMSG(this, sp[1], msgextract[2].substring(1));
            } catch (Exception e) {
                log.error((String) null, e);
            }
            if (msgextract[1].equals("*jib")) {
                passthrough = false;
                sendLine(":*jib!jib@JIB.jib PRIVMSG " + trackNick + " :" + "YOU ARE " + authed.getUUID() + "\r\n");
                if (authed != null) {
                    try {
                        JavaIrcBouncer.jibCommand.processCommand(this, authed, JIBStringUtil.remDD(msgextract[2]));
                    } catch (Exception e) {
                        log.error("jibCommand.processCommand(...)", e);
                    }
                    sendLine(":*jib!jib@JIB.jib PRIVMSG " + trackNick + " :" + "REPLAY" + "\r\n");
                    sendLine(":*jib!jib@JIB.jib PRIVMSG " + trackNick + " :" + msgextract[2].substring(1) + "\r\n");
                    if (msgextract[2].substring(1).startsWith("REPLAY")) {
                        Iterator<String> logReplay = JavaIrcBouncer.jibDbUtil.replayLog().iterator();
                        while (logReplay.hasNext()) {
                            sendLine(logReplay.next() + "\r\n");
                        }
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
                            if (mtps.length >= 5) {
                                String mtpNSACCT = mtps[4];
                                tmpServ2.setNickServUser(mtpNSACCT);
                            }
                            if (mtps.length >= 6) {
                                String mtpNSPASS = mtps[5];
                                tmpServ2.setNickServPass(mtpNSPASS);
                            }
                            if (mtps.length >= 7) {
                                String[] mtpCHANS = mtps[6].split(",");
                                for (int j = 0; j < mtpCHANS.length; j++) {
                                    tmpServ2.addChannel(mtpCHANS[j]);
                                }
                            }
                            tmpServ2.resolve();
                            authed.addIrcServer(tmpServ2);
                            sendLine(":*jib!jib@JIB.jib PRIVMSG " + trackNick + " :" + "ADDED IRC SERVER" + "\r\n");
                        } else {
                            // FIXME: PROVIDE HELP
                        }
                    }
                } else {
                    sendLine(":*jib!jib@JIB.jib PRIVMSG " + trackNick + " :" + "Live long and prosper!" + "\r\n");
                }
                refreshNick();
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
            //sendLine("PONG JIB.jib " + pong + "\r\n");
            sendLine(":JIB.jib PONG JIB.jib :" + JIBStringUtil.remDD(pong) + "\r\n");
        }
        if (passthrough) {
            if (getSingleJIBIRC() != null) {
                getSingleJIBIRC().writeLine(l + "\r\n");
            }
        }
    }

    public void run() {
        String l = null;
        String pl = "";
        while (pl != null) {
            try {
                processError();
            } catch (Exception ex1) {
                log.error((String) null, ex1);
            }
            try {
                l = sock.readLine();
            } catch (Exception ex2) {
                log.error((String) null, ex2);
            }
            try {
                processLine(l);
            } catch (Exception ex3) {
                log.error((String) null, ex3);
            }
            pl = l;
        }
    }
}
