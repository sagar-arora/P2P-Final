import java.util.List;

public class peerProcess {

	
	public static void main(String[] args){
		CommonConfigParser commonParser=new CommonConfigParser();
		commonParser.saveCommonConfig();
		System.out.println(CommonConfigParser.filesize+" "+CommonConfigParser.pieceSize+" "
				+" "+CommonConfigParser.Filename);
		PeerConfigParser peerParser=new PeerConfigParser();
		peerParser.savepeerinfo();
		int peerId = 1002;
		/*try {
			 peerId = Integer.parseInt(args[0]);

		} catch (Exception e) {
			System.out.println("no input peer Id specified.");
				System.exit(0);
		}*/
		
		List<AdjacentPeers> peerIds=peerParser.getneighborinfo();
	    
		Process process =new Process (peerId,peerIds);
		process.createPeerDirs(peerId);
		
	}
}
