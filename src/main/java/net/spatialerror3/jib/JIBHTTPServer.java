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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;

/**
 *
 * @author spatialerror3
 */
public class JIBHTTPServer {

    private static final Logger log = LogManager.getLogger(JIBHTTPServer.class);
    private int Port = -1;

    public JIBHTTPServer(int Port) {
        if (Port == -1) {
            this.Port = 7680;
        }
        //
        Server server = new Server();
        int acceptors = 4;
        int selectors = 4;
        ServerConnector connector = new ServerConnector(server, acceptors, selectors, new HttpConnectionFactory());
        connector.setPort(this.Port);
        connector.setHost("0.0.0.0");
        connector.setAcceptQueueSize(128);
        server.addConnector(connector);
        server.setDefaultHandler(new JIBHTTPHandler());
        try {
            server.start();
        } catch (Exception ex) {
            log.error("Error Starting HTTP Server", ex);
        }
    }
}
