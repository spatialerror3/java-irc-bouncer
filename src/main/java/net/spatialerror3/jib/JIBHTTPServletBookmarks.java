/*
 * Copyright (C) 2026 spatialerror3
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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author spatialerror3
 */
public class JIBHTTPServletBookmarks extends JIBHTTPServletBase {

    private static final Logger log = LogManager.getLogger(JIBHTTPServletBookmarks.class);

    public void listBookmarks(JIBUser u) {

    }

    public void addBookmark(JIBUser u, String title, String url) {

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var session = req.getSession(true);
        String path = req.getServletPath();
        log.debug(this + " " + path);
        JIBUser u = getJIBUser(req, resp);
        String whattodo = req.getParameter("whattodo");
        String bm_title = req.getParameter("bmtitle");
        String bm_url = req.getParameter("bmurl");

        login(req, resp);
        header(req, resp);
        try {
            if (u != null) {
                if (whattodo != null && whattodo.equals("addbm")) {
                    addBookmark(u, bm_title, bm_url);
                }
                listBookmarks(u);
            }
        } catch (Exception ex1) {
            log.error((String) null, ex1);
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
