/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cdvirtualclient;

import java.io.*;
import java.util.Vector;
import javax.swing.JOptionPane;
/**
 *
 * @author Chayan-Dhaddha
 */
public class MentorThread extends Thread{
    
    Mentor mentor;
    public MentorThread(Mentor mentor){
        this.mentor=mentor;
        this.start();
    }
    
    public void run(){
         try{
            while(true){
                 ObjectInputStream in=new ObjectInputStream(CommResource.client.getInputStream());
                 Resource.ResponseCodes resp=(Resource.ResponseCodes)in.readObject();
                 if(resp==Resource.ResponseCodes.INIT_MENTOR_RESPONSE){
                     Vector<Vector>main=(Vector<Vector>)in.readObject();
                     this.mentor.panel_session_initiate.DATA.clear();
                     for(int i=0;i<main.size();i++){
                         this.mentor.panel_session_initiate.DATA.add(main.elementAt(i));
                     }
                     this.mentor.panel_session_initiate.table.repaint();
                     
                 }
                 else if(resp==Resource.ResponseCodes.SESSION_REGISTER_SUCCESS){
                     JOptionPane.showMessageDialog(this.mentor,"Registration Successfull","Success",JOptionPane.INFORMATION_MESSAGE);
                        this.mentor.panel_session_initiate.set_txt_null();
                 }
                 else if(resp==Resource.ResponseCodes.SESSION_REGISTER_FAILED){
                        JOptionPane.showMessageDialog(this.mentor,"Registration Unsuccessfull","Error",JOptionPane.INFORMATION_MESSAGE);   
                 }
                 else if(resp==Resource.ResponseCodes.NEW_SESSION){
                     System.out.println("New Session");
                     Vector<Vector>main=(Vector<Vector>)in.readObject();
                     this.mentor.panel_session_initiate.DATA.clear();
                     System.out.println("Total sessions : " + main.size());
                     
                     for(int i=0;i<main.size();i++){
                         this.mentor.panel_session_initiate.DATA.insertElementAt(main.elementAt(i),i);
                     }
                     System.out.println("Data Size: " + this.mentor.panel_session_initiate.DATA.size());
                     this.mentor.panel_session_initiate.table.repaint();
                     System.out.println("Table painted");
                 }
                 else if(resp==Resource.ResponseCodes.FILTER_SUCCESS){
                     Vector<Vector>main=(Vector<Vector>)in.readObject();
                     this.mentor.panel_past_session.DATA.clear();
                     for(int i=0;i<main.size();i++){
                         this.mentor.panel_past_session.DATA.add(main.elementAt(i));
                     }
                     this.mentor.panel_past_session.table.repaint();
                     System.out.println("filter success");
                     
                 }
            }
        }catch(Exception ex){
            System.out.println("Exception occured " + ex);
        }
    }
}
