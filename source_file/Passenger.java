/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.sql.*;
import java.util.*;
import java.text.*;

/**
 *
 * @author user
 */
public class Passenger {
    public Connection conn;
    public Passenger(Connection conn){
        this.conn = conn;
    }
    public boolean inPassenger(int pid){
        String checkid = "SELECT * from passenger where id=?";
        ResultSet rs = null;
        PreparedStatement stmt;
        try {
            stmt = conn.prepareStatement(checkid);
            stmt.setInt(1,pid);
            if (stmt.execute()) {
                rs = stmt.getResultSet();
            }
            if(!rs.isBeforeFirst())
                return false;   
        }catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
        return true;
    }
    
    public boolean inTaxistop(String dest){
        String checkid = "SELECT * from taxi_stop where name=?";
        ResultSet rs = null;
        PreparedStatement stmt;
        try {
            stmt = conn.prepareStatement(checkid);
            stmt.setString(1,dest);
            if (stmt.execute()) {
                rs = stmt.getResultSet();
            }
            if(!rs.isBeforeFirst())
                return false;   
        }catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
        return true;
    }
    
    public boolean inRequest(int pid){
        String checkrequest = "SELECT * from request where passenger_id=? and taken=0";
         ResultSet rs = null;
        PreparedStatement stmt;
        try {
            stmt = conn.prepareStatement(checkrequest);
            stmt.setInt(1,pid);
            if (stmt.execute()) {
                rs = stmt.getResultSet();
            }
            if(rs.isBeforeFirst())
                return true;
        }catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
        return false;
    }
    
    public boolean findpossiblemodel(int numofpass,String model){
        ResultSet rs = null;
        PreparedStatement stmt;
        int count = 0;
        String matchrequest = "SELECT COUNT(*) "
                            + "From driver d, vehicle v "
                            + "WHERE d.vehicle_id=v.id AND v.seats >= ? AND (? IS NULL OR v.model LIKE ?) AND d.id NOT IN "
                            + "(SELECT driver_id FROM trip WHERE end_time IS NULL)";
        try{
            stmt = conn.prepareStatement(matchrequest);
            stmt.setInt(1,numofpass);
            if (!model.isEmpty()){
                 stmt.setString(2,model);
                 String temp = "%" + model + "%";
                 stmt.setString(3, temp);
            }
            else{
                stmt.setString(2, null);
                stmt.setString(3, null);
            }
            if (stmt.execute()) {
                 rs = stmt.getResultSet();
            }
            while(rs.next()){
                count = rs.getInt(1);
            }
            if (count > 0)
                return true;
        }catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
        return false;
    }
    
