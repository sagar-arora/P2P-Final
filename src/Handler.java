import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class Handler {

	
	public Map<Integer, Socket> socket;
	public Map<Integer, ObjectOutputStream> outputStream;
	public Map<Integer, ObjectInputStream> inputStream;
	
	List<AdjacentPeers> remotePeers;
	public  byte[] bitfield;
	public  int number_of_bits;
	public  int number_of_bytes;
	public boolean every_peer_has_file;
	public  List<Integer> peerIdList;
	public int clientId=-1;
	
	public Handler(int peerId,List<AdjacentPeers> remotePeers){
		this.remotePeers=remotePeers;
		peerIdList=new ArrayList<>();
		makePeerIdList();
		clientId=Collections.binarySearch(peerIdList, peerId);
	}


	private void makePeerIdList() {
	
		for(AdjacentPeers adjacentPeer:remotePeers){
			peerIdList.add(adjacentPeer.peerId);
		}
		
	}
	
	
	
	
	


}
