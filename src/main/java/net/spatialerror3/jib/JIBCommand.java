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

import java.util.Iterator;
import java.util.ArrayList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author spatialerror3
 */
public class JIBCommand {

    private static final Logger log = LogManager.getLogger(JIBCommand.class);

    public JIBCommand() {

    }

    public void processCommand(JIBHandleClient hc, JIBUser authed, String message) {
        String[] excmd = message.split(" ", 2);
        if (excmd[0].startsWith("CONNECT")) {
            authed.getJibIRC().connect2(null);
        }
        if (excmd[0].startsWith("DISCONNECT")) {
            authed.getJibIRC().disconnect(null);
            authed.getJibIRC().disconnect(authed.getJibIRC().getConnectedTo());
        }
        if (excmd[0].startsWith("RECONNECT")) {
            authed.getJibIRC().reconnect();
        }
        if (excmd[0].startsWith("JUMP")) {
            authed.getJibIRC().disconnect(null);
            authed.getJibIRC().disconnect(authed.getJibIRC().getConnectedTo());
            authed.getJibIRC().connect2(null);
        }
        if (excmd[0].startsWith("LISTSERVERS")) {
            ArrayList<JIBIRCServer> sv = authed.getIrcServers();
            Iterator<JIBIRCServer> svi = sv.iterator();
            while (svi.hasNext()) {
                JIBIRCServer lss = svi.next();
                hc.sendLine(":*jib!jib@JIB.jib PRIVMSG " + hc.trackNick1() + " :" + "SERVER[" + lss.getUUID() + "]" + "= " + lss.getServer() + "\r\n");
            }
            hc.sendLine(":*jib!jib@JIB.jib PRIVMSG " + hc.trackNick1() + " :" + "DONE LISTING SERVERS" + "\r\n");
        }
        if (excmd[0].startsWith("DELSERVER")) {
            String[] params = excmd[1].split(" ", 2);
            ArrayList<JIBIRCServer> sv = authed.getIrcServers();
            Iterator<JIBIRCServer> svi = sv.iterator();
            while (svi.hasNext()) {
                JIBIRCServer dss = svi.next();
                if (dss.getUUID().toString().equals(params[0])) {
                    svi.remove();
                    JavaIrcBouncer.jibDbUtil.removeServer(authed, dss);
                    hc.sendLine(":*jib!jib@JIB.jib PRIVMSG " + hc.trackNick1() + " :" + "SERVER[" + dss.getUUID() + "]" + " DELETED" + "\r\n");
                }
            }
            hc.sendLine(":*jib!jib@JIB.jib PRIVMSG " + hc.trackNick1() + " :" + "DONE DELETE SERVER" + "\r\n");
        }
        if (excmd[0].startsWith("SERVER")) {
            String mtp = message;
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
                hc.sendLine(":*jib!jib@JIB.jib PRIVMSG " + hc.trackNick1() + " :" + "ADDED IRC SERVER" + "\r\n");
            } else {
                hc.sendLine(":*jib!jib@JIB.jib PRIVMSG " + hc.trackNick1() + " :" + "SERVER [host] [port] [ssl] [nsacct] [nspass] [chans]" + "\r\n");
            }
        }
        if (excmd[0].equals("ADDSERVER")) {

        }
        if (excmd[0].equals("GET")) {
            String[] params = excmd[1].split(" ", 2);
            if (params[0].equals("NICK")) {
                hc.sendLine(":*jib!jib@JIB.jib PRIVMSG " + hc.trackNick1() + " :" + params[0] + "= " + authed.getIRCUserInfo().getNick() + "\r\n");
            }
            if (params[0].equals("USER")) {
                hc.sendLine(":*jib!jib@JIB.jib PRIVMSG " + hc.trackNick1() + " :" + params[0] + "= " + authed.getIRCUserInfo().getUser() + "\r\n");
            }
            if (params[0].equals("REALNAME")) {
                hc.sendLine(":*jib!jib@JIB.jib PRIVMSG " + hc.trackNick1() + " :" + params[0] + "= " + authed.getIRCUserInfo().getRealname() + "\r\n");
            }
        }
        if (excmd[0].equals("SET")) {
            String[] params = excmd[1].split(" ", 2);
            if (params[0].equals("NICK")) {
                authed.setNick(params[1]);
            }
            if (params[0].equals("USER")) {
                authed.setUser(params[1]);
            }
            if (params[0].equals("REALNAME")) {
                authed.setRealname(params[1]);
            }
        }
        if (excmd[0].equals("REPLAY")) {
            Iterator<String> logReplay = JavaIrcBouncer.jibDbUtil.replayLog(authed).iterator();
            while (logReplay.hasNext()) {
                String logReplayStr = logReplay.next();
                hc.sendLine(logReplayStr + "\r\n");
            }
        }
        if (excmd[0].equals("REPLAYLOG")) {
            Iterator<String> logReplay = JavaIrcBouncer.jibDbUtil.replayLog(authed).iterator();
            while (logReplay.hasNext()) {
                String logReplayStr = logReplay.next();
                hc.sendLine(logReplayStr + "\r\n");
            }
        }
        if (excmd[0].equals("REPLAYLOGL2D")) {
            Iterator<String> logReplay = JavaIrcBouncer.jibDbUtil.replayLogDays(authed, 2).iterator();
            while (logReplay.hasNext()) {
                String logReplayStr = logReplay.next();
                hc.sendLine(logReplayStr + "\r\n");
            }
        }
        if (excmd[0].equals("CLEAR")) {
            JavaIrcBouncer.jibDbUtil.clearLog(authed);
        }
        if (excmd[0].equals("CLEARLOG")) {
            JavaIrcBouncer.jibDbUtil.clearLog(authed);
        }
        if (authed.admin() && excmd[0].equals("CLEARALL")) {
            JavaIrcBouncer.jibDbUtil.clearLog(null);
        }
        if (authed.admin() && excmd[0].equals("CLEARALLLOG")) {
            JavaIrcBouncer.jibDbUtil.clearLog(null);
        }
        if (authed.admin() && excmd[0].equals("UPTIME")) {
            hc.sendJibMsg("Uptime: " + (JavaIrcBouncer.jibStatus.getUptime() / 1000));
        }
        if (authed.admin() && excmd[0].equals("ADDUSER")) {
            String[] up = excmd[1].split(" ", 2);
            JavaIrcBouncer.jibConfig.createUser(up[0], up[1]);
            hc.sendJibMsg("ADDED USER: " + up[0]);
        }
        if (excmd[0].equals("HELP")) {
            hc.sendLine(":*jib!jib@JIB.jib PRIVMSG " + hc.trackNick1() + " :" + "AVAILABLE COMMANDS:" + "\r\n");
            hc.sendLine(":*jib!jib@JIB.jib PRIVMSG " + hc.trackNick1() + " :" + " - LISTSERVERS" + "\r\n");
            hc.sendLine(":*jib!jib@JIB.jib PRIVMSG " + hc.trackNick1() + " :" + " - ADDSERVER" + "\r\n");
            hc.sendLine(":*jib!jib@JIB.jib PRIVMSG " + hc.trackNick1() + " :" + " - DELSERVER" + "\r\n");
            hc.sendLine(":*jib!jib@JIB.jib PRIVMSG " + hc.trackNick1() + " :" + " - CONNECT" + "\r\n");
            hc.sendLine(":*jib!jib@JIB.jib PRIVMSG " + hc.trackNick1() + " :" + " - DISCONNECT" + "\r\n");
            hc.sendLine(":*jib!jib@JIB.jib PRIVMSG " + hc.trackNick1() + " :" + " - RECONNECT" + "\r\n");
            hc.sendLine(":*jib!jib@JIB.jib PRIVMSG " + hc.trackNick1() + " :" + " - JUMP" + "\r\n");
            hc.sendLine(":*jib!jib@JIB.jib PRIVMSG " + hc.trackNick1() + " :" + " - SET NICK" + "\r\n");
            hc.sendLine(":*jib!jib@JIB.jib PRIVMSG " + hc.trackNick1() + " :" + " - SET USER" + "\r\n");
            hc.sendLine(":*jib!jib@JIB.jib PRIVMSG " + hc.trackNick1() + " :" + " - SET REALNAME" + "\r\n");
            hc.sendLine(":*jib!jib@JIB.jib PRIVMSG " + hc.trackNick1() + " :" + " - GET NICK" + "\r\n");
            hc.sendLine(":*jib!jib@JIB.jib PRIVMSG " + hc.trackNick1() + " :" + " - GET USER" + "\r\n");
            hc.sendLine(":*jib!jib@JIB.jib PRIVMSG " + hc.trackNick1() + " :" + " - GET REALNAME" + "\r\n");
            hc.sendLine(":*jib!jib@JIB.jib PRIVMSG " + hc.trackNick1() + " :" + " - REPLAY[LOG]" + "\r\n");
            hc.sendLine(":*jib!jib@JIB.jib PRIVMSG " + hc.trackNick1() + " :" + " - CLEAR[LOG]" + "\r\n");
            if (authed.admin()) {
                hc.sendLine(":*jib!jib@JIB.jib PRIVMSG " + hc.trackNick1() + " :" + "ADMIN COMMANDS:" + "\r\n");
                hc.sendLine(":*jib!jib@JIB.jib PRIVMSG " + hc.trackNick1() + " :" + " - CLEARALL[LOG]" + "\r\n");
                hc.sendJibMsg(" - ADDUSER [user] [pass]");
                hc.sendJibMsg(" - UPTIME");
            }
        }
    }
}
