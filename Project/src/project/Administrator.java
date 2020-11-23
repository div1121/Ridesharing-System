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
