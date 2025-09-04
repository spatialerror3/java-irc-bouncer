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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
    private ArrayList<JIBIRCServer> ircServers = null;
    private JIBUserInfo ircUserInfo = null;
    //
    ArrayList<JIBHandleClient> clients = new ArrayList<JIBHandleClient>();

    /**
     *
     */
    public JIBUser() {
        ircServers = new ArrayList<JIBIRCServer>();
        ircUserInfo = new JIBUserInfo();
    }

    /**
     *
     * @param userName
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     *
     * @param authToken
     */
    public void setAuthToken(String authToken) {
        this.authToken = authToken;
        JavaIrcBouncer.jibDbUtil.addClientAuth(this, authToken);
        JavaIrcBouncer.jibDbUtil.refreshUser(this);
        JavaIrcBouncer.jibDbUtil.addUserAuthToken(this);
    }

    /**
     *
     * @return
     */
    public String getAuthToken() {
        return this.authToken;
    }

    /**
     *
     * @param authToken
     * @return
     */
    public JIBUser auth(String authToken) {
        if (this.authToken.equals(authToken)) {
            return this;
        }
        return null;
    }

    /**
     *
     * @return
     */
    public long getUserId() {
        return this.userId;
    }

    /**
     *
     * @param userId
     */
    public void setUserId(long userId) {
        this.userId = userId;
    }

    /**
     *
     * @return
     */
    public String getUserName() {
        return this.userName;
    }

    /**
     *
     * @return
     */
    public UUID getUUID() {
        return this.uuid;
    }

    /**
     *
     * @param _uuid
     */
    public void setUUID(UUID _uuid) {
        this.uuid = _uuid;
    }

    /**
     *
     * @param admin
     */
    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    /**
     *
     * @return
     */
    public boolean admin() {
        return this.admin;
    }

    /**
     *
     * @param jibIRC
     */
    public void setJibIRC(JIBIRC jibIRC) {
        this.jibIRC = jibIRC;
    }

    /**
     *
     * @return
     */
    public JIBIRC getJibIRC() {
        return this.jibIRC;
    }

    /**
     *
     * @param jibhc
     */
    public void addClient(JIBHandleClient jibhc) {
        List<JIBHandleClient> al1 = Collections.synchronizedList(clients);
        synchronized (al1) {
            al1.add(jibhc);
        }
    }

    /**
     *
     * @param l
     */
    public void writeAllClients(String l) {
        List<JIBHandleClient> al1 = Collections.synchronizedList(clients);
        Iterator<JIBHandleClient> it1 = null;
        synchronized (al1) {
            it1 = al1.iterator();
            while (it1.hasNext()) {
                JIBHandleClient tc = it1.next();
                if (tc.getError() == null && tc.getConnected() == true) {
                    tc.sendLine(l);
                }
            }
        }
    }

    /**
     *
     * @param skip
     * @param l
     */
    public void writeAllClients(JIBHandleClient skip, String l) {
        List<JIBHandleClient> al1 = Collections.synchronizedList(clients);
        Iterator<JIBHandleClient> it1 = null;
        synchronized (al1) {
            it1 = al1.iterator();
            while (it1.hasNext()) {
                JIBHandleClient tc = it1.next();
                if (!tc.equals(skip)) {
                    if (tc.getError() == null && tc.getConnected() == true) {
                        tc.sendLine(l);
                    }
                }
            }
        }
    }

    /**
     *
     * @param ircServer
     */
    public void addIrcServer(JIBIRCServer ircServer) {
        if (getIrcServer(ircServer) != null) {
            return;
        }
        ircServers.add(ircServer);
        JavaIrcBouncer.jibDbUtil.addServer(this, ircServer);
        JavaIrcBouncer.jibDbUtil.addChannels(this, ircServer, ircServer.getChannels());
    }

    /**
     *
     * @param ircServer
     */
    public void removeIrcServer(JIBIRCServer ircServer) {
        ircServers.remove(ircServer);
        JavaIrcBouncer.jibDbUtil.removeServer(this, ircServer);
    }

    public JIBIRCServer getIrcServer(JIBIRCServer ircServer) {
        JIBIRCServer ret = null;

        List<JIBIRCServer> isl1 = java.util.Collections.synchronizedList(getIrcServers());
        synchronized (isl1) {
            Iterator<JIBIRCServer> isl1it1 = isl1.iterator();
            while (isl1it1.hasNext()) {
                JIBIRCServer tmpServ = isl1it1.next();
                if (tmpServ.getUUID().equals(ircServer.getUUID())) {
                    ret = tmpServ;
                }
                if (tmpServ.getServer().equals(ircServer.getServer()) && tmpServ.getPortAsString().equals(ircServer.getPortAsString())) {
                    ret = tmpServ;
                }
            }
        }

        return ret;
    }

    /**
     *
     * @return
     */
    public ArrayList<JIBIRCServer> getIrcServers() {
        return this.ircServers;
    }

    /**
     *
     * @return
     */
    public JIBIRCServer getIrcServer() {
        int pos = -1;
        JIBIRCServer ret = null;
        try {
            pos = (int) (System.currentTimeMillis() % this.ircServers.size());
            ret = this.ircServers.get(pos);
        } catch (Exception e) {
            ret = null;
        }
        return ret;
    }

    /**
     *
     * @param nick
     */
    public void setNick(String nick) {
        ircUserInfo.nick = nick;
    }

    /**
     *
     * @param user
     */
    public void setUser(String user) {
        ircUserInfo.user = user;
    }

    /**
     *
     * @param realname
     */
    public void setRealname(String realname) {
        ircUserInfo.realname = realname;
    }

    /**
     *
     * @return
     */
    public JIBUserInfo getIRCUserInfo() {
        return this.ircUserInfo;
    }

    /**
     *
     * @return
     */
    public String toHTML() {
        StringBuilder sb1 = null;
        sb1 = new StringBuilder();

        sb1.append("<br>userName='" + getUserName() + "'");

        return sb1.toString();
    }
}
