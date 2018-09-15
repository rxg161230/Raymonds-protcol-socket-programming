//AOS Project 2 Summer 2017
//Ian Braunfeld, Ria Ghosh, and Chris Rhynes

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class TokenServer extends Thread{
Socket clientSocket;
int serverPort; //ADDED
ServerSocket tokenServer; //ADDED
	
	//constructor
	TokenServer(int sp, ServerSocket ts){ //took out Socket cs
		//clientSocket = cs;  //intialize passed socket for client
		serverPort = sp;  //initialize passed port //ADDED
		tokenServer = ts;  //initialize passed socket //ADDED
	}
	
	public void run(){		
		try{
			//TokenServer thread = new TokenServer(clientSocket);
			while(!Node.end_program){
				try{
					Thread.sleep(1000);
				}
				catch(Exception e){
					e.printStackTrace();
				}
				Socket clientSocket = tokenServer.accept();  //wait to accept client //ADDED
				//create a reader for passed messagee
				BufferedReader br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
				
				//read passed message
				String message = br.readLine();
				System.out.println("The message received is: " + message);
				
				//split passed message into separate variables
				String splitter[] = message.split("-");
				String type = splitter[0];  //split string variable
							
				//decipher message type to initialize proper variables
				if(type.equals("token")){
					Node.has_token = true;  //node now has token
					System.out.println("Process " + Node.process + " has token");
				}
				else if(type.equals("request")){
					int neighbor = Integer.parseInt(splitter[1]);  //split string variable and make into an integer
					
					System.out.println("Adding neighbor " + neighbor + " to the queue");
					Node.queue.add(neighbor);  //add request neighbor to queue
				}
				
				Node.bottomModule();
			}
			clientSocket.close();  //close socket to client
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
}
