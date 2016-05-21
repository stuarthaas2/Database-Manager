package com.uas.core;

import java.sql.SQLException;

import com.uas.db.Database;
import com.uas.db.QueryBuilder;
import com.uas.utils.Console;

public class Main{

	public static void main(String[] args) throws SQLException {
		new Main();
	}

	Database db;
	
	public Main(){
		try{
			init();
		}
		catch(SQLException e){
			Console.println(e.getMessage());
		}
	}
	
	private void init() throws SQLException{
		
		db = Database.getInstance();
		db.connect("jdbc:mysql://localhost:8889/uas", "root", "root");
		
		
		String sql = new QueryBuilder().select().column(
				"PERSONNEL.EDIPI", 
				"PERSONNEL.TYPE",
				"PERSONNEL.RANK",
				"PERSONNEL.LAST", 
				"PERSONNEL.FIRST", 
				"PERSONNEL.MI",
				"PERSONNEL.PMOS",
				"PERSONNEL.BLOOD",
				"PERSONNEL.PLT",
				"PERSONNEL.SQD",
				"PERSONNEL.BUS",
				"PERSONNEL.STATUS",
				"RIFLE.TYPE",
				"RIFLE.SN",
				"RIFLEOPTIC.TYPE",
				"RIFLEOPTIC.SN",
				"PVS14.SN",
				"PEQ16.SN",
				"M203.SN",
				"M9.SN").from("PERSONNEL").
				leftJoin("RIFLE","PERSONNEL.EDIPI", "RIFLE.EDIPI").
				leftJoin("RIFLEOPTIC","PERSONNEL.EDIPI", "RIFLEOPTIC.EDIPI").
				leftJoin("PVS14","PERSONNEL.EDIPI", "PVS14.EDIPI").
				leftJoin("PEQ16","PERSONNEL.EDIPI", "PEQ16.EDIPI").
				leftJoin("M203","PERSONNEL.EDIPI", "M203.EDIPI").
				leftJoin("M9","PERSONNEL.EDIPI", "M9.EDIPI").groupBy("PERSONNEL.EDIPI").orderBy("LAST ASC").toString();
		
		System.out.println(sql);
	}
}
	

	

	

