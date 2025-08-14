/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package net.spatialerror3.jib;

/**
 *
 * @author spatialerror3
 */
public class JIBIRCLog {
    public JIBIRCLog() {
        
    }
    
    public void processLine(String l) {
        String[] sp7 = l.split(" ",3);
        if(sp7.length > 2 && sp7[1].equals("PRIVMSG")) {
            String[] sp8 = l.split(" ",4);
            String logUser = sp8[0];
            String logTarget = sp8[2];
            String logMessage = sp8[3];
            JavaIrcBouncer.jibDbUtil.logUserTargetMessage(logUser, logTarget, logMessage);
        }
    }
}
