/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

//package project;

import java.sql.*;
import java.util.*;
import java.text.*;
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
        int flag=0,tid=0;
        Integer seat = 0, dy = 0, pid = 0;
        String model = "", sl = "", dest = "",pn="";
        System.out.println("2. Take a request");
        System.out.println("Please enter your ID.");
        Scanner sc = new Scanner(System.in);
        int did = sc.nextInt();
        System.out.println("Please enter the request ID.");
        int rid = sc.nextInt();
        String check_unfinished = "SELECT id,end_time FROM trip WHERE driver_id=?";
        try
        {
            PreparedStatement stmt = conn.prepareStatement(check_unfinished);
            stmt.setInt(1,did);
            ResultSet rs = stmt.executeQuery();
            while ( rs.next() )
            {
                tid=rs.getInt(1);
                if(rs.wasNull())
                {
                    flag=1;
                    break;
                }
            }
            if(flag==1)
            {
                System.out.println("Driver_id " + did +" have an unfinished trip (trip_id=" + tid + "), please finished it first to take another request.");
                System.out.println("");
                flag=0;
                menu();
            }
            else {


                String check_seats = "SELECT passenger_id,start_location,destination,passengers,model,driving_years FROM request WHERE id=?";
                stmt = conn.prepareStatement(check_seats);
                stmt.setInt(1, rid);
                rs = stmt.executeQuery();

                if (rs.next()) {
                    while (rs.next()) {
                        pid = rs.getInt(1);
                        sl = rs.getString(2);
                        dest = rs.getString(3);
                        seat = rs.getInt(4);
                        model = rs.getString(5);
                        dy = rs.getInt(6);
                    }

                    rs.close();

                    String check_vseats = "SELECT V.id FROM vehicle V WHERE EXISTS (SELECT * FROM vehicle V natural join driver D WHERE D.id=? AND V.seats>=?)";
                    stmt = conn.prepareStatement(check_vseats);
                    stmt.setInt(1, did);
                    stmt.setInt(2, seat);
                    rs = stmt.executeQuery();
                    if (rs.wasNull()) {
                        System.out.println("Not enough seat. Please try another request.");
                        System.out.println("");
                        menu();
                    }

                    if (!model.isEmpty()) {
                        String check_model = "SELECT V.id FROM vehicle V WHERE EXISTS (SELECT * FROM vehicle V natural join driver D WHERE D.id=? AND V.model LIKE ?)";
                        stmt = conn.prepareStatement(check_model);
                        stmt.setInt(1, did);
                        stmt.setString(2, "%" + model + "%");
                        rs = stmt.executeQuery();
                        if (rs.wasNull()) {
                            System.out.println("Not fulfill model requirement. Please try another request.");
                            System.out.println("");
                            menu();
                        }
                    } else {
                        if (dy != null) {
                            String check_dy = "SELECT V.id FROM vehicle V WHERE EXISTS (SELECT * FROM vehicle V natural join driver D WHERE D.id=? AND D.driving_years>=?)";
                            stmt = conn.prepareStatement(check_dy);
                            stmt.setInt(1, did);
                            stmt.setInt(2, dy);
                            rs = stmt.executeQuery();
                            if (rs.wasNull()) {
                                System.out.println("Not fulfill driving year requirement. Please try another request.");
                                System.out.println("");
                                menu();
                            } else {
                                String update_req = "UPDATE request SET taken=true WHERE id=?";
                                stmt = conn.prepareStatement(update_req);
                                stmt.setInt(1, rid);
                                stmt.execute();

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

                                String get_pn = "SELECT name FROM passenger WHERE id=?";
                                stmt = conn.prepareStatement(get_pn);
                                stmt.setInt(1, pid);
                                rs = stmt.executeQuery();
                                if(!rs.next())
                                {
                                    System.out.println("Empty passenger list.");
                                    System.out.println("");
                                    menu();
                                }
                                else
                                    pn = rs.getString(1);

                                String get_tid = "SELECT id FROM trip WHERE driver_id=? AND end_time IS NULL";
                                stmt = conn.prepareStatement(get_tid);
                                stmt.setInt(1, did);
                                rs = stmt.executeQuery();
                                if(!rs.next())
                                {
                                    System.out.println("Empty trip list.");
                                    System.out.println("");
                                    menu();
                                }
                                else
                                    tid = rs.getInt(1);

                                System.out.println("Trip ID, Passenger name, Start");
                                System.out.println(tid + ","+ pn +","+tmp);
                                System.out.println("");
                                menu();


                            }
                        }
                    }
                }
                else
                {
                    System.out.println("Empty request list.");
                    System.out.println("");
                    menu();
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

        System.out.println("");
        menu();

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
