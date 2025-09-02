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

import java.util.Iterator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author spatialerror3
 */
public class JIBStatus {

    private static final Logger log = LogManager.getLogger(JIBStatus.class);
    private static long bootTime = 0L;

    public JIBStatus() {
        JIBStatus.bootTime = System.currentTimeMillis();
    }

    public int getConnections() {
        int connz = 0;
        Iterator<JIBUser> jui = JavaIrcBouncer.jibCore.getUsers();
        while (jui.hasNext()) {
            JIBUser tmpU = jui.next();
            if (tmpU != null) {
                if (tmpU.getJibIRC() != null) {
                    if (tmpU.getJibIRC().connected() == true) {
                        connz++;
                    }
                }
            }
        }
        return connz;
    }

    public int getUsers() {
        int userz = 0;
        Iterator<JIBUser> jui = JavaIrcBouncer.jibCore.getUsers();
        while (jui.hasNext()) {
            JIBUser tmpU = jui.next();
            if (tmpU != null) {
                userz++;
            }
        }
        return userz;
    }

    public int getUsersConnectedClients() {
        int usersccz = 0;
        Iterator<JIBUser> jui = JavaIrcBouncer.jibCore.getUsers();
        while (jui.hasNext()) {
            JIBUser tmpU = jui.next();
            if (tmpU != null) {
                usersccz += tmpU.clients.size();
            }
        }
        return usersccz;
    }

    public long getUptime() {
        return System.currentTimeMillis() - JIBStatus.bootTime;
    }

    public long getDbLoadedUsers() {
        return JavaIrcBouncer.jibDbUtil.getDbLoadedUsers();
    }
}
