/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
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
