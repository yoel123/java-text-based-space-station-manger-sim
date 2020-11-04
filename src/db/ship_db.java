package db;

import java.util.ArrayList;

import helpers.IndexableMap;

public class ship_db {
	public static  IndexableMap<String,String> db;
	public static ArrayList<String> ship_names;
	public static String ychar ="//";
	public static void gen_ships() 
	{
		//name,class,type,size,cost,speed,turn speed,hull,armor,shield,energy
		//large weapons hardpoints,medium hardpoints,small hard points,
		//fighter decks
		//fuel,cargo,maintenance cost,fuel consumption
		//crew max,crew min
		//,special ability
		db = new IndexableMap<String,String>();
		gen_ships_names();//pupolate ship names db
		
		db.put("heavy shuttle", "heavy shuttle//Frigate//civilian//3//17500//6//7//"
				+ "2000//2//300//3//0//0//2//0//25//60//2//1//10//1//afterburn");
		
	}
	
	public static void gen_ships_names() 
	{
		 ship_names = new ArrayList<String>();
		 
		 ship_names.add("krakan");
		 ship_names.add("walrus");
		 ship_names.add("Atalanta");
		 ship_names.add("Alaric");
		 ship_names.add("gahena");
		 ship_names.add("m.r bob");
		 ship_names.add("Lossie");
		 ship_names.add("ogre");
		 ship_names.add("Alligator");
		 ship_names.add("skyrunner");
		 ship_names.add("Petrolla");	
		 
	}
	
	
}
