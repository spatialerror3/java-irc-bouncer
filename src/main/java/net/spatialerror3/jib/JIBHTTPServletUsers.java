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
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.UUID;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author spatialerror3
 */
public class JIBHTTPServletUsers extends JIBHTTPServletBase {

    private static final Logger log = LogManager.getLogger(JIBHTTPServletUsers.class);

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var session = req.getSession(true);
        String path = req.getServletPath();
        log.debug(this + " " + path);

        login(req, resp);
        header(req, resp);
        try {
            String user = req.getParameter("user");
            String pass = req.getParameter("pass");
            //
            String username = req.getParameter("username");
            String userpass = req.getParameter("userpass");

            PrintWriter out = resp.getWriter();
            out.println("<form action='/users' method=POST><br>");
            out.println("username=<input type=text name='username' /><br>");
            out.println("password=<input type=password name='userpass' /><br>");
            out.println("<input type=hidden name='whattodo' value='adduser' /><br>");
            out.println("<input type=submit /></form>");
            if (session.getAttribute("IDENTIFIEDAS") != null) {
                UUID tmpUUID = UUID.fromString((String) session.getAttribute("IDENTIFIEDAS"));
                JIBUser u = JavaIrcBouncer.jibCore.getUser(tmpUUID);
                if (u != null && u.admin()) {
                    if (req.getParameter("whattodo") != null && req.getParameter("whattodo").equals("adduser")) {
                        JIBUser tmpUser = JavaIrcBouncer.jibCore.createUser(username, false);
                        tmpUser.setAuthToken(userpass);
                        out.println("CREATED USER " + tmpUser.getUUID().toString() + "\r\n");
                    }
                    if (req.getParameter("whattodo") != null && req.getParameter("whattodo").equals("deluser")) {
                        JIBUser du = JavaIrcBouncer.jibCore.getUser(req.getParameter("useruuid"));
                        if (du != null && du.getUUID().toString().equals(req.getParameter("useruuid"))) {
                            JavaIrcBouncer.jibCore.removeUser(du);
                            out.println("DELETED USER " + req.getParameter("useruuid") + "\r\n");
                        }
                    }

                    out.println("<table>");
                    Iterator<JIBUser> it1 = JavaIrcBouncer.jibCore.getUsers();
                    while (it1.hasNext()) {
                        JIBUser it1user = it1.next();
                        out.println("<tr><td>");
                        out.println("<br>user(" + it1user.getUUID().toString() + ")=" + it1user.toHTML());
                        out.println("<form action='/users' method=POST>");
                        out.println("<input type=hidden name='useruuid' value='" + it1user.getUUID().toString() + "'>");
                        out.println("<input type=hidden name='whattodo' value='deluser'>");
                        out.println("<input type=submit value='[DELETE USER]' />");
                        out.println("<br>");
                        out.println("</form>");
                        out.println("</td></tr>");
                    }
                    out.println("</table>");
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
