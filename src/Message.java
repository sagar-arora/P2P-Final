
import java.io.IOException;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.*;

public class Message {

	public static MESS getMsgType(byte b) {

		if (b == 0)
			return MESS.CHOKE;
		else if (b == 1)
			return MESS.UNCHOKE;
		else if (b == 2)
			return MESS.INTERESTED;
		else if (b == 3)
			return MESS.NOT_INTERESTED;
		else if (b == 4)
			return MESS.HAVE;
		else if (b == 5)
			return MESS.BITFIELD;
		else if (b == 6)
			return MESS.REQUEST;
		else if (b == 7)
			return MESS.PIECE;
		else if (b == 8)
			return MESS.HANDSHAKE;
		else
			return null;
	}

	public int length;
	public byte[] lengthB;
	public MESS type;
	public byte[] payload;
	public int clID = -1;

	public Message(int length, byte type, byte[] payload, int clientId) {
		this.lengthB = ByteBuffer.allocate(4).putInt(length).array();
		this.type = getMsgType(type);
		this.length = length;
		this.clID = clientId;
		if (this.type == MESS.CHOKE || this.type == MESS.UNCHOKE || this.type == MESS.INTERESTED
				|| this.type == MESS.NOT_INTERESTED) {
			this.payload = null;
		} else {
			this.payload = new byte[length];
			this.payload = payload;
		}

	}

	public Message(byte[] data, int clientId) {

		byte[] message_payload = Arrays.copyOfRange(data, 5, data.length);
		int message_length = ByteBuffer.allocate(4).put(Arrays.copyOfRange(data, 0, 4)).getInt(0);
		this.type = type;
		this.payload = payload;
		this.clID = clientId;
	}

	public static void sendMessage(Handler handler, byte[] message, int index) {
		try {
			handler.outputStream.get(handler.peerIdList.get(index)).writeObject(message);
			handler.outputStream.get(handler.peerIdList.get(index)).flush();
		} catch (IOException e) {
			System.err.println("Message not sent error.");
		}
	}

	// public static void createMessage(int length,)

	public static byte[] getMessage(MESS type) {
		byte[] length = ByteBuffer.allocate(4).putInt(1).array();
		ByteBuffer bf = ByteBuffer.allocate(5);
		byte[] messageType = ByteBuffer.allocate(1).put(type.messageValue).array();
		bf.put(length);
		bf.put(messageType);
		return bf.array();
	}

	/*
	 * public static byte[] getMessage(byte[] data, MESS type){ int
	 * dataLength=ByteBuffer.wrap(data).getInt(); byte[]
	 * messageLength=ByteBuffer.allocate(4).putInt(dataLength+1).array();
	 * ByteBuffer bf=ByteBuffer.allocate(data.length+5); byte[]
	 * messageType=ByteBuffer.allocate(1).putInt(type.messageValue).array();
	 * bf.put(messageLength); bf.put(messageType); bf.put(data); return
	 * bf.array(); }
	 */
	public static byte[] getMessage(int length, MESS type, byte[] data) {
		byte[] dataLength = ByteBuffer.allocate(4).putInt(length).array();
		byte[] messageType = ByteBuffer.allocate(1).put(type.messageValue).array();
		ByteBuffer bf = ByteBuffer.allocate(length + 5);
		bf.put(dataLength);
		bf.put(messageType);
		bf.put(data);
		return bf.array();
	}

	public static byte[] getMessage(byte[] data, MESS type) {
		byte[] messageLength = ByteBuffer.allocate(4).putInt(5).array();
		ByteBuffer bf = ByteBuffer.allocate(9);
		byte[] messageType = ByteBuffer.allocate(1).put(type.messageValue).array();
		bf.put(messageLength);
		bf.put(messageType);
		bf.put(data);
		return bf.array();
	}

	public static void sendRequestMessage(Handler handler, int piece, int index) {
		byte[] pieceIndex = ByteBuffer.allocate(4).putInt(piece).array();
		byte[] message = getMessage(pieceIndex, MESS.REQUEST);
		sendMessage(handler, message, index);
	}

	public static void sendHandShakeMessage(Handler handler, int index) {
		byte[] hnd_shake_hdr = new byte[18];
		try {
			hnd_shake_hdr = "P2PFILESHARINGPROJ".getBytes("UTF-8");
		} catch (Exception e) {

		}
		byte[] zero_bits = new byte[10];
		byte[] peer_id_Arr = ByteBuffer.allocate(4).putInt(handler.peerId).array();

		ByteBuffer handShakeBuffer = ByteBuffer.allocate(32);

		handShakeBuffer.put(hnd_shake_hdr);
		handShakeBuffer.put(zero_bits);
		handShakeBuffer.put(peer_id_Arr);
		handShakeBuffer.clear();
		byte[] handShakeArray = handShakeBuffer.array();
		sendMessage(handler, handShakeArray, index);
	}
	