    public boolean findpossibleyear(int numofpass,String model,String driveyear){
        ResultSet rs = null;
        PreparedStatement stmt;
        int count = 0;
        int mindriveyear = 0;
        if (!driveyear.isEmpty())
            mindriveyear = Integer.parseInt(driveyear);
        String matchrequest = "SELECT COUNT(*) "
                            + "From driver d, vehicle v "
                            + "WHERE d.vehicle_id=v.id AND v.seats >= ? AND (? IS NULL OR v.model LIKE ?) AND (? is NULL OR d.driving_years >= ?) AND d.id NOT IN "
                            + "(SELECT driver_id FROM trip WHERE end_time IS NULL)";
        try{
             stmt = conn.prepareStatement(matchrequest);
                     stmt.setInt(1,numofpass);
                     if (!model.isEmpty()){
                         stmt.setString(2,model);
                         String temp = "%" + model + "%";
                         stmt.setString(3, temp);
                     }
                     else{
                         stmt.setString(2, null);
                         stmt.setString(3, null);
                     }
                     if (!driveyear.isEmpty()){
                         stmt.setInt(4,mindriveyear);
                         stmt.setInt(5,mindriveyear);
                     }
                     else{
                         stmt.setNull(4, java.sql.Types.INTEGER);
                         stmt.setNull(5, java.sql.Types.INTEGER);
                     }
            if (stmt.execute()) {
                 rs = stmt.getResultSet();
            }
            while(rs.next()){
                count = rs.getInt(1);
            }
            if (count > 0)
                return true;
        }catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
        return false;
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
    
    public static boolean isDate(String str) {
        if (str == null) {
            return false;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        try {
            dateFormat.parse(str.trim());
        } catch (ParseException pe) {
            return false;
        }
        return true;
    }
   
    
    void msg()
    {
        System.out.println("Login as Passenger");
    }

    private void ReqRide()
    {
            ResultSet rs = null;
            System.out.println("1. Request a ride");
            Scanner sc = new Scanner(System.in);
            String id,num,startloc,endloc,model,driveyear;
            int pid = -1,numofpass=1000,mindriveyear=0,count=0;;
            do{
                System.out.println("Please enter your ID.");
                id = sc.nextLine().trim();
                if (!isNumber(id))
                    System.out.println("[ERROR] The input is not a number");
                else {
                    pid = Integer.parseInt(id);
                    if (!inPassenger(pid))
                        System.out.println("[ERROR] Your ID is not found.");
                    if (inRequest(pid))
                        System.out.println("[ERROR] You have an open request with you");
                }
            }while(!isNumber(id) || !inPassenger(pid) || inRequest(pid));
            pid = Integer.parseInt(id);
            
            do{
                System.out.println("Please enter the number of passengers.");
                num = sc.nextLine().trim();
                if (!isNumber(num))
                    System.out.println("[ERROR] The input is not a number");
                else{
                    numofpass = Integer.parseInt(num);
                    if (numofpass>7)
                        System.out.println("[ERROR] Invalid number of passengers");
                }
            }while(!isNumber(num) || numofpass>7);
            
            
            do{
                System.out.println("Please enter the start location.");
                startloc = sc.nextLine().trim();
                if(!inTaxistop(startloc))
                     System.out.println("[ERROR] The start location is not found");
            }while(!inTaxistop(startloc));
            
            do{
                System.out.println("Please enter the destination.");
                endloc = sc.nextLine().trim();
                if(!inTaxistop(endloc))
                     System.out.println("[ERROR] The destination is not found");
                if (startloc.compareTo(endloc)==0)
                     System.out.println("[ERROR] Destinatino and start location should be different");
            }while(!inTaxistop(endloc) || startloc.compareTo(endloc)==0);
            
            do{
                System.out.println("Please enter the model. (Please enter to skip)");
                model = sc.nextLine().trim();
                if (!findpossiblemodel(numofpass,model))
                    System.out.println("[ERROR] Cannot find any possible matching, Please adjust the criteria");
            }while(!findpossiblemodel(numofpass,model));
            
            do{
                System.out.println("Please enter the minimum driving year of the driver. (Please enter to skip)");
                driveyear = sc.nextLine().trim();
                if (!driveyear.isEmpty() && !isNumber(driveyear))
                    System.out.println("[ERROR] The input is not valid");
                else{
                    if (!findpossibleyear(numofpass,model,driveyear))
                        System.out.println("[ERROR] Cannot find any possible matching, Please adjust the criteria");
                }
            }while((!driveyear.isEmpty() && !isNumber(driveyear)) || !findpossibleyear(numofpass,model,driveyear));
            
            PreparedStatement stmt;
            try {
                String matchrequest = "SELECT COUNT(*) "
                            + "From driver d, vehicle v "
                            + "WHERE d.vehicle_id=v.id AND v.seats >= ? AND (? IS NULL OR v.model LIKE ?) AND (? is NULL OR d.driving_years >= ?)";
                 stmt = conn.prepareStatement(matchrequest);
                 stmt.setInt(1,numofpass);
                 if (!model.isEmpty()){
                     stmt.setString(2,model);
                     String temp = "%" + model + "%";
                     stmt.setString(3, temp);
                }
                else{
                     stmt.setString(2, null);
                     stmt.setString(3, null);
                 }
                 if (!driveyear.isEmpty()){
                     stmt.setInt(4,mindriveyear);
                     stmt.setInt(5,mindriveyear);
                 }
                 else{
                     stmt.setNull(4, java.sql.Types.INTEGER);
                     stmt.setNull(5, java.sql.Types.INTEGER);
                 }
                 if (stmt.execute()) {
                     rs = stmt.getResultSet();
                 }
                 while(rs.next()){
                     count = rs.getInt(1);
                 }
                 String insertrequest = "INSERT INTO request (passenger_id,start_location,destination,model,passengers,taken,driving_years) "
                        + "VALUES (?,?,?,?,?,?,?)";
                 stmt = conn.prepareStatement(insertrequest);
                 stmt.setInt(1,pid);
                 stmt.setString(2,startloc);
                 stmt.setString(3,endloc);
                 if (!model.isEmpty())
                     stmt.setString(4,model);
                 else stmt.setString(4, null);
                 stmt.setInt(5,numofpass);
                 stmt.setByte(6,(byte)0);
                 if (!driveyear.isEmpty())
                     stmt.setInt(7,mindriveyear);
                 else
                     stmt.setNull(7, java.sql.Types.INTEGER);
                 stmt.execute();
                 //System.out.println("Insert record successfully");
                 System.out.println("Your request is placed. " + count + " drivers are able to take the request.");
            } catch (SQLException ex) {
                System.out.println("SQLException: " + ex.getMessage());
                System.out.println("SQLState: " + ex.getSQLState());
                System.out.println("VendorError: " + ex.getErrorCode());
            }
            
            System.out.println("");
            menu();
    }
    private void  CheckTripRecord()
    {
        ResultSet rs = null;
        System.out.println("2. Check trip records");
        String id,startdate,enddate,dest;
        Scanner sc = new Scanner(System.in);
        int pid = -1;
        do{
            System.out.println("Please enter your ID.");
            id = sc.nextLine().trim();
            if (!isNumber(id))
                System.out.println("[ERROR] The input is not a number");
            else{
                pid = Integer.parseInt(id);
                if (!inPassenger(pid))
                    System.out.println("[ERROR] Your ID is not found.");
            }
        }while(!isNumber(id) || !inPassenger(pid));
        pid = Integer.parseInt(id);
        do{
            System.out.println("Please enter the start date.");
            startdate = sc.nextLine().trim();
            if (!isDate(startdate))
                System.out.println("[ERROR] The input is not a correct date format");
        }while(!isDate(startdate));
        do {
            System.out.println("Please enter the end date.");
            enddate = sc.nextLine().trim();
            if (!isDate(enddate))
                System.out.println("[ERROR] The input is not a correct date format");
        }while(!isDate(enddate));
        do {
            System.out.println("Please enter the destination.");
            dest = sc.nextLine().trim();
            if (!inTaxistop(dest))
                System.out.println("[ERROR] The destination is not found");
        }while(!inTaxistop(dest));
        //System.out.println(pid + "," +startdate + "," + enddate + "," + dest);
        String triprecord = "SELECT T.id, D.name, V.id, V.model, T.start_time, T.end_time, T.fee, T.start_location, T.destination  "
                + "FROM trip T, driver D, vehicle V "
                + "WHERE D.id=T.driver_id AND D.vehicle_id=V.id AND T.end_time IS NOT NULL AND T.passenger_id=? AND T.start_time >= ? AND T.end_time < (? + INTERVAL 1 DAY) AND T.destination=? "
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
            if(!rs.isBeforeFirst())
                System.out.println("No records found.");
            else{
                System.out.println("Trip_id, Driver Name, Vehicle ID, Vehicle Model, Start, End, Fee, Start Location, Destination");
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
