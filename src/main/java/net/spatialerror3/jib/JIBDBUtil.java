/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
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
                server = Server.createTcpServer("").start();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        log.debug("JIBDBUtil() this=" + this + " conn=" + this.conn);
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
        String sql = "CREATE TABLE IF NOT EXISTS servers (id int auto_increment primary key, server varchar(256), port integer, u varchar(256));";
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
        sql = "CREATE TABLE IF NOT EXISTS log1 (id int auto_increment primary key, loguser varchar(256), logtarget varchar(256), logmessage varchar(513));";
        try {
            PreparedStatement ps4 = getDatabase().prepareStatement(sql);
            ps4.execute();
        } catch (SQLException ex) {
            log.error((String) null, ex);
        }
        sql = "CREATE TABLE IF NOT EXISTS users (id int auto_increment primary key, userId bigint, _uuid uuid, username varchar(256), admin boolean);";
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
        long userIdMax = 0;
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
        return userIdMax;
    }

    public void loadUsers() {
        String sql = "SELECT userId,_uuid,username,admin FROM users;";
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
                JIBUser tmpu = JavaIrcBouncer.jibCore.createUser(rs5.getString(3), rs5.getBoolean(4));
                tmpu.setUserId(rs5.getLong(1));
                tmpu.setUUID((UUID) rs5.getObject("_uuid"));

                log.debug("Contains User=" + rs5.getString(3));
            }
        } catch (SQLException ex) {
            log.error((String) null, ex);
        }
    }

    public void addUser(JIBUser u) {
        String sql = "INSERT INTO users (userId,_uuid,username,admin) VALUES(?,?,?,?);";
        PreparedStatement ps2 = null;
        try {
            ps2 = getDatabase().prepareStatement(sql);
            ps2.setLong(1, u.getUserId());
            ps2.setString(2, u.getUUID().toString());
            ps2.setString(3, u.getUserName());
            ps2.setBoolean(4, u.admin());
            ps2.execute();
            getDatabase().commit();
        } catch (SQLException ex) {
            log.error((String) null, ex);
        }
    }

    public void removeUser(JIBUser u) {

    }

    public void addServer(JIBUser u, String Server, int Port) {
        String sql = "INSERT INTO servers (server,port,u) VALUES(?,?,?);";
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
        addServer(u, serv.getServer(), serv.getPort());
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
        String sql = "SELECT u, server, port FROM servers WHERE u = ?;";
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
                JIBIRCServer tmp1 = new JIBIRCServer();
                tmp1.setServer(rs5.getString(2));
                tmp1.setPort(rs5.getInt(3));
                tmp1.setSsl(true);
                tmp1.setIpv6(false);

                log.debug("Contains Server=" + tmp1 + " tmp1.getServer()=" + tmp1.getServer() + " tmp1.getPort()=" + tmp1.getPort());
                ret.add(tmp1);
            }
        } catch (SQLException ex) {
            log.error((String) null, ex);
        }

        return ret;
    }

    public void addChannel(JIBUser u, String Channel) {
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

    public void logUserTargetMessage(String logUser, String logTarget, String logMessage) {
        String sql = "INSERT INTO log1 (loguser,logtarget,logmessage) VALUES(?,?,?);";
        PreparedStatement ps2 = null;
        try {
            ps2 = getDatabase().prepareStatement(sql);
            ps2.setString(1, logUser.substring(1));
            ps2.setString(2, logTarget);
            ps2.setString(3, logMessage.substring(1));
            ps2.execute();
            getDatabase().commit();
        } catch (SQLException ex) {
            log.error((String) null, ex);
        }
    }

    public Vector<String> replayLog() {
        String sql = "SELECT loguser,logtarget,logmessage FROM log1;";
        Vector<String> replay = new Vector<String>();
        PreparedStatement ps8 = null;
        ResultSet rs8 = null;
        try {
            ps8 = getDatabase().prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
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
                }
                replay.add(replayStr);
            } catch (SQLException ex) {
                log.error((String) null, ex);
            }
        }
        return replay;
    }

    public boolean checkUserPass(String User, String Pass) {
        return false;
    }
}
