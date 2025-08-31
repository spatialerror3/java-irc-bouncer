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
public class JIBHTTPServletServers extends JIBHTTPServletBase {

    private static final Logger log = LogManager.getLogger(JIBHTTPServletServers.class);

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
            String server = req.getParameter("server");
            String port = req.getParameter("port");
            String ssl = req.getParameter("ssl");
            String ipv6 = req.getParameter("ipv6");
            String clientbind = req.getParameter("clientbind");
            String serverpass = req.getParameter("serverpass");
            String uinick = req.getParameter("uinick");
            String uiuser = req.getParameter("uiuser");
            String uirealname = req.getParameter("uirealname");
            String nickservuser = req.getParameter("nickservuser");
            String nickservpass = req.getParameter("nickservpass");
            String channels = req.getParameter("channels");

            PrintWriter out = resp.getWriter();
            out.println("<form action='/servers' method=POST><br>");
            out.println("server=<input type=text name='server' /><br>");
            out.println("port=<input type=text name='port' /><br>");
            out.println("ssl=<input type=checkbox name='ssl' /><br>");
            out.println("ipv6=<input type=checkbox name='ipv6' /><br>");
            out.println("clientbind=<input type=text name='clientbind' /><br>");
            out.println("serverpass=<input type=password name='serverpass' /><br>");
            out.println("nick=<input type=text name='uinick' /><br>");
            out.println("user=<input type=text name='uiuser' /><br>");
            out.println("realname=<input type=text name='uirealname' /><br>");
            out.println("nickservuser=<input type=text name='nickservuser' /><br>");
            out.println("nickservpass=<input type=password name='nickservpass' /><br>");
            out.println("channels=<input type=text name='channels' /><br>");
            out.println("<input type=hidden name='whattodo' value='addserver' /><br>");
            out.println("<input type=submit /></form>");
            if (session.getAttribute("IDENTIFIEDAS") != null) {
                UUID tmpUUID = UUID.fromString((String) session.getAttribute("IDENTIFIEDAS"));
                JIBUser u = JavaIrcBouncer.jibCore.getUser(tmpUUID);
                if (u != null) {
                    if (req.getParameter("whattodo") != null && req.getParameter("whattodo").equals("addserver")) {
                        JIBUserInfo ui = new JIBUserInfo();
                        ui.setNick(uinick);
                        ui.setUser(uiuser);
                        ui.setRealname(uirealname);
                        JIBIRCServer tmpServ = JIBIRCServer.createJIBIRCServer(server, Integer.valueOf(port), (ssl != null), (ipv6 != null), clientbind, serverpass, ui, nickservuser, nickservpass, channels);
                        u.addIrcServer(tmpServ);
                    }
                    if (req.getParameter("whattodo") != null && req.getParameter("whattodo").equals("delserver")) {
                        ArrayList<JIBIRCServer> sv = u.getIrcServers();
                        Iterator<JIBIRCServer> svi = sv.iterator();
                        while (svi.hasNext()) {
                            JIBIRCServer dss = svi.next();
                            if (dss.getUUID().toString().equals(req.getParameter("serveruuid"))) {
                                svi.remove();
                                JavaIrcBouncer.jibDbUtil.removeServer(u, dss);
                                out.println("DELETED SERVER " + req.getParameter("serveruuid") + "\r\n");
                            }
                        }
                    }

                    out.println("<table>");
                    ArrayList<JIBIRCServer> servers = u.getIrcServers();
                    Iterator<JIBIRCServer> it1 = servers.iterator();
                    while (it1.hasNext()) {
                        JIBIRCServer serv = it1.next();
                        out.println("<tr><td>");
                        out.println("<br>serv(" + serv.getUUID().toString() + ")=" + serv.toHTML());
                        out.println("<form action='/servers' method=POST>");
                        out.println("<input type=hidden name='serveruuid' value='" + serv.getUUID().toString() + "'>");
                        out.println("<input type=hidden name='whattodo' value='delserver'>");
                        out.println("<input type=submit value='[DELETE SERVER]' />");
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
