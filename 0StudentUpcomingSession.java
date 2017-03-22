/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cdvirtualclient;

import java.awt.*;
import javax.swing.*;
import java.util.Vector;
/**
 *
 * @author Chayan-Dhaddha
 */
public class StudentUpcomingSession extends JPanel{
      
    Vector<String> HEAD;
    Vector DATA;
    JTable  table;
    JScrollPane jsp;
    public StudentUpcomingSession(){
        this.setLayout(null);
        HEAD=new Vector<String>();
        HEAD.add("INITIATED BY");
        HEAD.add("TOPIC");
        HEAD.add("DATE");
        HEAD.add("TIME");
        HEAD.add("DURATION");
        DATA=new Vector();
        table=new JTable(DATA,HEAD);
        jsp=new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        this.design();
    }

    void design(){
        this.setPosition(jsp,50,80,1200,550);    
    }
    
    void setPosition(Component C,int x,int y,int w,int h){
        this.add(C);
        C.setBounds(x,y,w,h);
    }    
}

