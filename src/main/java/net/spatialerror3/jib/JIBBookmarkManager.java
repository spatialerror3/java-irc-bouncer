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

import java.sql.Connection;
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

    public void removeBookmark(JIBBookmark b) {
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
        return loadedBookmarks;
    }
}
