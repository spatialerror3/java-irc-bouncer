/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package net.spatialerror3.jib;

/**
 *
 * @author spatialerror3
 */
public class JavaIrcBouncer {
    public static JIBConfig jibConfig = null;
    public static JIBDBUtil jibDbUtil = null;
    public static JIBServer jibServ = null;
    public static JIBIRC jibIRC = null;
    public static JIBJython jibJython = null;

    public static void main(String[] args) {
        jibConfig = new JIBConfig();
        jibConfig.parseArgs(args);
        jibDbUtil = new JIBDBUtil();
        jibDbUtil.initSchema();
        JIBServer jib1 = null;
        jib1 = new JIBServer();
        JavaIrcBouncer.jibServ=jib1;
        Thread t1 = new Thread(jib1);
        t1.start();
        jibJython = new JIBJython();
        jibJython.loadConfig();
        while(true) {
            try {
                Thread.sleep(60000);
            } catch (InterruptedException ex) {
                System.getLogger(JavaIrcBouncer.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            }
        }
    }
}
