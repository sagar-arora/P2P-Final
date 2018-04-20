
public class FileHandler {

	public  int fileSize=20;
	public  final int pieceSize=20;
	public  int number_of_bits;
	public  int  number_of_bytes;
	byte[][] file_pieces;
	
	FileHandler(){
		
	}
	
	public static void calculatePieceSize(){
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
	


}
