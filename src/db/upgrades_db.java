package db;

import java.util.ArrayList;

import helpers.IndexableMap;

public class upgrades_db {
	public static  IndexableMap<String,String> db;
	public static String ychar ="//";
	
	public static void gen_upgrades() 
	{
		//upgrade name,price,desc
		db = new IndexableMap<String,String>();
		db.put("test2","test2//200//test2");
		db.put("test3","test3//200//test3");
		db.put("tst","tst//200//tst");
		db.put("another test","another test//300//a test");
		
		
	}//end gen_star_systems
}
