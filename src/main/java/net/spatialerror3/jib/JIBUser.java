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

import java.io.Serializable;
import java.util.Iterator;
import java.util.UUID;
import java.util.Vector;

/**
 *
 * @author spatialerror3
 */
public class JIBUser implements Serializable {

    private static final long serialVersionUID = 1L;
    private static long userIdCnt = 0;
    private long userId = userIdCnt++;
    private UUID uuid = UUID.randomUUID();
    //
    private String userName = null;
    private String authToken = null;
    //
    private boolean admin = false;
    //
    private JIBIRC jibIRC = null;
    private Vector<JIBIRCServer> ircServers = null;
    private JIBUserInfo ircUserInfo = null;
    //
    Vector<JIBHandleClient> clients = new Vector<JIBHandleClient>();

    public JIBUser() {
        ircServers = new Vector<JIBIRCServer>();
        ircUserInfo = new JIBUserInfo();
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
        JavaIrcBouncer.jibDbUtil.addClientAuth(this, authToken);
    }

    public JIBUser auth(String authToken) {
        if (this.authToken.equals(authToken)) {
            return this;
        }
        return null;
    }

    public long getUserId() {
        return this.userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return this.userName;
    }

    public UUID getUUID() {
        return this.uuid;
    }

    public void setUUID(UUID _uuid) {
        this.uuid = _uuid;
    }

    public boolean admin() {
        return this.admin;
    }

    public void setJibIRC(JIBIRC jibIRC) {
        this.jibIRC = jibIRC;
    }

    public JIBIRC getJibIRC() {
        return this.jibIRC;
    }

    public void addClient(JIBHandleClient jibhc) {
        clients.add(jibhc);
    }

    public void writeAllClients(String l) {
        Iterator<JIBHandleClient> it1 = clients.iterator();
        while (it1.hasNext()) {
            JIBHandleClient tc = it1.next();
            if (tc.getError() == null && tc.getConnected() == true) {
                tc.sendLine(l);
            }
        }
    }

    public void writeAllClients(JIBHandleClient skip, String l) {
        Iterator<JIBHandleClient> it1 = clients.iterator();
        while (it1.hasNext()) {
            JIBHandleClient tc = it1.next();
            if (!tc.equals(skip)) {
                if (tc.getError() == null && tc.getConnected() == true) {
                    tc.sendLine(l);
                }
            }
        }
    }

    public void addIrcServer(JIBIRCServer ircServer) {
        ircServers.add(ircServer);
        JavaIrcBouncer.jibDbUtil.addServer(this, ircServer);
    }

    public Vector<JIBIRCServer> getIrcServers() {
        return this.ircServers;
    }

    public JIBIRCServer getIrcServer() {
        int pos = -1;
        JIBIRCServer ret = null;
        try {
            pos = (int) (System.currentTimeMillis() % this.ircServers.size());
            ret = this.ircServers.elementAt(pos);
        } catch (Exception e) {
            ret = null;
        }
        return ret;
    }

    public void setNick(String nick) {
        ircUserInfo.nick = nick;
    }

    public void setUser(String user) {
        ircUserInfo.user = user;
    }

    public void setRealname(String realname) {
        ircUserInfo.realname = realname;
    }

    public JIBUserInfo getIRCUserInfo() {
        return this.ircUserInfo;
    }
}
