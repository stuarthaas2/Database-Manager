package com.uas.utils;

import java.util.Scanner;

public class Console {
	
	private static String format = null;

	public static Scanner input = new Scanner(System.in);
	
	public static void println(Object s){
		System.out.println(s);
	}
	
	public static void printf(String format, Object...args){
		System.out.printf(format, args);
	}
	
	public static void print(Object s){
		System.out.print(s);
	}
	
	public static int validateInt(Object s){
		int n = 0;
		try{n = Integer.parseInt((String) s);}
		catch(Exception e){
			Console.print("Input is not an integer. Enter again: ");
			n = validateInt(input.nextLine());
		}
		return n;
	}

	public static Object validateRequired(Object s){
		Object val = s;
		if(val instanceof String){
			while(((String) val).isEmpty()){
				Console.print("Value is required. Enter again: ");
				val = input.nextLine();
			}
		}
		if(val instanceof Integer){
			while(((Integer) val) == null){
				Console.print("Value is required. Enter again: ");
				val = validateInt(input.nextLine());
			}
		}
		return val;
	}

	public static Object validateNull(Object s1, Object s2){
		Object val = null;
		if(s2 instanceof String){
			if(((String) s1).isEmpty())
				val = s2;
		else
			val = s1;
		}
		if(s2 instanceof Integer){
			if(((Integer) s1) == 0 || (Integer) s1 == null)
				val = s2;
		else
			val = s1;
		}
		return val;
	}
	
	public static int printMenu(String name, String...names){
		printMenuHeader(name);
		printMenuItems(names);
		print("\nPlease enter your choice >> ");
		return selectMenuItem();
	}
	
	public static void printMenuHeader(String name){
		String sep = "";
		for(int i = 0; i < name.length(); i ++){
			sep = sep.concat("=");
		}
		Console.println("\n" + sep);
		Console.println(name);
		Console.println(sep);
	}
	
	public static void printMenuItems(String...names){
		for(int i = 0; i < names.length; i ++){
			Console.printf("%-20s %-20s\n", names[i], "[" + i + "]");
		}
	}
	
	public static void printTableHeader(String format, Object...names){
		setFormat(format + "\n");
		Console.printf("\n" + getFormat(), names);
		for(int i = 0; i < names.length; i ++){
			names[i] = names[i].toString().replaceAll(".", "=");
		}
		Console.printf(getFormat(), names);
	}
	
	public static int selectMenuItem(){
		return Console.validateInt(Console.input.nextLine());
	}
	
	public static void setFormat(String format){
		Console.format = format;
	}
	
	public static String getFormat(){
		return Console.format;
	}
}
