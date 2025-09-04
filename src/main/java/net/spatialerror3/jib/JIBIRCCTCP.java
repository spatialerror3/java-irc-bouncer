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
public class JIBIRCCTCP implements JIBIRCLineProcessing {

    private static final Logger log = LogManager.getLogger(JIBIRCCTCP.class);

    private String ctcpMsg(String msg) {
        String _ctcpMsg = null;
        if (msg.charAt(0) == '\001' && msg.charAt(msg.length() - 1) == '\001') {
            _ctcpMsg = msg.substring(1, msg.length() - 1);
            String[] _ctcpMsgSp1 = _ctcpMsg.split(" ", 2);
            log.info("Received CTCP " + _ctcpMsgSp1[0]);
            if (_ctcpMsgSp1[0].equals("ENTROPY")) {
                log.info("ENTROPY ENTROPY=" + _ctcpMsgSp1[1]);
            }
        }
        return _ctcpMsg;
    }

    @Override
    public void processLine(JIBUser u, JIBIRC i, JIBIRCServer s, String l) {
        String[] sp1 = l.split(" ", 3);
        if (sp1[1].equals("PRIVMSG") || sp1[1].equals("NOTICE")) {
            JIBUserInfo ui1 = JIBUserInfo.parseNUH(sp1[0]);
            String[] sp2 = sp1[2].split(" ", 2);
            String target = sp2[0];
            String msg = JIBStringUtil.remDD(sp2[1]);
            String ctcp = ctcpMsg(msg);
            if (ctcp != null) {
                String[] ctcpsp1 = ctcp.split(" ", 2);
                if (ctcpsp1.equals("ENTROPY")) {
                    if (!target.equals(i.getNick())) {
                        i.writeLine("PRIVMSG " + target + " :" + JIBStringUtil.randHexString() + "\r\n");
                    }
                }
            }
        }
    }

}
