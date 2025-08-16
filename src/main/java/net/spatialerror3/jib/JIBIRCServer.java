/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package net.spatialerror3.jib;

/**
 *
 * @author spatialerror3
 */
public class JIBIRCServer {
    private JIBIRCNetType.NetType netType = JIBIRCNetType.NetType.GENERIC;
    private String server = null;
    private int port = 0;
    private boolean ssl = true;
    //
    private String clientBind = null;
    
    public JIBIRCServer() {
        
    }
    
    public JIBIRCNetType.NetType getNetType() {
        return this.netType;
    }
    
    public String getServer() {
        return this.server;
    }
    
    public int getPort() {
        return this.port;
    }
    
    public boolean getSsl() {
        return this.ssl;
    }
    
    public void setNetType(JIBIRCNetType.NetType netType) {
        this.netType=netType;
    }
    
    public void setServer(String server) {
        this.server=server;
    }
    
    public void setPort(int port) {
        this.port=port;
    }
    
    public void setSsl(boolean ssl) {
        this.ssl=ssl;
    }
}
