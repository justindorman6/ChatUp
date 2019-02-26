/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.io.*; 
import java.net.*; 
import java.util.Scanner; 
  
public class Client  
{ 
    final static int serverPort = 4200; 
    static String sendMsg;
    static String receiveMsg;
    public static DataInputStream input;
    public static DataOutputStream output;
    
    
    public static void main(String args[]) throws UnknownHostException, IOException  
    { 
        //Scanner scn = new Scanner(System.in); 
          
        // getting localhost ip 
        InetAddress ip = InetAddress.getByName("localhost"); 
          
        // establish the connection 
        Socket s = new Socket(ip, serverPort); 
        
        // obtaining input and out streams 
        input = new DataInputStream(s.getInputStream()); //HAVE TO SOME HOW ADJUST THE OUTPUTSTREAMS
        output = new DataOutputStream(s.getOutputStream()); 
        
         //open gui
        new ChatUp().setVisible(true);
    } 
    public void sendMessage(String message)
    {
        // write on the output stream
        try { 
            output.writeUTF(sendMsg); 
        } catch (IOException ex) { 
            ex.printStackTrace(); 
        }
    }
    public String readMessage()
    {
        //read the message sent to this client
        try {  
            return input.readUTF(); 
        } catch (IOException e) { 
            e.printStackTrace(); 
        } 
        return "No Message Received";
    }
    
} 
