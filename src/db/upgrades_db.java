package db;

import java.util.ArrayList;

import helpers.IndexableMap;

public class upgrades_db {
	public static  IndexableMap<String,String> db;
	
	public static void gen_upgrades() 
	{
		//upgrade name,price,desc
		db = new IndexableMap<String,String>();
		db.put("tst","test,2,test");
		
		
	}//end gen_star_systems
}
