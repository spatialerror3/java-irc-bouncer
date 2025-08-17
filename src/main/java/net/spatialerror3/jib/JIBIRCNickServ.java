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
        this.u=u;
        this.serv=serv;
    }

    public void init() {
        nickServAccount = JavaIrcBouncer.jibConfig.getValue("NICKSERVUSER");
        nickServPass = JavaIrcBouncer.jibConfig.getValue("NICKSERVPASS");
    }

    public void identify() {
        if (nickServAccount != null && nickServPass != null) {
            u.getJibIRC().writeLine("PRIVMSG NickServ :IDENTIFY " + nickServAccount + " " + nickServPass + "\r\n");
        }
    }
}
