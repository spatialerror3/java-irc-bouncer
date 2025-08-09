/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package net.spatialerror3.jib;

/**
 *
 * @author spatialerror3
 */
public class JIBPinger implements Runnable {

    JIBSocket s = null;
    private String pingStr = null;

    public JIBPinger(JIBSocket s) {
        this.s = s;
    }

    public void processLine(String l) {
        if (l.startsWith("PING ")) {
            s.writeLine("PONG " + l.substring(5) + "\r\n");
        }
    }

    public void doPing() {
        s.writeLine("PING :" + pingStr + "\r\n");
    }

    public void run() {
        while (true) {
            doPing();
            try {
                Thread.sleep(30000);
            } catch (InterruptedException ex) {
                System.getLogger(JIBPinger.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            }
        }
    }
}
