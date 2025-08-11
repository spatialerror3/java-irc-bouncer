/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package net.spatialerror3.jib;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author spatialerror3
 */
public class JIBDBUtil {
    public JIBDBUtil() {
        
    }
    
    public Connection getDatabase() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:h2:~/test", "sa", "");
        } catch (SQLException ex) {
            System.getLogger(JIBDBUtil.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        return conn;
    }
    
    public void initSchema() {
        String sql = "CREATE TABLE IF NOT EXISTS servers (id int primary key, server varchar(256), port integer);";
        try {
            PreparedStatement ps1 = getDatabase().prepareStatement(sql);
            ps1.execute();
        } catch (SQLException ex) {
            System.getLogger(JIBDBUtil.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        sql = "CREATE TABLE IF NOT EXISTS clientauth (id int primary key, username varchar(256), password varchar(256));";
        try {
            PreparedStatement ps3 = getDatabase().prepareStatement(sql);
            ps3.execute();
        } catch (SQLException ex) {
            System.getLogger(JIBDBUtil.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
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
    
    public boolean checkUserPass(String User, String Pass) {
        return false;
    }
}
