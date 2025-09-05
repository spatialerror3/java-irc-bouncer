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
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.UUID;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author spatialerror3
 */
public class JIBHTTPServletBase extends HttpServlet {

    private static final Logger log = LogManager.getLogger(JIBHTTPServletBase.class);
    private static final String backlink = "https://java-irc-bouncer.sourceforge.io";
    private static final String ghlink = "https://github.com/spatialerror3/java-irc-bouncer";
    
    public JIBUser getJIBUser(HttpServletRequest req, HttpServletResponse resp) {
        JIBUser u = null;
        HttpSession session = req.getSession(true);

        if ((String) session.getAttribute("IDENTIFIEDAS") != null) {
            UUID tmpUUID = UUID.fromString((String) session.getAttribute("IDENTIFIEDAS"));
            u = JavaIrcBouncer.jibCore.getUser(tmpUUID);
        }

        return u;
    }
    
    public void login(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var session = req.getSession(true);
        String path = req.getServletPath();
        log.debug(this + " " + path);

        try {
            String user = req.getParameter("user");
            String pass = req.getParameter("pass");

            PrintWriter out = resp.getWriter();
            if (pass != null && user != null && pass.length() > 0 && user.length() > 0) {
                JIBUser u = null;
                if ((u = JavaIrcBouncer.jibCore.authUser(user, pass)) != null) {
                    session.setAttribute("IDENTIFIEDAS", u.getUUID().toString());
                } else {
                    log.error("JIBHTTP Web Auth failed for user=" + user);
                    //out.println("<br>AUTH FAILED");
                }
            }
        } catch (Exception e) {
            log.error((String) null, e);
        }
    }

    public void header(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var session = req.getSession(true);
        String path = req.getServletPath();
        log.debug(this + " " + path);
        JIBUser u = null;

        PrintWriter out = resp.getWriter();
        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<title>JIB</title>");
        out.println("</head>");
        out.println("<body>");
        out.println("<table width='100%'>");
        out.println("<tr><td>");
        out.println("<br><img src='/static/JIB.png' alt='JIB'><br>");
        //out.println("<br>SESSION " + session.getId() + "<br>");
        //out.println("<br>SESSIONIDENTIFIEDAS=" + session.getAttribute("IDENTIFIEDAS") + "<br>");
        if ((String) session.getAttribute("IDENTIFIEDAS") != null) {
            UUID tmpUUID = UUID.fromString((String) session.getAttribute("IDENTIFIEDAS"));
            u = JavaIrcBouncer.jibCore.getUser(tmpUUID);
        }
        out.println("<br>USER=" + (u != null ? u.getUserName() : "null"));
        out.println("</td><td>");
        if ((String) session.getAttribute("IDENTIFIEDAS") == null) {
            out.println("<form action='/login' method=POST><br><input type=text name='user' /><br><input type=password name='pass' /><br><input type=submit value='LOGIN' /></form>");
        }
        out.println("</td><td>");
        if ((String) session.getAttribute("IDENTIFIEDAS") != null) {
            out.println("<form action='/logout' method=POST><br><input type=submit value='LOGOUT' /></form>");
        }
        out.println("</td></tr>");
        out.println("</table>");
        if (u != null) {
            // NAVIGATION
            out.println("<table>");
            out.println("<tr>");
            out.println("<td><a href='/'>home</a>&nbsp;");
            out.println("<a href='/login'>login</a>&nbsp;");
            out.println("<a href='/servers'>servers</a>&nbsp;");
            out.println("<a href='/status'>status</a>&nbsp;");
            if (u.admin()) {
                out.println("<a href='/users'>users</a>&nbsp;");
            }
            out.println("<a href='/logout'>logout</a>&nbsp;");
            out.println("</td>");
            out.println("</tr>");
            out.println("</table>");
        }
    }

    public void footer(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var session = req.getSession(true);
        String path = req.getServletPath();
        log.debug(this + " " + path);

        PrintWriter out = resp.getWriter();
        out.println("<hr width=90%>");
        out.println("<table width=100%><tr><td align=center><a href='" + backlink + "'>(c) 2025 JIB</a> (<a href='" + ghlink + "'>github</a>)</td></tr></table>");
        out.println("</body>");
        out.println("</html>");
    }
}
