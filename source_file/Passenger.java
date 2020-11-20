/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.sql.*;
import java.util.*;
/**
 *
 * @author user
 */
public class Passenger {
    public Connection conn;
    public Passenger(Connection conn){
        this.conn = conn;
    }
    void msg()
    {
        System.out.println("Login as Passenger");
    }

    private void FindTrip()
    {
        System.out.println("1. Find trips");
    }
    private void  CheckTripRecord()
    {
        System.out.println("2. Check trip records");
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
                FindTrip();
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
