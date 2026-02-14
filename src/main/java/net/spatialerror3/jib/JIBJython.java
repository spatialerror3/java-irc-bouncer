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

import java.io.File;
import java.util.HashMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.python.util.PythonInterpreter;

/**
 *
 * @author spatialerror3
 */
public class JIBJython {
    
    private static final Logger log = LogManager.getLogger(JIBJython.class);
    public static PythonInterpreter interp = null;
    public static HashMap<JIBUser, PythonInterpreter> interpz = new HashMap<>();

    public JIBJython() {
        interp = new PythonInterpreter();
        interp.set("a", this);
        interp.set("config", rJIBConfig());
        interp.set("configurator", rJIBConfig());
    }
    
    public void loadInit() {
        File cf = new File("./init.jy");
        if (cf.exists() && !cf.isDirectory()) {
            interp.execfile("./init.jy");
        }
    }
    
    public void loadConfig() {
        File cf = new File("./config.jy");
        if (cf.exists() && !cf.isDirectory()) {
            interp.execfile("./config.jy");
        }
    }
    
    public void loadConfig2() {
        File cf = new File("./config2.jy");
        if (cf.exists() && !cf.isDirectory()) {
            interp.execfile("./config2.jy");
        }
    }
    
    public JIBServer rJIBServ() {
        return JavaIrcBouncer.jibServ;
    }

    public JIBConfig rJIBConfig() {
        return JavaIrcBouncer.jibConfig;
    }
    
    public PythonInterpreter getPyInterpForUser(JIBUser u) {
        PythonInterpreter pyi = interpz.get(u);
        if(u == null) {
            return null;
        }
        if(pyi != null) {
            return pyi;
        }
        pyi = new PythonInterpreter();
        interpz.put(u, pyi);
        return pyi;
    }
}
