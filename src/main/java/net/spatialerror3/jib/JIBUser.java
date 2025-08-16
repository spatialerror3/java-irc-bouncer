/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package net.spatialerror3.jib;

import java.util.Iterator;
import java.util.UUID;
import java.util.Vector;

/**
 *
 * @author spatialerror3
 */
public class JIBUser {
    private static long userIdCnt = 0;
    private long userId = userIdCnt++;
    private UUID uuid = UUID.randomUUID();
    //
    private String userName = null;
    private String authToken = null;
    //
    private boolean admin = false;
    //
    private JIBIRC jibIRC = null;
    //
    Vector<JIBHandleClient> clients = new Vector<JIBHandleClient>();
    
    public JIBUser() {
        
    }
    
    public void setUserName(String userName) {
        this.userName=userName;
    }
    
    public void setAuthToken(String authToken) {
        this.authToken=authToken;
    }
    
    public JIBUser auth(String authToken) {
        if(authToken.equals(authToken)) {
            return this;
        }
        return null;
    }
    
    public UUID getUUID() {
        return this.uuid;
    }
    
    public void setJibIRC(JIBIRC jibIRC) {
        this.jibIRC=jibIRC;
    }
    
    public JIBIRC getJibIRC() {
        return this.jibIRC;
    }
    
    public void addClient(JIBHandleClient jibhc) {
        clients.add(jibhc);
    }
       
    public void writeAllClients(String l) {
        Iterator<JIBHandleClient> it1 = clients.iterator();
        while(it1.hasNext()) {
            JIBHandleClient tc = it1.next();
            if(tc.getError()==null&&tc.getConnected()==true) {
              tc.sendLine(l);
            }
        }
    }
}
