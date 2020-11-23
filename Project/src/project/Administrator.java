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
public class Administrator {
    public Connection conn;
    public Administrator(Connection conn){
        this.conn = conn;
    }
    public void teststatement() throws SQLException{
        String sql = "SELECT COUNT(*) FROM information_schema.tables";
        Statement stmt;
        ResultSet rs = null;
        try {
            stmt = conn.createStatement();
            if (stmt.execute(sql)) {
                rs = stmt.getResultSet();
            }
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
        if(!rs.isBeforeFirst())
            System.out.println("No records found.");
        else 
        while(rs.next()){
            System.out.print(rs.getInt(1));
            System.out.println();
	}
    }

    void msg()
    {
        System.out.println("Login as Admin");
    }

    private void CreateTables()
    {
        System.out.println("1. Create tables");
        String create_driver = 
                "CREATE TABLE driver" +
                    "(id INTEGER not NULL, " +
                    " name VARCHAR(31), " + 
                    " vehicle_id VARCHAR(7), " + 
                    " driving_years INTEGER unsigned, " + 
                    " PRIMARY KEY ( id ))" ;      
        
         String create_vehicle = 
                "CREATE TABLE vehicle" +
                    "(id VARCHAR(7) not NULL, " +
                    " model VARCHAR(31), "+
                    " seats INTEGER unsigned, " + 
                    " PRIMARY KEY ( id ))";
         
           String create_passenger =
                "CREATE TABLE vehicle" +
                    "(id INTEGER unsigned not NULL, " +
                    " name VARCHAR(31), "+
                    " PRIMARY KEY ( id ))";
           String create_taxi_stop = 
                   "CREATE TABLE taxi_stop" + 
                    " (name VARCHAR(21) not NULL,"+
                    " location_x INTEGER, "+ 
                    " location_y INTEGER)";
                

            String create_request =
                "CREATE TABLE request" +
                    "(id INTEGER unsigned not NULL, " +
                    " passenger_id INTEGER unsigned, "+//in passenger
                    " start_location VARCHAR(21), "+ //in taxi_stop
                    " destination VARCHAR(21), "+//in taxi_stop
                    " model VARCHAR(31),"+ //in vehicle
                    " passengers INTEGER unsigned,"+
                    " taken BIT"+ //boolean 
                    " driving_years INTEGER unsigned, "+ //in driver
                    " FOREIGN KEY start_location REFERENCES taxi_stop (name), "+
                    " FOREIGN KEY destination REFERENCES taxi_stop (name), "+
                    " FOREIGN KEY passenger_id REFERENCES passenger (id), "+
                    " FOREIGN KEY model REFERENCES vehicle, "+
                    " FOREIGN KEY driving_years REFERENCES driver) ";
            
            String create_trip = 
                   " CREATE TABLE trip " +
                   " (id INTEGER unsigned not NULL, "+
                    " driver_id INTEGER unsigned, " +//in driver
                    " passenger_id VARCHAR(7), "+// in psasenger
                    " start_location VARCHAR(21), "+ // in taxi_stop
                    " destination VARCHAR(21), "+// in taxi_stop
                    " start_time DATETIME, "+ 
                    " end_time DATETIME, "+
                    " fee INTEGER unsigned,"+
                    " FOREIGN KEY start_location REFERENCES taxi_stop (name), "+
                    " FOREIGN KEY destination REFERENCES taxi_stop (name), "+
                    " FOREIGN KEY driver_id REFERENCES driver (id), "+
                    " FOREIGN KEY passenger_id REFERENCES passenger (id))";
    
        Statement stmt;
        try {
            stmt = conn.createStatement();
            stmt.execute(create_driver);
            stmt.execute(create_vehicle);
            stmt.execute(create_passenger);
            stmt.execute(create_taxi_stop);
            stmt.execute(create_request);
            stmt.execute(create_trip);
            System.out.println("Processing...Done! Tables are created!");
             
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
    }
    private void DeleteTables()
    {
        System.out.println("2. Delete tables");
        
        
        
    }
    private void LoadData()
    {
        System.out.println("3. Load data");
    }
    private void CheckData()
    {
        System.out.println("4. Check data");
    }
    private void GoBack()
    {
        System.out.println("5. Go back");
        Project a = new Project();
        System.out.println("");
        a.Menu();
    }
 

    void menu()
    {
        System.out.println("Administrator, what would you like to do?");
        System.out.println("1. Create tables");
        System.out.println("2. Delete tables");
        System.out.println("3. Load data");
        System.out.println("4. Check data");
        System.out.println("5. Go back");
        System.out.println("Please enter [1-5]");
        Scanner sc = new Scanner(System.in);
        int op = sc.nextInt();
        switch (op)
        {
            case 1:
                CreateTables();
                break;
            case 2:
                DeleteTables();
                break;
            case 3:
                LoadData();
                break;
            case 4:
                CheckData();
                break;
            case 5:
                GoBack();
                break;
            default:
                System.out.println("Invalid input, please try again.");
                menu();
                break;
        }
    }


}
