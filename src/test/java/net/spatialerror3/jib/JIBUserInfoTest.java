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

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author spatialerror3
 */
public class JIBUserInfoTest {
    
    public JIBUserInfoTest() {
    }
    
    @BeforeAll
    public static void setUpClass() {
    }
    
    @AfterAll
    public static void tearDownClass() {
    }
    
    @BeforeEach
    public void setUp() {
    }
    
    @AfterEach
    public void tearDown() {
    }

    /**
     * Test of getNick method, of class JIBUserInfo.
     */
    @Test
    public void testGetNick() {
        System.out.println("getNick");
        JIBUserInfo instance = new JIBUserInfo();
        String expResult = null;
        String result = instance.getNick();
        assertEquals(expResult, result);
    }

    /**
     * Test of getUser method, of class JIBUserInfo.
     */
    @Test
    public void testGetUser() {
        System.out.println("getUser");
        JIBUserInfo instance = new JIBUserInfo();
        String expResult = null;
        String result = instance.getUser();
        assertEquals(expResult, result);
    }

    /**
     * Test of getHost method, of class JIBUserInfo.
     */
    @Test
    public void testGetHost() {
        System.out.println("getHost");
        JIBUserInfo instance = new JIBUserInfo();
        String expResult = null;
        String result = instance.getHost();
        assertEquals(expResult, result);
    }

    /**
     * Test of getRealname method, of class JIBUserInfo.
     */
    @Test
    public void testGetRealname() {
        System.out.println("getRealname");
        JIBUserInfo instance = new JIBUserInfo();
        String expResult = null;
        String result = instance.getRealname();
        assertEquals(expResult, result);
    }

    /**
     * Test of nuh method, of class JIBUserInfo.
     */
    @Test
    public void testNuh() {
        System.out.println("nuh");
        JIBUserInfo instance = new JIBUserInfo();
        String expResult = "null!null@null";
        String result = instance.nuh();
        assertEquals(expResult, result);
    }

    /**
     * Test of setNick method, of class JIBUserInfo.
     */
    @Test
    public void testSetNick() {
        System.out.println("setNick");
        String nick = "anick";
        JIBUserInfo instance = new JIBUserInfo();
        instance.setNick(nick);
    }

    /**
     * Test of setUser method, of class JIBUserInfo.
     */
    @Test
    public void testSetUser() {
        System.out.println("setUser");
        String user = "user";
        JIBUserInfo instance = new JIBUserInfo();
        instance.setUser(user);
    }

    /**
     * Test of setHost method, of class JIBUserInfo.
     */
    @Test
    public void testSetHost() {
        System.out.println("setHost");
        String host = "localhost";
        JIBUserInfo instance = new JIBUserInfo();
        instance.setHost(host);
    }

    /**
     * Test of setRealname method, of class JIBUserInfo.
     */
    @Test
    public void testSetRealname() {
        System.out.println("setRealname");
        String realname = "a realname";
        JIBUserInfo instance = new JIBUserInfo();
        instance.setRealname(realname);
    }

    /**
     * Test of parseNUH method, of class JIBUserInfo.
     */
    @Test
    public void testParseNUH() {
        System.out.println("parseNUH");
        String containsNUH = "a!b@c";
        JIBUserInfo expResult = null;
        JIBUserInfo result = JIBUserInfo.parseNUH(containsNUH);
        assertEquals("a", result.getNick());
        assertEquals("b", result.getUser());
        assertEquals("c", result.getHost());
    }
    
}
