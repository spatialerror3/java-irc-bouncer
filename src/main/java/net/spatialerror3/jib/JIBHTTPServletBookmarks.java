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
import java.util.Iterator;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author spatialerror3
 */
public class JIBHTTPServletBookmarks extends JIBHTTPServletBase {

    private static final Logger log = LogManager.getLogger(JIBHTTPServletBookmarks.class);

    public void listBookmarks(JIBUser u, HttpServletResponse resp) {
        List<JIBBookmark> bms = JavaIrcBouncer.jibBookmarkManager.getBookmarks();
        Iterator<JIBBookmark> it1 = bms.iterator();
        try {
            while (it1.hasNext()) {
                JIBBookmark nbm = it1.next();
                if (nbm.getUser().getUUID().toString().equals(u.getUUID().toString())) {
                    resp.getWriter().println("bookmark[" + nbm.getUuid().toString() + "]= (" + nbm.getTitle() + ") <a href='" + nbm.getUrl() + "'>" + nbm.getUrl() + "</a><br/>");
                }
            }
        } catch (IOException ex) {
            System.getLogger(JIBHTTPServletBookmarks.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }

    public void addBookmark(JIBUser u, String title, String url) {
        JIBBookmark newBookmark = new JIBBookmark();

        newBookmark.setUser(u);
        newBookmark.setTitle(title);
        newBookmark.setUrl(url);

        JavaIrcBouncer.jibBookmarkManager.addBookmark(newBookmark);
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
                listBookmarks(u, resp);
            }
        } catch (Exception ex1) {
            log.error((String) null, ex1);
        }
        resp.getWriter().print("<form method='GET'><input type='hidden' name='whattodo' value='addbm'/><input name='bmurl'/></form>");
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