	 public static int find(Handler handler, Integer peerId) {
		
		int temp = -1;
		
			for (int i = 0; i < handler.peerIdList.size(); i++) {
			if (handler.peerIdList.get(i) - peerId==0) {
				temp = i;
				break;
				}
			}
		
		return temp;

	} 

	static void bitFieldHandler(Handler handler, Message incomingMessage, int index) {
		//int index=findIndex(handler,index);
		handler.remotePeers.get(index).bit_field_map = incomingMessage.payload;
		handler.remotePeers.get(index).has_rcvd_bit_field = true;
		if (checkNeededPieces(handler.remotePeers.get(index), handler)) {
			Message.sendInterested(index, handler);
		} else {
			Message.sendNotInterested(handler, index);
		}
	}

	public static void sendNotInterested(Handler handler, int index) {
		sendMessage(handler, getMessage(MESS.NOT_INTERESTED), index);
	}

	public static void sendBitfield(Handler handler, int index) {
		//int index=findIndex(handler, peerId);
		byte[] bitfieldMessage = getMessage(handler.fh.bitfield.length, MESS.BITFIELD, handler.fh.bitfield);
		sendMessage(handler, bitfieldMessage, index);
	}

	public static void sendInterested(int index, Handler handler) {

		sendMessage(handler, getMessage(MESS.INTERESTED), index);
	}

	static void pieceMessageHandler(Handler handler, Message incomingMessage, int index) {
		handler.fh.file_pieces[handler.remotePeers.get(index).piece_num] = incomingMessage.payload;

		handler.remotePeers.get(index).is_waiting_for_piece = false;

		BigInteger tempField = new BigInteger(handler.fh.bitfield);

		tempField = tempField.setBit(handler.remotePeers.get(index).piece_num);

	/*	handler.fh.bitfield = tempField.toByteArray();
		if(handler.receivedData.get(index)==null){
		 handler.receivedData.set(index,0);
		}
		int sum=handler.receivedData.get(index)+ handler.fh.pieceSize;
		handler.receivedData.set(0, sum);*/
		// handler.writelogs.pieceDownloaded(handler.other_peer_Ids[handler.my_clID],
		// handler.peer_neighbours[msg_index].peerId,
		// handler.peer_neighbours[msg_index].piece_num, ++handler.tot_pieces);

		boolean haveFile = true;
		for (int i = 0; i < handler.fh.number_of_bits; i++) {
			if (!tempField.testBit(i)) {
				haveFile = false;
				break;
			}
		}

		handler.hasFile.set((handler.clientId), haveFile);

		if (haveFile) {
			// handler.writelogs.fileDownloaded(handler.my_peer_Id);
		}
		int i2 = 0;
		for (int peerId : handler.peerIdList) {
			if (i2 == handler.clientId)
				continue;
			Message.sendHave(i2, handler.remotePeers.get(index).piece_num, handler);
			i2++;
		}

		handler.remotePeers.get(index).piece_num = -1;

		int j = 0;
		for (AdjacentPeers peerId : handler.remotePeers) {
			if (j == handler.clientId)
				continue;

			boolean interested = false;

			if (handler.remotePeers.get(j).bit_field_map != null)
				interested = checkNeededPieces(handler.remotePeers.get(j), handler);

			if (!interested)
				Message.sendNotInterested(handler, j);
			j++;
		}

		int j2 = 0;
		boolean all_have_file = true;
		for (int peerId : handler.peerIdList) {
			if (!handler.hasFile.get(j2)) {
				all_have_file = false;
			}
			j2++;
		}

		handler.every_peer_has_file = all_have_file;
	}

	public static boolean checkNeededPieces(AdjacentPeers neighbor, Handler handler) {
		BigInteger self_field = new BigInteger(handler.fh.bitfield);
		BigInteger neighbour_field = new BigInteger(neighbor.bit_field_map);

		if (neighbour_field.and(self_field.and(neighbour_field).not()).doubleValue() > 0) {
			return true;
		}
		return false;
	}

	public static void handShakeHandler(Handler handler, Message incomingMessage, Integer msg_index) {
		System.out.println("only fucking here!");
		handler.clientIdtoPeerId.put(incomingMessage.clID, incomingMessage.length);
		System.out.println("here!");
		for (int i = 0; i < handler.peerIdList.size(); i++) {
			if (handler.peerIdList.get(i) == incomingMessage.length) {
				msg_index = i;
				System.out.println("shit here");
				break;
			}
		}
		handler.remotePeers.get(msg_index).has_rcvd_handshake = true;
	}

