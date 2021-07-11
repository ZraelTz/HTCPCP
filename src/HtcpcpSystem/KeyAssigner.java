package HtcpcpSystem;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

// this method is realised as a thread in order not to mix server sockets
// its purpose is to distribute encryption KEY on a given algorithm, in our case Ceasar Cipher

public class KeyAssigner implements Runnable {
    private ServerSocket secondSocket;
    private int secondPort = 5000;
    private String KEY = "13";
    
    public KeyAssigner () throws IOException, Exception {
        secondSocket = new ServerSocket(secondPort);  //create a server socket instance for KEY assignment

        // security key exchange
        System.out.println("Key Exchange");
    }
    
    public void run(){
        try{

            while (true)
            {
                Socket clientSocket = secondSocket.accept();                        // accept incoming requests
                ConnectionInstance inSocket = new ConnectionInstance(clientSocket);  // store socket instance values 
                System.out.println("KEY Socket accepted at " + clientSocket.getLocalPort());
                System.out.println(KEY.toString());
                inSocket.socketOutput.writeUTF(KEY);
                inSocket.socketOutput.flush();
                
                inSocket.socketInput.close();
                inSocket.socketOutput.close();
                inSocket.soc.close();
                System.out.println("KEY Socket closed");
            }
        }
        catch (IOException except){
            //Exception thrown (except) when something went wrong
            System.out.println("An Error occured in Server Connection Handler --> " + except.getMessage());
        }
    }

}

    