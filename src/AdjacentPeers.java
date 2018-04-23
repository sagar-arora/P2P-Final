
public class AdjacentPeers {

	
	public  boolean is_interested;
	public AdjacentPeers(int id, String address, int port, boolean hasFile2) {
		this.peerId=id;
		this.port_num=port;
		this.host_name=address;
		this.hasFile=hasFile2;
	}
	public int peerId;
	public String host_name = "";
	public int port_num;
	public byte[] bit_field_map;
	public boolean isConnection;
	public boolean has_rcvd_bit_field;
	public boolean has_rcvd_handshake;
	public boolean is_choked;
	public boolean is_waiting_for_piece;
	public int piece_num;
	public boolean is_conn_refused;
	public boolean hasFile;
	public boolean has_sent_bitfield;
	public boolean has_sent_handshake;
	
	
}
