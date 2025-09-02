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
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.UUID;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author spatialerror3
 */
public class JIBIRCServer implements Serializable {

    private static final Logger log = LogManager.getLogger(JIBIRCServer.class);
    private static final long serialVersionUID = 1L;
    private UUID uuid = UUID.randomUUID();
    //
    private JIBIRCNetType.NetType netType = JIBIRCNetType.NetType.GENERIC;
    private String server = null;
    private int port = 0;
    private boolean ssl = true;
    private String password = null;
    //
    private JIBUserInfo userinfo = null;
    //
    private String nickServUser = null;
    private String nickServPass = null;
    //
    private ArrayList<String> channels = null;
    //
    private String clientBind = null;
    //
    private boolean ipv6 = false;
    //
    private InetAddress resolved = null;

    /**
     *
     */
    public JIBIRCServer() {
        userinfo = new JIBUserInfo();
        channels = new ArrayList<String>();
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
     * @return
     */
    public JIBIRCNetType.NetType getNetType() {
        return this.netType;
    }

    /**
     *
     * @return
     */
    public String getServer() {
        return this.server;
    }

    /**
     *
     * @return
     */
    public int getPort() {
        return this.port;
    }

    /**
     *
     * @return
     */
    public String getPortAsString() {
        return (this.ssl ? "+" : "") + this.port + "";
    }

    /**
     *
     * @return
     */
    public boolean getSsl() {
        return this.ssl;
    }
    
    public String getPassword() {
        return this.password;
    }

    /**
     *
     * @return
     */
    public JIBUserInfo getUserInfo() {
        return this.userinfo;
    }

    /**
     *
     * @return
     */
    public String getNick() {
        return this.userinfo.getNick();
    }

    /**
     *
     * @return
     */
    public String getUser() {
        return this.userinfo.getUser();
    }

    /**
     *
     * @return
     */
    public String getRealname() {
        return this.userinfo.getRealname();
    }

    /**
     *
     * @return
     */
    public String getNickServUser() {
        return this.nickServUser;
    }

    /**
     *
     * @return
     */
    public String getNickServPass() {
        return this.nickServPass;
    }

    /**
     *
     * @return
     */
    public String getChannels() {
        StringBuilder sb1 = new StringBuilder();
        Iterator<String> it1 = this.channels.iterator();
        long i = 0;
        while (it1.hasNext()) {
            if (i > 0) {
                sb1.append(",");
            }
            sb1.append(it1.next());
            i++;
        }
        return sb1.toString();
    }

    /**
     *
     * @return
     */
    public String getClientBind() {
        return this.clientBind;
    }

    /**
     *
     * @return
     */
    public boolean getIpv6() {
        return this.ipv6;
    }

    /**
     *
     * @param netType
     */
    public void setNetType(JIBIRCNetType.NetType netType) {
        this.netType = netType;
    }

    /**
     *
     * @param server
     */
    public void setServer(String server) {
        this.server = server;
        if (this.server.equals("irc.oftc.net")) {
            setNetType(JIBIRCNetType.NetType.OFTC);
        }
        if (this.server.equals("irc.libera.chat")) {
            setNetType(JIBIRCNetType.NetType.LIBERA);
        }
        if (this.server.endsWith("dal.net")) {
            setNetType(JIBIRCNetType.NetType.DALNET);
        }
        if (this.server.endsWith("undernet.org")) {
            setNetType(JIBIRCNetType.NetType.UNDERNET);
        }
        if (this.server.endsWith("quakenet.org")) {
            setNetType(JIBIRCNetType.NetType.QUAKENET);
        }
    }

    /**
     *
     * @param port
     */
    public void setPort(int port) {
        this.port = port;
    }

    /**
     *
     * @param port
     */
    public void setPort(String port) {
        String port2 = null;
        if (port.charAt(0) == '+') {
            setSsl(true);
            port2 = port.substring(1);
        } else {
            setSsl(false);
            port2 = port;
        }
        setPort(Integer.parseInt(port2));
    }

    /**
     *
     * @param ssl
     */
    public void setSsl(boolean ssl) {
        this.ssl = ssl;
    }

    /**
     *
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     *
     * @param userinfo
     */
    public void setUserInfo(JIBUserInfo userinfo) {
        this.userinfo = userinfo;
    }

    /**
     *
     * @param nick
     */
    public void setNick(String nick) {
        this.userinfo.setNick(nick);
    }

    /**
     *
     * @param user
     */
    public void setUser(String user) {
        this.userinfo.setUser(user);
    }

    /**
     *
     * @param realname
     */
    public void setRealname(String realname) {
        this.userinfo.setRealname(realname);
    }

    /**
     *
     * @param nickServUser
     */
    public void setNickServUser(String nickServUser) {
        this.nickServUser = nickServUser;
    }

    /**
     *
     * @param nickServPass
     */
    public void setNickServPass(String nickServPass) {
        this.nickServPass = nickServPass;
    }

    /**
     *
     * @param chan
     */
    public void addChannel(String chan) {
        this.channels.add(chan);
    }

    /**
     *
     * @param chans
     */
    public void addChannels(String chans) {
        String[] chta = chans.split(",");
        for (int j = 0; j < chta.length; j++) {
            this.channels.add(chta[j]);
        }
    }

    /**
     *
     * @param clientBind
     */
    public void setClientBind(String clientBind) {
        this.clientBind = clientBind;
    }

    /**
     *
     * @param ipv6
     */
    public void setIpv6(boolean ipv6) {
        this.ipv6 = ipv6;
    }

    /**
     *
     */
    public void resolve() {
        try {
            this.resolved = InetAddress.getByName(this.server);
        } catch (UnknownHostException ex) {
            log.error((String) null, ex);
        }
    }

    /**
     *
     */
    public void resolve2() {
        InetAddress[] addrs = null;
        try {
            addrs = InetAddress.getAllByName(this.server);
        } catch (UnknownHostException ex) {
            log.error((String) null, ex);
        }
        if (ipv6 == false) {
            for (int i = 0; i < addrs.length; i++) {
                if (addrs[i] instanceof Inet4Address) {
                    this.resolved = addrs[i];
                }
            }
        }
        if (ipv6 == true) {
            for (int i = 0; i < addrs.length; i++) {
                if (addrs[i] instanceof Inet6Address) {
                    this.resolved = addrs[i];
                }
            }
        }
    }

    /**
     *
     * @return
     */
    public InetAddress getResolved() {
        return this.resolved;
    }

    /**
     *
     * @return
     */
    public String toHTML() {
        StringBuilder sb1 = new StringBuilder();

        sb1.append("<br>server='" + getServer() + "'");
        sb1.append("<br>port='" + getPort() + "'");
        sb1.append("<br>ssl='" + getSsl() + "'");
        sb1.append("<br>ipv6='" + getIpv6() + "'");
        sb1.append("<br>clientbind='" + getClientBind() + "'");
        sb1.append("<br>serverpass=" + (this.password == null ? "UNSET" : "SET"));
        sb1.append("<br>nick=" + (getUserInfo() != null ? "'" + getUserInfo().getNick() + "'" : "UNSET"));
        sb1.append("<br>user=" + (getUserInfo() != null ? "'" + getUserInfo().getUser() + "'" : "UNSET"));
        sb1.append("<br>realname=" + (getUserInfo() != null ? "'" + getUserInfo().getRealname() + "'" : "UNSET"));
        sb1.append("<br>nickservuser=" + (getNickServUser() == null ? "UNSET" : "SET"));
        sb1.append("<br>nickservpass=" + (getNickServPass() == null ? "UNSET" : "SET"));
        sb1.append("<br>channels=" + (getChannels() != null ? "'" + getChannels() + "'" : "UNSET"));
        sb1.append("<br><br>");

        return sb1.toString();
    }

    /**
     *
     * @param server
     * @param port
     * @param ssl
     * @param ipv6
     * @param clientBind
     * @param password
     * @param userInfo
     * @param nickServUser
     * @param nickServPass
     * @param channels
     * @return
     */
    public static JIBIRCServer createJIBIRCServer(String server, int port, boolean ssl, boolean ipv6, String clientBind, String password, JIBUserInfo userInfo, String nickServUser, String nickServPass, String channels) {
        JIBIRCServer tmpServ = null;
        tmpServ = new JIBIRCServer();
        tmpServ.setServer(server);
        tmpServ.setPort(port);
        tmpServ.setSsl(ssl);
        tmpServ.setIpv6(ipv6);
        tmpServ.setClientBind(clientBind);
        tmpServ.setPassword(password);
        //tmpServ.setUserInfo(userInfo);
        if (userInfo != null && userInfo.getNick() != null) {
            tmpServ.setNick(userInfo.getNick());
        }
        if (userInfo != null && userInfo.getUser() != null) {
            tmpServ.setUser(userInfo.getUser());
        }
        if (userInfo != null && userInfo.getRealname() != null) {
            tmpServ.setRealname(userInfo.getRealname());
        }
        tmpServ.setNickServUser(nickServUser);
        tmpServ.setNickServPass(nickServPass);
        tmpServ.addChannels(channels);
        return tmpServ;
    }
}
