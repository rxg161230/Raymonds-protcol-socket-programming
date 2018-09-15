//AOS Project 2 Summer 2017
//Ian Braunfeld, Ria Ghosh, and Chris Rhynes

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.*;
import java.util.*;

public class Node {
	private static BufferedReader br;
	private static BufferedReader data;
	public static boolean end_program = false;
	public static boolean enter_cs = false;
	public static boolean greedy = false;
	public static boolean has_token = false;
	public static boolean in_use = false;
	public static boolean request_received = false;
	public static boolean send_request = true;
	//public static boolean request_sent = false;
	public static boolean start = false;
	public static int ir_delay;
	public static int exec_time;
	public static int num_of_nodes;
	//public static int num_of_requests = 0;
	public static int total_requests;
	public static int messages_sent = 0;
	public static int node_id;
	public static int num_of_requests = 1;
	public static int process;
	//public static int request_neighbor;
	public static int token_neighbor;
	public static int token_neighbor_array[] = new int[20];
	public static ArrayList<Integer> queue = new ArrayList<Integer>();
	public static int neighbors[][] = new int[50][50];
	public static int tree_neighbors[][] = new int[50][50];
	public static String hostname[] = new String[20];
	public static String filename;
	public static int port[] = new int[20];
	public static double avg_time_in_cs = 0;
	public static double avg_time_waiting = 0;
	public static double avg_runtime = 0;
	//public static long program_start;
	public static long runtime = 0;
	public static long time_in_cs = 0;
	public static long time_waiting = 0;
	public static double throughput;
	public static long latency;
	
	public static int find_issue = 0;
	
	public static boolean isNumber(char character){
		
		//ignore any invalid lines (only process lines starting with numbers)
		switch(character){
		case '0':	
		case '1':	
		case '2':	
		case '3':	
		case '4':	
		case '5':	
		case '6':	
		case '7':	
		case '8':	
		case '9':	return true;
		default:	return false;
		}
	}
	
	public static void clearDataFile(String filename) {
		try{
	        FileWriter fwOb = new FileWriter(filename, false); 
	        PrintWriter pwOb = new PrintWriter(fwOb, false);
	        pwOb.flush();
	        pwOb.close();
	        fwOb.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}
    }
	
	public static void openDataFile(String filename){
        try{
        	//open config file
        	data = new BufferedReader(new FileReader(filename));
        }
        catch(FileNotFoundException e){
            System.out.println("File not found.");
        }
    }
	
	public static void writeDataFile(String filename, long message){
		try{
			PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(filename, true)));
			
