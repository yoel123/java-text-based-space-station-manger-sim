package db;

import helpers.IndexableMap;

public class event_db {
	public static  IndexableMap<String,String> db;
	
	public static void gen_events() 
	{
		//action,amount,desc,immediate
		db = new IndexableMap<String,String>();
		db.put("tst","test//2//test");
		db.put("metor storm","money_reduce//2//a metor storm hit the station"
				+ "//immediate");
		db.put("currupt_genral","smuglers_sell//0//currupt genral");
		db.put("shady_dude","smuglers_sell//0//shady dude");
		db.put("price_rise","price_rise//2//item price rise//immediate");
		db.put("crazy_inventor","crazy_inventor//2//test");
		
	}//end gen_star_systems
}
