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
public class JIBStringUtilTest {
    
    public JIBStringUtilTest() {
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
     * Test of remEOL method, of class JIBStringUtil.
     */
    @Test
    public void testRemEOL() {
        System.out.println("remEOL");
        String s = "ab\r\n";
        String expResult = "ab";
        String result = JIBStringUtil.remEOL(s);
        assertEquals(expResult, result);
    }

    /**
     * Test of remEOL2 method, of class JIBStringUtil.
     */
    @Test
    public void testRemEOL2() {
        System.out.println("remEOL2");
        String s = "abc\r\n";
        String expResult = "abc";
        String result = JIBStringUtil.remEOL2(s);
        assertEquals(expResult, result);
    }

    /**
     * Test of remDD method, of class JIBStringUtil.
     */
    @Test
    public void testRemDD() {
        System.out.println("remDD");
        String s = ":bla";
        String expResult = "bla";
        String result = JIBStringUtil.remDD(s);
        assertEquals(expResult, result);
    }

    /**
     * Test of randHexString method, of class JIBStringUtil.
     */
    @Test
    public void testRandHexString() {
        System.out.println("randHexString");
        String expResult = "";
        String result = JIBStringUtil.randHexString();
        //assertEquals(expResult, result);
        assertNotNull(result);
    }
    
}
