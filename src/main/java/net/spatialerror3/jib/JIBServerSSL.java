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

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Vector;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.TrustManagerFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author spatialerror3
 */
public class JIBServerSSL implements Runnable {

    private static final Logger log = LogManager.getLogger(JIBServerSSL.class);
    SSLServerSocket ss = null;
    Vector<JIBHandleClient> clients = null;
    //
    String trustStoreName = null;
    char[] trustStorePassword = null;
    String keyStoreName = null;
    char[] keyStorePassword = null;

    public JIBServerSSL(JIBServer jibServ) {
        InetAddress bindAddr = null;
        KeyStore trustStore = null;
        InputStream trustStoreIS = null;
        KeyStore keyStore = null;
        InputStream KeyStoreIS = null;
        TrustManagerFactory tmf = null;
        KeyManagerFactory kmf = null;
        SSLContext ctx = null;
        SSLServerSocketFactory sslsff = null;

        clients = jibServ.getClientsVector();

        trustStoreName = JavaIrcBouncer.jibConfig.getValue("trustStoreName".toUpperCase());
        trustStorePassword = JavaIrcBouncer.jibConfig.getValue("trustStorePassword".toUpperCase()).toCharArray();
        keyStoreName = JavaIrcBouncer.jibConfig.getValue("keyStoreName".toUpperCase());
        keyStorePassword = JavaIrcBouncer.jibConfig.getValue("keyStorePassword".toUpperCase()).toCharArray();

        try {
            trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStoreIS = new FileInputStream(trustStoreName);
            trustStore.load(trustStoreIS, trustStorePassword);
            trustStoreIS.close();
            tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmf.init(trustStore);

            keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            InputStream keyStoreIS = new FileInputStream(keyStoreName);
            keyStore.load(keyStoreIS, keyStorePassword);
            keyStoreIS.close();
            kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            kmf.init(keyStore, keyStorePassword);
        } catch (Exception e) {
            log.error((String) null, e);
        }

        try {
            ctx = SSLContext.getInstance("SSLv3");
        } catch (NoSuchAlgorithmException ex) {
            log.error((String) null, ex);
        }
        try {
            ctx.init(kmf.getKeyManagers(), tmf.getTrustManagers(), SecureRandom.getInstanceStrong());
        } catch (Exception ex) {
            log.error((String) null, ex);
        }

        sslsff = ctx.getServerSocketFactory();

        if (JavaIrcBouncer.jibConfig.getValue("ServerBind") != null) {
            try {
                bindAddr = InetAddress.getByName(JavaIrcBouncer.jibConfig.getValue("ServerBind"));
            } catch (UnknownHostException ex) {
                log.error((String) null, ex);
            }
        }
        try {
            ss = (SSLServerSocket) sslsff.createServerSocket(7668, 0, bindAddr);
        } catch (IOException ex) {
            log.error((String) null, ex);
        }
    }

    @Override
    public void run() {
        Socket cs = null;
        JIBHandleClient nc = null;
        while (true) {
            try {
                cs = ss.accept();
            } catch (IOException ex) {
                System.getLogger(JIBServer.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            }
            nc = new JIBHandleClient(cs);
            clients.add(nc);
            Thread ct = new Thread(nc);
            ct.start();
        }
    }
}
