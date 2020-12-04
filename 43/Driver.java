/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.sql.*;
import java.util.*;
import java.text.*;
import java.lang.*;
/**
 *
 * @author user
 */
public class Driver {
    public Connection conn;
    public Scanner sc;
    public Driver(Connection conn, Scanner sc){
        this.conn = conn;
        this.sc = sc;
    }
    void msg()
    {
        System.out.println("Login as Driver");
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

    private void SearchReq()
    {

        ResultSet rs1 = null;
        ResultSet rs2 = null;
        ResultSet rs3 = null;

        String search_model="";
       
        int search_driving_years=0;
        
        String input_did=null;
        
        String input_max_dist =null;
    
        int coor_x=-1;
        int coor_y=-1;
        
        boolean set_coor_x=false;
        boolean set_coor_y=false;
 
        

        System.out.println("1. Search requests");

        //Scanner sc = new Scanner(System.in);
        
        boolean did_not_found=false;
        int did=0;//for input to SQL
        

         
        do{
           did_not_found=false;
            System.out.println("Please enter your ID.");
            input_did = sc.nextLine().trim();
            if (!isNumber( input_did))
                System.out.println("[ERROR] The input is not a number");
            else {
            did = Integer.parseInt(input_did);
            //check if there is that id in the Database
                String check_id =" SELECT *"
                + " FROM driver D"
                + " WHERE D.id= ? ";
                try {
                    PreparedStatement stmt_check_id = conn.prepareStatement(check_id);
                    stmt_check_id.setInt(1,did);
                    stmt_check_id.execute();
                    rs1=stmt_check_id.getResultSet();
                    if(!rs1.isBeforeFirst())
                    {   
                        did_not_found=true;
                        System.out.println("[Error] No records found.");
                    }
                }
                catch (SQLException ex) {
                    System.out.println("SQLException: " + ex.getMessage());
                    System.out.println("SQLState: " + ex.getSQLState());
                    System.out.println("VendorError: " + ex.getErrorCode());
                }
            }
        }while((!isNumber(input_did))||did_not_found);

        
       boolean coor_invalid=false;
        
       
        do{
            coor_invalid=false;
            set_coor_x=false;
            set_coor_y=false;
            coor_x=-1;
            coor_y=-1;
            try{
            System.out.println("Please enter the coordinates of your location.");
            String coor[]= sc.nextLine().split(" ");
       
            for(int i =0 ;i < coor.length;i++){
            
              if(i==0){
                coor_x = Integer.parseInt(coor[i]);
                set_coor_x=true;
              }
                if(i==1){
                coor_y= Integer.parseInt(coor[i]);
                 set_coor_y=true;
                }
            }
            if(set_coor_x==false||set_coor_y==false||coor.length>2){  //     if(coor_x<0||coor_y<0||coor.length>2)
                 System.out.println("[ERROR] Invalid input");
                  //System.out.println("coor_x is "+ coor_x+" and coor_y is "+ coor_y);
                  //System.out.println("set_coor_x is "+ set_coor_x+" and set_coor_x is "+ set_coor_y+" and coor.length is "+ coor.length);
                coor_invalid=true;

            }
            }
        
            catch(NumberFormatException e){
                System.out.println("[ERROR] Please enter integer");
                coor_invalid=true;
                
            }

        }
        while(coor_invalid);
        
         // System.out.println("coor_x is "+ coor_x+" and coor_y is "+ coor_y);
         
       do{
        
            System.out.println("Please enter the maximum distance from you to your passenger");
            input_max_dist = sc.nextLine().trim();
             if (!isNumber(input_max_dist))
                System.out.println("[ERROR] Invalid input");
     
        }
        while(!isNumber(input_max_dist));
        int max_dist= Integer.parseInt(input_max_dist);
       
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
                + " WHERE R.passenger_id=P.id  AND R.taken=0 "
                + " AND ( (R.model IS NULL) OR (? LIKE  CONCAT('%',R.model,'%'))  ) " // + " AND ((? LIKE ('%'+R.model+'%')) OR (R.model IS NULL) ) " 
                + " AND ((R.driving_years IS NULL) OR (R.driving_years <= ?)) "
                + " AND T.name= R.start_location"
                + " AND (ABS(? - T.location_x) + ABS(? - T.location_y)) <= ? ";


        //1:search_model
        //2:search_driving_years
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

           
            stmt3.setString(1,search_model);
            stmt3.setInt(2,search_driving_years);
            stmt3.setInt(3,coor_x);
            stmt3.setInt(4,coor_y);
            stmt3.setInt(5,max_dist);

            if (stmt3.execute()) {
                rs3= stmt3.getResultSet();
            }
           
            if(!rs3.isBeforeFirst())
                System.out.println("No records found.");
            else{
                 System.out.println("request ID, passenger name, num of passengers, start location, destination");
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

                }
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }

        //System.out.println("");
        //menu();



    }
       private void TakeReq()
    {
        int flag=0,tid=0;
        Integer seat = 0, dy = 0, pid = 0;
        String model = "", sl = "", dest = "",pn="";
        Timestamp et;
        boolean taken=true;
        PreparedStatement stmt;
        ResultSet rs;
        System.out.println("2. Take a request");
        int did,rid;
        while(true)
        {
            try {
                System.out.println("Please enter your ID.");
                //Scanner input = new Scanner(System.in);
                did = Integer.parseInt(sc.nextLine().trim());
                String check_did_exist = "SELECT * FROM driver WHERE id=?";
                stmt = conn.prepareStatement(check_did_exist);
                stmt.setInt(1, did);
                rs = stmt.executeQuery();

                if(!rs.next())
                {
                    System.out.println("[ERROR] Driver_id " + did + " don't exist.");
                    System.out.println("");
                }
                else
                break;
            }
            catch(InputMismatchException | NumberFormatException ex ) {
                System.out.println("[ERROR] Invalid input");
            }
            catch (SQLException ex) {
                System.out.println("SQLException: " + ex.getMessage());
                System.out.println("SQLState: " + ex.getSQLState());
                System.out.println("VendorError: " + ex.getErrorCode());
            }
        }
        try {


        while(true)
        {
            try {
                System.out.println("Please enter the request ID.");
                //Scanner input = new Scanner(System.in);
                rid = Integer.parseInt(sc.nextLine().trim());
                String check_rid_exist = "SELECT * FROM request WHERE id=?";
                stmt = conn.prepareStatement(check_rid_exist);
                stmt.setInt(1, rid);
                rs = stmt.executeQuery();

                if(!rs.next())
                {
                    System.out.println("[ERROR] Request_id " + rid + " don't exist.");
                    System.out.println("");
                }
                else
                break;
            }
            catch(InputMismatchException | NumberFormatException ex ) {
                System.out.println("[ERROR] Invalid input");
            }
            catch (SQLException ex) {
                System.out.println("SQLException: " + ex.getMessage());
                System.out.println("SQLState: " + ex.getSQLState());
                System.out.println("VendorError: " + ex.getErrorCode());
            }
        }

        String check_unfinished = "SELECT id,end_time FROM trip WHERE driver_id=?";

            stmt = conn.prepareStatement(check_unfinished);
            stmt.setInt(1, did);
            rs = stmt.executeQuery();

            while (rs.next())
            {
                tid = rs.getInt(1);
                et = rs.getTimestamp(2);
                if (rs.wasNull())
                {
                    flag = 1;
                    break;
                }
            }
            if (flag == 1)
            {
                System.out.println("[ERROR] Driver_id " + did + " have an unfinished trip (trip_id=" + tid + "), please finished it first to take another request.");
                System.out.println("");
                flag = 0;
                //menu();
            }
            else
            {
                System.out.println("Driver does not have unfinished trip.");
                String check_request = "SELECT passenger_id,start_location,destination,passengers,model,taken,driving_years FROM request WHERE id=?";
                stmt = conn.prepareStatement(check_request);
                stmt.setInt(1, rid);
                rs = stmt.executeQuery();

                while (rs.next())
                {
                    pid = rs.getInt(1);
                    sl = rs.getString(2);
                    dest = rs.getString(3);
                    seat = rs.getInt(4);
                    model = rs.getString(5);
                    taken = rs.getBoolean(6);
                    dy = rs.getInt(7);
                }

                System.out.println("Request info : " + pid + " " + sl + " " + dest + " " + seat + " " + model + " " + dy + " taken: " + taken);
                if(taken)
                {
                    System.out.println("[ERROR] This is a closed request.");
                    System.out.println("");
                    //menu();
                }
                else
                {
                    System.out.println("This is an open request.");
                    if (sl.isEmpty())
                    {
                        System.out.println("[ERROR] Don't have this request ID.");
                        System.out.println("");
                        //menu();
                    }
                    else
                    {
                        //System.out.println("Request info : " + pid + " " + sl + " " + dest + " " + seat + " " + model + " " + dy);
                        String check_vseats = "SELECT V.id FROM vehicle V CROSS JOIN driver D WHERE D.vehicle_id=V.id AND D.id=? AND V.seats>=?";
                        stmt = conn.prepareStatement(check_vseats);
                        stmt.setInt(1, did);
                        stmt.setInt(2, seat);
                        rs = stmt.executeQuery();
                        if (!rs.next())
                        {
                            System.out.println("[ERROR] Not enough seat. Please try another request.");
                            System.out.println("");
                            //menu();
                        }
                        else
                        {
                            System.out.println("Have enough seat.");
                            if (model!=null)
                            {
                                System.out.println("Checking model: "+model);
                                String check_model = "SELECT V.id FROM vehicle V CROSS JOIN driver D WHERE D.vehicle_id=V.id AND D.id=? AND V.model LIKE ?";
                                stmt = conn.prepareStatement(check_model);
                                stmt.setInt(1, did);
                                stmt.setString(2, "%" + model + "%");
                                rs = stmt.executeQuery();
                                if (!rs.next())
                                {
                                    System.out.println("[ERROR] Not fulfill model requirement. Please try another request.");
                                    System.out.println("");
                                    //menu();
                                }
                            }

                                System.out.println("Model match.");
                                if (dy > 0)
                                {
                                    System.out.println("Check dy");
                                    String check_dy = "SELECT V.id FROM vehicle V CROSS JOIN driver D WHERE D.vehicle_id=V.id AND D.id=? AND D.driving_years>=?";
                                    stmt = conn.prepareStatement(check_dy);
                                    stmt.setInt(1, did);
                                    stmt.setInt(2, dy);
                                    rs = stmt.executeQuery();
                                    if (!rs.next()) {
                                        System.out.println("[ERROR] Not fulfill driving year requirement. Please try another request.");
                                        System.out.println("");
                                        //menu();
                                    }
                                }
                                else
                                {
                                    System.out.println("Enough driving year.");
                                    String update_req = "UPDATE request SET taken=true WHERE id=?";
                                    stmt = conn.prepareStatement(update_req);
                                    stmt.setInt(1, rid);
                                    stmt.execute();
                                    System.out.println("Updated taken of the request.");

                                    String insert_trip = "INSERT INTO trip(driver_id,passenger_id,start_location,destination,start_time,end_time,fee) VALUES (?,?,?,?,?,?,?)";
                                    stmt = conn.prepareStatement(insert_trip);
                                    stmt.setInt(1, did);
                                    stmt.setInt(2, pid);
                                    stmt.setString(3, sl);
                                    stmt.setString(4, dest);
                                    SimpleDateFormat sf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                                    java.util.Date current = new java.util.Date();
                                    String tmp = sf.format(current);
                                    java.util.Date tmp1 = sf.parse(tmp);
                                    stmt.setTimestamp(5, new java.sql.Timestamp(tmp1.getTime()));
                                    stmt.setNull(6, java.sql.Types.TIMESTAMP);
                                    stmt.setNull(7, Types.INTEGER);
                                    stmt.execute();
                                    System.out.println("Inserted unfinished trip.");


                                    String get_pn = "SELECT name FROM passenger WHERE id=?";
                                    stmt = conn.prepareStatement(get_pn);
                                    stmt.setInt(1, pid);
                                    rs = stmt.executeQuery();
                                    if(rs.next())
                                        pn = rs.getString(1);
                                    if(pn.isEmpty())
                                    {
                                        System.out.println("[ERROR] Passenger with this id doesn't exist.");
                                        System.out.println("");
                                        //menu();
                                    }
                                    else
                                    {
                                        String get_tid = "SELECT id FROM trip WHERE driver_id=? AND end_time IS NULL";
                                        stmt = conn.prepareStatement(get_tid);
                                        stmt.setInt(1, did);
                                        rs = stmt.executeQuery();
                                        if (!rs.next())
                                        {
                                            System.out.println("[ERROR] No trip found.");
                                            System.out.println("");
                                            //menu();
                                        }
                                        else
                                            tid = rs.getInt(1);

                                        System.out.println("Trip ID, Passenger name, Start");
                                        System.out.println(tid + ", " + pn + ", " + tmp);
                                    }


                                    System.out.println("");
                                    //menu();
                                }

                        }
                    }
                }
            }
            }
        catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
        catch(NullPointerException e)
        {
            e.printStackTrace();
        }
    }
    private void FinishTrip()
    {
        int tid=0,pid=0,fee;
        java.util.Date start=new java.util.Date();
        String tmp="";
        String pn="";
        //Scanner sc = new Scanner(System.in);
        SimpleDateFormat sf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        System.out.println("3. Finish a trip");
        PreparedStatement stmt;
        ResultSet rs;
        int did,rid;
        while(true)
        {
            try {
                System.out.println("Please enter your ID.");
                //Scanner input = new Scanner(System.in);
                did = Integer.parseInt(sc.nextLine().trim());
                String check_did_exist = "SELECT * FROM driver WHERE id=?";
                stmt = conn.prepareStatement(check_did_exist);
                stmt.setInt(1, did);
                rs = stmt.executeQuery();

                if(!rs.next())
                {
                    System.out.println("[ERROR] Driver_id " + did + " don't exist.");
                    System.out.println("");
                }
                else
                    break;
            }
            catch(InputMismatchException | NumberFormatException ex ) {
                System.out.println("[ERROR] Invalid input");
            }
            catch (SQLException ex) {
                System.out.println("SQLException: " + ex.getMessage());
                System.out.println("SQLState: " + ex.getSQLState());
                System.out.println("VendorError: " + ex.getErrorCode());
            }
        }
        try
        {

        String check_ut = "SELECT T.id,passenger_id,start_time,name FROM trip T cross join passenger P WHERE T.passenger_id=P.id and end_time IS NULL and driver_id=?";

            stmt = conn.prepareStatement(check_ut);
            stmt.setInt(1, did);
            rs = stmt.executeQuery();
            if(!rs.next())
            {
                System.out.println("[ERROR] You don't have an unfinish trip.");
                System.out.println("");
                // menu();
            }
            else
            {
                   tid = rs.getInt(1);
                   pid = rs.getInt(2);
                   Timestamp tt = rs.getTimestamp(3);
                   start = tt;
                   tmp=sf.format(start);
                   pn= rs.getString(4);
                   System.out.println("Trip ID, Passenger ID, Start");
                   System.out.println(tid+", "+pid+", "+tmp);
                   System.out.println("Do you wish to finish the trip? [y/n]");
                   String r = sc.nextLine();
                   char op = r.charAt(0);
                   if(op=='y')
                   {
                       System.out.println("Trip ID, Passenger name, Start, End, Fee");
                       java.util.Date current = new java.util.Date();
                       tmp = sf.format(current);
                       java.util.Date tmp1 = sf.parse(tmp);
                       Calendar calendar = GregorianCalendar.getInstance(); // creates a new calendar instance
                       calendar.setTime(start);   // assigns calendar to given date
                       int start_hr = calendar.get(Calendar.HOUR_OF_DAY); // gets hour in 24h format
                       int start_min = calendar.get(Calendar.MINUTE); // gets hour in 24h format
                       int start_s = calendar.get(Calendar.SECOND); // gets hour in 24h format
                       calendar.setTime(current);   // assigns calendar to given date
                       int end_hr = calendar.get(Calendar.HOUR_OF_DAY); // gets hour in 24h format
                       int end_min = calendar.get(Calendar.MINUTE); // gets hour in 24h format
                       int end_s = calendar.get(Calendar.SECOND); // gets hour in 24h format
                       fee = java.lang.Math.abs((end_hr-start_hr)*60 + (int)java.lang.Math.abs(end_min-start_min));

                       String ft = "UPDATE trip SET end_time=?, fee=? WHERE id=?";
                       stmt = conn.prepareStatement(ft);
                       stmt.setTimestamp(1, new java.sql.Timestamp(tmp1.getTime()));
                       stmt.setInt(2,fee);
                       stmt.setInt(3,tid);
                       stmt.execute();


                       String check_final = "SELECT T.id,start_time,end_time,fee,name FROM trip T cross join passenger P WHERE T.id=? and T.passenger_id=P.id";
                       stmt = conn.prepareStatement(check_final);
                       stmt.setInt(1, tid);
                       rs = stmt.executeQuery();

                       if(!rs.next())
                       {
                           System.out.println("Error in finding trip record.");
                           System.out.println("");
                           //menu();
                       }
                       else
                       {
                           tid = rs.getInt(1);
                           tt = rs.getTimestamp(2);
                           start = tt;
                           tmp=sf.format(start);
                           tt = rs.getTimestamp(3);
                           java.util.Date end = tt;
                           String tmp2=sf.format(end);
                           fee = rs.getInt(4);
                           pn= rs.getString(5);
                           System.out.println(tid+", "+pn+", "+tmp+", "+tmp2+", "+fee);
                       }

                   }

                System.out.println("");
                //menu();

            }

        }
        catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
        catch(ParseException e)
        {
            e.printStackTrace();
        }
        catch(NullPointerException e)
        {
            e.printStackTrace();
        }
    }

    void menu()
    {
        while(true){
            boolean b = false;
            System.out.println("Driver, what would you like to do?");
            System.out.println("1. Search requests");
            System.out.println("2. Take a request");
            System.out.println("3. Finish a trip");
            System.out.println("4. Go back");
            System.out.println("Please enter [1-4]");
            //Scanner sc = new Scanner(System.in);
            if (!sc.hasNextLine())
                System.exit(0);
            try{
                int op = Integer.parseInt(sc.nextLine());
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
                        //GoBack();
                        b = true;
                        break;
                default:
                    System.out.println("[Error] Invalid input.");     
                    //menu();
                    break;
                }
                if (b)
                    break;
            }
            catch(InputMismatchException e) {
                System.out.println("[Error] Invalid input.");     
                //menu();
            }
        }
    }

}
