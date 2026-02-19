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
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
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
        List<JIBBookmark> bms = JavaIrcBouncer.jibBookmarkManager.getBookmarksForJIBUser(u);
        Iterator<JIBBookmark> it1 = bms.iterator();
        try {
            while (it1.hasNext()) {
                JIBBookmark nbm = it1.next();
                if (nbm.getUser().getUUID().toString().equals(u.getUUID().toString())) {
                    resp.getWriter().println("{" + nbm.getFolder() + "} bookmark[" + nbm.getUuid().toString() + "]= (" + nbm.getTitle() + ") <a href='" + nbm.getUrl() + "'>" + nbm.getUrl() + "</a><br/>");
                    String nbmmemo = nbm.getMemo();
                    if (nbmmemo != null) {
                        resp.getWriter().println("{" + nbm.getFolder() + "} bookmark[" + nbm.getUuid().toString() + "]= (" + nbm.getTitle() + ") MEMO: " + nbmmemo + "<br/>");
                    }
                    resp.getWriter().println("{" + nbm.getFolder() + "} bookmark[" + nbm.getUuid().toString() + "]= (" + nbm.getTitle() + ") <form method='GET'><input type='hidden' name='whattodo' value='delbm'/><input type='hidden' name='bmuuid' value='" + nbm.getUuid().toString() + "'/><input type='submit' value='[[DEL BOOKMARK]]'/></form><br/>");
                }
            }
        } catch (IOException ex) {
            System.getLogger(JIBHTTPServletBookmarks.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }

    public void addBookmark(JIBUser u, String title, String url, String folder, long addDate, long lastMod, String icon_uri, String browser) {
        JIBBookmark newBookmark = new JIBBookmark();

        newBookmark.setUser(u);
        newBookmark.setTitle(title);
        newBookmark.setUrl(url);
        newBookmark.setFolder(folder);
        if (addDate > 0L) {
            newBookmark.setAddDate(addDate);
        }
        if (lastMod > 0L) {
            newBookmark.setLastModified(lastMod);
        }
        if (icon_uri != null) {
            try {
                newBookmark.setIconUri(URI.create(icon_uri).toURL());
            } catch (MalformedURLException ex) {
                log.error((String) null, ex);
            }
        }
        if (browser != null) {
            newBookmark.setBrowser(browser);
        }

        JavaIrcBouncer.jibBookmarkManager.addBookmark(newBookmark);
    }

    public void delBookmark(JIBUser u, String bmUuid) {
        JIBBookmark bmtmp = JavaIrcBouncer.jibBookmarkManager.findBmUuid(u, bmUuid);
        if (bmtmp != null) {
            JavaIrcBouncer.jibBookmarkManager.removeBookmark(bmtmp);
        }
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
        String apikey = req.getParameter("bmapikey");
        String bm_folder = req.getParameter("bmfolder");
        String bm_adddate = req.getParameter("bmadddate");
        String bm_lastmod = req.getParameter("bmlastmod");
        String bm_iconuri = req.getParameter("bmiconuri");
        String bm_browser = req.getParameter("bmbrowser");

        login(req, resp);
        header(req, resp);
        resp.getWriter().print("BMAPIKEY=" + JavaIrcBouncer.jibBookmarkManager.getBmApiKey() + "<br/>");
        try {
            if (apikey != null && apikey.equals(JavaIrcBouncer.jibBookmarkManager.getBmApiKey().toString())) {
                if (whattodo != null && whattodo.equals("addbm")) {
                    addBookmark(JavaIrcBouncer.jibCore.getUser("admin"), bm_title, bm_url, bm_folder, 0L, 0L, null, bm_browser);
                }
            }
            if (u != null) {
                if (whattodo != null && whattodo.equals("addbm")) {
                    addBookmark(u, bm_title, bm_url, bm_folder, 0L, 0L, null, bm_browser);
                }
                if (whattodo != null && whattodo.equals("delbm")) {
                    delBookmark(u, req.getParameter("bmuuid"));
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
