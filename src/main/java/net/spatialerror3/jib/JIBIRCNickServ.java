/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
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
