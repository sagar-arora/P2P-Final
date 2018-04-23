

enum MESS{
	CHOKE((byte)0),
	UNCHOKE((byte)1),
	INTERESTED((byte)2),
	NOT_INTERESTED((byte)3),
	HAVE((byte)4),
	BITFIELD((byte)5),
	REQUEST((byte)6),
	PIECE((byte)7),
	HANDSHAKE((byte)8);

	byte messageValue = -1;

	MESS(byte b){
		this.messageValue = b;
	}
}
