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
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 *
 * @author spatialerror3
 */
public class JIBEventCore {

    public ArrayList<JIBEventHandler> eventHandlers = null;

    public JIBEventCore() {
        this.eventHandlers = new ArrayList<JIBEventHandler>();
    }

    public void addEventHandler(JIBEventHandler eh) {
        List<JIBEventHandler> syncEH = Collections.synchronizedList(this.eventHandlers);
        synchronized (syncEH) {
            syncEH.add(eh);
        }
    }

    public void handleEvent(Map<String, Object> event) {
        List<JIBEventHandler> syncEH = Collections.synchronizedList(this.eventHandlers);
        synchronized (syncEH) {
            Iterator<JIBEventHandler> it1 = syncEH.iterator();
            while (it1.hasNext()) {
                JIBEventHandler eh1 = it1.next();
                eh1.processEvent(event);
            }
        }
    }
}
