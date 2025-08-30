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
import java.util.UUID;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author spatialerror3
 */
public class JIBHTTPServletBase extends HttpServlet {

    private static final Logger log = LogManager.getLogger(JIBHTTPServletBase.class);

    public void header(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var session = req.getSession(true);
        String path = req.getServletPath();
        log.debug(this + " " + path);

        PrintWriter out = resp.getWriter();
        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<title>JIB</title>");
        out.println("</head>");
        out.println("<body>");
        out.println("<table width='100%'>");
        out.println("<tr><td>");
        out.println("<br>JIB<br>");
        //out.println("<br>SESSION " + session.getId() + "<br>");
        //out.println("<br>SESSIONIDENTIFIEDAS=" + session.getAttribute("IDENTIFIEDAS") + "<br>");
        UUID tmpUUID = UUID.fromString((String) session.getAttribute("IDENTIFIEDAS"));
        JIBUser u = JavaIrcBouncer.jibCore.getUser(tmpUUID);
        out.println("<br>USER=" + (u != null ? u.getUserName() : "null"));
        out.println("</td><td>");
        out.println("<form action='/login' method=POST><br><input type=text name='user' /><br><input type=password name='pass' /><br><input type=submit /></form>");
        out.println("</td><td>");
        out.println("<form action='/logout' method=POST><br><input type=submit value='LOGOUT' /></form>");
        out.println("</td></tr>");
        out.println("</table>");
    }

    public void footer(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var session = req.getSession(true);
        String path = req.getServletPath();
        log.debug(this + " " + path);

        PrintWriter out = resp.getWriter();
        out.println("</body>");
        out.println("</html>");
    }
}
