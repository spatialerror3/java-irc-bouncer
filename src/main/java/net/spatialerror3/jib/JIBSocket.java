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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import javax.net.ssl.SSLHandshakeException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author spatialerror3
 */
public class JIBSocket {

    private static final Logger log = LogManager.getLogger(JIBSocket.class);
    private static boolean SOCKETDEBUGGING = false;
    //
    Socket s = null;
    InputStream IS = null;
    InputStreamReader ISR = null;
    BufferedReader BR = null;
    OutputStream OS = null;
    OutputStreamWriter OSW = null;
    BufferedWriter BW = null;
    //
    Exception e = null;
    //
    long linesWritten = 0L;
    long linesRead = 0L;
    //
    SocketAddress remoteAddr = null;

    /**
     *
     * @param s
     */
    public JIBSocket(Socket s) {
        JIBSocket.SOCKETDEBUGGING = (JavaIrcBouncer.jibConfig.getValue("SOCKETDEBUGGING") != null ? true : false);
        this.s = s;
        if (this.s == null) {
            if (e == null) {
                e = new NullPointerException();
            }
            return;
        }
        this.remoteAddr = s.getRemoteSocketAddress();
        try {
            IS = s.getInputStream();
        } catch (SSLHandshakeException ex) {
            if (e == null) {
                e = ex;
            }
            if (JIBSocket.SOCKETDEBUGGING) {
                log.error((String) null, ex);
            }
        } catch (EOFException ex) {
            if (e == null) {
                e = ex;
            }
            if (JIBSocket.SOCKETDEBUGGING) {
                log.error((String) null, ex);
            }
        } catch (SocketException ex) {
            if (e == null) {
                e = ex;
            }
            if (JIBSocket.SOCKETDEBUGGING) {
                log.error((String) null, ex);
            }
        } catch (IOException ex) {
            if (e == null) {
                e = ex;
            }
            log.error((String) null, ex);
        }
        if (IS != null) {
            ISR = new InputStreamReader(IS);
            BR = new BufferedReader(ISR);
        }
        try {
            OS = s.getOutputStream();
        } catch (SSLHandshakeException ex) {
            if (e == null) {
                e = ex;
            }
            if (JIBSocket.SOCKETDEBUGGING) {
                log.error((String) null, ex);
            }
        } catch (EOFException ex) {
            if (e == null) {
                e = ex;
            }
            if (JIBSocket.SOCKETDEBUGGING) {
                log.error((String) null, ex);
            }
        } catch (SocketException ex) {
            if (e == null) {
                e = ex;
            }
            if (JIBSocket.SOCKETDEBUGGING) {
                log.error((String) null, ex);
            }
        } catch (IOException ex) {
            if (e == null) {
                e = ex;
            }
            log.error((String) null, ex);
        }
        if (OS != null) {
            OSW = new OutputStreamWriter(OS);
            BW = new BufferedWriter(OSW);
        }
    }

    /**
     *
     * @param l
     * @return
     */
    public JIBSocket writeLineNoEOL(String l) {
        return writeLine(JIBStringUtil.remEOL2(l) + "\r\n");
    }

    /**
     *
     * @param l
     * @return
     */
    public JIBSocket writeLine(String l) {
        if (getError() != null) {
            return null;
        }
        linesWritten++;
        try {
            BW.write(l);
            BW.flush();
            OSW.flush();
            OS.flush();
        } catch (SSLHandshakeException ex) {
            if (e == null) {
                e = ex;
            }
            if (JIBSocket.SOCKETDEBUGGING) {
                log.error((String) null, ex);
            }
            return null;
        } catch (EOFException ex) {
            if (e == null) {
                e = ex;
            }
            if (JIBSocket.SOCKETDEBUGGING) {
                log.error((String) null, ex);
            }
            return null;
        } catch (SocketException ex) {
            if (e == null) {
                e = ex;
            }
            if (JIBSocket.SOCKETDEBUGGING) {
                log.error((String) null, ex);
            }
            return null;
        } catch (IOException ex) {
            if (e == null) {
                e = ex;
            }
            log.error((String) null, ex);
            return null;
        }
        if (JIBSocket.SOCKETDEBUGGING) {
            System.err.println(this.s + " writeLine()=" + JIBStringUtil.remEOL(l));
        }
        return this;
    }

    /**
     *
     * @return
     */
    public String readLine() {
        String l = null;
        try {
            if (BR != null) {
                l = BR.readLine();
            }
            if (l == null) {
                if (e == null) {
                    e = new NullPointerException();
                }
            }
        } catch (SSLHandshakeException ex) {
            if (e == null) {
                e = ex;
            }
            if (JIBSocket.SOCKETDEBUGGING) {
                log.error((String) null, ex);
            }
            return null;
        } catch (EOFException ex) {
            if (e == null) {
                e = ex;
            }
            if (JIBSocket.SOCKETDEBUGGING) {
                log.error((String) null, ex);
            }
            return null;
        } catch (SocketException ex) {
            if (e == null) {
                e = ex;
            }
            if (JIBSocket.SOCKETDEBUGGING) {
                log.error((String) null, ex);
            }
        } catch (IOException ex) {
            if (e == null) {
                e = ex;
            }
            log.error((String) null, ex);
        }
        if (JIBSocket.SOCKETDEBUGGING) {
            System.err.println(this.s + " readLine()=" + JIBStringUtil.remEOL(l));
        }
        linesRead++;
        return l;
    }

    /**
     *
     * @return
     */
    public Exception getError() {
        return e;
    }

    public long getLinesRead() {
        return this.linesRead;
    }

    public long getLinesWritten() {
        return this.linesWritten;
    }

    /**
     *
     * @return
     */
    public boolean connected() {
        if (e != null) {
            return false;
        }
        return s.isConnected();
    }
}
