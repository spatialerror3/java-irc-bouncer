/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package net.spatialerror3.jib;

/**
 *
 * @author spatialerror3
 */
public class JavaIrcBouncer {
    
    public static JIBDebug jibDebug = null;
    public static JIBCore jibCore = null;
    public static JIBConfig jibConfig = null;
    public static JIBSysEnv jibSysEnv = null;
    public static JIBDBUtil jibDbUtil = null;
    public static JIBServer jibServ = null;
    public static JIBIRC jibIRC = null;
    public static JIBJython jibJython = null;
    
    public static void main(String[] args) {
        System.setProperty("python.import.site", "false");
        jibDebug = new JIBDebug();
        jibCore = new JIBCore();
        jibConfig = new JIBConfig();
        jibConfig.parseArgs(args);
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
        jibDbUtil = new JIBDBUtil();
        jibDbUtil.initSchema();
        jibJython = new JIBJython();
        jibJython.loadConfig();
        if (jibConfig.getValue("AUTHPASS") == null) {
            jibConfig.setValue("AUTHPASS", JIBStringUtil.randHexString());
            System.err.println("Setting AUTHPASS for admin user to: " + jibConfig.getValue("AUTHPASS"));
        }
        if (jibConfig.getValue("AUTHUSER") != null) {
            jibCore.createUser(jibConfig.getValue("AUTHUSER"), true).setAuthToken(jibConfig.getValue("AUTHPASS"));
        } else {
            jibCore.createUser("admin", true).setAuthToken(jibConfig.getValue("AUTHPASS"));
        }
        JIBServer jib1 = null;
        jib1 = new JIBServer();
        JavaIrcBouncer.jibServ = jib1;
        Thread t1 = new Thread(jib1);
        t1.start();
        while (true) {
            try {
                Thread.sleep(60000);
            } catch (InterruptedException ex) {
                System.getLogger(JavaIrcBouncer.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            }
        }
    }
}
