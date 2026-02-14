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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author spatialerror3
 */
public class JIBBookmarkManager {

    private ArrayList<JIBBookmark> bookmarks = null;

    public JIBBookmarkManager() {
        bookmarks = new ArrayList<>();
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

    public void addBookmark(JIBBookmark b) {
        if (findUrl(b.getUser(), b.getUrl()) == null) {
            bookmarks.add(b);
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
}
