package HtcpcpSystem;

import java.io.IOException;
import java.util.ArrayList;


// Scope of this class is to implement the logic of the service, namely to serve the clients
// In our case to read input and reply back to client

public class ConnectionHandler implements Runnable {
    public String outgoingMessage="";
    public static String msgString="";
    ConnectionInstance socketInstance = null;
    ArrayList<ConnectionInstance> clientInstanceList = null;
    
    // constructor of the class that inititates the instance variables
    public ConnectionHandler (ArrayList<ConnectionInstance> inArrayListVar, ConnectionInstance inSocVar){
        socketInstance = inSocVar;
        clientInstanceList = inArrayListVar;
        //connection establishment and first state initialization 
    }
    public void run(){
        try{
            //CONNECTION ESTABLISHMENT        
            System.out.println(" Connection Handler: SERVER STATE: INIT" );
            outgoingMessage = "220" + " " + "HTCPCP Service is Ready"; 
            socketInstance.socketOutput.writeUTF(outgoingMessage);
            socketInstance.socketOutput.flush();


                String clientMessage = socketInstance.socketInput.readUTF();
                System.out.println("{**} Connection Handler: New Client at Port " + socketInstance.soc.getPort()); 
                //decryption Starts
                Decryption_CaesarCipher decryption = new Decryption_CaesarCipher(clientMessage, 13);
                String decryptedMessage = decryption.get_decrypted();
                ServerMachine sMachine = new ServerMachine(decryptedMessage ); 
            
            // as long as the socket isntance is not closed
            while (!socketInstance.soc.isClosed()) {
                outgoingMessage = sMachine.getMessage();
                System.out.println(" >>Connection Handler: " + outgoingMessage); 
                System.out.println("......."); 

                //encryption starts
                Encryption_CaesarCipher encryption = new Encryption_CaesarCipher(outgoingMessage, 13);
                String encryptedMSG = encryption.get_encrypted();                
                
                socketInstance.socketOutput.writeUTF(encryptedMSG);
                socketInstance.socketOutput.flush();

                if(outgoingMessage.contains("221")){
                    socketInstance.socketInput.close();
                    socketInstance.socketOutput.close();
                    socketInstance.soc.close();
                    clientInstanceList.remove(socketInstance);                    
                } else {
                    clientMessage = socketInstance.socketInput.readUTF();

                    //decryption
                    decryption = new Decryption_CaesarCipher(clientMessage, 13);
                    decryptedMessage = decryption.get_decrypted();                
                    
                    new ServerMachine(decryptedMessage);
                }
                    
            }   //while socket NOT CLOSED
        }
        catch (IOException except){
            //Exception thrown (except) when something went wrong
            System.out.println("An Error occured in the Server Connection Handler --> " + except.getMessage());
        }
    }
}

    