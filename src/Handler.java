import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Timer;

public class Handler {

	public Map<Integer, Socket> socket;
	public Map<Integer, ObjectOutputStream> outputStream;
	public Map<Integer, ObjectInputStream> inputStream;
	public FileHandler fh;
	public ServerConnector sc;
	public Random random_num_gen ;
	
	List<AdjacentPeers> remotePeers;
	public  int number_of_bits;
	public  int number_of_bytes;
	public boolean every_peer_has_file;
	public  List<Integer> peerIdList;
	List<Boolean> hasFile;
	List<Integer> receivedData;
	public int clientId=-1;
	public int peerId;
    Timer pref_neighbours_scheduler;
    Timer opt_neighbour_scheduler;
    List<Integer> pref_neighbours_clIDs;
    Map<Integer, Integer> clientIdtoPeerId;
    public PeerLogfile pfl;
	public Handler(int peerId,List<AdjacentPeers> remotePeers){
		this.peerId=peerId;
		pfl=new PeerLogfile();
		this.remotePeers=remotePeers;
		peerIdList=new ArrayList<>();
		hasFile=new ArrayList<>();
		receivedData=new ArrayList<>();
		socket=new HashMap<>();
		outputStream=new HashMap<>();
		inputStream=new HashMap<>();
		makePeerIdList();
		clientId=Collections.binarySearch(peerIdList, peerId);
		fh=new FileHandler(peerId, CommonConfigParser.filesize, CommonConfigParser.pieceSize, 
				CommonConfigParser.Filename, hasFile.get(clientId));
		random_num_gen = new Random();
		pref_neighbours_clIDs=new ArrayList<>();
		pref_neighbours_scheduler=new Timer();
		clientIdtoPeerId=new HashMap<>();
		opt_neighbour_scheduler=new Timer();
		receivedData=new ArrayList<>();
		}


	private void makePeerIdList() {
		for(AdjacentPeers adjacentPeer:remotePeers){
			peerIdList.add(adjacentPeer.peerId);
			hasFile.add(adjacentPeer.hasFile);
		}
	
	
	
	}
	
	
	
	
	


}
