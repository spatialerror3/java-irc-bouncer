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
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.session.DefaultSessionIdManager;
import org.eclipse.jetty.session.HouseKeeper;
import org.eclipse.jetty.util.Callback;
import org.eclipse.jetty.util.resource.ResourceFactory;

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

        DefaultSessionIdManager idMgr = new DefaultSessionIdManager(server);
        idMgr.setWorkerName("server1");
        server.addBean(idMgr, true);
        //try {
        //    idMgr.start();
        //} catch (Exception e3) {
        //    log.error((String) null, e3);
        //}

        try {
            HouseKeeper houseKeeper = new HouseKeeper();
            houseKeeper.setSessionIdManager(idMgr);
            //set the frequency of scavenge cycles
            houseKeeper.setIntervalSec(600L);
            idMgr.setSessionHouseKeeper(houseKeeper);
            //server.addBean(houseKeeper, true);
        } catch (IllegalStateException ise2) {
            log.debug("HouseKeeper IllegalStateException ise2", ise2);
        } catch (Exception e2) {
            log.error((String) null, e2);
        }

        int acceptors = 4;
        int selectors = 4;
        ServerConnector connector = new ServerConnector(server, acceptors, selectors, new HttpConnectionFactory());
        connector.setPort(this.Port);
        connector.setHost("0.0.0.0");
        connector.setAcceptQueueSize(128);
        server.addConnector(connector);
        
        ServletContextHandler handler = new ServletContextHandler("/", true, true);
        URL staticResources = getClass().getClassLoader().getResource("static");
        handler.setBaseResourceAsString(staticResources.toExternalForm());

        //handler.addServlet(JIBHTTPServletLogin.class.getName(), "/");
        handler.addServlet(JIBHTTPServletLogin.class.getName(), "/login");
        handler.addServlet(JIBHTTPServletLogin.class.getName(), "/login/");
        handler.addServlet(JIBHTTPServletServers.class.getName(), "/servers");
        handler.addServlet(JIBHTTPServletServers.class.getName(), "/servers/");
        handler.addServlet(JIBHTTPServletUsers.class.getName(), "/users");
        handler.addServlet(JIBHTTPServletUsers.class.getName(), "/users/");
        handler.addServlet(JIBHTTPServletStatus.class.getName(), "/status");
        handler.addServlet(JIBHTTPServletStatus.class.getName(), "/status/");
        handler.addServlet(JIBHTTPServletLogout.class.getName(), "/logout");
        handler.addServlet(JIBHTTPServletLogout.class.getName(), "/logout/");
        
        final ServletHolder defaultHolder = new ServletHolder("default", ResourceServlet.class);
        defaultHolder.setInitParameter("baseResource", staticResources.toExternalForm());
        handler.addServlet(defaultHolder, "/static/*");
        handler.addServlet(defaultHolder, "/static/JIB.png");
        
        ResourceHandler handler3 = new ResourceHandler();
        handler3.setBaseResource(ResourceFactory.of(handler3).newResource("./static/"));
        handler3.setAcceptRanges(true);
        //server.setDefaultHandler(handler3);
                
        ContextHandlerCollection contextCollection = new ContextHandlerCollection();
        contextCollection.addHandler(new ContextHandler(handler3, "/static"));
        contextCollection.addHandler(new ContextHandler(handler, "/login"));
        contextCollection.addHandler(new ContextHandler(handler, "/login/"));
        contextCollection.addHandler(new ContextHandler(handler, "/servers"));
        contextCollection.addHandler(new ContextHandler(handler, "/servers/"));
        contextCollection.addHandler(new ContextHandler(handler, "/users"));
        contextCollection.addHandler(new ContextHandler(handler, "/users/"));
        contextCollection.addHandler(new ContextHandler(handler, "/status"));
        contextCollection.addHandler(new ContextHandler(handler, "/status/"));
        contextCollection.addHandler(new ContextHandler(handler, "/logout"));
        contextCollection.addHandler(new ContextHandler(handler, "/logout/"));
        server.setHandler(contextCollection);
        server.setDefaultHandler(contextCollection);
        contextCollection.deployHandler(handler, Callback.NOOP);
        
        //server.setDefaultHandler(new JIBHTTPHandler());
        //server.setDefaultHandler(handler);
        try {
            server.start();
        } catch (UnsupportedOperationException uoe1) {
            log.debug("Unsupported Operation", uoe1);
        } catch (Exception ex) {
            log.error("Error Starting HTTP Server", ex);
        }
    }
}
