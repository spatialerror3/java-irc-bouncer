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

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Response;
import org.eclipse.jetty.util.Callback;

/**
 *
 * @author spatialerror3
 */
public class JIBHTTPHandlerError extends Handler.Abstract {

    private static final Logger log = LogManager.getLogger(JIBHTTPHandlerError.class);

    @Override
    public boolean handle(Request rqst, Response rspns, Callback clbck) throws Exception {
        log.error(this + " handle() rqst=" + rqst + " rspns=" + rspns + " clbck=" + clbck);
        rspns.write(true, ByteBuffer.wrap(("<html><head><title>JIB</title><meta http-equiv='refresh' content='0; URL=/login/'></head><body><a href='/login/'>login</a></body></html>").getBytes(StandardCharsets.UTF_8)), clbck);
        return true;
    }

}
