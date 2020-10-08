package db;

import helpers.IndexableMap;

public class item_db {
	
	public static  IndexableMap<String,String> db;
	public static String ychar ="//";
	public static void gen_items() 
	{
		//name,amount,paid,cost,min_price,max_price
		db = new IndexableMap<String,String>();
		db.put("fuel","fuel,3,0,10,10,300");
		db.put("supplies","supplies,3,0,10,10,300");
		db.put("cheese","cheese,5,0,2,1,30");

	}//end gen_star_systems
}
