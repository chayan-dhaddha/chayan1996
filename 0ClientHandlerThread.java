/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cdvirtualserver;

import java.net.*;
import java.io.*;
import java.sql.*;
import java.text.*;
import java.util.Date;
import java.util.Vector;
import javax.swing.*;

/**
 *
 * @author Chayan-Dhaddha
 */

public class ClientHandlerThread extends Thread{
    Socket client;
    String role,login_id,id,name;
    
    public ClientHandlerThread(Socket client){
        this.client=client;
        this.start();
    }
    
    public void run(){
        String query="";
        try{
            while(true){
                 ObjectInputStream in=new ObjectInputStream(this.client.getInputStream());
                 Resource.RequestCodes code=(Resource.RequestCodes)in.readObject();
                 if(code==Resource.RequestCodes.LOGIN_DETAIL){
                     this.role=in.readObject().toString();
                     this.login_id=in.readObject().toString();
                     String pwd=in.readObject().toString();
                                
                     query="select * from "+this.role +" where login_id='"+this.login_id+"' and password='"+pwd+"'";
                
                     ResultSet rs=ConnectionFactory.getInstance().getResultSet(query);
                     ObjectOutputStream out=new ObjectOutputStream(this.client.getOutputStream());
                     if(rs.next()){
                        
                        ClientInfo info=new ClientInfo();
                        info.id=rs.getString("id");
                        this.id=info.id;
                        info.client=this.client;
                        info.name=rs.getString("name");
                        this.name=info.name;
                        info.role=this.role;
                        CommResource.loggedInClient.add(info);
                         
                        out.writeObject("Success");
                        String str="\n" + this.role + "(" + this.name + "): Logged in on " + this.client.getInetAddress().getHostAddress() + " at " + new java.util.Date()/*Calendar.getInstance().toString()*/;
                        CommResource.admin.panel_server.txt_area.append(str);
                     }else{
                        out.writeObject("Failed");
                     }
                 }    
                 else if(code==Resource.RequestCodes.STUDENT_REGISTER){
                     this.login_id=in.readObject().toString();
                     String pwd=in.readObject().toString();
                     String name=in.readObject().toString();
                     String branch=in.readObject().toString();
                     String semester=in.readObject().toString();
                     String contact=in.readObject().toString();
                     query="insert into student set "+
                           "login_id='"+this.login_id+"',"+
                           "password='"+pwd+"',"+
                           "name='"+name+"',"+
                           "branch='"+branch+"',"+
                           "semester='"+semester+"',"+
                           "contact='"+contact+"'";
                     
                     int n=ConnectionFactory.getInstance().setData(query);
                     ObjectOutputStream out= new ObjectOutputStream(this.client.getOutputStream());
                     if(n>0){
                         out.writeObject("Success");
                     }else{
                         out.writeObject("Failed");
                     }
                 }    
                 else if(code==Resource.RequestCodes.INIT_UPCOMING_SESSION_MENTOR){
                      query="select initiated_by,topic,session_date,session_time,duration from session where status=0";
                      Vector<Vector> list=ConnectionFactory.getInstance().getData(query);
                      ObjectOutputStream out= new ObjectOutputStream(this.client.getOutputStream());
                      out.writeObject(Resource.ResponseCodes.INIT_MENTOR_RESPONSE);
                      out.writeObject(list);
                 }
                 else if(code==Resource.RequestCodes.INIT_UPCOMING_SESSION_STUDENT){
                      query="select initiated_by,topic,session_date,session_time,duration from session where status=0";
                      Vector<Vector> list=ConnectionFactory.getInstance().getData(query);
                      ObjectOutputStream out= new ObjectOutputStream(this.client.getOutputStream());
                      out.writeObject(Resource.ResponseCodes.INIT_STUDENT_RESPONSE);
                      out.writeObject(list);
                 }
                 else if(code==Resource.RequestCodes.SESSION_REGISTER){
                      String topic=in.readObject().toString();
                      String session_date=in.readObject().toString();
                      String session_time=in.readObject().toString();
                      String duration=in.readObject().toString();
                      String desc=in.readObject().toString();
                      String status="0";
                     
                      String initiated_on=new Date().toString();
                      //DateFormat ddf = new SimpleDateFormat("yyyy/MM/dd");
                      //Date date=ddf.parse(session_date);
                      query="insert into session set "+
                           "initiated_by="+this.id+","+
                           "topic='"+topic+"',"+
                           "session_date='"+session_date+"',"+
                           "session_time='"+session_time+"',"+
                           "duration='"+duration+"',"+
                           "description='"+desc+"',"+
                           "initiated_on='"+initiated_on+"',"+
                           "status="+status;
                     
                      int n=ConnectionFactory.getInstance().setData(query);
                     ObjectOutputStream out= new ObjectOutputStream(this.client.getOutputStream());
                     if(n>0){
                         out.writeObject(Resource.ResponseCodes.SESSION_REGISTER_SUCCESS);
                     }else{
                         out.writeObject(Resource.ResponseCodes.SESSION_REGISTER_FAILED);
                     }
                     query="select initiated_by,topic,session_date,session_time,duration from session where status=0";
                      Vector<Vector> list=ConnectionFactory.getInstance().getData(query);
                      for(int i=0;i<CommResource.loggedInClient.size();i++){
                           ObjectOutputStream tmp=new ObjectOutputStream(CommResource.loggedInClient.elementAt(i).client.getOutputStream());
                           tmp.writeObject(Resource.ResponseCodes.NEW_SESSION);
                           tmp.writeObject(list);
                      }
                 }
                 else if(code==Resource.RequestCodes.FILTER_PAST_SESSION){
                     Resource.RequestCodes req=(Resource.RequestCodes)in.readObject();
                     if(req==Resource.RequestCodes.FILTER_DATE){
                         String date_from=in.readObject().toString();
                         String date_to=in.readObject().toString();
                         query="select initiated_by,topic,session_date,session_time,duration from session where session_date>='"+date_from+"' and session_date<='"+date_to+"'";
                         Vector<Vector>list=ConnectionFactory.getInstance().getData(query);
                         ObjectOutputStream out=new ObjectOutputStream(this.client.getOutputStream());
                         out.writeObject(Resource.ResponseCodes.FILTER_SUCCESS);
                         out.writeObject(list);
                                   
                     }
                     else if(req==Resource.RequestCodes.FILTER_SEARCH){
                         String search=in.readObject().toString();
                         query="select initiated_by,topic,session_date,session_time,duration from session where topic='"+search+"'";
                         Vector<Vector>list=ConnectionFactory.getInstance().getData(query);
                         ObjectOutputStream out=new ObjectOutputStream(this.client.getOutputStream());
                         out.writeObject(Resource.ResponseCodes.FILTER_SUCCESS);
                         out.writeObject(list);
                         System.out.println("in filter_search");
                     }
                     else if(req==Resource.RequestCodes.FILTER_SESSION){
                         String session=in.readObject().toString();
                         query="select initiated_by,topic,session_date,session_time,duration from session where initiated_by='"+session+"'";
                         Vector<Vector>list=ConnectionFactory.getInstance().getData(query);
                         ObjectOutputStream out=new ObjectOutputStream(this.client.getOutputStream());
                         out.writeObject(Resource.ResponseCodes.FILTER_SUCCESS);
                         out.writeObject(list);
                         System.out.println("in filter session");
                     }    
                 }
                 else if(code==Resource.RequestCodes.LOGOUT){
                      for(int i=0;i<CommResource.loggedInClient.size();i++){
                          if(CommResource.loggedInClient.elementAt(i).client.equals(this.client)){
                              CommResource.loggedInClient.remove(i);
                              CommResource.admin.panel_server.txt_area.append("\n" + this.name + "(" + this.role + ")" + " Logged out (" + new java.util.Date() + ")");
                              break;
                          }
                     }
                     break;
                 }
                     
            }
        }catch(Exception ex){
              JOptionPane.showMessageDialog(null,"Error on client handler thread : " + ex);
              ex.printStackTrace();
        }
    }
}
