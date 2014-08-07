package application;

import application.net.SSHManager;

public class SDEUtils {
    public static SSHManager openSSHSession(String connectionIP, String username, String password) {
        //JSch.setConfig("StrictHostKeyChecking", "yes");
        SSHManager instance = new SSHManager(username, password, connectionIP, "C:\\Users\\alex\\.ssh\\known_hosts");
        String errorMessage = instance.connect();
        instance.createShellChannel();

        if (errorMessage != null) {
            System.out.println("ERROR " + errorMessage);
        }

        //String result = instance.sendCommand(command);
        //instance.scpFrom("/home/spiralinks/test.txt", "C:\\Users\\alex\\Desktop\\Meetup\\test.txt");
        // close only after all commands are sent
        return instance;
        //instance.close();
        //System.out.println("Done " + result);
    }

    public static void sshCommand(String command) {
        System.out.println("sendCommand");
        //JSch.setConfig("StrictHostKeyChecking", "yes");
        String userName = "spiralinks";
        String password = "C0deFreeze09";
        String connectionIP = "172.16.10.212";
        SSHManager instance = new SSHManager(userName, password, connectionIP, "C:\\Users\\alex\\.ssh\\known_hosts");
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
        SSHManager instance = new SSHManager(userName, password, connectionIP, "C:\\Users\\alex\\.ssh\\known_hosts");
        String errorMessage = instance.connect();

        if (errorMessage != null) {
            System.out.println("ERROR " + errorMessage);
        }

        instance.scpTo("/opt/jboss/jboss-4.2.2.GA/server/spl-8080/deploy/focal-v3-rbs2010.ear", "C:\\jboss-4.2.1.GA\\server\\default\\deploy\\focal-v3-rbs2010.ear");
    }
}
