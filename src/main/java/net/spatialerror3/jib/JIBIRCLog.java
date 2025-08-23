/*
 * Copyright (C) 2025 spatialerror3
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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author spatialerror3
 */
public class JIBIRCLog {
    private static final Logger log = LogManager.getLogger(JIBIRCLog.class);
    private JIBUser u = null;
    
    public JIBIRCLog(JIBUser u) {
        this.u=u;
    }
    
    public void processLine(String l) {
        String[] sp7 = l.split(" ",3);
        if(sp7.length > 2 && sp7[1].equals("PRIVMSG")) {
            String[] sp8 = l.split(" ",4);
            String logUser = sp8[0];
            String logTarget = sp8[2];
            String logMessage = sp8[3];
            JavaIrcBouncer.jibDbUtil.logUserTargetMessage(this.u, logUser, logTarget, logMessage);
        }
    }
}
