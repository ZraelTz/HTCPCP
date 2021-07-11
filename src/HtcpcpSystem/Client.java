package HtcpcpSystem;

import java.io.*;
import java.net.*;

// this is a plain implementantion of client

public class Client {
    static int firstPort = 4000;
    static int secondPort = 5000;
    static String serverAddr = "localhost";    
    static DataInputStream socketInput = null;
    static DataOutputStream socketOutput = null;
    static String keyString = "";
    
    public static void main(String[] args) {  
        String bytesInput = "";
        String messageForServer = "";
        try{      
            // key string distribution for secured identification
            // security key is gotten from the server
            System.out.println("Client key exchange begining");
            Socket secondSocket = new Socket("localhost", secondPort);
            socketOutput = new DataOutputStream(secondSocket.getOutputStream());
            socketOutput.writeUTF("Request for Key");
            socketOutput.flush();
            
            socketInput = new DataInputStream(secondSocket.getInputStream());
            keyString = socketInput.readUTF();
            System.out.println("Client key exchange starting " + Integer.parseInt(keyString));
            
            // close sockets
            socketOutput.close();
            socketInput.close();
            secondSocket.close();
            
            
            Socket soc = new Socket(serverAddr, firstPort);
            ConnectionInstance socInstance = new ConnectionInstance(soc);
            System.out.println("Client has started");
            ClientOperator cOperator = new ClientOperator (socInstance.socketInput.readUTF());

            while(!soc.isClosed()){
                //Encryption starts
                messageForServer = cOperator.getHtcpcpRequest();    
                Encryption_CaesarCipher encryption = new Encryption_CaesarCipher(messageForServer, Integer.parseInt(keyString));
                String encryptedMessage = encryption.get_encrypted();
                
                socInstance.socketOutput.writeUTF(encryptedMessage);  //(messageForServer);
                socInstance.socketOutput.flush();               
                bytesInput = socInstance.socketInput.readUTF();

                //Decryption starts
                Decryption_CaesarCipher decryption = new Decryption_CaesarCipher(bytesInput, 13);
                String decryptedMessage = decryption.get_decrypted();                
                    
                cOperator = new ClientOperator(decryptedMessage); 
            }             
        }
        catch(Exception e){System.out.println("Connection Terminated");}  
    }  
}
