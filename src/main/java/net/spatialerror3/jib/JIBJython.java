/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package net.spatialerror3.jib;

import java.io.File;
import org.python.util.PythonInterpreter;

/**
 *
 * @author spatialerror3
 */
public class JIBJython {

    public static PythonInterpreter interp = null;

    public JIBJython() {
        interp = new PythonInterpreter();
        interp.set("a", this);
    }

    public void loadConfig() {
        File cf = new File("./config.jy");
        if (cf.exists() && !cf.isDirectory()) {
            interp.execfile("./config.jy");
        }
    }

    public JIBServer rJIBServ() {
        return JavaIrcBouncer.jibServ;
    }

    public JIBConfig rJIBConfig() {
        return JavaIrcBouncer.jibConfig;
    }
}
