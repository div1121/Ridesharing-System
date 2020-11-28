/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package project;

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
        int flag=0,tid;
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
            else
            {
                Integer seat,dy,pid;
                String model,sl,dest;

                String check_seats = "SELECT passenger_id,start_location,destination,passengers,model,driving_years FROM request WHERE id=?";
                stmt = conn.prepareStatement(check_seats);
                stmt.setInt(1,rid);
                rs = stmt.executeQuery();
                pid = rs.getInt(1);
                sl = rs.getString(2);
                dest = rs.getString(3);
                seat = rs.getInt(4);
                model = rs.getString(5);
                dy = rs.getInt(6);
                rs.close();

                    String check_vseats = "SELECT V.id FROM vehicle V WHERE EXISTS (SELECT * FROM vehicle V natural join driver D WHERE D.id=? AND V.seats>=?)";
                    stmt = conn.prepareStatement(check_vseats);
                    stmt.setInt(1,did);
                    stmt.setInt(2,seat);
                    rs = stmt.executeQuery();
                    if(rs.wasNull())
                    {
                        System.out.println("Not enough seat. Please try another request.");
                        System.out.println("");
                        menu();
                    }

                    if(!model.isEmpty())
                    {
                        String check_model = "SELECT V.id FROM vehicle V WHERE EXISTS (SELECT * FROM vehicle V natural join driver D WHERE D.id=? AND V.model LIKE ?)";
                        stmt = conn.prepareStatement(check_model);
                        stmt.setInt(1,did);
                        stmt.setString(2,"%"+model+"%");
                        rs = stmt.executeQuery();
                        if(rs.wasNull())
                        {
                            System.out.println("Not fulfill model requirement. Please try another request.");
                            System.out.println("");
                            menu();
                        }
                    }
                    else
                    {
                        if(dy!=null)
                        {
                            String check_dy = "SELECT V.id FROM vehicle V WHERE EXISTS (SELECT * FROM vehicle V natural join driver D WHERE D.id=? AND D.driving_year>=?)";
                            stmt = conn.prepareStatement(check_dy);
                            stmt.setInt(1,did);
                            stmt.setInt(2,dy);
                            rs = stmt.executeQuery();
                            if(rs.wasNull())
                            {
                                System.out.println("Not fulfill driving year requirement. Please try another request.");
                                System.out.println("");
                                menu();
                            }
                            else
                            {
                                String update_req = "UPDATE request SET taken=(byte)1 WHERE id=?";
                                stmt = conn.prepareStatement(update_req);
                                stmt.setInt(1,rid);
                                stmt.execute();

                                String insert_trip = "INSERT INTO trip(driver_id,passenger_id,start_location,destination,start_time,end_time,fee) VALUES (?,?,?,?,?,?,?)";
                                stmt = conn.prepareStatement(insert_trip);
                                stmt.setInt(1,did);
                                stmt.setInt(2,pid);
                                stmt.setString(3,sl);
                                stmt.setString(4,dest);
                                SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                                java.util.Date current = new java.util.Date();
                                java.util.Date tmp1 = sdFormat.parse(current);
                                stmt.setTimestamp(5,new java.sql.Timestamp(tmp1.getTime()));
                                stmt.setNull(6,Types.DATETIME);
                                stmt.setNull(7,Types.INTEGER);
                                stmt.execute();

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