	public static boolean checkHandshake(Message incomingMessage, Integer peerId) {
		return (peerId == null && !(incomingMessage.type == MESS.HANDSHAKE));
	}

	/*
	 * public static void messageHandling(Handler handler){ List<Message>
	 * msg_to_remove = new ArrayList<Message>();
	 * 
	 * synchronized (handler.sc.messages_rcvd) {
	 * 
	 * Iterator<Message> it = handler.sc.messages_rcvd.iterator(); while
	 * (it.hasNext()) { Message msg_incom = it.next(); Integer peerId =
	 * handler.clientIdtoPeerId.get(msg_incom.clID);
	 * if(Message.checkHandshake(msg_incom, peerId)) continue;
	 * 
	 * System.out.println(msg_incom.type); messageProcessing(handler, msg_incom,
	 * peerId); msg_to_remove.add(msg_incom); }
	 * 
	 * for (Message m : msg_to_remove) { handler.sc.messages_rcvd.remove(m); } }
	 * }
	 */

	public static void chokeMessage(Handler handler, int index) {
		//int index = findIndex(handler, peerId);
		handler.remotePeers.get(index).is_choked = true;
		// pd.writelogs.chokedMsgType(pd.other_peer_Ids[pd.my_clID],
		// pd.peer_neighbours[msg_index].peerId);
	}

	static void unchokeMessage(Handler handler, int index) {
		//int index = findIndex(handler, index);
		handler.remotePeers.get(index).is_choked = true;
		// pd.writelogs.unchokedMsgType(pd.other_peer_Ids[pd.my_clID],
		// pd.peer_neighbours[msg_index].peerId);
	}

	public static int findIndex(Handler handler, Integer peerId, Message incoming) {
		
		int temp = -1;
		if(incoming.type!=MESS.HANDSHAKE){
			for (int i = 0; i < handler.peerIdList.size(); i++) {
			if (handler.peerIdList.get(i) - peerId==0) {
				temp = i;
				break;
			}
		}
		}
		return temp;

	}

	static void interestedNeighborMessage(Handler handler, int index) {
		//int index = findIndex(handler, index);
		handler.remotePeers.get(index).is_interested = true;
		// pd.writelogs.interestedMsgType(pd.other_peer_Ids[pd.my_clID],
		// pd.peer_neighbours[msg_index].peerId);
	}

	static void uninterestedMessage(Handler handler, int index) {
		//int index = findIndex(handler, peerId);
		handler.remotePeers.get(index).is_interested = false;
		// pd.writelogs.notInterestedMsgType(pd.other_peer_Ids[pd.my_clID],
		// pd.peer_neighbours[msg_index].peerId);
	}

	static void requestMessage(Handler handler, Message incomingMessage, int index) {
		ByteBuffer buffer = ByteBuffer.wrap(incomingMessage.payload);
		int pieceNumber = buffer.getInt();
		Message.sendFilePiece(handler, pieceNumber, index);
	}

	public static void sendFilePiece(Handler handler, int pieceNumber, int index) {
		sendMessage(handler, getMessage(handler.fh.pieceSize, MESS.PIECE, handler.fh.file_pieces[pieceNumber]),
				index);
	}

	public static void sendHave(int index, int pieceNumber, Handler handler) {
		byte[] pieceIndex = ByteBuffer.allocate(4).putInt(pieceNumber).array();
		sendMessage(handler, getMessage(4, MESS.HAVE, pieceIndex), index);
	}

	static void haveMessageHandler(Handler pd, Message incomingMessage, int index) {
		BigInteger tempField = new BigInteger(pd.remotePeers.get(index).bit_field_map);
		ByteBuffer buffer = ByteBuffer.wrap(incomingMessage.payload);

		int this_indx = buffer.getInt();
		tempField = tempField.setBit(this_indx);

		pd.remotePeers.get(index).bit_field_map = tempField.toByteArray();

		boolean neighborHasFile = true;
		for (int i = 0; i < pd.fh.number_of_bits; i++) {
			if (!tempField.testBit(i)) {
				neighborHasFile = false;
				break;
			}
		}

		pd.hasFile.set(index, neighborHasFile);

		boolean temp = true;

		for(int i=0;i<pd.peerIdList.size();i++){
			if(!pd.hasFile.get(index)){
				temp=false;
				break;
			}
		}
		
		pd.every_peer_has_file = temp;
		// pd.writelogs.haveMsgType(pd.other_peer_Ids[pd.my_clID],
		// pd.peer_neighbours[msg_index].peerId, this_indx);

		BigInteger myField = new BigInteger(pd.fh.bitfield);
		if (!myField.testBit(this_indx))
			Message.sendInterested(index, pd);
	}
	

}