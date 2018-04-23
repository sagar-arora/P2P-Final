import java.io.File;
import java.io.FileInputStream;
import java.math.BigInteger;

public class FileHandler {

	public int fileSize;
	public  int pieceSize;
	public int number_of_bits;
	public int  number_of_bytes;
	byte[][] file_pieces;
	public byte[] bitfield;
	public String file_name;
	public int peerId;
	public boolean hasFile;
	
	FileHandler(int peerId, int fileSize, int pieceSize, String file_name, boolean hasFile){
		this.peerId=peerId;
		this.file_name=file_name;
		this.pieceSize=pieceSize;
		this.fileSize=fileSize;
		this.hasFile=hasFile;
		calculatePieceSize();
		initBitField();
		initializeFilePieces(hasFile);
	}
	
	public  void calculatePieceSize(){
		if (fileSize%pieceSize != 0){
            number_of_bits=(int)(fileSize/pieceSize + 1);
		}
        else{
            number_of_bits=(int)(fileSize/pieceSize);
        }
		
        
		if (number_of_bits%8 != 0){
            number_of_bytes=(int)(number_of_bits/8 + 1);
        }
        else{
            number_of_bytes=(int)(number_of_bits/8);
        }
	}
	
	public void initBitField() { 
		bitfield=new byte[number_of_bytes+1];
        if (hasFile) {
            BigInteger tmp_field = new BigInteger("0");
            for (int i=0; i<number_of_bits; i++) {
                tmp_field=tmp_field.setBit(i);
            }
            byte[] array=tmp_field.toByteArray();
            bitfield=array;
         }
	}
	
    private void initializeFilePieces(boolean hasFile) {
    	file_pieces=new byte[number_of_bits][pieceSize];

    	if (hasFile == true){
	    	try {
	    	 	File file=new File(file_name);
	    	 	FileInputStream inputStream=new FileInputStream("peer_" + peerId + "//" + file);
	            int currentPieceIndex = 0;
	            while (currentPieceIndex < file_pieces.length) {
	            	inputStream.read(file_pieces[currentPieceIndex++]);
	            }
	            inputStream.close();
	            
	        } catch (Exception e) {
	        	System.out.println("file pieces generation error");
	            System.exit(0);
	        }
    	}
}


}
