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
            String ple = it3.next();
            u.getJibIRC().writeLine(ple + "\r\n");
        }
    }
}
