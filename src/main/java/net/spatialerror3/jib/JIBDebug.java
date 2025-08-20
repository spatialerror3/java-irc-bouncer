/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package net.spatialerror3.jib;

/**
 *
 * @author spatialerror3
 */
public class JIBDebug {
    private boolean DEBUGGING = false;
    
    public JIBDebug() {
        
    }
    
    public void setDebugging(boolean debugging) {
        this.DEBUGGING=debugging;
    }
    
    public boolean debug() {
        return this.DEBUGGING;
    }
}
