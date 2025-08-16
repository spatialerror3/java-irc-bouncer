/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package net.spatialerror3.jib;

/**
 *
 * @author spatialerror3
 */
public class JIBStringUtil {
    public static String remEOL(String s) {
        String r = null;
        if(s==null) {
            return s;
        }
        r = s.replaceAll("\r\n", "");
        return r;
    }
    
    public static String remDD(String s) {
        if(s==null) {
            return s;
        }
        if(s.charAt(0)==':') {
            return s.substring(1);
        }
        return s;
    }
}
