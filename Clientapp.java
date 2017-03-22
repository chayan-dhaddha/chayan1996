
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clientapp;

import java.io.*;
import java.net.*;
import java.util.Scanner;

/**
 *
 * @author Chayan-Dhaddha
 */
public class Clientapp {

    /**
     * @param args the command line arguments
     */
    String name;
    Socket client;
    Thread cast;
    public Clientapp(String IpAddress,String name){
        try{
           InetAddress inet=InetAddress.getByName(IpAddress);
           client=new Socket(inet,2233); 
        }catch(Exception ex){
            System.out.println("cannot");
        }
        this.name=name;
        cast=new BroadCast();
        cast.start();
    }
    public void message()throws Exception{
         String str;
         ObjectOutputStream out=new ObjectOutputStream(client.getOutputStream());
         while(true){
             Scanner s=new Scanner(System.in);
             str=s.nextLine();
             if(str.equals("quit")){
                out.writeObject("QUIT");
                System.exit(0);
             }
             out.writeObject(name);
             out.writeObject(str);
             
         }
         
             
    }
    public static void main(String[] args)throws Exception {
        // TODO code application logic here
        
        System.out.println("Enter Server IP address:");
        Scanner s=new Scanner(System.in);
        String ip=s.nextLine();
        System.out.println("Enter your name:");
        String name=s.nextLine();
        Clientapp client=new Clientapp(ip,name);
        System.out.println("Connected successfull from server");
        client.message();
    }
    
    public class BroadCast extends Thread {
        String message,name; 
        public void run(){
             try{
                 while(true){
                     ObjectInputStream in=new ObjectInputStream(client.getInputStream()); 
                     name=in.readObject().toString();
                     message=in.readObject().toString();
                     System.out.println("Message From "+name+ " :: "+message);
                 }    
             }catch(Exception ex){
                 
             }
             
        }
    }
}
