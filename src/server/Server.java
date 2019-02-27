/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.*; 
import java.util.*; 
import java.net.*; 
  
// Server class 
public class Server  
{ 
    // ArrayList to store active clients 
    static Vector<ClientHandler> clientList = new Vector<>(); 
      
    // counter for clients 
    static int i = 1; 
  
    public static void main(String[] args) throws IOException  
    { 
        // server is listening on port 4200 
        ServerSocket ss = new ServerSocket(4200); 
        
        Socket s; 
          
        // running infinite loop for getting 
        // client request 
        while (true)  
        { 
            // Accept the incoming request 
            s = ss.accept(); 
  
           // System.out.println("New client request received : " + s); 
              
            // obtain input and output streams 
            DataInputStream input = new DataInputStream(s.getInputStream()); 
            DataOutputStream output = new DataOutputStream(s.getOutputStream()); 
              
           // System.out.println("Creating a new handler for this client..."); 
  
            // Create a new handler object for handling this request. 
            ClientHandler client = new ClientHandler(s,"client" + i, input, output); 
            System.out.println("CREATING NEW CLIENT HOLDER");
            // Create a new Thread with this object. 
            Thread t = new Thread(client); 
              
            //System.out.println("Adding this client to active client list"); 
  
            // add this client to active clients list 
            clientList.add(client); 
  
            // start the thread. 
            t.start(); 
  
            // increment i for new client. 
            // i is used for naming only, and can be replaced 
            // by any naming scheme 
            i++; 
            
        } 
    } 
} 
  
// ClientHandler class 
class ClientHandler implements Runnable  
{ 
   
    private String name; 
    final DataInputStream dis; 
    final DataOutputStream dos; 
    Socket s; 
    boolean isloggedin; 
      
    // constructor 
    public ClientHandler(Socket s, String name, DataInputStream input, DataOutputStream output) { 
        this.dis = input; 
        this.dos = output; 
        this.name = name; 
        this.s = s; 
        this.isloggedin=true; 
    } 

    @Override
    public void run() { 
        String received; 
        while (true)  
        { 
            try
            { 
                // receive the string 
                received = dis.readUTF(); 
                //logout of server
                if(received.equals("logout")){ 
                    this.isloggedin=false; 
                    this.s.close(); 
                    break; 
                } 
                  
                // break the string into message and recipient part 
                /*GETS THE MESSAGE AND THE RECIPIENT - MAYBE WE COULD ALLOW THE USER 
                * TO CLICK ON THE CLIENT THEY WANT TO TALK TO SO THAT THEY DONT HAVE TO TYPE WITH THE '-'
                */
                StringTokenizer st = new StringTokenizer(received, "-"); //message is split with '-'
                String msg = st.nextToken(); //the message to be sent
                String recipient = st.nextToken(); //the name of the recipient
                System.out.println("Message from "+name+" : "+"\""+msg +"\""+" to : "+recipient);//THIS MUST STAY
                
                // search for the recipient in the connected devices list (clientList). 
                // clientList is the vector storing client of active users 
                for (ClientHandler ch : Server.clientList)  
                { 
                    // if the recipient is found, write on its output stream 
                    if (ch.name.equals(recipient) && ch.isloggedin==true)  
                    { 
                        ch.dos.writeUTF(this.name+": "+msg); //THIS IS SENT TO THE CLIENT AND IS READ TO BE PRINTED OUT
                        break; 
                    } 
                } 
            } catch (IOException e) { 
                  
                e.printStackTrace(); 
            } 
              
        } 
        try
        { 
            // closing resources 
            this.dis.close(); 
            this.dos.close(); 
              
        }catch(IOException e){ 
            e.printStackTrace(); 
        } 
    } 
    public String getName()
    {
        return name;
    }
} 
