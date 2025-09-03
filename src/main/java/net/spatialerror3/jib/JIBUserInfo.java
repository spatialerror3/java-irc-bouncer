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
        if (tmp1.indexOf('!') != -1 && tmp1.indexOf('@') != -1) {
            String[] tmp2 = tmp1.split("!", 2);
            String[] tmp3 = (tmp2[1]).split("@", 2);
            ret = new JIBUserInfo();
            ret.setNick(tmp2[0]);
            ret.setUser(tmp3[0]);
            ret.setHost(tmp3[1]);
        } else {
            ret = new JIBUserInfo();
            ret.setHost(tmp1);
        }
        return ret;
    }
}
