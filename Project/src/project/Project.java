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
        System.out.println("Welcome! Who are you?");
        System.out.println("1. An administrotor");
        System.out.println("2. A passenger");
        System.out.println("3. A driver");
        System.out.println("4. A manager");
        System.out.println("5. None of the above");
        System.out.println("Please enter [1-4]");
        Scanner sc = new Scanner(System.in);
        int role = sc.nextInt();
        switch(role)
        {
            case 1:
                Admin(conn);
                break;
            case 2:
                Passenger(conn);
                break;
            case 3:
                Driver(conn);
                break;
            case 4:
                Manager(conn);
                break;
            default:
                System.out.println("Bye.");
        }
    }

    public static void Admin(Connection conn)
    {
        Administrator a = new Administrator(conn);
        a.menu();

    }
    public static void Driver(Connection conn)
    {
        Driver a = new Driver(conn);
        a.menu();
    }
    public static void Manager(Connection conn)
    {
        Manager a = new Manager(conn);
        a.menu();
    }
    public static void Passenger(Connection conn)
    {
        Passenger a = new Passenger(conn);
        a.menu();
    }


    public static void main(String[] args) throws SQLException {

        //Administrator admin = new Administrator(conn);
        //admin.teststatement();
        Menu();



    }
    
}
