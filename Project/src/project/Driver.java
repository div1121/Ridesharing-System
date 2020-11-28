/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package project;

import java.sql.*;
import java.util.*;
/**
 *
 * @author user
 */
public class Driver {
    public Connection conn;
    public Driver(Connection conn){
        this.conn = conn;
    }
    void msg()
    {
        System.out.println("Login as Driver");
    }

    private void SearchReq()
    {
        
        ResultSet rs1 = null;
        ResultSet rs2 = null;
        ResultSet rs3 = null;
        
        String search_model="";
        int search_driving_years=0;
        
        
        System.out.println("1. Search requests");
        
        Scanner sc = new Scanner(System.in);
        System.out.println("Please enter your ID.");
        int did = Integer.parseInt(sc.nextLine());
        System.out.println("Please enter the coordinates of your location.");
        String coor[]= sc.nextLine().split(" ");
        int coor_x=0;
        int coor_y=0;
        for(int i =0 ;i < coor.length;i++){
            if(i==0)
                coor_x = Integer.parseInt(coor[i]);
            if(i==1)
                coor_y= Integer.parseInt(coor[i]);
           }
        
        System.out.println("Please enter the maximum distance from you to your passenger");
        int max_dist = Integer.parseInt(sc.nextLine());
        
        String model =" SELECT V.Model"
                + " FROM driver D,vehicle V "
                + " WHERE D.id= ? AND D.vehicle_id  = V.id ";
        //1:did
        
        String driving_years =" SELECT D.driving_years"
                + " FROM driver D "
                + " WHERE D.id = ? ";
        //1:did
        
        String searchreq = "SELECT R.id, P.name, R.passengers, R.start_location , R.destination "
                + " FROM request R, passenger P, taxi_stop T "
                + " WHERE R.id=P.id  AND R.taken=0 "
                + " AND ((R.model IS NULL) OR (R.model= ?)) "
                + " AND ((R.driving_years IS NULL) OR (R.driving_years <= ?)) "
                + " AND T.name= R.start_location"
                + " AND (ABS(? - T.location_x) + ABS(? - T.location_y)) <= ? ";
         
        
        //1:search_driving_years
        //2:search_model
        //3:coor_x
        //4:coor_y
        //5:max_dist 
        
         try {
            PreparedStatement stmt1 = conn.prepareStatement(model);//for model
            PreparedStatement stmt2 = conn.prepareStatement(driving_years);//for years
            PreparedStatement stmt3 = conn.prepareStatement(searchreq);

            stmt1.setInt(1,did);
            stmt1.execute();
            rs1=stmt1.getResultSet();
            if(!rs1.isBeforeFirst())
                System.out.println("No records found.");
            else{
               while(rs1.next()){
                    search_model=rs1.getString(1);
               }
            }
            
            stmt2.setInt(1,did);
            stmt2.execute();
            rs2=stmt2.getResultSet();
            if(!rs2.isBeforeFirst())
                System.out.println("No records found.");
            else{
            while(rs2.next()){
               search_driving_years=rs2.getInt(1);
                }
            }
            
            stmt3.setInt(1,search_driving_years);
            stmt3.setString(2,search_model);
            stmt3.setInt(3,coor_x);
            stmt3.setInt(4,coor_y);
            stmt3.setInt(5,max_dist);
            
            if (stmt3.execute()) {
                rs3= stmt3.getResultSet();
            }
            System.out.println("request ID, passenger name, num of passengers, start location, destination");
            if(!rs3.isBeforeFirst())
                System.out.println("No records found.");
            else
            while(rs3.next()){
                System.out.print(rs3.getInt(1));
                System.out.print(", ");
                System.out.print(rs3.getString(2));
                System.out.print(", ");
                System.out.print(rs3.getInt(3));
                System.out.print(", ");
                System.out.print(rs3.getString(4));
                System.out.print(", ");
                System.out.print(rs3.getString(5));
                System.out.println();
           
                
            }
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
        
        System.out.println("");
        menu();
              
        
        
    }
    private void TakeReq()
    {
        System.out.println("2. Take a request");
        Scanner sc = new Scanner(System.in);
       
    }
    private void FinishTrip()
    {
        System.out.println("3. Finish a trip");
    }
    private void GoBack()
    {
        Project a = new Project();
        System.out.println("");
        a.Menu();
    }

    void menu()
    {
        System.out.println("Driver, what would you like to do?");
        System.out.println("1. Search requests");
        System.out.println("2. Take a request");
        System.out.println("3. Finish a trip");
        System.out.println("4. Go back");
        System.out.println("Please enter [1-4]");
        Scanner sc = new Scanner(System.in);
        int op = sc.nextInt();
        switch (op)
        {
            case 1:
                SearchReq();
                break;
            case 2:
                TakeReq();
                break;
            case 3:
                FinishTrip();
                break;
            case 4:
                GoBack();
                break;
            default:
                System.out.println("Invalid input, please try again.");
                menu();
                break;
        }
    }

}
