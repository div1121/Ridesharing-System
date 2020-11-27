/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//package project;

import java.sql.*;
import java.util.*;
import java.io.*;
import java.text.*;
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
                        "(id INTEGER unsigned not NULL, " +
                        " name VARCHAR(31), " +
                        " vehicle_id VARCHAR(7), " +
                        " driving_years INTEGER unsigned, " +
                        " PRIMARY KEY ( id ), " +
                        " FOREIGN KEY (vehicle_id) REFERENCES vehicle(id) ON DELETE CASCADE ON UPDATE CASCADE)";

        String create_vehicle =
                "CREATE TABLE vehicle" +
                        "(id VARCHAR(7) not NULL, " +
                        " model VARCHAR(31), "+
                        " seats INTEGER unsigned, " +
                        " PRIMARY KEY ( id )) " ;

        String create_passenger =
                "CREATE TABLE passenger" +
                        "(id INTEGER unsigned not NULL, " +
                        " name VARCHAR(31), "+
                        " PRIMARY KEY ( id ))";

        String create_taxi_stop =
                "CREATE TABLE taxi_stop" +
                        " (name VARCHAR(21) not NULL,"+
                        " location_x INTEGER, "+
                        " location_y INTEGER, "+
                        " PRIMARY KEY ( name ))";


        String create_request =
                "CREATE TABLE request" +
                        "(id INTEGER unsigned not NULL, " +
                        " start_location VARCHAR(21) not NULL, "+ //in taxi_stop
                        " destination VARCHAR(21) not NULL, "+    //in taxi_stop
                        " passenger_id INTEGER unsigned not NULL, "+//in passenger
                        " model VARCHAR(31), "+ //in vehicle
                        " passengers INTEGER unsigned, "+
                        " taken BIT, "+ //boolean
                        " driving_years INTEGER unsigned, "+ //in driver
                        " PRIMARY KEY (id)) ";

        String alter_request =
                "ALTER TABLE request" +
                        " ADD FOREIGN KEY (start_location) REFERENCES taxi_stop(name) ON DELETE CASCADE ON UPDATE CASCADE, "+
                        " ADD FOREIGN KEY (destination) REFERENCES taxi_stop(name) ON DELETE CASCADE ON UPDATE CASCADE, "+
                        " ADD FOREIGN KEY (passenger_id) REFERENCES passenger(id) ON DELETE CASCADE ON UPDATE CASCADE ";
        //" ADD FOREIGN KEY (model) REFERENCES vehicle ON DELETE CASCADE ON UPDATE CASCADE ";
        //** Problem Here, model is not a primary key, and we cannot use UNIQUE on it, so.... **
        //" ADD FOREIGN KEY (driving_years) REFERENCES driver ON DELETE CASCADE ON UPDATE CASCADE ";

        String create_trip =
                " CREATE TABLE trip " +
                        " (id INTEGER unsigned not NULL, "+
                        " driver_id INTEGER unsigned not NULL, " +//in driver
                        " passenger_id INTEGER unsigned not NULL, "+// in psasenger
                        " start_location VARCHAR(21) not NULL, "+ // in taxi_stop
                        " destination VARCHAR(21) not NULL, "+// in taxi_stop
                        " start_time DATETIME, "+
                        " end_time DATETIME, "+
                        " fee INTEGER unsigned,"+
                        " PRIMARY KEY (id), "+
                        " FOREIGN KEY (start_location) REFERENCES taxi_stop(name), "+
                        " FOREIGN KEY (destination) REFERENCES taxi_stop(name), "+
                        " FOREIGN KEY (driver_id) REFERENCES driver(id), "+
                        " FOREIGN KEY (passenger_id) REFERENCES passenger(id))";

        Statement stmt;
        try {
            stmt = conn.createStatement();
            stmt.execute(create_vehicle);
            stmt.execute(create_driver);
            stmt.execute(create_passenger);
            stmt.execute(create_taxi_stop);
            stmt.execute(create_request);
            stmt.execute(alter_request);
            stmt.execute(create_trip);
            System.out.println("Processing...Done! Tables are created!");

        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }

        System.out.println("");
        menu();

    }
    private void DeleteTables()
    {
        System.out.println("2. Delete tables");
        String delete_driver = "DROP TABLE IF EXISTS driver";
        String delete_vehicle = "DROP TABLE IF EXISTS vehicle";
        String delete_passenger = "DROP TABLE IF EXISTS passenger";
        String delete_taxi_stop = "DROP TABLE IF EXISTS taxi_stop";
        String delete_request = "DROP TABLE IF EXISTS request";
        String delete_trip = "DROP TABLE IF EXISTS trip";
        Statement stmt;
        try
        {
            stmt = conn.createStatement();
            stmt.execute(delete_request);
            stmt.execute(delete_trip);
            stmt.execute(delete_driver);
            stmt.execute(delete_vehicle);
            stmt.execute(delete_passenger);
            stmt.execute(delete_taxi_stop);

            System.out.println("Processing...Done! Tables are deleted!");
        }
        catch (SQLException ex)
        {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }

        System.out.println("");
        menu();

    }
    private void load_drivers(String path)
    {
        int id, driving_years;
        String name, vid;
        try {
            InputStreamReader isr = new InputStreamReader(new FileInputStream(path));
            BufferedReader reader = new BufferedReader(isr);
            String line = null;
            while ((line = reader.readLine()) != null) {
                String item[] = line.split(",");
                id = Integer.parseInt(item[0].trim());
                name = item[1].trim();
                vid = item[2].trim();
                driving_years = Integer.parseInt(item[3].trim());
                //System.out.println(id + "," + name + "," + vid + "," + driving_years);
                String insert_driver = "INSERT INTO driver (id,name,vehicle_id,driving_years) VALUES (?,?,?,?)" ;
                PreparedStatement stmt = conn.prepareStatement(insert_driver);
                stmt.setInt(1,id);
                stmt.setString(2,name);
                stmt.setString(3,vid);
                stmt.setInt(4,driving_years);
                stmt.addBatch();
                stmt.executeBatch();
            }
        }
        catch(FileNotFoundException e){
            e.printStackTrace();
        }
        catch(IOException e){
            e.printStackTrace();
        }
        catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
    }
    private void load_vehicle(String path)
    {
        String id, model;
        int seats;
        try {
            InputStreamReader isr = new InputStreamReader(new FileInputStream(path));
            BufferedReader reader = new BufferedReader(isr);
            String line = null;
            while ((line = reader.readLine()) != null) {
                String item[] = line.split(",");
                id = item[0].trim();
                model = item[1].trim();
                seats = Integer.parseInt(item[2].trim());
                //System.out.println(id + "," + name + "," + vid + "," + driving_years);
                String insert_vehicle = "INSERT INTO vehicle(id,model,seats) VALUES (?,?,?)" ;
                PreparedStatement stmt = conn.prepareStatement(insert_vehicle);
                stmt.setString(1,id);
                stmt.setString(2,model);
                stmt.setInt(3,seats);
                stmt.addBatch();
                stmt.executeBatch();
            }
        }
        catch(FileNotFoundException e){
            e.printStackTrace();
        }
        catch(IOException e){
            e.printStackTrace();
        }
        catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
    }
    private void load_taxi_stop(String path)
    {
        String name;
        int x,y;
        try {
            InputStreamReader isr = new InputStreamReader(new FileInputStream(path));
            BufferedReader reader = new BufferedReader(isr);
            String line = null;
            while ((line = reader.readLine()) != null) {
                String item[] = line.split(",");
                name = item[0].trim();
                x = Integer.parseInt(item[1].trim());
                y = Integer.parseInt(item[2].trim());
                //System.out.println(id + "," + name + "," + vid + "," + driving_years);
                String insert_vehicle = "INSERT INTO taxi_stop(name,location_x,location_y) VALUES (?,?,?)" ;
                PreparedStatement stmt = conn.prepareStatement(insert_vehicle);
                stmt.setString(1,name);
                stmt.setInt(2,x);
                stmt.setInt(3,y);
                stmt.addBatch();
                stmt.executeBatch();
            }
        }
        catch(FileNotFoundException e){
            e.printStackTrace();
        }
        catch(IOException e){
            e.printStackTrace();
        }
        catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
    }
    private void load_trip(String path)
    {
        String start_loc,dest,start_time, end_time;
        int id,did,pid,fee;

        try {
            InputStreamReader isr = new InputStreamReader(new FileInputStream(path));
            BufferedReader reader = new BufferedReader(isr);
            String line = null;
            while ((line = reader.readLine()) != null) {
                String item[] = line.split(",");
                id = Integer.parseInt(item[0].trim());
                did = Integer.parseInt(item[1].trim());
                pid = Integer.parseInt(item[2].trim());
                start_time = item[3].trim();
                end_time = item[4].trim();
                start_loc = item[5].trim();
                dest = item[6].trim();
                fee = Integer.parseInt(item[7].trim());
                java.util.Date tmp1 = new SimpleDateFormat("YYY-MM-DD HH:mm:ss").parse(start_time);
                java.util.Date tmp2 = new SimpleDateFormat("YYY-MM-DD HH:mm:ss").parse(end_time);
                //System.out.println(id + "," + name + "," + vid + "," + driving_years);
                String insert_vehicle = "INSERT INTO trip(id, driver_id,passenger_id,start_time,end_time,start_location,destination,fee) VALUES (?,?,?,?,?,?,?,?)" ;
                PreparedStatement stmt = conn.prepareStatement(insert_vehicle);
                stmt.setInt(1,id);
                stmt.setInt(2,did);
                stmt.setInt(3,pid);
                stmt.setTimestamp(4,new java.sql.Timestamp(tmp1.getTime()));
                stmt.setTimestamp(5,new java.sql.Timestamp(tmp2.getTime()));
                stmt.setString(6,start_loc);
                stmt.setString(7,dest);
                stmt.setInt(8,fee);
                stmt.addBatch();
                stmt.executeBatch();
            }
        }
        catch(FileNotFoundException e){
            e.printStackTrace();
        }
        catch(IOException e){
            e.printStackTrace();
        }
        catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
        catch(ParseException e){
            e.printStackTrace();
        }
    }
    private void load_passenger(String path)
    {
        String name;
        int id;
        try {
            InputStreamReader isr = new InputStreamReader(new FileInputStream(path));
            BufferedReader reader = new BufferedReader(isr);
            String line = null;
            while ((line = reader.readLine()) != null) {
                String item[] = line.split(",");
                id = Integer.parseInt(item[0].trim());
                name = item[1].trim();
                //System.out.println(id + "," + name + "," + vid + "," + driving_years);
                String insert_vehicle = "INSERT INTO passenger(id, name) VALUES (?,?)" ;
                PreparedStatement stmt = conn.prepareStatement(insert_vehicle);
                stmt.setInt(1,id);
                stmt.setString(2,name);
                stmt.addBatch();
                stmt.executeBatch();
            }
        }
        catch(FileNotFoundException e){
            e.printStackTrace();
        }
        catch(IOException e){
            e.printStackTrace();
        }
        catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
    }
    private void LoadData()
    {
        System.out.println("3. Load data");
        System.out.println("Please enther the folder path");
        Scanner sc = new Scanner(System.in);
        String path = sc.nextLine();
        String path_driver = path + "\\drivers.csv";
        String path_vehicle = path + "\\vehicles.csv";
        String path_passenger = path + "\\passengers.csv";
        String path_trips = path + "\\trips.csv";
        String path_taxi_stop = path + "\\taxi_stops.csv";

        load_taxi_stop(path_taxi_stop);
        System.out.println("Finished load_taxi_stop");
        load_vehicle(path_vehicle);
        System.out.println("Finished load_vehicle");
        load_passenger(path_passenger);
        System.out.println("Finished load_passenger");
        load_drivers(path_driver);
        System.out.println("Finished load_driver");
        load_trip(path_trips);
        System.out.println("Finished load_trip");

        System.out.println("Processing...Data is loaded!");
        System.out.println("");
        menu();
    }
    private void CheckData()
    {
        System.out.println("4. Check data");
        String checkvehicle = "SELECT COUNT(*) FROM vehicle";
        String checkpassenger = "SELECT COUNT(*) FROM passenger";
        String checkdriver = "SELECT COUNT(*) FROM driver";
        String checktrip = "SELECT COUNT(*) FROM trip";
        String checkrequest = "SELECT COUNT(*) FROM request";
        String checktaxistop = "SELECT COUNT(*) FROM taxi_stop";
        Statement stmt;
        ResultSet rs = null;
        try {
            stmt = conn.createStatement();
            if (stmt.execute(checkvehicle))
                rs = stmt.getResultSet();
            if(!rs.isBeforeFirst())
                System.out.println("No records found.");
            else
                while(rs.next()){
                    System.out.print("Vehicle: " + rs.getInt(1));
                    System.out.println();
                }
            if (stmt.execute(checkpassenger))
                rs = stmt.getResultSet();
            if(!rs.isBeforeFirst())
                System.out.println("No records found.");
            else
                while(rs.next()){
                    System.out.print("Passenger: " + rs.getInt(1));
                    System.out.println();
                }
            if (stmt.execute(checkdriver))
                rs = stmt.getResultSet();
            if(!rs.isBeforeFirst())
                System.out.println("No records found.");
            else
                while(rs.next()){
                    System.out.print("Driver: " + rs.getInt(1));
                    System.out.println();
                }
            if (stmt.execute(checktrip))
                rs = stmt.getResultSet();
            if(!rs.isBeforeFirst())
                System.out.println("No records found.");
            else
                while(rs.next()){
                    System.out.print("Trip: " + rs.getInt(1));
                    System.out.println();
                }
            if (stmt.execute(checkrequest))
                rs = stmt.getResultSet();
            if(!rs.isBeforeFirst())
                System.out.println("No records found.");
            else
                while(rs.next()){
                    System.out.print("Request: " + rs.getInt(1));
                    System.out.println();
                }
            if (stmt.execute(checktaxistop))
                rs = stmt.getResultSet();
            if(!rs.isBeforeFirst())
                System.out.println("No records found.");
            else
                while(rs.next()){
                    System.out.print("Taxi stop: " + rs.getInt(1));
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
