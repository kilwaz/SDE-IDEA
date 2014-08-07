package application;

import application.net.SSHManager;
import com.jcraft.jsch.JSch;

import java.io.File;
import java.io.IOException;

public class SDEUtils {
    static {
        try {
            knownHosts = new File(System.getProperty("user.home"), ".ssh/known_hosts").getCanonicalPath();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String knownHosts;

    public static SSHManager openSSHSession(String connection, String username, String password) {
        JSch.setConfig("StrictHostKeyChecking", "no");
        SSHManager instance = null;

        String[] connectionInfo = connection.split(":");
        if (connectionInfo.length > 1) {
            instance = new SSHManager(username, password, connectionInfo[0], knownHosts, Integer.parseInt(connectionInfo[1]));
        } else {
            instance = new SSHManager(username, password, connectionInfo[0], knownHosts, 22);
        }

        String errorMessage = instance.connect();
        instance.createShellChannel();

        if (errorMessage != null) {
            System.out.println("ERROR " + errorMessage);
        }

        return instance;
    }

    public static void sshCommand(String command) {
        System.out.println("sendCommand");
        //JSch.setConfig("StrictHostKeyChecking", "yes");
        String userName = "spiralinks";
        String password = "C0deFreeze09";
        String connectionIP = "172.16.10.212";
        SSHManager instance = new SSHManager(userName, password, connectionIP, knownHosts);
        String errorMessage = instance.connect();

        if (errorMessage != null) {
            System.out.println("ERROR " + errorMessage);
        }

        String result = instance.sendCommand(command);
        //instance.scpFrom("/home/spiralinks/test.txt", "C:\\Users\\alex\\Desktop\\Meetup\\test.txt");
        // close only after all commands are sent

        instance.close();
        System.out.println("Done " + result);
    }

    public static void scpTo() {
        System.out.println("SCP");
        String userName = "spiralinks";
        String password = "C0deFreeze09";
        String connectionIP = "172.16.10.212";
        SSHManager instance = new SSHManager(userName, password, connectionIP, knownHosts);
        String errorMessage = instance.connect();

        if (errorMessage != null) {
            System.out.println("ERROR " + errorMessage);
        }

        instance.scpTo("/opt/jboss/jboss-4.2.2.GA/server/spl-8080/deploy/focal-v3-rbs2010.ear", "C:\\jboss-4.2.1.GA\\server\\default\\deploy\\focal-v3-rbs2010.ear");
    }
}
