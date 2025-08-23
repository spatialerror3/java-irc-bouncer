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

import java.io.Serializable;
import java.util.Hashtable;

/**
 *
 * @author spatialerror3
 */
public class JIBConfig implements Serializable {
    private static final long serialVersionUID = 1L;
    private Hashtable<String,String> kvStore = null;
    
    public JIBConfig() {
        kvStore = new Hashtable<String,String>();
    }
    
    public void parseArgs(String[] args) {
        if(args.length > 0) {
            setValue("Server", args[0]);
        }
    }
    
    public void setValue(String Key, String Value) {
        if(Value!=null)
          kvStore.put(Key, Value);
    }
    
    public String getValue(String Key) {
        return kvStore.get(Key);
    }
    
    public void createUser(String userName, String pass) {
        JIBUser u = JavaIrcBouncer.jibCore.createUser(userName, false);
        u.setAuthToken(pass);
    }
    
    public void createUser(String userName, String pass, String n, String id, String r, String server, int port) {
        JIBUser u = JavaIrcBouncer.jibCore.createUser(userName, false);
        u.setAuthToken(pass);
        u.setNick(n);
        u.setUser(id);
        u.setRealname(r);
        JIBIRCServer tmpServ = new JIBIRCServer();
        tmpServ.setServer(server);
        tmpServ.setPort(port);
        tmpServ.setSsl(true);
        u.addIrcServer(tmpServ);
    }
}
