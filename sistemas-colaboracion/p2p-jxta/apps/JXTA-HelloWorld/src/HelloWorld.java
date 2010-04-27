import java.io.File;
import java.text.MessageFormat;

import net.jxta.peergroup.PeerGroup;
import net.jxta.platform.NetworkManager;
import net.jxta.platform.NetworkManager.ConfigMode;

public class HelloWorld {
    
    static String appName = "HelloWorld";
    
    public static void main(String args[]) throws Exception  {
        System.out.println("Starting JXTA ....");
        
        // Uncomment to disable logging
        // LogManager.getLogManager().reset();
        
        // Config directory will be "./.cache/JxtaHelloWorld
        File configDir = new File(new File(".cache"), appName);
        
        // Create, and Start the default jxta NetPeerGroup
        NetworkManager manager = new NetworkManager(ConfigMode.EDGE, appName, configDir.toURI());
        
        // Connect to network (default group)
        manager.startNetwork();
        
        // Some info about the default group and the created peer
        PeerGroup peerGroup = manager.getNetPeerGroup();
        System.out.println("Hello from JXTA group " + peerGroup.getPeerGroupName());
        System.out.println(" Group ID = " + peerGroup.getPeerGroupID().toString());
        System.out.println(" Peer name = " + peerGroup.getPeerName());
        System.out.println(" Peer ID = " + peerGroup.getPeerID().toString());
        
        System.out.println("Waiting for a rendezvous connection");
        boolean connected = manager.waitForRendezvousConnection(12000);
        System.out.println(MessageFormat.format("Connected :{0}", connected));
        
        System.out.println("Stopping JXTA");
        manager.stopNetwork();
        
        System.out.println("Good Bye ....");
    }
}
