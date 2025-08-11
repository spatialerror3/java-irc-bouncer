/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package net.spatialerror3.jib;

import java.util.Hashtable;

/**
 *
 * @author spatialerror3
 */
public class JIBConfig {
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
}
