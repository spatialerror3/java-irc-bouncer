/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package net.spatialerror3.jib;

import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author spatialerror3
 */
public class JIBIRCPerform {

    private ArrayList<String> performList = null;
    private JIBUser u = null;
    private JIBIRCServer serv = null;

    public JIBIRCPerform(JIBUser u, JIBIRCServer serv) {
        this.performList = new ArrayList<String>();
        this.u = u;
        this.serv = serv;
    }

    public void performListAdd(String perform) {
        this.performList.add(perform);
    }

    public Iterator<String> performListIterator() {
        return this.performList.iterator();
    }

    public void perform() {
        Iterator<String> it3 = performList.iterator();
        while (it3.hasNext()) {
            u.getJibIRC().writeLine(it3.next() + "\r\n");
        }
    }
}
