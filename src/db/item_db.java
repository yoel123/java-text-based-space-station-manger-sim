package db;

import helpers.IndexableMap;

public class item_db {
	
	public static  IndexableMap<String,String> db;
	public static String ychar ="//";
	public static void gen_items() 
	{
		//name,amount,paid,cost,min_price,max_price,type
		db = new IndexableMap<String,String>();
		db.put("fuel","fuel,3,0,10,10,300,important");
		db.put("supplies","supplies,3,0,10,10,300,important");
		db.put("cheese","cheese,5,0,2,1,30,food");
		db.put("unprocessed ore","unprocessed ore,5,0,2,10,800,material");
		db.put("steel","steel,5,0,50,50,1500,material");
		db.put("chips","chips,5,0,50,50,2500,tech");
		db.put("medicine","medicine,5,0,50,30,2000,tech");
		db.put("common goods","common goods,5,0,5,5,1000,goods");
		
	}//end gen_star_systems
}
