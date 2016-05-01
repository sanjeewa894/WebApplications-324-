
import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
/**
 * HTTP server that accepts GET and POST requests.
 */
public class KVServer extends Thread {
	static final int PORT = 4321;
	static final Map<String, String> database = new HashMap<>();
    static String line;
    final static Pattern SEPARATOR = Pattern.compile("\\s*:\\s*");

	/** Used to serve an individual client */
	final Socket socket;

	public KVServer(Socket s) {
		this.socket = s;
	}

	public static void main(String[] args) throws IOException {
		try (ServerSocket ss = new ServerSocket(PORT)) {
			while (!Thread.interrupted())
				new KVServer(ss.accept())
						.run();
		}
	}
	/** Logic to handle a single client request */
	public void run() {
		try (DataInputStream sin = new DataInputStream(socket.getInputStream());
		     PrintStream sout = new PrintStream(socket.getOutputStream());) {
			// Deserialise HTTP request
			//DataInputStream si1 = sin;
			line = sin.readLine();
			    
			Message req = new Message(sin);
			/* TODO:
			Handle GET and POST requests and send appropriate response.
			NOTE: the code given below is just for testing!
			 */
			 //to get post or get method splitting line store it in array 
			 String[] kv = line.split(" ");
			 
	/*
	**according to post and get method put in database and get value according to provided key
	**if it is there show it with ok state else not found message back...
	**if valid method is not given reply with bad request
	** to get or post splitted array we arrange it to find it get or post furthre splitting or not. 
	*/
			switch(kv[0]){
			
			 case "POST" :
			   database.put(req.keys(), req.vals());
			   req.serialise(new PrintStream(System.out));
			   Status.OK.message.serialise(sout);
			  
			    break;
			    
			 case "GET" :
			    String[] key2 = kv[1].split("/");
			    
			    if(database.get(key2[1]) != null){
			         System.out.println("\noutput value is " +database.get(key2[1]) + "\n");
			         req.deserialise(new PrintStream(System.out),database.get(key2[1]));
			         Status.OK.message.deserialise(sout,database.get(key2[1]));
			     }
			    else  Status.NOTFOUND.message.serialise(sout);
			
			    break;
			
			 default:
			      Status.BADREQ.message.serialise(sout);
			   }
			//Status.OK.message.serialise(sout);
		} catch (IOException e) {
			Logger.getGlobal().log(Level.WARNING, "Client error", e);
		} finally {
			try { socket.close(); } catch (IOException e) {}
		}
	}
}
