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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.UUID;
import java.util.Vector;

/**
 *
 * @author spatialerror3
 */
public class JIBCore {

    private static long userCnt = 0;
    ArrayList<JIBUser> users = null;
    HashMap<String, JIBUser> userMap = null;
    HashMap<UUID, JIBUser> userMap2 = null;

    /**
     *
     */
    public JIBCore() {
        users = new ArrayList<JIBUser>();
        userMap = new HashMap<String, JIBUser>();
        userMap2 = new HashMap<UUID, JIBUser>();
    }

    /**
     *
     * @param userName
     * @param admin
     * @return
     */
    public JIBUser createUser(String userName, boolean admin) {
        return createUser(userName, admin, false);
    }

    /**
     *
     * @param userName
     * @param admin
     * @param nodb
     * @return
     */
    public JIBUser createUser(String userName, boolean admin, boolean nodb) {
        if (getUser(userName) != null) {
            return getUser(userName);
        }
        JIBUser u = new JIBUser();
        userCnt++;
        long newUserId = JavaIrcBouncer.jibDbUtil.getUsersMaxUserId();
        ++newUserId;
        u.setUserId(newUserId);
        u.setUserName(userName);
        u.setAdmin(admin);
        userMap.put(userName, u);
        userMap2.put(u.getUUID(), u);
        users.add(u);
        if (!nodb) {
            JavaIrcBouncer.jibDbUtil.addUser(u);
        }
        return u;
    }

    public JIBUser loadUser(JIBUser u, String userName, boolean admin) {
        if (getUser(userName) != null) {
            return getUser(userName);
        }
        if (getUser(u.getUserName()) != null) {
            return getUser(u.getUserName());
        }
        userCnt++;
        long newUserId = JavaIrcBouncer.jibDbUtil.getUsersMaxUserId();
        ++newUserId;
        u.setUserId(newUserId);
        u.setUserName(userName);
        u.setAdmin(admin);
        userMap.put(userName, u);
        userMap2.put(u.getUUID(), u);
        users.add(u);
        
        return u;
    }

    /**
     *
     * @param u
     */
    public void removeUser(JIBUser u) {
        userMap.remove(u.getUserName(), u);
        userMap2.remove(u.getUUID(), u);
        users.remove(u);
        JavaIrcBouncer.jibDbUtil.removeUser(u);
    }

    /**
     *
     * @return
     */
    public long getUserCount() {
        return JIBCore.userCnt;
    }

    /**
     *
     * @param userName
     * @return
     */
    public JIBUser getUser(String userName) {
        return userMap.get(userName);
    }

    /**
     *
     * @param uuid
     * @return
     */
    public JIBUser getUser(UUID uuid) {
        return userMap2.get(uuid);
    }

    /**
     *
     * @return
     */
    public Iterator<JIBUser> getUsers() {
        return this.users.iterator();
    }

    /**
     *
     * @param userName
     * @param authToken
     * @return
     */
    public JIBUser authUser(String userName, String authToken) {
        JIBUser u = getUser(userName);
        if (u == null) {
            return null;
        }
        return u.auth(authToken);
    }
}
