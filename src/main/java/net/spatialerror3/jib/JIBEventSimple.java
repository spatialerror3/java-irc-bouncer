/*
 * Copyright (C) 2026 spatialerror3
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package net.spatialerror3.jib;

/**
 *
 * @author spatialerror3
 */
public class JIBEventSimple {
    public static JIBEvent eventCONNECTED(JIBUser u, JIBIRCServer s) {
        JIBEvent r = JIBEvent.newEvent();
        r.put("CONNECTED", "CONNECTED");
        r.put("USER", u);
        r.put("UUID", u.getUUID());
        r.put("SERVER", s);
        r.put("SERVERUUID", s.getUUID());
        return r;
    }
    
    public static JIBEvent eventERROR(JIBUser u, Exception ex, Error err) {
        JIBEvent r = JIBEvent.newEvent();
        r.put("ERROR", "ERROR");
        r.put("USER", u);
        r.put("UUID", u.getUUID());
        if(ex != null) {
          r.put("EX", ex);
        }
        if(err != null) {
          r.put("ERR", err);
        }
        return r;
    }
}
