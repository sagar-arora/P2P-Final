
	import java.io.BufferedReader;
	import java.io.FileNotFoundException;
	import java.io.FileReader;
	import java.io.IOException;
	import java.util.*;

	public class PeerConfigParser {
	    private static final LinkedList < AdjacentPeers > neighborpeers = new LinkedList < > ();

	    public void savepeerinfo() {
	        BufferedReader peercfgreader;
	        try {
	            peercfgreader = new BufferedReader(new FileReader("PeerInfo.cfg"));

	            String nextline = null;
	            try {
	                while ((nextline = peercfgreader.readLine()) != null) {
	                    String[] peerinfotokens = nextline.split("\\s+");
	                    int id = Integer.parseInt(peerinfotokens[0]);
	                    int port = Integer.parseInt(peerinfotokens[2]);
	                    String address = peerinfotokens[1];
	                    boolean hasFile = Integer.parseInt(peerinfotokens[3]) == 1;
	                    AdjacentPeers newpeer;
	                    newpeer = new AdjacentPeers(id,address,port,hasFile);
	                    neighborpeers.add(newpeer);
	                    System.out.println("Added new peer");
	                    System.out.println(newpeer.host_name+" "+ newpeer.hasFile);
	                }
	            }
	            catch (IOException e1) {
	                // TODO Auto-generated catch block
	                e1.printStackTrace();
	            }

	        } catch (FileNotFoundException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        }

	    }

	    public LinkedList getneighborinfo()
	    {
	        return neighborpeers;
	    }
	}