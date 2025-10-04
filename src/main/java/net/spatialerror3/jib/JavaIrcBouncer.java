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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author spatialerror3
 */
public class JavaIrcBouncer {

    private static final Logger log = LogManager.getLogger(JavaIrcBouncer.class);
    public static JIBDebug jibDebug = null;
    public static JIBCore jibCore = null;
    public static JIBConfig jibConfig = null;
    public static JIBSysEnv jibSysEnv = null;
    public static JIBDBUtil jibDbUtil = null;
    public static JIBServer jibServ = null;
    public static JIBServerSSL jibServSsl = null;
    public static JIBIRC jibIRC = null;
    public static JIBJython jibJython = null;
    public static JIBHTTPServer jibHttpServ = null;
    public static JIBHTTPSServer jibHttpsServ = null;
    public static JIBCommand jibCommand = null;
    public static JIBShutdown jibShutdown = null;
    public static JIBPeriodic jibPeriodic = null;
    public static JIBQuartz jibQuartz = null;
    public static JIBStatus jibStatus = null;
    public static JIBPluginCore jibPluginCore = null;
    public static JIBEventCore jibEventCore = null;

    public static void main(String[] args) {
        System.setProperty("python.import.site", "false");
        jibDebug = new JIBDebug();
        jibCore = new JIBCore();
        jibConfig = new JIBConfig();
        jibConfig.parseArgs(args);
        jibJython = new JIBJython();
        jibJython.loadInit();
        jibSysEnv = new JIBSysEnv();
        jibSysEnv.envToConfig("AUTHUSER");
        jibSysEnv.envToConfig("AUTHPASS");
        jibSysEnv.envToConfig("Server");
        jibSysEnv.envToConfig("Port");
        jibSysEnv.envToConfig("Nick");
        jibSysEnv.envToConfig("User");
        jibSysEnv.envToConfig("Realname");
        jibSysEnv.envToConfig("ClientNoSSL");
        jibSysEnv.envToConfig("NICKSERVUSER");
        jibSysEnv.envToConfig("NICKSERVPASS");
        jibSysEnv.envToConfig("TRUSTSTORENAME");
        jibSysEnv.envToConfig("TRUSTSTOREPASSWORD");
        jibSysEnv.envToConfig("KEYSTORENAME");
        jibSysEnv.envToConfig("KEYSTOREPASSWORD");
        jibSysEnv.envToConfig("DEBUGGING");
        if (jibConfig.getValue("DEBUGGING") != null) {
            jibDebug.setDebugging(true);
        }
        jibSysEnv.envToConfig("RANDAUTHTOKENS");
        jibSysEnv.envToConfig("ALTDBTYPE");
        jibSysEnv.envToConfig("DBUSER");
        jibSysEnv.envToConfig("DBPASS");
        jibSysEnv.envToConfig("DBHOST");
        jibSysEnv.envToConfig("DBPORT");
        jibSysEnv.envToConfig("DBNAME");
        jibSysEnv.envToConfig("H2SERVER");
        jibSysEnv.envToConfig("H2DBFILE");
        String h2dbfile = null;
        if (jibConfig.getValue("H2DBFILE") != null) {
            h2dbfile = jibConfig.getValue("H2DBFILE");
        } else {
            h2dbfile = "mem:test";
        }
        jibDbUtil = new JIBDBUtil(h2dbfile);
        jibDbUtil.initSchema();
        long loadedUsers = jibDbUtil.loadUsers();
        jibDbUtil.loadServers(null);
        jibJython.loadConfig();
        JIBUser adminUser = null;
        if (loadedUsers < 1) {
            if (jibConfig.getValue("AUTHPASS") == null) {
                jibConfig.setValue("AUTHPASS", JIBStringUtil.randHexString());
                log.info("Setting AUTHPASS for admin user to: " + jibConfig.getValue("AUTHPASS"));
            }
            if (jibConfig.getValue("AUTHUSER") != null) {
                (adminUser = jibCore.createUser(jibConfig.getValue("AUTHUSER"), true)).setAuthToken(jibConfig.getValue("AUTHPASS"));
            } else {
                (adminUser = jibCore.createUser("admin", true)).setAuthToken(jibConfig.getValue("AUTHPASS"));
            }
            if (adminUser != null) {
                if (jibConfig.getValue("Server") != null && jibConfig.getValue("Port") != null) {
                    JIBIRCServer tmpServ = new JIBIRCServer();
                    tmpServ.setServer(jibConfig.getValue("Server"));
                    tmpServ.setPort(jibConfig.getValue("Port"));
                    tmpServ.setNick(jibConfig.getValue("Nick"));
                    tmpServ.setUser(jibConfig.getValue("User"));
                    tmpServ.setRealname(jibConfig.getValue("Realname"));
                    tmpServ.setNickServUser(jibConfig.getValue("NICKSERVUSER"));
                    tmpServ.setNickServPass(jibConfig.getValue("NICKSERVPASS"));

                    adminUser.addIrcServer(tmpServ);
                }
            }
        }
        JIBServer jib1 = null;
        jib1 = new JIBServer();
        JavaIrcBouncer.jibServ = jib1;
        Thread t1 = new Thread(jib1);
        t1.start();
        JIBServerSSL jib2 = null;
        if (jibConfig.getValue("TRUSTSTORENAME") != null && jibConfig.getValue("KEYSTORENAME") != null) {
            jib2 = new JIBServerSSL(jib1);
            JavaIrcBouncer.jibServSsl = jib2;
            Thread t2 = new Thread(jib2);
            t2.start();
        }
        jibHttpServ = new JIBHTTPServer(-1);
        if (jibConfig.getValue("TRUSTSTORENAME") != null && jibConfig.getValue("KEYSTORENAME") != null) {
            jibHttpsServ = new JIBHTTPSServer(-1);
        }
        jibCommand = new JIBCommand();
        jibShutdown = new JIBShutdown();
        Runtime.getRuntime().addShutdownHook(jibShutdown);
        jibPeriodic = new JIBPeriodic();
        jibQuartz = new JIBQuartz();
        jibQuartz.init();
        jibStatus = new JIBStatus();
        jibPluginCore = new JIBPluginCore();
        jibEventCore = new JIBEventCore();
        if (jibConfig.getValue("RANDAUTHTOKENS") != null && jibConfig.getValue("RANDAUTHTOKENS").equals("TRUE")) {
            jibCore.randAuthTokens();
        }
        log.info("Up...");
        jibJython.loadConfig2();
        if (jibHttpServ != null) {
            log.info(jibHttpServ.toString());
        }
        if (jibHttpsServ != null) {
            log.info(jibHttpsServ.toString());
        }
        while (true) {
            try {
                Thread.sleep(60000);
            } catch (InterruptedException ex) {
                System.getLogger(JavaIrcBouncer.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            }
            try {
                Thread jibPeriodicThread = new Thread(JavaIrcBouncer.jibPeriodic);
                jibPeriodicThread.start();
            } catch (Exception ex) {
                log.error("jibPeriodicThread", ex);
            }
        }
    }
}
