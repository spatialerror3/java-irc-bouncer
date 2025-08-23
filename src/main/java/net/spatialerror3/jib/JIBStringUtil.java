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
    
    public static String remEOL2(String s) {
        String r = null;
        if(s==null) {
            return s;
        }
        r = s.replaceAll("\r\n", "");
        r = r.replaceAll("\n", "");
        r = r.replaceAll("\r", "");
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
