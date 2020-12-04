/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package project; 

import java.sql.*;
import java.util.Scanner;

/**
 *
 * @author user
 */
public class Project {

    public static Connection connect(){
        // function to set up connection
        String dbAddress = "jdbc:mysql://projgw.cse.cuhk.edu.hk:2633/group43";
        String dbUsername = "Group43";
        String dbPassword = "3170group43";
        Connection conn = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(dbAddress,dbUsername,dbPassword);
         //   System.out.println("Yes");
        // Do something with the Connection
        } catch (SQLException ex) {
            // handle any errors
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        } catch (ClassNotFoundException ex) {
            System.out.println("Java MYSQL DB drive not found");
            System.exit(0);
        }
    return conn; 
    }
    /**
     * @param args the command line arguments
     * @throws java.sql.SQLException
     */

    public static void Menu()
    {
        Connection conn = connect();
        Scanner sc = new Scanner(System.in);
        while(true){
            boolean b = false;
            System.out.println("Welcome! Who are you?");
            System.out.println("1. An administrotor");
            System.out.println("2. A passenger");
            System.out.println("3. A driver");
            System.out.println("4. A manager");
            System.out.println("5. None of the above");
            System.out.println("Please enter [1-4]");
            //Scanner sc = new Scanner(System.in);
            if (!sc.hasNextLine())
                System.exit(0);
            try{
            String temp = sc.nextLine();
            //System.out.println(temp);
            int role = Integer.parseInt(temp);
            switch(role)
            {
                case 1:
                    Admin(conn,sc);
                    break;
                case 2:
                    Passenger(conn,sc);
                    break;
                case 3:
                    Driver(conn,sc);
                    break;
                case 4:
                    Manager(conn,sc);
                    break;
                default:
                    System.out.println("Bye.");
                    b = true;
            }
            if (b)
                break;
            }catch(Exception e){
                System.out.println("Exception");
            }
        }
    }

    public static void Admin(Connection conn, Scanner sc)
    {
        Administrator a = new Administrator(conn, sc);
        a.menu();

    }
    public static void Driver(Connection conn, Scanner sc)
    {
        Driver a = new Driver(conn, sc);
        a.menu();
    }
    public static void Manager(Connection conn, Scanner sc)
    {
        Manager a = new Manager(conn, sc);
        a.menu();
    }
    public static void Passenger(Connection conn, Scanner sc)
    {
        Passenger a = new Passenger(conn, sc);
        a.menu();
    }


    public static void main(String[] args) throws SQLException {

        //Administrator admin = new Administrator(conn);
        //admin.teststatement();
        Menu();

        

    }
    
}
