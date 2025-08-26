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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author spatialerror3
 */
public class JIBHTTPServletLogin extends JIBHTTPServletBase {

    private static final Logger log = LogManager.getLogger(JIBHTTPServletLogin.class);

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var session = req.getSession(true);
        String path = req.getServletPath();
        log.debug(this + " " + path);

        header(req, resp);
        try {
            String user = req.getParameter("user");
            String pass = req.getParameter("pass");

            PrintWriter out = resp.getWriter();
            if (user != null) {
                resp.getWriter().write("<br>user=" + user);
            }
            if (pass != null && user != null && pass.length() > 0 && user.length() > 0) {
                JIBUser u = null;
                if ((u = JavaIrcBouncer.jibCore.authUser(user, pass)) != null) {
                    resp.getWriter().write("<br>IDENTIFIED AS " + u.getUUID().toString());
                    session.setAttribute("IDENTIFIEDAS", u.getUUID().toString());
                    resp.getWriter().write("<br>userMaxId=" + JavaIrcBouncer.jibDbUtil.getUsersMaxUserId());
                    resp.getWriter().write("<br>userCount=" + JavaIrcBouncer.jibCore.getUserCount());
                    out.println("<br><a href='/servers'>servers</a><br>");
                } else {
                    log.error("JIBHTTP Web Auth failed for user=" + user);
                    resp.getWriter().write("<br>AUTH FAILED");
                }
            }
        } catch (Exception e) {
            log.error((String) null, e);
        }
        footer(req, resp);
        resp.getWriter().close();
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
