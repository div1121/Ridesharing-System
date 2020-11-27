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
    }
    private void TakeReq()
    {
        System.out.println("2. Take a request");
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
