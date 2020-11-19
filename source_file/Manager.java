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
public class Manager {
    public Connection conn;
    public Manager(Connection conn){
        this.conn = conn;
    }
    void msg()
    {
        System.out.println("Login as Manager");
    }
}
