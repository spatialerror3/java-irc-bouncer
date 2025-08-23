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

/**
 *
 * @author spatialerror3
 */
public class JIBIRCNickServ {

    private String nickServAccount = "";
    private String nickServPass = "";
    //
    private JIBUser u = null;
    private JIBIRCServer serv = null;

    public JIBIRCNickServ(JIBUser u, JIBIRCServer serv) {
        this.u = u;
        this.serv = serv;
    }

    public void init() {
        if (serv.getNickServUser() == null || serv.getNickServPass() == null) {
            if (u.getUserId() == 0 && u.admin() == true) {
                nickServAccount = JavaIrcBouncer.jibConfig.getValue("NICKSERVUSER");
                nickServPass = JavaIrcBouncer.jibConfig.getValue("NICKSERVPASS");
            }
        } else {
            nickServAccount = serv.getNickServUser();
            nickServPass = serv.getNickServPass();
        }
    }

    public void identify() {
        if (serv.getNickServUser() != null && serv.getNickServPass() != null) {
            if (serv.getNetType() == JIBIRCNetType.NetType.LIBERA) {
                u.getJibIRC().writeLine("PRIVMSG NickServ :IDENTIFY " + serv.getNickServUser() + " " + serv.getNickServPass() + "\r\n");
            }
            if (serv.getNetType() == JIBIRCNetType.NetType.OFTC) {
                u.getJibIRC().writeLine("PRIVMSG NickServ :IDENTIFY " + serv.getNickServPass() + "\r\n");
            }
        }
    }
}
