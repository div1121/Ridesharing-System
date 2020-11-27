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
        
        String searchreq = "SELECT T.id, D.name, V.id, V.model, T.start_time, T.end_time, T.fee, T.start_location, T.destination  "
                + "FROM request R, trip T, driver D, vehicle V "
                + "WHERE D.id=T.driver_id AND D.vehicle_id=V.id AND T.passenger_id=? AND T.start_time >= ? AND T.end_time < (? + INTERVAL 1 DAY) AND T.destination=? "
                + "ORDER BY T.start_time DESC";
        
        
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
