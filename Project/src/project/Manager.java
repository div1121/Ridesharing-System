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
public class Manager {
    public Connection conn;
    public Manager(Connection conn){
        this.conn = conn;
    }
    
    public static boolean isNumber(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            int d = Integer.parseInt(strNum);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }
    
    void msg()
    {
        System.out.println("Login as Manager");
    }

    private void FindTrip()
    {
        System.out.println("1. Find trips");
        ResultSet rs = null;
        String mdis=null,mxdis=null;
        Scanner sc = new Scanner(System.in);
        do{
            System.out.println("Please enter the minimum travelling distance.");
            mdis = sc.nextLine().trim();
            if (!isNumber(mdis))
                System.out.println("[ERROR] The input is not a number");
        }while(!isNumber(mdis));
        int mindis = Integer.parseInt(mdis);
        do{
            System.out.println("Please enter the maximum travelling distance.");
            mxdis = sc.nextLine().trim();
            if (!isNumber(mxdis))
                System.out.println("[ERROR] The input is not a number");
        }while(!isNumber(mxdis));
        int maxdis = Integer.parseInt(mxdis);
        String triprecord = "SELECT T.id, D.name, P.name, T.start_location, T.destination, TIMESTAMPDIFF(MINUTE,T.start_time,T.end_time)  "
                + "FROM trip T, driver D, passenger P, taxi_stop S1, taxi_stop S2 "
                + "WHERE D.id=T.driver_id AND T.passenger_id=P.id AND T.start_location=S1.name AND T.destination=S2.name AND T.end_time IS NOT NULL "
                + "AND (ABS(S1.location_x - S2.location_x) + ABS(S1.location_y - S2.location_y)) >= ? AND (ABS(S1.location_x - S2.location_x) + ABS(S1.location_y - S2.location_y)) <= ?";
        try {
            PreparedStatement stmt = conn.prepareStatement(triprecord);
            stmt.setInt(1,mindis);
            stmt.setInt(2,maxdis);
            if (stmt.execute()) {
                rs = stmt.getResultSet();
            }
            if(!rs.isBeforeFirst())
                System.out.println("No records found.");
            else {
                System.out.println("Trip_id, Driver Name, Passenger Name, Start Location, Destination, Duration");
                while(rs.next()){
                    System.out.print(rs.getInt(1));
                    System.out.print(", ");
                    System.out.print(rs.getString(2));
                    System.out.print(", ");
                    System.out.print(rs.getString(3));
                    System.out.print(", ");
                    System.out.print(rs.getString(4));
                    System.out.print(", ");
                    System.out.print(rs.getString(5));
                    System.out.print(", ");
                    System.out.print(rs.getString(6));
                    System.out.println();
                }
            }
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
        
        System.out.println("");
        menu();
    }
    private void GoBack()
    {
        Project a = new Project();
        System.out.println("");
        a.Menu();
    }

    void menu()
    {
        System.out.println("Manager, what would you like to do?");
        System.out.println("1. Find trips");
        System.out.println("2. Go back");
        System.out.println("Please enter [1-2]");
        Scanner sc = new Scanner(System.in);
        int op = sc.nextInt();
        switch (op)
        {
            case 1:
                FindTrip();
                break;
            case 2:
                GoBack();
                break;
            default:
                System.out.println("Invalid input, please try again.");
                menu();
                break;
        }
    }
}
