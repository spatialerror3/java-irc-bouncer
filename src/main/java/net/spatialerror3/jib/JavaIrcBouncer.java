/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package net.spatialerror3.jib;

/**
 *
 * @author spatialerror3
 */
public class JavaIrcBouncer {
    public static JIBServer jibServ = null;
    public static JIBIRC jibIRC = null;

    public static void main(String[] args) {
        JIBServer jib1 = null;
        jib1 = new JIBServer();
        JavaIrcBouncer.jibServ=jib1;
        Thread t1 = new Thread(jib1);
        t1.start();
        while(true) {
            try {
                Thread.sleep(60000);
            } catch (InterruptedException ex) {
                System.getLogger(JavaIrcBouncer.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            }
        }
        //System.out.println("Hello World!");
    }
}
