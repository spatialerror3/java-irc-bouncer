/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package net.spatialerror3.jib;

import java.net.Socket;

/**
 *
 * @author spatialerror3
 */
public class JIBHandleClient implements Runnable {
    JIBSocket sock = null;
    private boolean inAuth = true;
    private String authUser = null;
    private String authPass = null;
    private boolean authOk = false;
    //
    private JIBUser authed = null;
    //
    private String trackNick = null;
    
    public JIBHandleClient(Socket cs) {
        sock=new JIBSocket(cs);
        onConnect();
    }

    public void onConnect() {
        sendLine("");
        sendLine(":JIB.jib NOTICE "+"*"+" :AUTHENTICATION MANDATORY\r\n");
    }
    
    public void onAuthDone() {
        String[] channels = JavaIrcBouncer.jibDbUtil.getChannels();
        getSingleJIBIRC().simulateNick(trackNick, null);
        for(int j = 0; j < channels.length; j++) {
            if(channels[j]!= null) {
                getSingleJIBIRC().simulateJoin(channels[j]);
                getSingleJIBIRC().simulateJoin(channels[j], trackNick);
            }
        }
        sendLine(":JIB.jib NOTICE "+trackNick+" :IDENTIFIED AS "+authed.getUUID().toString()+"\r\n");
    }
    
    public Exception getError() {
        return sock.getError();
    }
    
    public JIBIRC getSingleJIBIRC() {
        if(authOk==false)
            return null;
        int dstPort = Integer.valueOf(JavaIrcBouncer.jibConfig.getValue("Port")).intValue();
        if(JavaIrcBouncer.jibIRC==null) {
            if(JavaIrcBouncer.jibConfig.getValue("Server") != null) {
                JavaIrcBouncer.jibIRC=new JIBIRC(JavaIrcBouncer.jibConfig.getValue("Server"),dstPort,JavaIrcBouncer.jibConfig.getValue("Nick"),JavaIrcBouncer.jibConfig.getValue("User"),JavaIrcBouncer.jibConfig.getValue("Realname"));
            } else {
                System.exit(255);
            }
        }
        return JavaIrcBouncer.jibIRC;
    }
    
    public void processError() {
        if(JavaIrcBouncer.jibIRC!=null) {
            if(JavaIrcBouncer.jibIRC.getError()!=null) {
                JavaIrcBouncer.jibIRC=null;
            }
        }
    }
    
    public void sendLine(String l) {
        sock.writeLine(l + "\r\n");
    }
    
    public void checkUserPass() {
        if(this.authUser==null||this.authPass==null) {
            return;
        }
        String cnfUserChk = JavaIrcBouncer.jibConfig.getValue("AUTHUSER");
        String cnfPassChk = JavaIrcBouncer.jibConfig.getValue("AUTHPASS");
        if(this.authUser.equals(cnfUserChk)&&this.authPass.equals(cnfPassChk)) {
            authed = JavaIrcBouncer.jibCore.authUser(this.authUser, this.authPass);
            this.authOk=true;
            onAuthDone();
        } else {
            sendLine("ERROR :Auth failed\r\n");
        }
        this.inAuth=false;
    }
    
    public void processLine(String l) {
        boolean passthrough = true;
        String[] sp = l.split(" ");
        System.err.println("l=" + l);
        if (l.startsWith("CAP")) {
            passthrough=false;
            sendLine(l);
        }
        if (l.startsWith("NICK")) {
            passthrough=false;
            trackNick=l.substring(5);
            sendLine(l);
        }
        if (l.startsWith("USER")) {
            passthrough=false;
            this.authUser=sp[1];
            checkUserPass();
            sendLine(l);
        }
        if (l.startsWith("PASS")) {
            passthrough=false;
            this.authPass=l.substring(5);
            checkUserPass();
            sendLine(l);
        }
        if (l.startsWith("QUIT")) {
            passthrough=false;
            return;
        }
        if(l.startsWith("CAP END")) {
            getSingleJIBIRC();
        }
        if(l.startsWith("JOIN")) {
            String[] channels = l.substring(5).split(",");
            for(int j = 0; j < channels.length; j++) {
                getSingleJIBIRC().simulateJoin(channels[j]);
                JavaIrcBouncer.jibDbUtil.addChannel(channels[j]);
            }
        }
        if(l.startsWith("PRIVMSG")) {
            String[] msgextract = l.split(" ", 3);
            getSingleJIBIRC().simulatePRIVMSG(sp[1], msgextract[2].substring(1));
        }
        if(l.startsWith("PART")) {
            String[] sp4 = l.split(" ",3);
            JavaIrcBouncer.jibDbUtil.removeChannel(sp4[1]);
        }
        if(l.startsWith("RAW")) {
            passthrough=false;
            if(l.length() >= 4) {
              getSingleJIBIRC().writeLine(l.substring(4)+"\r\n");
            }
        }
        if(passthrough) {
            getSingleJIBIRC().writeLine(l+"\r\n");
        }
    }

    public void run() {
        String l = null;
        while (true) {
            processError();
            l=sock.readLine();
            processLine(l);
        }
    }
}
