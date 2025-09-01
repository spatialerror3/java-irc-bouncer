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

import java.io.IOException;
import java.net.*;
import java.util.Iterator;
import java.util.Vector;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author spatialerror3
 */
public class JIBServer implements Runnable {

    private static final Logger log = LogManager.getLogger(JIBServer.class);
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

    public Vector<JIBHandleClient> getClientsVector() {
        return this.clients;
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
    
    public void tidy() {
        cleanErrorClients();
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
