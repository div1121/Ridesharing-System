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
public class Passenger {
    public Connection conn;
    public Passenger(Connection conn){
        this.conn = conn;
    }
    void msg()
    {
        System.out.println("Login as Passenger");
    }

    private void ReqRide()
    {
        System.out.println("1. Request a ride");
    }
    private void  CheckTripRecord()
    {
        ResultSet rs = null;
        System.out.println("2. Check trip records");
        Scanner sc = new Scanner(System.in);
        System.out.println("Please enter your ID.");
        int pid = Integer.parseInt(sc.nextLine());
        System.out.println("Please enter the start date.");
        String startdate = sc.nextLine();
        System.out.println("Please enter the end date.");
        String enddate = sc.nextLine();
        System.out.println("Please enter the destination.");
        String dest = sc.nextLine();
        //System.out.println(pid + "," +startdate + "," + enddate + "," + dest);
        String triprecord = "SELECT T.id, D.name, V.id, V.model, T.start_time, T.end_time, T.fee, T.start_location, T.destination  "
                + "FROM trip T, driver D, vehicle V "
                + "WHERE D.id=T.driver_id AND D.vehicle_id=V.id AND T.passenger_id=? AND T.start_time >= ? AND T.end_time < (? + INTERVAL 1 DAY) AND T.destination=? "
                + "ORDER BY T.start_time DESC";
        //String triprecord1 = "SELECT T.id, D.name, V.id, V.model, T.start_time, T.end_time, T.fee, T.start_location, T.destination  FROM trip T, driver D, vehicle V WHERE D.id=T.driver_id AND D.vehicle_id=V.id AND T.passenger_id=? AND T.destination=?";
        try {
            PreparedStatement stmt = conn.prepareStatement(triprecord);
            stmt.setInt(1,pid);
            stmt.setString(2,startdate);
            stmt.setString(3,enddate);
            stmt.setString(4,dest);
            if (stmt.execute()) {
                rs = stmt.getResultSet();
            }
            System.out.println("Trip_id, Driver Name, Vehicle ID, Vehicle Model, Start, End, Fee, Start Location, Destination");
            if(!rs.isBeforeFirst())
                System.out.println("No records found.");
            else
            while(rs.next()){
                System.out.print(rs.getInt(1));
                System.out.print(", ");
                System.out.print(rs.getString(2));
                System.out.print(", ");
                System.out.print(rs.getString(3));
                System.out.print(", ");
                System.out.print(rs.getString(4));
                System.out.print(", ");
                System.out.print(rs.getTimestamp(5));
                System.out.print(", ");
                System.out.print(rs.getTimestamp(6));
                System.out.print(", ");
                System.out.print(rs.getInt(7));
                System.out.print(", ");
                System.out.print(rs.getString(8));
                System.out.print(", ");
                System.out.print(rs.getString(9));
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
    private void GoBack()
    {
        Project a = new Project();
        System.out.println("");
        a.Menu();
    }

    void menu()
    {
        System.out.println("Passenger, what would you like to do?");
        System.out.println("1. Request a ride");
        System.out.println("2. Check trip records");
        System.out.println("3. Go back");
        System.out.println("Please enter [1-3]");
        Scanner sc = new Scanner(System.in);
        int op = sc.nextInt();
        switch (op)
        {
            case 1:
                ReqRide();
                break;
            case 2:
                CheckTripRecord();
                break;
            case 3:
                GoBack();
                break;
            default:
                System.out.println("Invalid input, please try again.");
                menu();
                break;
        }
    }
}
