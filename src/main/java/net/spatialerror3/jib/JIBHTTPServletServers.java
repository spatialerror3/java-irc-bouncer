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

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.UUID;

/**
 *
 * @author spatialerror3
 */
public class JIBHTTPServletServers extends HttpServlet {

    private static final Logger log = LogManager.getLogger(JIBHTTPLoginServlet.class);

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var session = req.getSession(true);
        String path = req.getServletPath();
        log.debug(this + " " + path);

        try {
            String user = req.getParameter("user");
            String pass = req.getParameter("pass");

            PrintWriter out = resp.getWriter();
            resp.getWriter().write("<html>");
            resp.getWriter().write("<head>");
            resp.getWriter().write("<title>JIB</title>");
            resp.getWriter().write("</head>");
            resp.getWriter().write("<body>");
            resp.getWriter().write("<br>SESSION " + session.getId() + "<br>");
            resp.getWriter().write("<br>SESSIONIDENTIFIEDAS=" + session.getAttribute("IDENTIFIEDAS") + "<br>");
            resp.getWriter().write("<form action='/login' method=POST><br><input type=text name='user' /><br><input type=password name='pass' /><br><input type=submit /></form>");
            resp.getWriter().write("<form action='/servers' method=POST><br>");
            resp.getWriter().write("<input type=text name='server' /><br>");
            resp.getWriter().write("<input type=text name='port' /><br>");
            resp.getWriter().write("<input type=checkbox name='ssl' /><br>");
            resp.getWriter().write("<input type=checkbox name='ipv6' /><br>");
            resp.getWriter().write("<input type=password name='pass' /><br>");
            resp.getWriter().write("<input type=submit /></form>");
            if (session.getAttribute("IDENTIFIEDAS") != null) {
                UUID tmpUUID = UUID.fromString((String) session.getAttribute("IDENTIFIEDAS"));
                JIBUser u = JavaIrcBouncer.jibCore.getUser(tmpUUID);
                if (u != null) {
                    ArrayList<JIBIRCServer> servers = u.getIrcServers();
                    Iterator<JIBIRCServer> it1 = servers.iterator();
                    while(it1.hasNext()) {
                        JIBIRCServer serv = it1.next();
                        resp.getWriter().write("<br>serv("+serv.getUUID().toString()+")="+serv.toHTML()+"<br>");
                    }
                }
            }
            resp.getWriter().write("</body>");
            resp.getWriter().write("</html>");
            resp.getWriter().close();
        } catch (Exception e) {
            log.error((String) null, e);
        }

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }

    @Override
    protected void doHead(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }

}
