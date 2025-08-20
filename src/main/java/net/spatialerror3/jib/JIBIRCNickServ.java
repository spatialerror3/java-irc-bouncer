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
        if (u.getUserId() == 0 && u.admin() == true) {
            nickServAccount = JavaIrcBouncer.jibConfig.getValue("NICKSERVUSER");
            nickServPass = JavaIrcBouncer.jibConfig.getValue("NICKSERVPASS");
        } else {
            nickServAccount = serv.getNickServUser();
            nickServPass = serv.getNickServPass();
        }
    }

    public void identify() {
        if (nickServAccount != null && nickServPass != null) {
            if (serv.getNetType().equals(JIBIRCNetType.NetType.LIBERA)) {
                u.getJibIRC().writeLine("PRIVMSG NickServ :IDENTIFY " + nickServAccount + " " + nickServPass + "\r\n");
            }
            if (serv.getNetType().equals(JIBIRCNetType.NetType.OFTC)) {
                u.getJibIRC().writeLine("PRIVMSG NickServ :IDENTIFY " + nickServPass + "\r\n");
            }
        }
    }
}
