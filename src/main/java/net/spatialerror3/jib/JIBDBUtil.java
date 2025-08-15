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
import java.util.Vector;
import org.h2.tools.Server;

/**
 *
 * @author spatialerror3
 */
public class JIBDBUtil {

    private Connection conn = null;
    private Server server = null;

    public JIBDBUtil() {
        getDatabase();
        if (JavaIrcBouncer.jibConfig.getValue("H2SERVER") != null) {
            try {
                server = Server.createTcpServer("").start();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public Connection getDatabase() {
        if (conn == null) {
            try {
                conn = DriverManager.getConnection("jdbc:h2:mem:test", "sa", "");
            } catch (SQLException ex) {
                System.getLogger(JIBDBUtil.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            }
        }
        return conn;
    }

    public void initSchema() {
        String sql = "CREATE TABLE IF NOT EXISTS servers (id int auto_increment primary key, server varchar(256), port integer);";
        try {
            PreparedStatement ps1 = getDatabase().prepareStatement(sql);
            ps1.execute();
        } catch (SQLException ex) {
            System.getLogger(JIBDBUtil.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        sql = "CREATE TABLE IF NOT EXISTS channels (id int auto_increment primary key, channel varchar(256));";
        try {
            PreparedStatement ps4 = getDatabase().prepareStatement(sql);
            ps4.execute();
        } catch (SQLException ex) {
            System.getLogger(JIBDBUtil.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        sql = "CREATE TABLE IF NOT EXISTS clientauth (id int auto_increment primary key, username varchar(256), password varchar(256));";
        try {
            PreparedStatement ps3 = getDatabase().prepareStatement(sql);
            ps3.execute();
        } catch (SQLException ex) {
            System.getLogger(JIBDBUtil.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        sql = "CREATE TABLE IF NOT EXISTS log1 (id int auto_increment primary key, loguser varchar(256), logtarget varchar(256), logmessage varchar(513));";
        try {
            PreparedStatement ps4 = getDatabase().prepareStatement(sql);
            ps4.execute();
        } catch (SQLException ex) {
            System.getLogger(JIBDBUtil.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        sql = "CREATE TABLE IF NOT EXISTS users (id int auto_increment primary key, userId bigint, _uuid uuid, username varchar(256), admin boolean);";
        try {
            PreparedStatement ps4 = getDatabase().prepareStatement(sql);
            ps4.execute();
        } catch (SQLException ex) {
            System.getLogger(JIBDBUtil.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        try {
            getDatabase().commit();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public int cntResultSet(ResultSet rs) {
        int r = -1;

        try {
            rs.last();
            r = rs.getRow();
            rs.beforeFirst();
        } catch (Exception e) {
            System.getLogger(JIBDBUtil.class.getName()).log(System.Logger.Level.ERROR, (String) null, e);
            r = 0;
        }

        return r;
    }

    public void addServer(String Server, int Port) {
        String sql = "INSERT INTO servers (server,port) VALUES(?,?);";
        PreparedStatement ps2 = null;
        try {
            ps2 = getDatabase().prepareStatement(sql);
            ps2.setString(1, Server);
            ps2.setInt(2, Port);
            ps2.execute();
        } catch (SQLException ex) {
            System.getLogger(JIBDBUtil.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }

    public void addChannel(String Channel) {
        String sql = "INSERT INTO channels (channel) VALUES(?);";
        PreparedStatement ps2 = null;
        try {
            ps2 = getDatabase().prepareStatement(sql);
            ps2.setString(1, Channel);
            ps2.execute();
        } catch (SQLException ex) {
            System.getLogger(JIBDBUtil.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }

    public void removeChannel(String Channel) {
        String sql = "DELETE FROM channels WHERE channel = ?;";
        PreparedStatement ps2 = null;
        try {
            ps2 = getDatabase().prepareStatement(sql);
            ps2.setString(1, Channel);
            ps2.execute();
        } catch (SQLException ex) {
            System.getLogger(JIBDBUtil.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }

    }

    public String[] getChannels() {
        Vector<String> cv = new Vector<String>();
        String[] channels = new String[100];
        PreparedStatement ps5 = null;
        ResultSet rs5 = null;
        String sql = "SELECT channel FROM channels;";
        try {
            ps5 = getDatabase().prepareStatement(sql);
            rs5 = ps5.executeQuery();
        } catch (SQLException ex) {
            System.getLogger(JIBDBUtil.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        try {
            //if (rs5 != null) {
            //    rs5.first();
            //}
            while (rs5 != null && rs5.next()) {
                cv.add(rs5.getString(1));
                System.err.println("Contains Channel=" + rs5.getString(1));
            }
        } catch (SQLException ex) {
            System.getLogger(JIBDBUtil.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
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
            System.getLogger(JIBDBUtil.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
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
            System.getLogger(JIBDBUtil.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        try {
            rs8 = ps8.executeQuery();
        } catch (SQLException ex) {
            System.getLogger(JIBDBUtil.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        if (rs8 != null) {
            String replayStr = null;
            String logUser = null;
            String logTarget = null;
            String logMessage = null;
            System.err.println("rs8 rowCnt=" + cntResultSet(rs8));
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
                System.getLogger(JIBDBUtil.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            }
        }
        return replay;
    }

    public boolean checkUserPass(String User, String Pass) {
        return false;
    }
}
