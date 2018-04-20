import java.io.IOException;
import java.nio.ByteBuffer;

public class Message {
	
	public enum MESSAGE{
        CHOKE((byte)0),
        UNCHOKE((byte)1),
        INTERESTED((byte)2),
        NOT_INTERESTED((byte)3),
        HAVE((byte)4),
        BITFIELD((byte)5),
        REQUEST((byte)6),
        PIECE((byte)7);

        byte messageValue = -1;

        MESSAGE(byte b){
            this.messageValue = b;
        }
    }

	private int length;
	private MESSAGE type;
	private byte[] payload;
	
	public Message(MESSAGE type, byte[] payload){

        this.length = (payload == null ? 0 : payload.length);
        this.type = type;
        this.payload = payload;
    }
	
	
	public static void sendMessage(Handler handler,byte[] message, int peerId) {
        try {
            handler.outputStream.get(peerId).writeObject(message);
            handler.outputStream.get(peerId).flush();
        }
        catch(IOException e){
        	System.err.println("Message not sent error.");
        }     
    }
	
	 public static byte[] getMessage(MESSAGE  type){
	        byte[] length=ByteBuffer.allocate(4).putInt(1).array();
	        ByteBuffer bf=ByteBuffer.allocate(5);
	        byte[] messageType=ByteBuffer.allocate(1).putInt(type.messageValue).array();
	        bf.put(length);
	        bf.put(messageType);
	        return bf.array();
	    }

	    public static byte[] getMessage(byte[] data, MESSAGE type){
	        int dataLength=ByteBuffer.wrap(data).getInt();
	        byte[] messageLength=ByteBuffer.allocate(4).putInt(dataLength+1).array();
	        ByteBuffer bf=ByteBuffer.allocate(data.length+5);
	        byte[] messageType=ByteBuffer.allocate(1).putInt(type.messageValue).array();
	        bf.put(messageLength);
	        bf.put(messageType);
	        bf.put(data);
	        return bf.array();
	    }
	
	public static void sendRequestMessage(Handler handler, int piece, int peerId){
		byte[] pieceIndex = ByteBuffer.allocate(4).putInt(piece).array();
		byte[] message=getMessage(pieceIndex,MESSAGE.REQUEST);
		sendMessage(handler, message, peerId);
	}
	
}
