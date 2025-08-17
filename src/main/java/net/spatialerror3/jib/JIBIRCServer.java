/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package net.spatialerror3.jib;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 *
 * @author spatialerror3
 */
public class JIBIRCServer {

    private JIBIRCNetType.NetType netType = JIBIRCNetType.NetType.GENERIC;
    private String server = null;
    private int port = 0;
    private boolean ssl = true;
    //
    private String clientBind = null;
    //
    private boolean ipv6 = false;
    //
    private InetAddress resolved = null;

    public JIBIRCServer() {

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
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setSsl(boolean ssl) {
        this.ssl = ssl;
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
            System.getLogger(JIBIRC.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }

    public void resolve2() {
        InetAddress[] addrs = null;
        try {
            addrs = InetAddress.getAllByName(this.server);
        } catch (UnknownHostException ex) {
            System.getLogger(JIBIRC.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
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
