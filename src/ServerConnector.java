
	import java.net.*;
	import java.io.*;
	import java.nio.*;
	import java.nio.charset.Charset;
	import java.util.*;

public class ServerConnector {
		int port_num; 
		public List<Message> messages_rcvd;
		int num_of_clients = 0;
        ServerSocket socket_listener = null;
        Handler handler;
	    public ServerConnector(int port_num) {
	        this.port_num = port_num;
	        messages_rcvd = Collections.synchronizedList(new ArrayList<Message>());
	        new Thread() {
	    	public void run() {
	            try {
	                while (true) {
	                    try {
	                        socket_listener = new ServerSocket(port_num);
	                        new Requesthandler(socket_listener.accept(),num_of_clients).start();
	                        num_of_clients++;
	                    } catch (SocketException io_exception) {
	                        Thread.sleep(50);
	                    }
	                    finally {
	                        socket_listener.close();
	                    }
	                }
	            } catch (Exception io_exception) {
	                
	            }
	        }
	    }.start();
	    }

	    Message bytes_to_message(byte[] byte_data, int cl_Id) {
	        try {
	            Thread.sleep(1);
	        } catch (Exception io_exception) {
	
	        }
	        if (byte_data.length >= 18) {
	            byte[] byte_arr = Arrays.copyOfRange(byte_data, 0, 18);
	            String message_header = new String(byte_arr, Charset.forName("UTF-8"));
	            if (message_header.equalsIgnoreCase("P2PFILESHARINGPROJ")) {
	                int new_peerId = ByteBuffer.allocate(4).put(Arrays.copyOfRange(byte_data, 28, 32)).getInt(0);
	                return new Message(new_peerId, (byte)8, null, cl_Id);
	            }
	        }
	        byte[] message_payload = Arrays.copyOfRange(byte_data, 5, byte_data.length);
	        int message_length = ByteBuffer.allocate(4).put(Arrays.copyOfRange(byte_data, 0, 4)).getInt(0);
	        return new Message(message_length, byte_data[4], message_payload, cl_Id);
	    }

	     class Requesthandler extends Thread {
	    	int cl_Id;
	    	Socket conn;
	        ObjectInputStream input;  
	        Message msg; 
	        
	        public Requesthandler(Socket connection, int clientID) {
	            this.conn = connection;
	            this.cl_Id = clientID;
	        }
	        public void run() {
	            try{
	                input = new ObjectInputStream(conn.getInputStream());
	                try{
	                    while(true)
	                    {
	                        byte[] tmp = (byte[])input.readObject();
	                        msg = bytes_to_message(tmp, cl_Id);
	                        synchronized (messages_rcvd) {
	                            messages_rcvd.add(msg);
	                        }
	                    }
	                }
	                catch(ClassNotFoundException io_exception){
	                    System.err.println("Data received in unknown format");
	                }
	            }
	            catch(IOException io_exception){
	            	//io_exception.printStackTrace();
	            }
	            finally{
	                try{
	                    input.close();
	                    conn.close();
	                }
	                catch(IOException io_exception){
	                	//io_exception.printStackTrace();
	                }
	            }
	        }

	    }

	}

