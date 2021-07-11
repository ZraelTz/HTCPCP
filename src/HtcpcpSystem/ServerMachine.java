package HtcpcpSystem;

import java.util.ArrayList;
import java.util.Arrays;

public class ServerMachine {
    private static String ServeState = "COFFEE_STATE";
    private static String incomingMsg;
    private static String outgoingMsg;

    public static String[] coffeAdditions = {"whiskey", "tumeric", "garlic", "sugar", "honey", "cream", "almonds", 
                    "milk",  "cream", "vodka", "cream", "vanilla", "butter", "bourbon"};
    public static String coffee;

    public static String hostAddr = "localhost";
    
    public ServerMachine(String clientMessage) {
        incomingMsg = clientMessage;
        
        //switch between states inorder to send appropriate message codes to the client machine
        switch(ServeState){
            // INIT state is accomplished with socket connection establishment
            case "COFFEE_STATE":
                System.out.println(">>>" + ServeState + " " + incomingMsg);
                // parsing packet
                if(incomingMsg.length()>512) {            // line too long
                     outgoingMsg = "500 command not recognized";
                }
                else if(incomingMsg.contains("coffee://")){
                    coffee = incomingMsg;
                    outgoingMsg = "201, COFFEE_STATE";    
                    ServeState = "BREW_STATE";
                    new ServerMachine("");
                    }
                else if (incomingMsg.contains("propfind://")) {
                    ServeState = "PROPFIND_STATE";
                    new ServerMachine("");
                }
                break;
                
                case "PROPFIND_STATE":
                System.out.println(">>>}}" + ServeState + " " + incomingMsg);
                // parsing packet
                if (incomingMsg.length() > 512) {            // line too long
                    outgoingMsg = "500 command not recognized";
                } else if (incomingMsg.contains("propfind://")) {
                    
                    ServeState = "COFFEE_STATE";
                    
                    new ServerMachine("");
                }

                break;
                
            case "BREW_STATE":
                System.out.println(">>>" + ServeState + " " + incomingMsg);
                // parsing packet
                if(incomingMsg.length()>512) {            // line too long
                     outgoingMsg = "500 command not recognized";
                }  else if(incomingMsg.contains("brew://")) {
                    outgoingMsg = "202, BREW_STATE";
                    ServeState = "POST_STATE";
                    
                }
                break;
                
                case "POST_STATE":
                System.out.println(">>>" + ServeState + " " + incomingMsg);
                // parsing packet
                if (incomingMsg.length() > 512) {            // line too long
                    outgoingMsg = "500 command not recognized";
                } else if (incomingMsg.contains("post://")) {
                    outgoingMsg = "203, POST_STATE";
                    ServeState = "FINAL_STATE";
                    
                }
                
                case "FINAL_STATE":
                // parsing packet
                if (incomingMsg.length() > 512) {            // line too long
                    outgoingMsg = "500 command not recognized";
                } else if (incomingMsg.equals("final")) { 
                    //extracts the coffee composition from the htcpcp coffee uri scheme
                     outgoingMsg = "204" + " here's your coffee: " + "COFFEE " 
                             + "\r\n" + coffee.substring(coffee.indexOf("?")+1)
                             .replaceAll(";", ":")
                             .replaceAll("#", "\r\n");
                     ServeState = "QUIT_STATE";
                }
                break;

                case "QUIT_STATE":
                System.out.println("**Client is Exiting**");
                if (incomingMsg.length() > 500) {            // line too long
                    outgoingMsg = "500 command not recognized";
                } else if (incomingMsg.contains("QUIT_STATE")) {
                    outgoingMsg = "221, quiting";
                }

                break;
                
            default:
                System.out.println("STUCK AT State " + ServeState);
        }
    }
    
    //accesible get method for the server messages to be sent to the client machine
    public String getMessage(){
        return outgoingMsg;
    }
    
}
