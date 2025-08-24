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
import java.util.Vector;
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
        }
        if (excmd[0].startsWith("RECONNECT")) {
            authed.getJibIRC().reconnect();
        }
        if (excmd[0].startsWith("JUMP")) {
            authed.getJibIRC().disconnect(null);
            authed.getJibIRC().connect2(null);
        }
        if (excmd[0].startsWith("LISTSERVERS")) {
            Vector<JIBIRCServer> sv = authed.getIrcServers();
            Iterator<JIBIRCServer> svi = sv.iterator();
            while (svi.hasNext()) {
                JIBIRCServer lss = svi.next();
                hc.sendLine(":*jib!jib@JIB.jib PRIVMSG " + hc.trackNick1() + " :" + "SERVER[" + lss.getUUID() + "]" + "= " + lss.getServer() + "\r\n");
            }
            hc.sendLine(":*jib!jib@JIB.jib PRIVMSG " + hc.trackNick1() + " :" + "DONE LISTING SERVERS" + "\r\n");
        }
        if (excmd[0].startsWith("DELSERVER")) {
            String[] params = excmd[1].split(" ", 2);
            Vector<JIBIRCServer> sv = authed.getIrcServers();
            Iterator<JIBIRCServer> svi = sv.iterator();
            while (svi.hasNext()) {
                JIBIRCServer dss = svi.next();
                if (dss.getUUID().toString().equals(params[0])) {
                    authed.getIrcServers().remove(dss);
                    JavaIrcBouncer.jibDbUtil.removeServer(authed, dss);
                    hc.sendLine(":*jib!jib@JIB.jib PRIVMSG " + hc.trackNick1() + " :" + "SERVER[" + dss.getUUID() + "]" + " DELETED" + "\r\n");
                }
            }
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
    }
}
