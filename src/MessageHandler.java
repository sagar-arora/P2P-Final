import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MessageHandler {
    
	
	public static void messageHandling(Handler handler){

		
		List<Message> msg_to_remove = new ArrayList<Message>();
	
		synchronized (handler.sc.messages_rcvd) {
		    Iterator<Message> it = handler.sc.messages_rcvd.iterator();
		    while (it.hasNext()) {
		        Message msg_incom = it.next();
		        Integer peerId = handler.clientIdtoPeerId.get(msg_incom.clID);
		        if(Message.checkHandshake(msg_incom, peerId))
		        	continue;
		        
		        int index=Message.findIndex(handler, peerId, msg_incom);
		        	if (index != -1) {
		        	if ((msg_incom.type != MESS.BITFIELD) && 
		            		handler.remotePeers.get(index).has_rcvd_bit_field == false &&
		            				handler.remotePeers.get(index).has_rcvd_bit_field == true) {
		                continue;
		            }
		        }
		        
		       	messageProcessing(handler, msg_incom, index);
		        msg_to_remove.add(msg_incom);
		    }

		    for (Message m : msg_to_remove) {
		        handler.sc.messages_rcvd.remove(m);
		    }
		}
    }
	

	public static  void messageProcessing(Handler handler, Message msg_incom, int index) {

		if(msg_incom.type==MESS.CHOKE){
            Message.chokeMessage(handler, index);
            handler.pfl.peerchoked(handler.peerId, handler.peerIdList.get(index));
		}
		else if(msg_incom.type == MESS.UNCHOKE){
            Message.unchokeMessage(handler, index);
            handler.pfl.peerunchoked(handler.peerId, handler.peerIdList.get(index));
		}
        else if(msg_incom.type == MESS.INTERESTED){
            Message.interestedNeighborMessage(handler, index);
            handler.pfl.receivedinterestedmsg(handler.peerId, handler.peerIdList.get(index));
        }
        else if(msg_incom.type == MESS.NOT_INTERESTED){
            Message.uninterestedMessage(handler, index);
            handler.pfl.receivedinterestedmsg(handler.peerId, handler.peerIdList.get(index));
        }
        else if(msg_incom.type == MESS.HAVE){
            Message.haveMessageHandler(handler, msg_incom, index);
           // handler.pfl.receivedhavemsg(handler.peerId,handler.peerIdList.get(index), );
        }
        else if(msg_incom.type == MESS.BITFIELD){
            Message.bitFieldHandler(handler, msg_incom, index);
        }
        else if(msg_incom.type == MESS.REQUEST){
            Message.requestMessage(handler, msg_incom, index);
        }
        else if(msg_incom.type == MESS.PIECE){
            Message.pieceMessageHandler(handler, msg_incom, index);
        }
		else if(msg_incom.type==MESS.HANDSHAKE){
			Message.handShakeHandler(handler, msg_incom, index);
		}

	}
    
}
