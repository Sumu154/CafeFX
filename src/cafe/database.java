/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cafe;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 *
 * @author sumaiya
 */
public class database {
       public static Connection connectDB(){
           try{
               String url = "jdbc:mysql://localhost:3307/cafefx";
               String username = "admin";
               String password = "admin@7";
               
               Class.forName("com.mysql.cj.jdbc.Driver");
               
               Connection connect = DriverManager.getConnection(url, username, password);
               return connect;
               
      
           }catch(Exception e){e.printStackTrace();}
           return null;
       }
}
