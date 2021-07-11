package HtcpcpSystem;

import java.io.*;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class ClientOperator {

    private static String clientState = "INITIAL_STATE";
    private static String sendMessage;
    private static ConnectionInstance socketInstance;

    public static String clientRequest = "";
    public static String firstAddition = "";
    public static String secondAddition = "";
    public static String thirdAddition = "";

    public static int firstAddonVol;
    public static int secondAddonVol;
    public static int thirdAddonVol;
    public static String moveOn;
    public static String propMoveOn;

    ClientOperator(String incommingMsg) throws IOException {

        //switch between clientStates based on server reply
        switch (clientState) {
            case "INITIAL_STATE":
                System.out.println("HTCPCP 1.0");
                if (incommingMsg.contains("220")) {
                    System.out.println("Coffee request: "
                            + "would you like to send a PROPFIND request,"
                            + " to get a list of all coffee additions? REPLY WITH EITHER YES or NO");
                    //loop to keep prompting for correct input
                    while (true) {
                        Scanner propfindRequest = new Scanner(System.in);
                        String prompt = propfindRequest.nextLine();
                        if (prompt.equalsIgnoreCase("yes") || prompt.equalsIgnoreCase("no")) {
                            propMoveOn = prompt;
                            break;
                        } else {
                            System.out.println("REPLY WITH EITHER YES or NO");
                        }
                    }

                    //conditional statement for the respective input given
                    if (propMoveOn.equalsIgnoreCase("no")) {

                        System.out.println("Coffee request: Please specify three coffee additions to be added");

                        Scanner request = new Scanner(System.in);
                        firstAddition = request.nextLine().toLowerCase();
                        secondAddition = request.nextLine().toLowerCase();
                        thirdAddition = request.nextLine().toLowerCase();

                        System.out.println("Alright, " + firstAddition + ", " + secondAddition + ", " + thirdAddition);
                        System.out.println("How much " + firstAddition + " should be added? ");
                        System.out.println(firstAddition + ": ");
                        while (true) {
                            try {
                                Scanner additionsRequest = new Scanner(System.in);
                                firstAddonVol = additionsRequest.nextInt();
                                break;
                            } catch (InputMismatchException e) {
                                } System.out.println("Only Integer Inputs Allowed Please");
                        }
                        
                        System.out.println("Alright, "  + firstAddonVol + " " + firstAddition);
                        System.out.println("How much " + secondAddition + " should be added? ");
                        System.out.println(secondAddition + ": ");
                        
                        while (true) {
                            try {
                                Scanner additionsRequest = new Scanner(System.in);
                                secondAddonVol = additionsRequest.nextInt();
                                break;
                            } catch (InputMismatchException e) {
                                    System.out.println("Only Integer Inputs Allowed Please");
                                }
                            }
                     
                        System.out.println("Alright, "  + secondAddonVol + " " + secondAddition);
                        System.out.println("How much " + thirdAddition + " should be added? ");
                        System.out.println(thirdAddition + ": ");
                        while (true) {
                            try {
                                Scanner additionsRequest = new Scanner(System.in);
                                thirdAddonVol = additionsRequest.nextInt();
                                break;
                            } catch (InputMismatchException e) {
                                    System.out.println("Only Integer Inputs Allowed Please");
                            }
                        }
                        System.out.println("Alright, "  + thirdAddonVol + " " + thirdAddition);
                        //create htcpcp request from the provided coffee additions and additions volume'
                        clientRequest = "coffee://localhost/"
                                + Client.firstPort + "?" + firstAddition + ";" + firstAddonVol + "#"
                                + secondAddition + ";" + secondAddonVol + "#"
                                + thirdAddition + ";" + thirdAddonVol;

                        System.out.println(clientRequest);
                        System.out.println("The above coffee request is ready to be sent to the server");
                        System.out.println("Do you want to change your coffee request? REPLY WITH EITHER YES or NO");

                        //keep prompting for correct input
                        while (true) {
                            Scanner continueRequest = new Scanner(System.in);
                            String toContinue = continueRequest.nextLine();
                            if (toContinue.equalsIgnoreCase("yes") || toContinue.equalsIgnoreCase("no")) {
                                moveOn = toContinue;
                                break;
                            } else {
                                System.out.println("REPLY WITH EITHER YES or NO");
                            }
                        }

                        //conditionals for the correct input given
                        if (moveOn.equalsIgnoreCase("no")) {
                            if (!Arrays.toString((ServerMachine.coffeAdditions)).contains(firstAddition.toLowerCase())) {
                                System.out.println(">>The coffee addition " + firstAddition + " isn't available");
                                new ClientOperator("220");
                            }

                            if (!Arrays.toString((ServerMachine.coffeAdditions)).contains(secondAddition.toLowerCase())) {
                                System.out.println(">>The coffee addition " + secondAddition + " isn't available");
                                new ClientOperator("220");
                            }

                            if (!Arrays.toString((ServerMachine.coffeAdditions)).contains(thirdAddition.toLowerCase())) {
                                System.out.println(">>The coffee addition " + thirdAddition + " isn't available");
                                new ClientOperator("220");
                            } else {
                                clientState = "BREW_STATE"; // change clientState
                            }

                        } else if (moveOn.equalsIgnoreCase("yes")) {
                            new ClientOperator("220");
                        }

                        //craft htcpcp propfind request if the input given is 'yes'
                    } else if (propMoveOn.equalsIgnoreCase("yes")) {

                        clientRequest = "propfind://localhost/";
                        System.out.println(clientRequest);
                        System.out.println(incommingMsg);
                        if (incommingMsg.contains("220")) {
                            System.out.println(Arrays.toString(ServerMachine.coffeAdditions) + "\r\n");
                            new ClientOperator("220");
                        }
                    }

                } else {
                    System.out.println("There was an unexpected input in: " + clientState);
                }

                break;
            case "BREW_STATE":
                System.out.println("> CLIENT IS NOW IN: " + clientState + " STAGE");
                System.out.println(".........."); 

                //create htcpcp brew request from the given additions
                clientRequest = "brew://localhost/"
                        + Client.firstPort + "?" + firstAddition + ";" + firstAddonVol + "#"
                        + secondAddition + ";" + secondAddonVol + "#"
                        + thirdAddition + ";" + thirdAddonVol;
                System.out.println(">>> Response from Server " + incommingMsg);
                System.out.println(clientRequest);
                if (incommingMsg.contains("202")) {
                    clientState = "POST_STATE";
                    System.out.println(">>> Coffee Brewing!");
                }
                break;

            case "POST_STATE":
                System.out.println("> CLIENT IS NOW IN: " + clientState);
                System.out.println(".........."); 

                //create htcpcp brew request from the given additions
                clientRequest = "post://localhost/"
                        + Client.firstPort + "?" + firstAddition + ";" + firstAddonVol + "#"
                        + secondAddition + ";" + secondAddonVol + "#"
                        + thirdAddition + ";" + thirdAddonVol;
                System.out.println("2.2 received response " + incommingMsg);
                System.out.println(clientRequest);
                if (incommingMsg.toUpperCase().contains("203")) {
                    System.out.println(">>> Sending Coffee Request");
                    clientState = "FINAL_STATE";

                } else if (incommingMsg.toUpperCase().contains("551")) {
                    clientState = "QUIT_STATE";
                }
                break;

            case "FINAL_STATE":
                System.out.println("> CLIENT IS NOW IN: " + clientState);
                System.out.println(".........."); 

                //send a 'final' message to the coffee server to request delivery of the COFFEE
                clientRequest = "final";
                if (incommingMsg.toUpperCase().contains("204")) {  //parse message
                    System.out.println("Final Server Response: " + incommingMsg);
                clientState ="WAIT_QUIT_STATE";
                clientRequest = "QUIT_STATE";
                } else if (incommingMsg.toUpperCase().contains("551")) {
                    clientState = "QUIT_STATE";
                }
                break;

            case "QUIT_STATE":
                System.out.println("2.5. clientState IS : " + clientState);
                // start interaction with server machine
                clientRequest = "QUIT_STATE";
                System.exit(0);
                break;

            case "WAIT_QUIT_STATE":
                System.out.println("clientState IS : " + clientState);
                //parse message
                    clientRequest = "QUIT_STATE";
                    //quit through destructor
                    new ConnectionInstance(socketInstance);
                   if (incommingMsg.toUpperCase().contains("500")) {
                    clientState = "QUIT_STATE";
                } else if (incommingMsg.toUpperCase().isEmpty()) {
                    System.out.println("Connection Shutdown");
                }
                break;
            default:
                break;
        }

    }

    public String getMessage() {
        return sendMessage;
    }

    //get method for the created htcpcp request that returns the request string uri
    public String getHtcpcpRequest() {
        return clientRequest;
    }
}
