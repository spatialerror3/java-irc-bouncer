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
import java.time.Instant;
import java.util.UUID;

/**
 *
 * @author spatialerror3
 */
public class JIBBookmark {

    private JIBUser u = null; // sqltype varchar(256) toString()
    //
    private UUID uuid = null; // sqltype UUID
    private String title = null; // sqltype varchar(1024)
    private String url = null; // sqltype varchar(256)
    private String memo = null; // sqltype text
    private long add_date = 0L; // sqltype bigint
    private long last_modified = 0L;  // sqltype bigint
    private URL icon_uri = null; // sqltype varchar(256)
    private String onto = null; // sqltype text
    private String category = null; // sqltype text
    private String browser = null; // sqltype varchar(1024)
    private String folder = null; // sqltype text

    public JIBBookmark() {
        uuid = UUID.randomUUID();
    }
    
    public String sqlCreateTable() {
        String sql = "CREATE TABLE IF NOT EXISTS bookmarks (id int auto_increment primary key, userUuid varchar(256), _uuid uuid, title varchar(1024), url varchar(256), memo text, add_date bigint, last_modified bigint, icon_uri varchar(256), onto text, category text, browser varchar(1024), folder text);";
        return sql;
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
        this.setLastModified(Instant.now().getEpochSecond());
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
        this.setLastModified(Instant.now().getEpochSecond());
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
        this.setLastModified(Instant.now().getEpochSecond());
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
        this.setLastModified(Instant.now().getEpochSecond());
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
        this.setLastModified(Instant.now().getEpochSecond());
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
        this.setLastModified(Instant.now().getEpochSecond());
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
        this.setLastModified(Instant.now().getEpochSecond());
        this.category = category;
    }

    public void appendCategory(String category) {
        this.category = this.category + "/" + category;
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
        this.setLastModified(Instant.now().getEpochSecond());
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
        this.setLastModified(Instant.now().getEpochSecond());
        this.folder = folder;
    }

    public void appendFolder(String folder) {
        this.folder = this.folder + "/" + folder;
    }
}
