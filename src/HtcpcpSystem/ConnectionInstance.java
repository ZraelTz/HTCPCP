package HtcpcpSystem;

import java.io.*;
import java.net.Socket;

// this method is used in order to hold the socket instace valu
public class ConnectionInstance {
    public Socket soc = null;
    public DataInputStream socketInput = null;
    public DataOutputStream socketOutput = null;
    
    // constructor, inititates the values of the object
    public ConnectionInstance(Socket socket) throws IOException {
            soc = socket;
            socketInput = new DataInputStream(soc.getInputStream());
            socketOutput = new DataOutputStream(soc.getOutputStream());
    }
    // destructor = constructor of destruction of object'socConn values
    public ConnectionInstance(ConnectionInstance socConn) throws IOException{
        try{
        socConn.socketInput.close();
        socConn.socketOutput.close();
        socConn.soc.close();
        } catch(Exception e){
            System.out.println("Conection Finished");
        }
    }
}    
