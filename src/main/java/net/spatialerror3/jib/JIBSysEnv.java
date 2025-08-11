/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package net.spatialerror3.jib;

/**
 *
 * @author spatialerror3
 */
public class JIBSysEnv {
    public JIBSysEnv() {
        
    }
    
    public void envToConfig(String name) {
        if(System.getenv(name)!=null) {
            JavaIrcBouncer.jibConfig.setValue(name, System.getenv(name));
        }
    }
}
