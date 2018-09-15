//AOS Project 2 Summer 2017
//Ian Braunfeld, Ria Ghosh, and Chris Rhynes

import java.net.ServerSocket;
import java.net.Socket;

public class runServer extends Thread{
	int serverPort;
	ServerSocket tokenServer;
	
	//constructor
	runServer(int sp, ServerSocket cs){
		serverPort = sp;  //initialize passed port
		tokenServer = cs;  //initialize passed socket
	}
	
	public void run(){
		try{
			//loop until the end of the program
			while(!Node.end_program){
				Socket clientSocket = tokenServer.accept();  //wait to accept client
				TokenServer thread = new TokenServer(clientSocket);  //create thread for server
				thread.start();  //start server thread
			}
			
			tokenServer.close();  //close server thread
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
}
