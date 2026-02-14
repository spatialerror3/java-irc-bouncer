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
import org.eclipse.jetty.ee10.servlet.ResourceServlet;
import org.eclipse.jetty.ee10.servlet.ServletContextHandler;
import org.eclipse.jetty.ee10.servlet.ServletHolder;
import org.eclipse.jetty.ee10.servlet.security.ConstraintMapping;
import org.eclipse.jetty.ee10.servlet.security.ConstraintSecurityHandler;
import org.eclipse.jetty.security.Constraint;
import org.eclipse.jetty.security.HashLoginService;
import org.eclipse.jetty.security.SecurityHandler;
import org.eclipse.jetty.security.authentication.BasicAuthenticator;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.session.DefaultSessionIdManager;
import org.eclipse.jetty.session.HouseKeeper;
import org.eclipse.jetty.util.Callback;
import org.eclipse.jetty.util.resource.ResourceFactory;
import org.eclipse.jetty.util.security.Credential;

/**
 *
 * @author spatialerror3
 */
public class JIBWebBase {

    private static final Logger log = LogManager.getLogger(JIBWebBase.class);
    private int Port = -1;
    private String protocol = null;
    //
    private Server server = null;
    private String serverName = null;
    //
    private String realm = "JIB";
    private SecurityHandler sh = null;

    public JIBWebBase() {

    }

    public void setPort(int Port, String protocol) {
        this.Port = Port;
        this.protocol = protocol;
    }

    public int getPort() {
        return this.Port;
    }

    public void setServer(Server server, String serverName) {
        this.server = server;
        this.serverName = serverName;
    }

    public Server getServer() {
        return this.server;
    }

    public void addSessionIdManager() {
        DefaultSessionIdManager idMgr = new DefaultSessionIdManager(server);
        idMgr.setWorkerName(this.serverName);
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
    }

    public void addSecurityHandler() {
        HashLoginService loginService = new HashLoginService();
        loginService.setName(realm);
        sh = new ConstraintSecurityHandler();
        sh.setServer(server);
        sh.setAuthenticator(new BasicAuthenticator());
        sh.setRealmName(realm);
        sh.setLoginService(loginService);
    }

    public void addHandlers() {
        ServletContextHandler handler = new ServletContextHandler("/", true, true);
        URL staticResources = getClass().getClassLoader().getResource("static");
        handler.setBaseResourceAsString(staticResources.toExternalForm());
        //handler.setSessionHandler(sessionHandler);
        //handler.addServlet(JIBHTTPServletLogin.class.getName(), "/");
        handler.addServlet(JIBHTTPServletLogin.class.getName(), "/login");
        //handler.addServlet(JIBHTTPServletLogin.class.getName(), "/login/");
        handler.addServlet(JIBHTTPServletServers.class.getName(), "/servers");
        //handler.addServlet(JIBHTTPServletServers.class.getName(), "/servers/");
        handler.addServlet(JIBHTTPServletUsers.class.getName(), "/users");
        //handler.addServlet(JIBHTTPServletUsers.class.getName(), "/users/");
        handler.addServlet(JIBHTTPServletStatus.class.getName(), "/status");
        //handler.addServlet(JIBHTTPServletStatus.class.getName(), "/status/");
        handler.addServlet(JIBHTTPServletBookmarks.class.getName(), "/bookmarks");
        handler.addServlet(JIBHTTPServletBookmarks.class.getName(), "/bookmark");
        handler.addServlet(JIBHTTPServletLogout.class.getName(), "/logout");
        //handler.addServlet(JIBHTTPServletLogout.class.getName(), "/logout/");

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
        //contextCollection.addHandler(new ContextHandler(handler, "/"));
        contextCollection.addHandler(new ContextHandler(handler, "/login"));
        //contextCollection.addHandler(new ContextHandler(handler, "/login/"));
        contextCollection.addHandler(new ContextHandler(handler, "/servers"));
        //contextCollection.addHandler(new ContextHandler(handler, "/servers/"));
        contextCollection.addHandler(new ContextHandler(handler, "/users"));
        //contextCollection.addHandler(new ContextHandler(handler, "/users/"));
        contextCollection.addHandler(new ContextHandler(handler, "/status"));
        //contextCollection.addHandler(new ContextHandler(handler, "/status/"));
        contextCollection.addHandler(new ContextHandler(handler, "/bookmarks"));
        contextCollection.addHandler(new ContextHandler(handler, "/bookmark"));
        contextCollection.addHandler(new ContextHandler(handler, "/logout"));
        //contextCollection.addHandler(new ContextHandler(handler, "/logout/"));
        server.setHandler(contextCollection);
        server.setDefaultHandler(contextCollection);
        if (sh != null) {
            sh.setHandler(contextCollection);
            server.setHandler(sh);
        }
        contextCollection.deployHandler(handler, Callback.NOOP);

        //server.setErrorHandler(new JIBHTTPHandlerError());
        //server.setDefaultHandler(new JIBHTTPHandler());
        server.setDefaultHandler(handler);
    }

    public String getURL() {
        return this.protocol + "://127.0.0.1:" + this.Port + "/login";
    }

    public String toString() {
        return "Enter " + getURL() + " into your web browser to login...";
    }
}