			out.println(message);
			out.close();

		} catch (IOException e) {
			//exception handling left as an exercise for the reader
		}
	}
  
    public static void closeDataFile(){
        try {
        	//close toX file
            data.close();
        }
        catch (IOException e) {
            System.out.println("Could not close file.");
        }
    }
	
	public static void openFile(){
        try{
        	//open config file
            br = new BufferedReader(new FileReader("/home/eng/c/car021000/Stuff/Project2/project2/src/Config.txt"));
        }
        catch(FileNotFoundException e){
            System.out.println("File not found.");
        }
    }
   
    public static void readFile(){
        String line;
        char firstCharacter;
        int validLine = 0;
        int increment = 0;
       
        try {
        	//read each line of config file and initialize the variables
            while((line = br.readLine()) != null){
                line = line.trim();
                
                if(!line.isEmpty()){
                	firstCharacter = line.charAt(0); //read first character of non-empty line
                
                	//separate array only if line starts with a number
                	if(isNumber(firstCharacter)){
                		String splitter[] = line.split("[\\s]+");
                		if(validLine == 0){
	                        num_of_nodes = Integer.parseInt(splitter[0]);  //initialize number of nodes
	                        ir_delay = Integer.parseInt(splitter[1]);  //initialize exponential seed for inter-request delay
	                        exec_time = Integer.parseInt(splitter[2]);  //initialize exponential seed for critical section execution time
	                        total_requests = Integer.parseInt(splitter[3]); //initialize total number of requests
	                        validLine++;  //increment variable if the line is valid
                		}
                		else{
                			node_id = Integer.parseInt(splitter[0]);  //initialize node id
	                        hostname[increment] = splitter[1];  //initialize hostname
	                        port[increment] = Integer.parseInt(splitter[2]);  //initialize port number
	                        token_neighbor_array[increment] = Integer.parseInt(splitter[3]);  //initialize token neighbor array
	                        
	                        
	                        System.out.println("token_neighbor = " + token_neighbor + "   process = " + process + "   token = " + has_token);
	                        increment++;  //increment counter
                		}
                	}
            	}
            }
        }
        catch (IOException e) {
            System.out.println("Could not read file.");
        }
    }
  
    public static void closeFile(){
        try {
        	//close config file
            br.close();
        }
        catch (IOException e) {
            System.out.println("Could not close file.");
        }
    }
    
    public static void topModule(){
    	//int num_of_requests = 1;
    	
    	//assign neighbor that is direction of token to the process from the token neighbor array
    	token_neighbor = token_neighbor_array[process];
    	
    	//if process is its own token neighbor, then process has token
    	if(token_neighbor == process){
        	has_token = true;
        }
    	
    	System.out.println("Process = " + process + "   Token Neighbor = " + token_neighbor + "   has_token = " + has_token);
    	
    	long start_throughput = System.currentTimeMillis();
    	long start_latency = System.currentTimeMillis();
    	
    	//loop until number of requests is satisfied
    	while(num_of_requests <= Node.total_requests){
			try{
				queue.add(process); //add process to queue
				
				//exponential inter-request time
				double expRandom1 = -(0.001 * Node.ir_delay) * Math.log(Math.random());
				int ir_time = (int)(Math.floor(100000*expRandom1));
				
				//exponential critical section time
				double expRandom2 = -(0.001 * Node.exec_time) * Math.log(Math.random());
				int cs_time = (int)(Math.floor(100000*expRandom2));
		    	
				long start_time = System.currentTimeMillis();
				long ir_start_time = System.currentTimeMillis();  //waiting start time for request
		    	//program_start = System.currentTimeMillis();
				Thread.sleep(ir_time); //delay between requests
		    	System.out.println("Process " + Node.process + " sending request #" + num_of_requests);
				
				csEnter(); //blocking function; waiting for permission to enter cs
				
				long ir_end_time = System.currentTimeMillis();  //time request was granted
				time_waiting = time_waiting + (ir_end_time - ir_start_time);
				//start time for entering critical section
				long cs_start_time = System.currentTimeMillis();
				writeDataFile(filename, cs_start_time);
				System.out.println("Process " + process + " entering cs at time " + cs_start_time + "ms");
				
				Thread.sleep(cs_time); //run critical section
				
				//end time for leaving critical section
				long cs_end_time = System.currentTimeMillis();
				writeDataFile(filename, cs_end_time);
				time_in_cs = time_in_cs + (cs_end_time - cs_start_time);
				System.out.println("Process " + process + " leaving cs at time " + cs_end_time + "ms");
				
				csLeave(); //let bottomModule know process is finished with critical section
		    	num_of_requests++;
		    	long end_time = System.currentTimeMillis();
		    	runtime = runtime + (end_time - start_time);
		    	//throughput = runtime/num_of_requests;
		    	//System.out.println("Throughput = " + throughput);
	    	}
			catch(Exception e){
				e.printStackTrace();
			}
		}
    	long end_throughput = System.currentTimeMillis();
    	long end_latency = System.currentTimeMillis();
    	
    	avg_runtime = runtime/total_requests;
    	avg_time_waiting = time_waiting/total_requests;
    	avg_time_in_cs = time_in_cs/total_requests;
    	latency = end_latency-start_latency;
    	throughput = 1/avg_runtime;
    	
    	System.out.println("");
    	System.out.println("Report:");
    	System.out.println("Messages sent: " + messages_sent);
    	System.out.println("Process " + process + " average waiting time to run cs is " + avg_time_waiting);
    	System.out.println("Process " + process + " average time in cs is " + avg_time_in_cs);
    	System.out.println("Process " + process + " average running time is " + avg_runtime);
    	System.out.println("Process " + process + " latency to run " + total_requests + " is " + latency);
    	System.out.println("Process " + process + " throughput for " + total_requests + " is " + throughput);
    	System.out.println("Process " + process + " COMPLETE");
	}
    
    public static void bottomModule(){
    	int neighbor;
    	String message;
    	
    	if(token_neighbor == process){
    		has_token = true;
    	}
    	
    	///////////////////System.out.println("This is bottomModule");
    	
    	if(has_token && !in_use){
    		///////////////////////System.out.println("I don't think this is the issue");
    		if(!queue.isEmpty() && greedy){
    			if(queue.contains(process)){
    				enter_cs = true;
    				in_use = true;
    				token_neighbor = process;
    				
    				//remove first request (ONLY) from queue
    				for(int i = 0; i < queue.size(); i++){
    					if(queue.get(i) == process){
    						queue.remove(i);  //remove request (node number) from queue
    						break;  //break to remove first request only
    					}
    				}
    			}
    			else if(!queue.contains(process)){
    				//neighbor = queue.get(0);
    				token_neighbor = queue.remove(0);
    				message = "token";
    				//token_neighbor = neighbor;
    				send_request = true;
    				has_token = false;
    				//messages_sent++;
    				sendMessage(token_neighbor, message);
    				try{
    					Thread.sleep(1000);
    				}
    				catch(Exception e){
    					e.printStackTrace();
    				}
    				///////////////System.out.println("Token has just now...this minute...been sent");
    			}
    		}
    		else if(!queue.isEmpty() && !greedy){
    			System.out.println("queue.isEmpty = " + queue.isEmpty() + "   queue = " + queue);
    			neighbor = queue.remove(0);
    			if(neighbor == process){
    				enter_cs = true;
    				in_use = true;
    				token_neighbor = process;
    			}
    			else if(neighbor != process){
    				message = "token";
    				token_neighbor = neighbor;
    				send_request = true;
    				has_token = false;
    				sendMessage(token_neighbor, message);
    				try{
    					Thread.sleep(1000);
    				}
    				catch(Exception e){
    					e.printStackTrace();
    				}
    				//////////////////System.out.println("I sent cho token!");
    			}
    		}
    	}
    	else if(!has_token){
    		find_issue++;
    		//System.out.println("The issue is here somewhere");
    		//System.out.println("find_issue = " + find_issue);
    		//System.out.println("queue.isEmpty = " + queue.isEmpty());
    		//System.out.println("queue = " + queue);
    		//System.out.println("send_request = " + send_request);
    		//System.out.println("num_of_requests = " + num_of_requests);
    		//System.out.println();
    		if(!queue.isEmpty() && send_request && (num_of_requests <= total_requests)){
    			send_request = false;
    			message = "request" + "-" + process;
    			messages_sent++;
    			sendMessage(token_neighbor, message);
    			/////////////////System.out.println("Request HAS been sent!");
    		}
    	}
    }
    
    public static void csEnter(){
    	///////////////////////////System.out.println("csEnter is not the issue?");
    	//continuously ask permission to enter critical section
    	while(!enter_cs){
    		try{
    			Thread.sleep(1000);
    		}
    		catch(Exception e){
    			e.printStackTrace();
    		}
    		
    		bottomModule();
    	}
    	
    	//long cs_start_time = System.currentTimeMillis();
		//writeDataFile(filename, cs_start_time);
		//System.out.println("Process " + process + " entering cs at time " + cs_start_time + "ms");
    }
    
    public static void csLeave(){
    	in_use = false;  //let bottomModule know process is done with critical section and token
    	enter_cs = false;
    	
    	//long cs_end_time = System.currentTimeMillis();
		//writeDataFile(filename, cs_end_time);
		//time_in_cs = time_in_cs + (cs_end_time - cs_start_time);
		//System.out.println("Process " + process + " leaving cs at time " + cs_end_time + "ms");
    	System.out.println("Process " + process + " is no longer using token");
    	
    	try{
			Thread.sleep(1000);
		}
		catch(Exception e){
			e.printStackTrace();
		}
    	bottomModule();
    }
    
    public static void startProcess(){
		try{
			//open socket and establish communication
			int serverPort = port[process];  //initialize the process's port
			ServerSocket tokenServer = new ServerSocket(serverPort);  //create socket
			//runServer server = new runServer(serverPort, tokenServer);  //initialize thread to run server
			//server.start();  //start thread
			TokenServer thread = new TokenServer(serverPort, tokenServer);
			thread.start();
			
			if(end_program){
				System.out.println("SOCKET IS CLOSED");
				tokenServer.close();
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
    }
    
    public static void sendMessage(int neighbor_process, String message){
    	System.out.println("Going to Client: " + message);
    	
    	//send message to client to client can send to server
		tokenClient(hostname[neighbor_process], port[neighbor_process], message);
    }
    
    public static void tokenClient(String sh, int serverPort, String message){
    	try{
    		if(Node.start == false){
				Thread.sleep(5000);
				Node.start = true;
			}
    		
    		InetAddress serverHost = InetAddress.getByName(sh);
    		
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
    
    public static void main(String args[]){
    	
    	//initialize process number of node
    	process = Integer.parseInt(args[0]);
    	filename = "/home/eng/c/car021000/Stuff/Project2/project2/src/log" + process + ".txt";
		
		Path out;
    	
    	//initialize if algorithm will be greedy or not greedy
    	if(args[1].toLowerCase().equals("y")){
    		greedy = true;
    	}
    	else{
    		greedy = false;
    	}
    	
    	try{
    		out = Files.createFile(Paths.get(filename));
		} 
    	catch (FileAlreadyExistsException faee) {
			clearDataFile(filename);
		    out = Paths.get(filename);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	openFile();  //open config file
    	openDataFile(filename);
    	readFile();  //read config file and initialize variables
    	closeFile();  //close config file
    	startProcess();  //start communication between nodes
    	topModule();  //begin algorithm
    	
    }
}
