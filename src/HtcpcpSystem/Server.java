
package HtcpcpSystem;

import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
    
    public static void main(String[] args) {
        int firstPort = 4000;              //listening port
        
        try {
            ServerSocket serverSocket = new ServerSocket(firstPort);   // create a server socket instance
            // + add an array list that hold the incoming requrests so as to keep track on them
            ArrayList<ConnectionInstance> activeClients = new ArrayList<ConnectionInstance>();

            KeyAssigner assigner = new KeyAssigner();
            Thread assignerThread = new Thread(assigner);                 // create a thread type instance
            assignerThread.start();             
            
            // infinetelly loop over
            while (true)
            {
                System.out.println("SERVER IS LISTENING FOR INCOMING CONNECTIONS ...");
                Socket clientSocket = serverSocket.accept();                        // accept incoming requests
                ConnectionInstance inSocket = new ConnectionInstance(clientSocket);  // store socket instance values 
                activeClients.add(inSocket);        
                System.out.println(" Connection accepted at " + clientSocket.getLocalPort());
                
                // create a server thread for handling each incoming client connection        
                // at first create a variable that shall keep the values of socket
                ConnectionHandler handler = new ConnectionHandler(activeClients, inSocket);
                Thread handlerThread = new Thread(handler);                 // create a thread type instance
                handlerThread.start();                                  // start the thread
                // the thread does the rest of the job
                System.out.println("Thread " + handlerThread.getId() + " started ");
            }   
        }   // this part of the code is rquired in order to handle error situations without forcing the program execution to exit.
        catch(Exception e){
            //Exception thrown (except) when something went wrong, pushing message to the console
            System.out.println("Error occured in Server --> " + e.getMessage());
        }
    }
}