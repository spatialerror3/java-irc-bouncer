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

import java.net.MalformedURLException;
import java.net.URI;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author spatialerror3
 */
public class JIBBookmarkManager {

    private static final Logger log = LogManager.getLogger(JIBBookmarkManager.class);
    private ArrayList<JIBBookmark> bookmarks = null;
    private UUID bmApiKey = UUID.randomUUID();

    public JIBBookmarkManager() {
        bookmarks = new ArrayList<>();
        log.error(this + " BMAPIKEY=" + this.bmApiKey);
    }

    public JIBBookmark findUrl(JIBUser u, String url) {
        Iterator<JIBBookmark> it3 = getBookmarksForJIBUser(u).iterator();
        while (it3.hasNext()) {
            JIBBookmark tmp = it3.next();
            if (tmp.getUrl().equals(url)) {
                return tmp;
            }
        }
        return null;
    }

    public JIBBookmark findBmUuid(JIBUser u, String uuid) {
        Iterator<JIBBookmark> it3 = getBookmarksForJIBUser(u).iterator();
        while (it3.hasNext()) {
            JIBBookmark tmp = it3.next();
            if (tmp.getUuid().toString().equals(uuid)) {
                return tmp;
            }
        }
        return null;
    }

    public JIBBookmark findBmCat(JIBUser u, String category) {
        Iterator<JIBBookmark> it3 = getBookmarksForJIBUser(u).iterator();
        while (it3.hasNext()) {
            JIBBookmark tmp = it3.next();
            if (tmp.getCategory().equals(category)) {
                return tmp;
            }
        }
        return null;
    }

    public JIBBookmark findBmFolder(JIBUser u, String folder) {
        Iterator<JIBBookmark> it3 = getBookmarksForJIBUser(u).iterator();
        while (it3.hasNext()) {
            JIBBookmark tmp = it3.next();
            if (tmp.getFolder().equals(folder)) {
                return tmp;
            }
        }
        return null;
    }

    public void addBookmark(JIBBookmark b) {
        if (findUrl(b.getUser(), b.getUrl()) == null) {
            b.setAddDate(Instant.now().getEpochSecond());
            bookmarks.add(b);
            b.sqlInsert();
        }
    }

    public void removeBookmark(JIBBookmark b, boolean internal) {
        bookmarks.remove(b);
    }

    public List<JIBBookmark> getBookmarks() {
        return Collections.synchronizedList(bookmarks);
    }

    public List<JIBBookmark> getBookmarksForJIBUser(JIBUser u) {
        ArrayList<JIBBookmark> ubms = new ArrayList<>();
        Iterator<JIBBookmark> it2 = getBookmarks().iterator();
        while (it2.hasNext()) {
            JIBBookmark bm2 = it2.next();
            if (bm2.getUser().equals(u)) {
                ubms.add(bm2);
            }
        }
        return ubms;
    }

    /**
     * @return the bmApiKey
     */
    public UUID getBmApiKey() {
        return bmApiKey;
    }

    /**
     * @param bmApiKey the bmApiKey to set
     */
    public void setBmApiKey(UUID bmApiKey) {
        this.bmApiKey = bmApiKey;
    }

    public long loadBookmarks() {
        Connection zconn = null;
        long loadedBookmarks = 0L;
        String sql = "SELECT userUuid,_uuid,title,url,memo,add_date,last_modified,icon_uri,onto,category,browser,folder FROM bookmarks;";
        PreparedStatement ps25 = null;
        ResultSet rs25 = null;
        try {
            zconn = JavaIrcBouncer.jibDbUtil.getDatabase();
            ps25 = zconn.prepareStatement(sql);
            rs25 = ps25.executeQuery();
        } catch (SQLException ex) {
            log.error((String) null, ex);
        }
        try {
            while (rs25 != null && rs25.next()) {
                JIBBookmark tmpbm = new JIBBookmark();
                tmpbm.setUser(JavaIrcBouncer.jibCore.getUser(UUID.fromString(rs25.getString(1))));
                tmpbm.setUuid(UUID.fromString(rs25.getString(2)));
                tmpbm.setTitle(rs25.getString(3));
                tmpbm.setUrl(rs25.getString(4));
                tmpbm.setMemo(rs25.getString(5));
                tmpbm.setAddDate(rs25.getLong(6));
                tmpbm.setLastModified(rs25.getLong(7));
                tmpbm.setIconUri(URI.create(rs25.getString(8)).toURL());
                tmpbm.setOnto(rs25.getString(9));
                tmpbm.setCategory(rs25.getString(10));
                tmpbm.setBrowser(rs25.getString(11));
                tmpbm.setFolder(rs25.getString(12));

                bookmarks.add(tmpbm);
                loadedBookmarks++;
            }
        } catch (MalformedURLException mue1) {
            log.error((String) null, mue1);
        } catch (SQLException ex) {
            log.error((String) null, ex);
        }
        JavaIrcBouncer.jibDbUtil.finishDbConn(zconn);
        return loadedBookmarks;
    }

    public void removeBookmark(JIBBookmark bm) {
        Connection zconn = null;
        String sql = "DELETE FROM bookmarks WHERE _uuid = ?;";
        PreparedStatement ps26 = null;
        try {
            zconn = JavaIrcBouncer.jibDbUtil.getDatabase();
            ps26 = zconn.prepareStatement(sql);
            ps26.setString(1, bm.getUuid().toString());
            ps26.execute();
            zconn.commit();
        } catch (SQLException ex) {
            log.error((String) null, ex);
        }
        if (zconn != null) {
            if (JavaIrcBouncer.jibDbUtil.altDbTypeMariadb()) {
                try {
                    zconn.close();
                } catch (SQLException ex) {
                    log.error(ex);
                }
            }
        }
        removeBookmark(bm, true);
    }
}
