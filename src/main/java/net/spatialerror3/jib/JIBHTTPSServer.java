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
import org.eclipse.jetty.ee10.servlet.ServletContextHandler;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.SecureRequestCustomizer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.SslConnectionFactory;
import org.eclipse.jetty.util.ssl.SslContextFactory;

/**
 *
 * @author spatialerror3
 */
public class JIBHTTPSServer {

    private static final Logger log = LogManager.getLogger(JIBHTTPServer.class);
    private int Port = -1;

    public JIBHTTPSServer(int Port) {
        if (Port == -1) {
            this.Port = 7643;
        }        //
        Server server = new Server();

        HttpConfiguration https = new HttpConfiguration();
        https.addCustomizer(new SecureRequestCustomizer());

        SslContextFactory.Server sslContextFactory = new SslContextFactory.Server();
        sslContextFactory.setKeyStorePath(JavaIrcBouncer.jibConfig.getValue("keyStoreName".toUpperCase()));
        sslContextFactory.setKeyStorePassword(JavaIrcBouncer.jibConfig.getValue("keyStorePassword".toUpperCase()));
        sslContextFactory.setKeyManagerPassword(JavaIrcBouncer.jibConfig.getValue("keyStorePassword".toUpperCase()));

        ServerConnector sslConnector = new ServerConnector(server, new SslConnectionFactory(sslContextFactory, "http/1.1"), new HttpConnectionFactory(https));
        sslConnector.setPort(this.Port);

        server.addConnector(sslConnector);
        ServletContextHandler handler = new ServletContextHandler();
        handler.addServlet(JIBHTTPLoginServlet.class.getName(), "/");
        server.setDefaultHandler(new JIBHTTPHandler());
        server.setDefaultHandler(handler);
        try {
            server.start();
        } catch (UnsupportedOperationException uoe1) {
            log.debug("Unsupported Operation", uoe1);
        } catch (Exception ex) {
            log.error("Error Starting HTTPS Server", ex);
        }
    }
}
