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

import java.net.URL;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.ee10.servlet.DefaultServlet;
import org.eclipse.jetty.ee10.servlet.ResourceServlet;
import org.eclipse.jetty.ee10.servlet.ServletContextHandler;
import org.eclipse.jetty.ee10.servlet.ServletHolder;
import org.eclipse.jetty.ee10.servlet.SessionHandler;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.SecureRequestCustomizer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.SslConnectionFactory;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.session.DefaultSessionIdManager;
import org.eclipse.jetty.session.HouseKeeper;
import org.eclipse.jetty.util.Callback;
import org.eclipse.jetty.util.resource.ResourceFactory;
import org.eclipse.jetty.util.ssl.SslContextFactory;

/**
 *
 * @author spatialerror3
 */
public class JIBHTTPSServer extends JIBWebBase {
    
    private static final Logger log = LogManager.getLogger(JIBHTTPSServer.class);
    
    public JIBHTTPSServer(int Port) {
        if (Port == -1) {
            setPort(7643, "https");
        }        //
        Server server = new Server();
        setServer(server,"server2");
        addSessionIdManager();
        
        HttpConfiguration https = new HttpConfiguration();
        https.addCustomizer(new SecureRequestCustomizer());
        
        SslContextFactory.Server sslContextFactory = new SslContextFactory.Server();
        sslContextFactory.setKeyStorePath(JavaIrcBouncer.jibConfig.getValue("keyStoreName".toUpperCase()));
        sslContextFactory.setKeyStorePassword(JavaIrcBouncer.jibConfig.getValue("keyStorePassword".toUpperCase()));
        sslContextFactory.setKeyManagerPassword(JavaIrcBouncer.jibConfig.getValue("keyStorePassword".toUpperCase()));
        
        ServerConnector sslConnector = new ServerConnector(server, new SslConnectionFactory(sslContextFactory, "http/1.1"), new HttpConnectionFactory(https));
        sslConnector.setPort(getPort());
        sslConnector.setHost("0.0.0.0");
        
        server.addConnector(sslConnector);
        //SessionHandler sessionHandler = new SessionHandler();
        //sessionHandler.setSessionIdManager(idMgr);
        //sessionHandler.setServer(server);
        //server.addBean(sessionHandler, true);
        
        //addSecurityHandler();
        addHandlers();
        try {
            server.start();
        } catch (UnsupportedOperationException uoe1) {
            log.debug("Unsupported Operation", uoe1);
        } catch (Exception ex) {
            log.error("Error Starting HTTPS Server", ex);
        }
    }
        
    public String toString() {
        return super.toString();
    }
}
