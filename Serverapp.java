/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverapp;
import java.net.*;
import java.io.*;
import java.util.Vector;
/**
/**
 *
 * @author Chayan-Dhaddha
 */
public class Serverapp {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
         try{
             int count=1;
            ServerSocket server=new ServerSocket(2233);
            System.out.println("Server Started Successfully");
            ClientInfo.loggedInClient=new Vector<Socket>();
            
            while(true){
                System.out.println("Waiting for the client...");
                Socket client=server.accept();
                System.out.println("Client "+ count+" connected Successfully");
                new ClientHandler(client);
                count++;
            }
            
        }catch(Exception ex){
            System.out.println("Cannot Start Server!!! "+ ex);
            ex.printStackTrace();
        }
         
    }
    
}
