/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package net.spatialerror3.jib;

import java.io.IOException;
import java.net.*;
import java.util.Iterator;
import java.util.Vector;

/**
 *
 * @author spatialerror3
 */
public class JIBServer implements Runnable {

    ServerSocket ss = null;
    Vector<JIBHandleClient> clients = new Vector<JIBHandleClient>();

    public JIBServer() {
        InetAddress bindAddr = null;

        if (JavaIrcBouncer.jibConfig.getValue("ServerBind") != null) {
            try {
                bindAddr = InetAddress.getByName(JavaIrcBouncer.jibConfig.getValue("ServerBind"));
            } catch (UnknownHostException ex) {
                System.getLogger(JIBServer.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            }
        }
        try {
            ss = new ServerSocket(7667, 0, bindAddr);
        } catch (IOException ex) {
            System.getLogger(JIBServer.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }

    }

    public void cleanErrorClients() {
        Iterator<JIBHandleClient> it1 = clients.iterator();
        while (it1.hasNext()) {
            JIBHandleClient tc = it1.next();
            if (tc.getError() != null) {
                it1.remove();
            } else if (tc.getConnected() == false) {
                it1.remove();
            }
        }
    }

    public void writeAllClients(String l) {
        cleanErrorClients();
        Iterator<JIBHandleClient> it1 = clients.iterator();
        while (it1.hasNext()) {
            JIBHandleClient tc = it1.next();
            tc.sendLine(l);
        }
    }

    public void run() {
        Socket cs = null;
        JIBHandleClient nc = null;
        while (true) {
            try {
                cs = ss.accept();
            } catch (IOException ex) {
                System.getLogger(JIBServer.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            }
            nc = new JIBHandleClient(cs);
            clients.add(nc);
            Thread ct = new Thread(nc);
            ct.start();
        }
    }
}
