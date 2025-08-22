/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package net.spatialerror3.jib;

import java.io.Serializable;

/**
 *
 * @author spatialerror3
 */
public class JIBIRCNetType implements Serializable {
    private static final long serialVersionUID = 1L;
    public enum NetType {
        GENERIC,
        LIBERA,
        OFTC,
        DALNET,
        UNDERNET,
        QUAKENET,
        HYBRID,
        INSPIRCD,
        UNREALIRCD
    }
}
