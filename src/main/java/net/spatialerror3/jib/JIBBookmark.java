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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.Instant;
import java.util.UUID;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author spatialerror3
 */
public class JIBBookmark {

    private static final Logger log = LogManager.getLogger(JIBBookmark.class);

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

    public String sqlCreateTable(String altDbType) {
        String sql = "CREATE TABLE IF NOT EXISTS bookmarks (id int auto_increment primary key, userUuid varchar(256), _uuid uuid, title varchar(1024), url varchar(256), memo text, add_date bigint, last_modified bigint, icon_uri varchar(256), onto text, category text, browser varchar(1024), folder text);";
        return sql;
    }

    public void sqlInsert() {
        Connection zconn = null;
        String sql = "INSERT INTO bookmarks (userUuid,_uuid,title,url,memo,add_date,last_modified,icon_uri,onto,category,browser,folder) VALUES(?,?,?,?,?,?,?,?,?,?,?,?);";
        PreparedStatement ps10 = null;
        try {
            zconn = JavaIrcBouncer.jibDbUtil.getDatabase();
            ps10 = zconn.prepareStatement(sql);
            ps10.setString(1, u.getUUID().toString());
            ps10.setString(2, uuid.toString());
            ps10.setString(3, title);
            ps10.setString(4, url);
            if (memo != null) {
                ps10.setString(5, memo);
            } else {
                ps10.setNull(5, java.sql.Types.NULL);
            }
            ps10.setLong(6, add_date);
            ps10.setLong(7, last_modified);
            if (icon_uri != null) {
                ps10.setString(8, icon_uri.toString());
            } else {
                ps10.setNull(8, java.sql.Types.VARCHAR);
            }
            if (onto != null) {
                ps10.setString(9, onto);
            } else {
                ps10.setNull(9, java.sql.Types.NULL);
            }
            if (category != null) {
                ps10.setString(10, category);
            } else {
                ps10.setNull(10, java.sql.Types.NULL);
            }
            if (browser != null) {
                ps10.setString(11, browser);
            } else {
                ps10.setNull(11, java.sql.Types.VARCHAR);
            }
            if (folder != null) {
                ps10.setString(12, folder);
            } else {
                ps10.setNull(12, java.sql.Types.NULL);
            }
            ps10.execute();
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
            if (JavaIrcBouncer.jibDbUtil.altDbTypePgSql()) {
                JavaIrcBouncer.jibDbUtil.finishDbConn(zconn);
            }
        }
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
