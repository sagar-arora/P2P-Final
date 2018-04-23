import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;


public class CommonConfigParser {

    public static int NumberOfPreferredNeighbors;
    public static int UnchokingInterval;
    public static int OptimisticUnchokingInterval;
    public static String Filename;
    public static int filesize;
    public static int pieceSize;

    public void saveCommonConfig() {

        BufferedReader commoncfgreader;
        try {
            commoncfgreader = new BufferedReader(new FileReader("Common.cfg"));

            String nextline = null;
            try {
                while ((nextline = commoncfgreader.readLine()) != null) {
                    String[] commonConfigTokens = nextline.split("\\s+");
                    if (nextline.contains("NumberOfPreferredNeighbors")){
                        NumberOfPreferredNeighbors = Integer.parseInt(commonConfigTokens[1]);
                    }
                    if (nextline.contains("UnchokingInterval")) {
                        UnchokingInterval = Integer.parseInt(commonConfigTokens[1]);
                    }
                    if (nextline.contains("OptimisticUnchokingInterval")) {
                        OptimisticUnchokingInterval = Integer.parseInt(commonConfigTokens[1]);
                    }
                    if (nextline.contains("FileName")) {
                        Filename = commonConfigTokens[1];
                    }
                    if (nextline.contains("FileSize")) {
                        filesize = Integer.parseInt(commonConfigTokens[1]);
                    }
                    if (nextline.contains("PieceSize")) {
                        pieceSize = Integer.parseInt(commonConfigTokens[1]);
                    }


                }
            }
            catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

	public int getNumberOfPreferredNeighbors() {
		return NumberOfPreferredNeighbors;
	}

	public void setNumberOfPreferredNeighbors(int numberOfPreferredNeighbors) {
		NumberOfPreferredNeighbors = numberOfPreferredNeighbors;
	}

	public int getUnchokingInterval() {
		return UnchokingInterval;
	}

	public void setUnchokingInterval(int unchokingInterval) {
		UnchokingInterval = unchokingInterval;
	}

	public int getOptimisticUnchokingInterval() {
		return OptimisticUnchokingInterval;
	}

	public void setOptimisticUnchokingInterval(int optimisticUnchokingInterval) {
		OptimisticUnchokingInterval = optimisticUnchokingInterval;
	}

	public String getFilename() {
		return Filename;
	}

	public void setFilename(String filename) {
		Filename = filename;
	}

	public int getFilesize() {
		return filesize;
	}

	public void setFilesize(int filesize) {
		this.filesize = filesize;
	}

	public int getPiecesize() {
		return pieceSize;
	}

	public void setPiecesize(int piecesize) {
		this.pieceSize = piecesize;
	}
}