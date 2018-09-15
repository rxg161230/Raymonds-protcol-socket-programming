//AOS Project 2 Summer 2017
//Ian Braunfeld, Ria Ghosh, and Chris Rhynes

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class TokenClient extends Thread{
	InetAddress serverHost;
	int serverPort;
	String message;
	
	public TokenClient(String sh, int sp, String msg){
		try{
			serverHost = InetAddress.getByName(sh);  //initialize passed hostname of server
			serverPort = sp;  //initialize passed port number of server
			message = msg;  //initialize passed message
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void run(){
		try{
			//initial waiting period for starting processes
			if(Node.start == false){
				Thread.sleep(5000);
				Node.start = true;
			}
			
			//create a socket
			Socket clientSocket = new Socket(serverHost, serverPort);
			
			//create a printstream to pass message
			PrintStream ps = new PrintStream(clientSocket.getOutputStream());
			
			System.out.println("Message being sent to server is: " + message);
			ps.println(message);  //pass message to server
			ps.flush();  //flush out message to pass completely
			
			clientSocket.close();  //close socket
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
}
