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
public class JIBIRCLogon implements Runnable {

    private static final Logger log = LogManager.getLogger(JIBIRCLogon.class);
    private JIBIRC i = null;
    private JIBUser u = null;

    public JIBIRCLogon(JIBIRC i, JIBUser u) {
        this.i = i;
        this.u = u;
    }

    public void run() {
        try {
            this.i.getNS().identify();
        } catch (Exception e) {
            log.error((String) null, e);
        }
        try {
            Thread.sleep(30000);
        } catch (InterruptedException ex) {
            log.error((String) null, ex);
        }
        try {
            this.i.getPerform().perform();
        } catch (Exception e) {
            log.error((String) null, e);
        }
        try {
            String[] chans = JavaIrcBouncer.jibDbUtil.getChannels(u);
            for (int i = 0; i < chans.length; i++) {
                if (chans[i] != null) {
                    this.i.writeLine("JOIN " + chans[i] + "\r\n");
                }
            }
        } catch (Exception e) {
            log.error((String) null, e);
        }
        this.i.setPreLogon(false);
    }
}
