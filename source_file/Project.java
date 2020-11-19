/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.sql.*;
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
           // System.out.println("Yes");
        // Do something with the Connection
        } catch (SQLException ex) {
            // handle any errors
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        } catch (ClassNotFoundException ex) {
            System.out.println("Java MYSQL DB drive not found");
        }
    return conn; 
    }
    /**
     * @param args the command line arguments
     * @throws java.sql.SQLException
     */
    public static void main(String[] args) throws SQLException {
        Connection conn = connect();
        Administrator admin = new Administrator(conn);
        admin.teststatement();
    }
    
}
