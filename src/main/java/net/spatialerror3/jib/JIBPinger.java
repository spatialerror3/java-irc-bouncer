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

    public void setPingStr() {
        pingStr = "LAG" + System.currentTimeMillis();
    }

    public void processLine(String l) {
        if (l == null) {
            return;
        }
        String[] sp = l.split(" ");
        if (l.startsWith("PING ")) {
            s.writeLine("PONG " + l.substring(5) + "\r\n");
        }
        if (sp.length > 1 && sp[1].equals("PONG")) {
            setPingStr();
        }
    }

    public void doPing() {
        s.writeLine("PING :" + pingStr + "\r\n");
    }

    public void run() {
        boolean noErrors = true;
        while (noErrors) {
            doPing();
            try {
                Thread.sleep(30000);
            } catch (InterruptedException ex) {
                System.getLogger(JIBPinger.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            }
            if (s.connected() == false) {
                noErrors = false;
            }
            if (s.getError() != null) {
                noErrors = false;
            }
        }
    }
}
