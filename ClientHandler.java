/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverapp;
import java.net.*;
import java.io.*;
/**
 *
 * @author Chayan-Dhaddha
 */
public class ClientHandler extends Thread {
      Socket client;
      int index;
      String name,str;
     public ClientHandler(Socket client){
         this.client=client;
         ClientInfo.loggedInClient.add(client);
         this.start();
     }
     public void run(){
         try{
             ObjectInputStream in=new ObjectInputStream(client.getInputStream());
             ObjectOutputStream out;
             while(true){
                  name=in.readObject().toString(); 
                  if(name.equals("QUIT")){
                         for(int i=0;i<ClientInfo.loggedInClient.size();i++){
                          if(ClientInfo.loggedInClient.elementAt(i).equals(this.client)){
                              ClientInfo.loggedInClient.remove(i);
                              System.out.println("Client removed from the network");
                              break;
                          }
                     }
                     break;    
                  }
                  else{
                       str=in.readObject().toString();
                    if(!str.equals(null)){
                      for(int i=0;i<ClientInfo.loggedInClient.size();i++){
                          if(!ClientInfo.loggedInClient.elementAt(i).equals(this.client)){
                              out=new ObjectOutputStream(ClientInfo.loggedInClient.elementAt(i).getOutputStream());
                              out.writeObject(name);
                              out.writeObject(str);
                          }
                      }   
                    }   
                  } 
                   
             }   
             
                
         }catch(Exception ex){
             System.out.println("hascjhb");
         }
    }
}