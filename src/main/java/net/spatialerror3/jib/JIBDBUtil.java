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

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;
import java.util.Vector;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.h2.tools.Server;

/**
 *
 * @author spatialerror3
 */
public class JIBDBUtil {

    private static final Logger log = LogManager.getLogger(JIBDBUtil.class);
    private String dbFile = null;
    private Connection conn = null;
    private Server server = null;

    public JIBDBUtil(String dbFile) {
        log.debug("JIBDBUtil() this=" + this);
        this.dbFile = dbFile;
        getDatabase();
        if (JavaIrcBouncer.jibConfig.getValue("H2SERVER") != null) {
            try {
                server = Server.createTcpServer("-tcp").start();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        log.debug("JIBDBUtil() this=" + this + " conn=" + this.conn);
    }

    public void shutdown() {

    }

    public Connection getDatabase() {
        if (conn == null) {
            try {
                conn = DriverManager.getConnection("jdbc:h2:" + this.dbFile, "sa", "");
            } catch (SQLException ex) {
                log.error((String) null, ex);
            }
        }
        return conn;
    }

    public void initSchema() {
        String sql = "CREATE TABLE IF NOT EXISTS servers (id int auto_increment primary key, server varchar(256), port integer, ssl boolean, ipv6 boolean, nick varchar(256), username varchar(256), realname varchar(256), nsacct varchar(256), nspass varchar(256), channels varchar(512), u varchar(256), opt object(10000000));";
        try {
            PreparedStatement ps1 = getDatabase().prepareStatement(sql);
            ps1.execute();
        } catch (SQLException ex) {
            log.error((String) null, ex);
        }
        sql = "CREATE TABLE IF NOT EXISTS channels (id int auto_increment primary key, channel varchar(256), u varchar(256));";
        try {
            PreparedStatement ps4 = getDatabase().prepareStatement(sql);
            ps4.execute();
        } catch (SQLException ex) {
            log.error((String) null, ex);
        }
        sql = "CREATE TABLE IF NOT EXISTS clientauth (id int auto_increment primary key, username varchar(256), password varchar(256));";
        try {
            PreparedStatement ps3 = getDatabase().prepareStatement(sql);
            ps3.execute();
        } catch (SQLException ex) {
            log.error((String) null, ex);
        }
        sql = "CREATE TABLE IF NOT EXISTS log1 (id int auto_increment primary key, loguser varchar(256), logtarget varchar(256), logmessage varchar(513), u varchar(256), s varchar(256), ts1 timestamp DEFAULT NOW());";
        try {
            PreparedStatement ps4 = getDatabase().prepareStatement(sql);
            ps4.execute();
        } catch (SQLException ex) {
            log.error((String) null, ex);
        }
        sql = "CREATE TABLE IF NOT EXISTS users (id int auto_increment primary key, userId bigint, _uuid uuid, username varchar(256), authtoken varchar(256), admin boolean, opt object(10000000));";
        try {
            PreparedStatement ps4 = getDatabase().prepareStatement(sql);
            ps4.execute();
        } catch (SQLException ex) {
            log.error((String) null, ex);
        }
        try {
            getDatabase().commit();
        } catch (SQLException e) {
            log.error((String) null, e);
        }
    }

    public int cntResultSet(ResultSet rs) {
        int r = -1;

        try {
            rs.last();
            r = rs.getRow();
            rs.beforeFirst();
        } catch (Exception e) {
            log.error((String) null, e);
            r = 0;
        }

        return r;
    }

    public long getUsersMaxUserId() {
        long userIdMax = -1;
        String sql = "SELECT userId FROM users;";
        PreparedStatement ps5 = null;
        ResultSet rs5 = null;
        try {
            ps5 = getDatabase().prepareStatement(sql);
            rs5 = ps5.executeQuery();
        } catch (SQLException ex) {
            log.error((String) null, ex);
        }
        try {
            while (rs5 != null && rs5.next()) {
                if (rs5.getLong(1) > userIdMax) {
                    userIdMax = rs5.getLong(1);
                }
            }
        } catch (SQLException ex) {
            log.error((String) null, ex);
        }
        log.trace("userIdMax=" + userIdMax);
        return userIdMax;
    }

    public long loadUsers() {
        long loadedUsers = 0L;
        String sql = "SELECT userId,_uuid,username,authtoken,admin,opt FROM users;";
        PreparedStatement ps5 = null;
        ResultSet rs5 = null;
        try {
            ps5 = getDatabase().prepareStatement(sql);
            rs5 = ps5.executeQuery();
        } catch (SQLException ex) {
            log.error((String) null, ex);
        }
        try {
            while (rs5 != null && rs5.next()) {
                JIBUser opt = rs5.getObject("opt", JIBUser.class);
                JIBUser tmpu = JavaIrcBouncer.jibCore.createUser(rs5.getString(3), rs5.getBoolean(5), true);
                tmpu.setUserId(rs5.getLong(1));
                tmpu.setUUID((UUID) rs5.getObject("_uuid"));
                tmpu.setAuthToken(rs5.getString(4));

                log.debug("Contains User=" + rs5.getString(3));
                loadedUsers++;
            }
        } catch (SQLException ex) {
            log.error((String) null, ex);
        }
        return loadedUsers;
    }

    public void addUser(JIBUser u) {
        String sql = "INSERT INTO users (userId,_uuid,username,authtoken,admin,opt) VALUES(?,?,?,?,?,?);";
        PreparedStatement ps2 = null;
        try {
            ps2 = getDatabase().prepareStatement(sql);
            ps2.setLong(1, u.getUserId());
            ps2.setString(2, u.getUUID().toString());
            ps2.setString(3, u.getUserName());
            ps2.setString(4, u.getAuthToken());
            ps2.setBoolean(5, u.admin());
            ps2.setObject(6, u);
            ps2.execute();
            getDatabase().commit();
        } catch (SQLException ex) {
            log.error((String) null, ex);
        }
    }
    
    public void addUserAuthToken(JIBUser u) {
        String sql = "UPDATE users SET authtoken = ? WHERE _uuid = ?";
        PreparedStatement ps2 = null;
        try {
            ps2 = getDatabase().prepareStatement(sql);
            ps2.setString(1, u.getAuthToken());
            ps2.setString(2, u.getUUID().toString());
            ps2.execute();
            getDatabase().commit();
        } catch (SQLException ex) {
            log.error((String) null, ex);
        }
    }

    public void removeUser(JIBUser u) {
        String sql = "DELETE FROM users WHERE _uuid = ?;";
        PreparedStatement ps2 = null;
        try {
            ps2 = getDatabase().prepareStatement(sql);
            ps2.setLong(1, u.getUserId());
            ps2.execute();
            getDatabase().commit();
        } catch (SQLException ex) {
            log.error((String) null, ex);
        }
    }

    public void refreshUser(JIBUser u) {
        String sql = "UPDATE users SET opt = ? WHERE _uuid = ?;";
        PreparedStatement ps2 = null;
        try {
            ps2 = getDatabase().prepareStatement(sql);
            ps2.setObject(1, u);
            ps2.setString(2, u.getUUID().toString());
            ps2.execute();
            getDatabase().commit();
        } catch (SQLException ex) {
            log.error((String) null, ex);
        }
    }

    public void addClientAuth(JIBUser u, String authToken) {
        String sql = "INSERT INTO clientauth (username,password) VALUES(?,?);";
        PreparedStatement ps2 = null;
        try {
            ps2 = getDatabase().prepareStatement(sql);
            ps2.setString(1, u.getUserName());
            ps2.setString(2, authToken);
            ps2.execute();
        } catch (SQLException ex) {
            log.error((String) null, ex);
        }
    }

    public void addServer(JIBUser u, String Server, int Port) {
        String sql = "INSERT INTO servers (server,port,u,ssl,ipv6) VALUES(?,?,?,?,?);";
        PreparedStatement ps2 = null;
        try {
            ps2 = getDatabase().prepareStatement(sql);
            ps2.setString(1, Server);
            ps2.setInt(2, Port);
            ps2.setString(3, u.getUUID().toString());
            ps2.execute();
        } catch (SQLException ex) {
            log.error((String) null, ex);
        }
    }

    public void addServer(JIBUser u, JIBIRCServer serv) {
        String sql = "INSERT INTO servers (server,port,u,ssl,ipv6,nick,username,realname,nsacct,nspass,channels,opt) VALUES(?,?,?,?,?,?);";
        PreparedStatement ps2 = null;
        try {
            ps2 = getDatabase().prepareStatement(sql);
            ps2.setString(1, serv.getServer());
            ps2.setInt(2, serv.getPort());
            ps2.setString(3, u.getUUID().toString());
            ps2.setBoolean(4, serv.getSsl());
            ps2.setBoolean(5, serv.getIpv6());
            ps2.setString(6, serv.getNick());
            ps2.setString(7, serv.getUser());
            ps2.setString(8, serv.getRealname());
            ps2.setString(9, serv.getNickServUser());
            ps2.setString(10, serv.getNickServPass());
            ps2.setString(11, serv.getChannels());
            ps2.setObject(12, serv);
            ps2.execute();
        } catch (SQLException ex) {
            log.error((String) null, ex);
        }
    }

    public void removeServer(JIBUser u, JIBIRCServer serv) {
        String sql = "DELETE FROM servers WHERE server = ? AND port = ? AND u = ?;";
        PreparedStatement ps2 = null;
        try {
            ps2 = getDatabase().prepareStatement(sql);
            ps2.setString(1, serv.getServer());
            ps2.setInt(2, serv.getPort());
            ps2.setString(3, u.getUUID().toString());
            ps2.execute();
        } catch (SQLException ex) {
            log.error((String) null, ex);
        }
    }

    public ArrayList<JIBIRCServer> getServers(JIBUser u) {
        String sql = "SELECT u, server, port, ssl, ipv6, opt FROM servers WHERE u = ?;";
        ArrayList<JIBIRCServer> ret = null;
        ret = new ArrayList<JIBIRCServer>();
        PreparedStatement ps5 = null;
        ResultSet rs5 = null;

        try {
            ps5 = getDatabase().prepareStatement(sql);
            ps5.setString(1, u.getUUID().toString());
            rs5 = ps5.executeQuery();
        } catch (SQLException ex) {
            log.error((String) null, ex);
        }

        try {
            //if (rs5 != null) {
            //    rs5.first();
            //}
            while (rs5 != null && rs5.next()) {
                JIBIRCServer opt = rs5.getObject("opt", JIBIRCServer.class);
                JIBIRCServer tmp1 = new JIBIRCServer();
                tmp1.setServer(rs5.getString(2));
                tmp1.setPort(rs5.getInt(3));
                tmp1.setSsl(rs5.getBoolean(4));
                tmp1.setIpv6(rs5.getBoolean(5));

                log.debug("Contains Server=" + tmp1 + " tmp1.getServer()=" + tmp1.getServer() + " tmp1.getPort()=" + tmp1.getPort());
                ret.add(tmp1);
            }
        } catch (SQLException ex) {
            log.error((String) null, ex);
        }

        return ret;
    }

    public void addChannel(JIBUser u, String Channel) {
        if (containsChannel(u, Channel) != 0) {
            return;
        }
        String sql = "INSERT INTO channels (channel,u) VALUES(?,?);";
        PreparedStatement ps2 = null;
        try {
            ps2 = getDatabase().prepareStatement(sql);
            ps2.setString(1, Channel);
            ps2.setString(2, u.getUUID().toString());
            ps2.execute();
            getDatabase().commit();
        } catch (SQLException ex) {
            log.error((String) null, ex);
        }
    }

    public void removeChannel(JIBUser u, String Channel) {
        if (containsChannel(u, Channel) < 1) {
            return;
        }
        String sql = "DELETE FROM channels WHERE channel = ? AND u = ?;";
        PreparedStatement ps2 = null;
        try {
            ps2 = getDatabase().prepareStatement(sql);
            ps2.setString(1, Channel);
            ps2.setString(2, u.getUUID().toString());
            ps2.execute();
        } catch (SQLException ex) {
            log.error((String) null, ex);
        }
    }

    public String[] getChannels(JIBUser u) {
        Vector<String> cv = new Vector<String>();
        String[] channels = new String[100];
        PreparedStatement ps5 = null;
        ResultSet rs5 = null;
        String sql = "SELECT channel FROM channels WHERE u = ?;";
        try {
            ps5 = getDatabase().prepareStatement(sql);
            ps5.setString(1, u.getUUID().toString());
            rs5 = ps5.executeQuery();
        } catch (SQLException ex) {
            log.error((String) null, ex);
        }
        try {
            //if (rs5 != null) {
            //    rs5.first();
            //}
            while (rs5 != null && rs5.next()) {
                cv.add(rs5.getString(1));
                log.debug("Contains Channel=" + rs5.getString(1));
            }
        } catch (SQLException ex) {
            log.error((String) null, ex);
        }
        channels = (String[]) cv.toArray(channels);
        return channels;
    }

    public long containsChannel(JIBUser u, String chan) {
        long count = -1;
        PreparedStatement ps5 = null;
        ResultSet rs5 = null;
        String sql = "SELECT channel FROM channels WHERE u = ? AND channel = ?;";
        try {
            ps5 = getDatabase().prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ps5.setString(1, u.getUUID().toString());
            ps5.setString(2, chan);
            rs5 = ps5.executeQuery();
        } catch (SQLException ex) {
            log.error((String) null, ex);
        }
        if (rs5 != null) {
            count = cntResultSet(rs5);
        }
        return count;
    }

    public void logUserTargetMessage(JIBUser u, JIBIRCServer s, String logUser, String logTarget, String logMessage) {
        String sql = "INSERT INTO log1 (loguser,logtarget,logmessage,u,s) VALUES(?,?,?,?,?);";
        PreparedStatement ps2 = null;
        try {
            ps2 = getDatabase().prepareStatement(sql);
            ps2.setString(1, logUser.substring(1));
            ps2.setString(2, logTarget);
            ps2.setString(3, logMessage.substring(1));
            ps2.setString(4, u.getUUID().toString());
            ps2.setString(5, s.getUUID().toString());
            ps2.execute();
            getDatabase().commit();
        } catch (SQLException ex) {
            log.error((String) null, ex);
        }
    }

    public ArrayList<String> replayLog(JIBUser u) {
        String sql = "SELECT loguser,logtarget,logmessage FROM log1 WHERE u = ?;";
        ArrayList<String> replay = new ArrayList<String>();
        PreparedStatement ps8 = null;
        ResultSet rs8 = null;
        try {
            ps8 = getDatabase().prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ps8.setString(1, u.getUUID().toString());
        } catch (SQLException ex) {
            log.error((String) null, ex);
        }
        try {
            rs8 = ps8.executeQuery();
        } catch (SQLException ex) {
            log.error((String) null, ex);
        }
        if (rs8 != null) {
            String replayStr = null;
            String logUser = null;
            String logTarget = null;
            String logMessage = null;
            log.debug("rs8 rowCnt=" + cntResultSet(rs8));
            try {
                rs8.beforeFirst();
                while (rs8.next()) {
                    logUser = rs8.getString(1);
                    logTarget = rs8.getString(2);
                    logMessage = rs8.getString(3);
                    replayStr = ":" + logUser + " PRIVMSG " + logTarget + " :" + logMessage + "\r\n";
                    replay.add(replayStr);
                }
            } catch (SQLException ex) {
                log.error((String) null, ex);
            }
        }
        return replay;
    }

    public void clearLog(JIBUser u) {
        if (u != null) {
            String sql = "DELETE FROM log1 WHERE u = ?;";
            PreparedStatement ps2 = null;
            try {
                ps2 = getDatabase().prepareStatement(sql);
                ps2.setString(1, u.getUUID().toString());
                ps2.execute();
                getDatabase().commit();
            } catch (SQLException ex) {
                log.error((String) null, ex);
            }
        } else {
            String sql = "DELETE FROM log1;";
            PreparedStatement ps2 = null;
            try {
                ps2 = getDatabase().prepareStatement(sql);
                ps2.execute();
                getDatabase().commit();
            } catch (SQLException ex) {
                log.error((String) null, ex);
            }
        }
    }

    public void clearLogOlderThan2Days() {
        String sql = "DELETE FROM log1 WHERE NOW() - ts1 > INTERVAL '2' DAY;";
        PreparedStatement ps2 = null;
        try {
            ps2 = getDatabase().prepareStatement(sql);
            ps2.execute();
            getDatabase().commit();
        } catch (SQLException ex) {
            log.error((String) null, ex);
        }
    }

    public boolean checkUserPass(String User, String Pass) {
        String sql = "SELECT COUNT(*) FROM clientauth WHERE username = ? AND password = ?;";
        PreparedStatement ps9 = null;
        ResultSet rs9 = null;
        try {
            ps9 = getDatabase().prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        } catch (SQLException ex) {
            log.error((String) null, ex);
        }
        try {
            rs9 = ps9.executeQuery();
        } catch (SQLException ex) {
            log.error((String) null, ex);
        }
        if (rs9 != null) {
            try {
                while (rs9.next()) {
                    if (rs9.getInt(1) > 0) {
                        return true;
                    } else {
                        return false;
                    }
                }
            } catch (SQLException se) {
                log.error((String) null, se);
            }
        }
        return false;
    }
}
