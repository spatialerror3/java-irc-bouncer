/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package net.spatialerror3.jib;

/**
 *
 * @author spatialerror3
 */
public class JIBUserInfo {
    public String nick;
    public String user;
    public String host;
    public String realname;
    
    public String nuh() {
        return nick+"!"+user+"@"+host;
    }
}
