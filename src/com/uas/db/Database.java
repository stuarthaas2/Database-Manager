package com.uas.db;

import java.sql.Connection;
import java.sql.DriverManager;

import com.uas.utils.Console;

public class Database{

	private static Database instance = null;
	
	public static Database getInstance(){
		if(instance == null){
			instance = new Database();
		}
		return instance;
	}
	
	private Connection connection;
	
	protected Database(){}
	
	public void connect(String host, String user, String pass){
	    try {
	    	Console.println("Opening database connection");
	        connection = DriverManager.getConnection(host, user, pass);
	
	    } catch (Exception e){
	    	Console.println(e.getMessage());
	        System.exit(0);
	    }  
	    Console.println("Connected to database");
	}
	
	public Connection getConnection(){
		return connection;
	}
}
