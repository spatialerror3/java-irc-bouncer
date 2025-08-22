/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package net.spatialerror3.jib;

import java.io.Serializable;

/**
 *
 * @author spatialerror3
 */
public class JIBUserInfo implements Serializable {

    private static final long serialVersionUID = 1L;
    public String nick;
    public String user;
    public String host;
    public String realname;

    public String getNick() {
        return this.nick;
    }

    public String getUser() {
        return this.user;
    }

    public String getHost() {
        return this.host;
    }

    public String getRealname() {
        return this.realname;
    }

    public String nuh() {
        return nick + "!" + user + "@" + host;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public static JIBUserInfo parseNUH(String containsNUH) {
        JIBUserInfo ret = null;
        String tmp1 = JIBStringUtil.remDD(containsNUH);
        String[] tmp2 = tmp1.split("!", 2);
        String[] tmp3 = (tmp2[1]).split("@", 2);
        ret = new JIBUserInfo();
        ret.setNick(tmp2[0]);
        ret.setUser(tmp3[0]);
        ret.setHost(tmp3[1]);
        return ret;
    }
}
