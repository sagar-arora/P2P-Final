import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Process {

	List<AdjacentPeers> remotePeers;
	Handler handler;
	public Process(int peerId, List<AdjacentPeers> remotePeers) {
    	
    	handler=new Handler(peerId, remotePeers);
        initiateCommunications();
        
	}
	
	private void initiateCommunications() {
		
		initClientConnections();
		makeOutPutStreamForClients();
		
		List<Integer> peerIdList=handler.peerIdList;
		while (!handler.every_peer_has_file) {
	        for (int i = 0; i < peerIdList.size(); i++) {
	            if (i != handler.clientId) {
	                conditionalSendHanshake(remotePeers.get(i));
	                conditionalSendBitField(i);
	                conditionalRequestRandomPiece(i);
	            }
	        }
	        if (only_once == false) {
	          initiateNeighbourTaskSchedulers();
	           only_once=true;
	        }
	
	        Message.messageHandling(pd);
}
		
	}

	
	private void conditionalSendHanshake(AdjacentPeers adjacentPeer) {
		
		if (adjacentPeer.has_rcvd_bit_field && adjacentPeer.has_rcvd_handshake &&
				adjacentPeer.is_choked == false && adjacentPeer.is_waiting_for_piece == false) {
				adjacentPeer.is_waiting_for_piece = true;
			    int rqst_piece_num = getReqPieceAtRandom(adjacentPeer);
			    adjacentPeer.piece_num = rqst_piece_num;	
			    if (rqst_piece_num != -1) {
			        Message.sendRequestMessage(handler, rqst_piece_num, adjacentPeer.peerId);
			    }
		
		}
	}

	private int getReqPieceAtRandom(AdjacentPeers adjacentPeer) {
		// TODO Auto-generated method stub
		return 0;
	}

	public void makeOutPutStreamForClients(){
		List<Integer> peerIdList=handler.peerIdList;
		Map<Integer,Socket> socketMap=handler.socket;
		 for (int i = 0; i < peerIdList.size(); i++) {
	            if (i != handler.clientId && handler.remotePeers.get(0).isConnection) {
	            	int peerId=peerIdList.get(i);
	            	Socket socket=socketMap.get(peerId);
	            	try {
						handler.outputStream.put( peerId,new ObjectOutputStream(socket.getOutputStream()));
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
	            	try {
						handler.outputStream.get(peerId).flush();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	            }
	           
		 }
	}
	
	 public  void initClientConnections() {
		 List<Integer> peerIdList=handler.peerIdList; 
	       int remaining_connections = handler.peerIdList.size()-1;
	            while (remaining_connections > 0) {
	                System.out.println();

	                for (int i = 0; i < handler.peerIdList.size()-1; i++) {
	                    if (i != handler.clientId  && !remotePeers.get(i).isConnection) {
	                        System.out.println("Trying to establish connection to " + remotePeers.get(i).host_name + 
	                        		" on port " + remotePeers.get(i).port_num);
	                        try {
	                        	int peerId=remotePeers.get(i).peerId;
	                            Socket peerSocket = new Socket(remotePeers.get(i).host_name, remotePeers.get(i).port_num);
	                            handler.socket.put( peerId,peerSocket);
	                            if (handler.socket.get(peerId).isConnected()) {
	                            	remaining_connections=remaining_connections-1;
	                            	remotePeers.get(i).isConnection=true;
	                            }
	                        } catch (ConnectException e) {
	                            remotePeers.get(i).is_conn_refused = true;
	                        } catch (IOException e) {
	                        	remotePeers.get(i).is_conn_refused = true;
	                        }
	                    }
	                }
	                System.out.println();
	                for (int i = 0; i < peerIdList.size(); i++) {
	                    if (remotePeers.get(i).is_conn_refused) {
	                        
	                    }
	                }
	                if (remaining_connections > 0) {
	                    try {
	                    System.out.println("Waiting to reconnect..");
	                    Thread.sleep(1000);
	                    } catch (Exception e) {

	                    }
	                }
	            }
	    }

	
}
