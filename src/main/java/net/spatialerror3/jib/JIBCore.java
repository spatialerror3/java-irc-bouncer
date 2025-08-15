/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package net.spatialerror3.jib;

import java.util.Hashtable;
import java.util.Vector;

/**
 *
 * @author spatialerror3
 */
public class JIBCore {
    Vector<JIBUser> users = null;
    Hashtable<String,JIBUser> userMap = null;
    
    public JIBCore() {
        users = new Vector<JIBUser>();
        userMap = new Hashtable<String,JIBUser>();
    }
    
    public JIBUser createUser(String userName, boolean admin) {
        JIBUser u = new JIBUser();
        u.setUserName(userName);
        userMap.put(userName, u);
        users.add(u);
        return u;
    }
    
    public JIBUser getUser(String userName) {
        return userMap.get(userName);
    }
    
    public JIBUser authUser(String userName, String authToken) {
        JIBUser u = getUser(userName);
        return u.auth(authToken);
    }
}
