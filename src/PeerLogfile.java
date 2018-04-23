import java.text.SimpleDateFormat;
import java.io.*;
import java.sql.Timestamp;


    public class PeerLogfile {

        public String printdate() {
            String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Timestamp(System.currentTimeMillis()));
            return timeStamp;
        }

        public String haslogfile(int peer_id){	//this function is a sanity check for file existence
            String logfname = "peer_" + peer_id+"/log_peer_"+peer_id+".log";
            File newfile = new File(logfname);
            if (!newfile.exists()) {
                createnewLogFile(peer_id);
            }
            return logfname;
        }

        public void createnewLogFile(int peer_id)
        {
            try {
                String fName = "peer_" + peer_id+"/log_peer_"+peer_id+".log";
                File directory = new File("peer_" + peer_id);
                directory.mkdir();
                File file1 = new File(fName);
                if (!file1.exists()) {
                    file1.createNewFile();
                }}
            catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void initiateTcpconnection(int peer_id1, int peer_id2)
        {
            String fName= haslogfile(peer_id1);
            try
            {
                FileWriter writer = new FileWriter(fName,true);
                BufferedWriter writetofile = new BufferedWriter(writer);
                writetofile.write(printdate()+": Peer "+peer_id1+" initiates a connection to Peer "+peer_id2+".");
                writetofile.newLine();
                writetofile.close();
            }

            catch(IOException e)
            {
                e.printStackTrace();
            }
        }

        public void tcpconnected(int peer_id1, int peer_id2)
        {
            String fName= haslogfile(peer_id1);
            try
            {
                FileWriter writer = new FileWriter(fName,true);
                BufferedWriter writetofile = new BufferedWriter(writer);
                writetofile.write(printdate()+": Peer "+peer_id1+" is connected with Peer "+peer_id2+".");
                writetofile.newLine();
                writetofile.close();
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
        }
        public void displayPreferredNeighbors(int peer_id1, String prefpeerlist)
        {
            String fName= haslogfile(peer_id1);
            try
            {
                FileWriter writer = new FileWriter(fName,true);
                BufferedWriter writetofile = new BufferedWriter(writer);
                writetofile.write(printdate()+": Peer "+peer_id1+" has the Preferred neighbours "+prefpeerlist+".");
                writetofile.newLine();
                writetofile.close();
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }

        }

        public void updateOptimisticallyUnchokedNeighbor(int peer_id1, int peer_id2)
        {
            String fName= haslogfile(peer_id1);
            try
            {
                FileWriter writer = new FileWriter(fName,true);
                BufferedWriter writetofile = new BufferedWriter(writer);
                writetofile.write(printdate()+": Peer "+peer_id1+" has the optimistically unchoked neighbour Peer "+peer_id2+".");
                writetofile.newLine();
                writetofile.close();
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }

        }
        public void peerunchoked(int peer_id1, int peer_id2)
        {
            String fName= haslogfile(peer_id1);
            try
            {
                FileWriter writer = new FileWriter(fName,true);
                BufferedWriter writetofile = new BufferedWriter(writer);
                writetofile.write(printdate()+": Peer "+peer_id1+" is unchoked by Peer "+peer_id2+".");
                writetofile.newLine();
                writetofile.close();
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }

        }

        public void peerchoked(int peerId1, int peerId2)
        {
            String fName= haslogfile(peerId1);
            try
            {
                FileWriter writer = new FileWriter(fName,true);
                BufferedWriter writetofile = new BufferedWriter(writer);
                writetofile.write(printdate()+": Peer "+peerId1+" is choked by Peer "+peerId2+".");
                writetofile.newLine();
                writetofile.close();
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }

        }

        public void receivedhavemsg(int peer_id1, int peer_id2, int pieceindex)
        {
            String fName= haslogfile(peer_id1);
            try
            {
                FileWriter writer = new FileWriter(fName,true);
                BufferedWriter writetofile = new BufferedWriter(writer);
                writetofile.write(printdate()+": Peer "+peer_id1+" received the 'have' message from Peer "+peer_id2+" for the piece "+pieceindex+".");
                writetofile.newLine();
                writetofile.close();
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }

        }
        public void receivedinterestedmsg(int peer_id1, int peer_id2)
        {
            String fName= haslogfile(peer_id1);
            try
            {
                FileWriter writer = new FileWriter(fName,true);
                BufferedWriter writetofile = new BufferedWriter(writer);
                writetofile.write(printdate()+": Peer "+peer_id1+" received the 'interested' message from Peer "+peer_id2+".");
                writetofile.newLine();
                writetofile.close();
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
        }
        public void receivednotinterestedmsg(int peer_id1, int peer_id2)
        {
            String fName= haslogfile(peer_id1);
            try
            {
                FileWriter writer = new FileWriter(fName,true);
                BufferedWriter writetofile = new BufferedWriter(writer);
                writetofile.write(printdate()+": Peer "+peer_id1+" received the 'not interested' message from Peer "+peer_id2+".");
                writetofile.newLine();
                writetofile.close();
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
        }
        public void pieceDownloaded(int peer_id1, int peer_id2, int pieceindex, int numpieces)
        {
            String fName= haslogfile(peer_id1);
            try
            {
                FileWriter writer = new FileWriter(fName,true);
                BufferedWriter writetofile = new BufferedWriter(writer);
                writetofile.write(printdate()+": Peer "+peer_id1+" has downloaded the piece "+pieceindex+" from Peer "+peer_id2+". Now the number of pieces it has is "+numpieces+".");
                writetofile.newLine();
                writetofile.close();
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
        }
        public void fileDownloaded(int peer_id1)
        {
            String fName = "peer_" + peer_id1+"/log_peer_"+peer_id1+".log";
            try
            {
                FileWriter writer = new FileWriter(fName,true);
                BufferedWriter writetofile = new BufferedWriter(writer);
                writetofile.write(printdate()+": Peer "+peer_id1+" has downloaded the complete file.");
                writetofile.newLine();
                writetofile.close();
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
        }
}