/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package net.spatialerror3.jib;

import java.util.Random;
import java.util.UUID;
import java.util.random.RandomGenerator;
import org.apache.commons.codec.digest.DigestUtils;

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
    
    public static String randHexString() {
        String hexStr = null;
        String randStr1 = UUID.randomUUID().toString();
        String randStr2 = Long.toString(System.currentTimeMillis());
        String randStr3 = null;
        RandomGenerator g = RandomGenerator.of("L64X128MixRandom");
        randStr3=Long.toString(g.nextLong());
        hexStr=DigestUtils.sha256Hex(randStr3+randStr2+randStr1);
        return hexStr;
    }
}
