package HtcpcpSystem;

import java.util.Scanner;

public class Decryption_CaesarCipher {
    private String plainMessage = "";
    private String decryptedMessage = "";
    private int key;
    
    public Decryption_CaesarCipher(String message, int KEY){
        plainMessage = message;
        key = KEY;
        char ch;
        //System.out.println("message to decrypt "+plainMessage);
        for(int i = 0; i < plainMessage.length(); ++i){
            ch = plainMessage.charAt(i);
            if(ch >= 'a' && ch <= 'z'){
                ch = (char)(ch - key);
                if(ch < 'a'){
	            ch = (char)(ch + 'z' - 'a' + 1);
                }
	        decryptedMessage += ch;
	    } else if(ch >= 'A' && ch <= 'Z'){
                ch = (char)(ch - key);
	        if(ch < 'A'){
	            ch = (char)(ch + 'Z' - 'A' + 1);
	        }
	        decryptedMessage += ch;
	    } else {
                decryptedMessage += ch;
	    }
	}
	//System.out.println("Decrypted Message = " + decryptedMessage);
    }
    public String get_decrypted(){
        return decryptedMessage;
    }    
}
