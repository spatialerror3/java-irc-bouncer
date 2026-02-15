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

import java.net.URL;
import java.util.UUID;

/**
 *
 * @author spatialerror3
 */
public class JIBBookmark {

    private JIBUser u = null;
    //
    private UUID uuid = null;
    private String title = null;
    private String url = null;
    private String memo = null;
    private long add_date = 0L;
    private long last_modified = 0L;
    private URL icon_uri = null;
    private String onto = null;
    private String category = null;
    private String browser = null;
    private String folder = null;
    
    public JIBBookmark() {
        uuid = UUID.randomUUID();
    }

    /**
     * @return the uuid
     */
    public UUID getUuid() {
        return uuid;
    }

    /**
     * @param uuid the uuid to set
     */
    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the url
     */
    public String getUrl() {
        return url;
    }

    /**
     * @param url the url to set
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * @return the memo
     */
    public String getMemo() {
        return memo;
    }

    /**
     * @param memo the memo to set
     */
    public void setMemo(String memo) {
        this.memo = memo;
    }

    /**
     * @return the u
     */
    public JIBUser getUser() {
        return u;
    }

    /**
     * @param u the u to set
     */
    public void setUser(JIBUser u) {
        this.u = u;
    }

    /**
     * @return the add_date
     */
    public long getAddDate() {
        return add_date;
    }

    /**
     * @param add_date the add_date to set
     */
    public void setAddDate(long add_date) {
        this.add_date = add_date;
    }

    /**
     * @return the last_modified
     */
    public long getLastModified() {
        return last_modified;
    }

    /**
     * @param last_modified the last_modified to set
     */
    public void setLastModified(long last_modified) {
        this.last_modified = last_modified;
    }

    /**
     * @return the icon_uri
     */
    public URL getIconUri() {
        return icon_uri;
    }

    /**
     * @param icon_uri the icon_uri to set
     */
    public void setIconUri(URL icon_uri) {
        this.icon_uri = icon_uri;
    }

    /**
     * @return the onto
     */
    public String getOnto() {
        return onto;
    }

    /**
     * @param onto the onto to set
     */
    public void setOnto(String onto) {
        this.onto = onto;
    }

    /**
     * @return the category
     */
    public String getCategory() {
        return category;
    }

    /**
     * @param category the category to set
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * @return the browser
     */
    public String getBrowser() {
        return browser;
    }

    /**
     * @param browser the browser to set
     */
    public void setBrowser(String browser) {
        this.browser = browser;
    }
    
    /**
     * @return the folder
     */
    public String getFolder() {
        return folder;
    }

    /**
     * @param folder the folder to set
     */
    public void setFolder(String folder) {
        this.folder = folder;
    }
}
