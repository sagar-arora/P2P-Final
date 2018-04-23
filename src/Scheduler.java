/*import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Scheduler {

	
	public static double calculateDownloadRate(Handler handler,int peerIdIndex ){
		if(peerIdIndex!=handler.clientId ){
			if(handler.remotePeers.get(peerIdIndex).isConnection && handler.remotePeers.get(peerIdIndex).is_interested){
				double rate=handler.receivedData.get(peerIdIndex)/CommonConfigParser.UnchokingInterval;
				handler.receivedData.set(peerIdIndex,0);
				return rate;
			}
		}
		else{
			return 0;
		}
	}
	
	public static void decideCandUC(Handler handler){
		
		List<Double> unorderedRate=new ArrayList<>();
		for(int i=0;i<handler.peerIdList.size();i++){
			unorderedRate.add(calculateDownloadRate(handler, i));
		}
		List<Double> orderedListDesc=new ArrayList<>(unorderedRate);
		Collections.sort(orderedListDesc);
		Collections.reverse(unorderedRate);
		
		int randomIndex;
		int startPointer;
		int numberOfTies=1;
		BitSet top_pick=new BitSet(handler.peerIdList.size());
		List<Double> bestDown=new ArrayList<>();
		bestDown=orderedListDesc.subList(0,handler.pref_neighbours_clIDs.size());
		
		if(orderedListDesc.size()>handler.pref_neighbours_clIDs.size() && orderedListDesc.get(index))
		
		
		
	}
	
	
	
	public static void initiateNeighbourTaskSchedulers(Handler handler){
		pd.pref_neighbours_scheduler.scheduleAtFixedRate(new TimerTask() {
		     @Override
		      public void run(){
		         determinePreferredNeighbors(handler);
		      }
		     },0, peerProcess.cconfig.unchokingInterval * 1000);
		
	}
	
	determinePreferredNeighbors
}
*/