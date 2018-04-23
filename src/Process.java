import java.io.IOException;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.net.ConnectException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Process {

	
	List<AdjacentPeers> remotePeers;
	Handler handler;
	public int peerId;
	static boolean only_once=false;
	public Process(int peerId, List<AdjacentPeers> remotePeers) {
    	this.peerId=peerId;
    	handler=new Handler(peerId, remotePeers);
    	this.remotePeers=remotePeers;
        initiateCommunications();
        
	}
	
	private void initiateCommunications() {
		System.out.println("in process");
		int portNumber=Integer.valueOf(remotePeers.get(handler.clientId).port_num);
		handler.sc=new ServerConnector(portNumber);
		initClientConnections();
		makeOutPutStreamForClients();
		
		List<Integer> peerIdList=handler.peerIdList;
		while (!handler.every_peer_has_file) {
	        for (int i = 0; i < peerIdList.size(); i++) {
	            if (i != handler.clientId) {
	                conditionalSendHanshake(remotePeers.get(i));
	                conditionalSendBitField(remotePeers.get(i));
	                conditionalRequestRandomPiece(remotePeers.get(i));
	            }
	        }
	        if (only_once == false) {
	        //  Scheduler.initiateNeighbourTaskSchedulers(handler);
	           only_once=true;
	        }
	
	        MessageHandler.messageHandling(handler);
		}
		
	}

	
	private void conditionalRequestRandomPiece(AdjacentPeers adjacentPeer) {
		if (adjacentPeer.has_rcvd_bit_field &&
				adjacentPeer.has_rcvd_handshake &&
				adjacentPeer.is_choked == false &&
						adjacentPeer.is_waiting_for_piece == false) {
				
			adjacentPeer.is_waiting_for_piece = true;
			    int rqst_piece_num = getReqPieceAtRandom(adjacentPeer);
			    adjacentPeer.piece_num = rqst_piece_num;	
			    if (rqst_piece_num != -1) {
			        Message.sendRequestMessage(handler, rqst_piece_num,Message.find(handler,adjacentPeer.peerId));
			        System.out.println("handshake message sent from host"+adjacentPeer.host_name+"on"+adjacentPeer.peerId);
			    }
			}
		
		
	}

	private void conditionalSendBitField(AdjacentPeers adjacentPeer) {
		if (!adjacentPeer.has_sent_bitfield && adjacentPeer.isConnection && adjacentPeer.has_rcvd_handshake) {
		    Message.sendBitfield(handler,Message.find(handler, adjacentPeer.peerId));
		    adjacentPeer.has_sent_bitfield = true;
		    System.out.println("bitfield message sent from host"+adjacentPeer.host_name+"on"+adjacentPeer.peerId);
		}
		
	}

	private void conditionalSendHanshake(AdjacentPeers adjacentPeer) {
		if (!adjacentPeer.has_sent_handshake && adjacentPeer.isConnection) {
			Message.sendHandShakeMessage(handler,Message.find(handler,adjacentPeer.peerId));
		    adjacentPeer.has_sent_handshake = true;
		    System.out.println("handshake message sent from host"+adjacentPeer.host_name+"on"+adjacentPeer.peerId);
		    handler.pfl.tcpconnected(handler.peerId, adjacentPeer.peerId);
		}
	}

	private int getReqPieceAtRandom(AdjacentPeers adjacentPeer) {
		BigInteger self_field = new BigInteger(handler.fh.bitfield);
        BigInteger neighbour_field = new BigInteger(adjacentPeer.bit_field_map);
        BigInteger c=andNot(self_field, neighbour_field);
        BigInteger interesting_field = neighbour_field.and(c);
        int[] values = new int[interesting_field.bitLength()];
        int k = 0;
        boolean interesting_bit_exists = false;
        int i=0;
        while( i < interesting_field.bitLength()){
            if (interesting_field.testBit(i)) {
                interesting_bit_exists = true;
                values[k++] = i++;
            }
        }        
        if (interesting_bit_exists) {
            return values[handler.random_num_gen.nextInt(k)];
        } else {
            return -1;
        }
	}

	public static BigInteger andNot(BigInteger a, BigInteger b){
		return	(a.and(b)).not();
	}
	
	public void makeOutPutStreamForClients(){
		List<Integer> peerIdList=handler.peerIdList;
		Map<Integer,Socket> socketMap=handler.socket;
		 for (int i = 0; i < peerIdList.size(); i++) {
	            if (i != handler.clientId && handler.remotePeers.get(i).isConnection) {
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

	                for (int i = 0; i < handler.peerIdList.size(); i++) {
	                    if (i != handler.clientId  && !remotePeers.get(i).isConnection) {
	                        System.out.println("Trying to establish connection to " + remotePeers.get(i).host_name + 
	                        		" on port " + remotePeers.get(i).port_num);
	                        try {
	                        	int peerId=remotePeers.get(i).peerId;
	                            Socket peerSocket = new Socket(remotePeers.get(i).host_name, remotePeers.get(i).port_num);
	                            if(peerSocket!=null)
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
