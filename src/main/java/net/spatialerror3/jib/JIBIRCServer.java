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

    public JIBIRCServer() {
        channels = new ArrayList<String>();
    }

    public UUID getUUID() {
        return this.uuid;
    }

    public void setUUID(UUID _uuid) {
        this.uuid = _uuid;
    }

    public JIBIRCNetType.NetType getNetType() {
        return this.netType;
    }

    public String getServer() {
        return this.server;
    }

    public int getPort() {
        return this.port;
    }

    public boolean getSsl() {
        return this.ssl;
    }

    public JIBUserInfo getUserInfo() {
        return this.userinfo;
    }

    public String getNickServUser() {
        return this.nickServUser;
    }

    public String getNickServPass() {
        return this.nickServPass;
    }

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

    public String getClientBind() {
        return this.clientBind;
    }

    public boolean getIpv6() {
        return this.ipv6;
    }

    public void setNetType(JIBIRCNetType.NetType netType) {
        this.netType = netType;
    }

    public void setServer(String server) {
        this.server = server;
        if (this.server.equals("irc.oftc.net")) {
            setNetType(JIBIRCNetType.NetType.OFTC);
        }
        if (this.server.equals("irc.libera.chat")) {
            setNetType(JIBIRCNetType.NetType.LIBERA);
        }
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setSsl(boolean ssl) {
        this.ssl = ssl;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUserInfo(JIBUserInfo userinfo) {
        this.userinfo = userinfo;
    }

    public void setNickServUser(String nickServUser) {
        this.nickServUser = nickServUser;
    }

    public void setNickServPass(String nickServPass) {
        this.nickServPass = nickServPass;
    }

    public void addChannel(String chan) {
        this.channels.add(chan);
    }

    public void addChannels(String chans) {
        String[] chta = chans.split(",");
        for (int j = 0; j < chta.length; j++) {
            this.channels.add(chta[j]);
        }
    }

    public void setClientBind(String clientBind) {
        this.clientBind = clientBind;
    }

    public void setIpv6(boolean ipv6) {
        this.ipv6 = ipv6;
    }

    public void resolve() {
        try {
            this.resolved = InetAddress.getByName(this.server);
        } catch (UnknownHostException ex) {
            log.error((String) null, ex);
        }
    }

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

    public InetAddress getResolved() {
        return this.resolved;
    }
}
