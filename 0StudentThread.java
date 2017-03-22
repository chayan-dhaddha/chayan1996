/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
*/
package cdvirtualclient;


import java.io.*;
import java.util.Vector;
/**
 *
 * @author Chayan-Dhaddha
 */
public class StudentThread extends Thread{
    Student student;
    
    public StudentThread(Student student){
        this.student=student;
        try{
            }catch(Exception ex){
            System.out.println("Exception occured" + ex);
        }
        this.start();
    }
    
    public void run(){
        try{
            while(true){
                 ObjectInputStream in=new ObjectInputStream(CommResource.client.getInputStream());
                 Resource.ResponseCodes resp=(Resource.ResponseCodes)in.readObject();
                 if(resp==Resource.ResponseCodes.INIT_STUDENT_RESPONSE){
                     Vector<Vector>main=(Vector<Vector>)in.readObject();
                     //this.student.panel_upcoming_session.DATA.clear();
                     for(int i=0;i<main.size();i++){
                         this.student.panel_upcoming_session.DATA.add(main.elementAt(i));
                     }
                     this.student.panel_upcoming_session.table.repaint();
                 }
                 
                 if(resp==Resource.ResponseCodes.NEW_SESSION){
                     Vector<Vector>main=(Vector<Vector>)in.readObject();
                     //this.mentor.panel_session_initiate.DATA.clear();
                     for(int i=0;i<main.size();i++){
                         this.student.panel_upcoming_session.DATA.add(main.elementAt(i));
                     }
                     this.student.panel_upcoming_session.table.repaint();
                 }
                 else if(resp==Resource.ResponseCodes.FILTER_SUCCESS){
                     Vector<Vector>main=(Vector<Vector>)in.readObject();
                     this.student.panel_past_session.DATA.clear();
                     for(int i=0;i<main.size();i++){
                         this.student.panel_past_session.DATA.add(main.elementAt(i));
                     }
                     this.student.panel_past_session.table.repaint();
                     System.out.println("filter success");
                     
                 }
            }
        }catch(Exception ex){
            System.out.println("Exception occured" + ex);
        }
    }
}
