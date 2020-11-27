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
public class Manager {
    public Connection conn;
    public Manager(Connection conn){
        this.conn = conn;
    }
    void msg()
    {
        System.out.println("Login as Manager");
    }

    private void FindTrip()
    {
        System.out.println("1. Find trips");
    }
    private void GoBack()
    {
        Project a = new Project();
        System.out.println("");
        a.Menu();
    }

    void menu()
    {
        System.out.println("Manager, what would you like to do?");
        System.out.println("1. Find trips");
        System.out.println("2. Go back");
        System.out.println("Please enter [1-2]");
        Scanner sc = new Scanner(System.in);
        int op = sc.nextInt();
        switch (op)
        {
            case 1:
                FindTrip();
                break;
            case 2:
                GoBack();
                break;
            default:
                System.out.println("Invalid input, please try again.");
                menu();
                break;
        }
    }
}
