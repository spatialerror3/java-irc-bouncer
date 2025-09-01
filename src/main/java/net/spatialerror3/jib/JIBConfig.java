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

import java.io.Serializable;
import java.util.HashMap;
import java.util.Hashtable;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author spatialerror3
 */
public class JIBConfig implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LogManager.getLogger(JIBConfig.class);
    private HashMap<String, String> kvStore = null;

    /**
     * constructor
     */
    public JIBConfig() {
        kvStore = new HashMap<String, String>();
    }
    
    /**
     *
     * @return available options
     */
    private Options getOptions() {
        Options options = null;
        options = new Options();

        Option help = new Option("help", "print help");
        Option server = Option.builder().argName("server").hasArg().desc("specify irc server for admin user").build();

        options.addOption(help);
        options.addOption(server);

        return options;
    }

    /**
     *
     * @param args
     */
    public void parseArgs(String[] args) {
        CommandLineParser parser = new DefaultParser();
        Options options = null;
        CommandLine line = null;

        options = getOptions();

        try {
            line = parser.parse(options, args);
        } catch (ParseException exp) {
            log.error("Parsing failed.  Reason: " + exp.getMessage());
        }
        if (line != null) {
            if (line.hasOption("server")) {
                setValue("Server", line.getOptionValue("server"));
            }
        }
        /*
        if (args.length > 0) {
            setValue("Server", args[0]);
        }
         */
    }

    /**
     *
     * @param Key
     * @param Value
     */
    public void setValue(String Key, String Value) {
        if (Value != null) {
            kvStore.put(Key, Value);
        }
    }

    /**
     *
     * @param Key
     * @return
     */
    public String getValue(String Key) {
        return kvStore.get(Key);
    }

    /**
     *
     * @param userName
     * @param pass
     */
    public void createUser(String userName, String pass) {
        JIBUser u = JavaIrcBouncer.jibCore.createUser(userName, false);
        u.setAuthToken(pass);
    }

    /**
     *
     * @param userName
     * @param pass
     * @param n
     * @param id
     * @param r
     * @param server
     * @param port
     */
    public void createUser(String userName, String pass, String n, String id, String r, String server, int port) {
        JIBUser u = JavaIrcBouncer.jibCore.createUser(userName, false);
        u.setAuthToken(pass);
        u.setNick(n);
        u.setUser(id);
        u.setRealname(r);
        JIBIRCServer tmpServ = new JIBIRCServer();
        tmpServ.setServer(server);
        tmpServ.setPort(port);
        tmpServ.setSsl(true);
        tmpServ.setNick(n);
        tmpServ.setUser(id);
        tmpServ.setRealname(r);
        u.addIrcServer(tmpServ);
    }

    /**
     *
     * @param userName
     * @param pass
     * @param n
     * @param id
     * @param r
     * @param server
     * @param port
     */
    public void createUser(String userName, String pass, String n, String id, String r, String server, String port) {
        JIBUser u = JavaIrcBouncer.jibCore.createUser(userName, false);
        u.setAuthToken(pass);
        u.setNick(n);
        u.setUser(id);
        u.setRealname(r);
        JIBIRCServer tmpServ = new JIBIRCServer();
        tmpServ.setServer(server);
        tmpServ.setPort(port);
        //tmpServ.setSsl(true);
        tmpServ.setNick(n);
        tmpServ.setUser(id);
        tmpServ.setRealname(r);
        u.addIrcServer(tmpServ);
    }

    /**
     *
     * @param userName
     * @param pass
     * @param n
     * @param id
     * @param r
     * @param server
     * @param port
     * @param nsacct
     * @param nspass
     * @param chans
     */
    public void createUser(String userName, String pass, String n, String id, String r, String server, int port, String nsacct, String nspass, String chans) {
        JIBUser u = JavaIrcBouncer.jibCore.createUser(userName, false);
        u.setAuthToken(pass);
        u.setNick(n);
        u.setUser(id);
        u.setRealname(r);
        JIBIRCServer tmpServ = new JIBIRCServer();
        tmpServ.setServer(server);
        tmpServ.setPort(port);
        tmpServ.setSsl(true);
        tmpServ.setNick(n);
        tmpServ.setUser(id);
        tmpServ.setRealname(r);
        tmpServ.setNickServUser(nsacct);
        tmpServ.setNickServPass(nspass);
        tmpServ.addChannels(chans);
        u.addIrcServer(tmpServ);
    }

    /**
     *
     * @param userName
     * @param pass
     * @param n
     * @param id
     * @param r
     * @param server
     * @param port
     * @param nsacct
     * @param nspass
     * @param chans
     */
    public void createUser(String userName, String pass, String n, String id, String r, String server, String port, String nsacct, String nspass, String chans) {
        JIBUser u = JavaIrcBouncer.jibCore.createUser(userName, false);
        u.setAuthToken(pass);
        u.setNick(n);
        u.setUser(id);
        u.setRealname(r);
        JIBIRCServer tmpServ = new JIBIRCServer();
        tmpServ.setServer(server);
        tmpServ.setPort(port);
        //tmpServ.setSsl(true);
        tmpServ.setNick(n);
        tmpServ.setUser(id);
        tmpServ.setRealname(r);
        tmpServ.setNickServUser(nsacct);
        tmpServ.setNickServPass(nspass);
        tmpServ.addChannels(chans);
        u.addIrcServer(tmpServ);
    }
}
