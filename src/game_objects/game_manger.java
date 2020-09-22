package game_objects;

import java.util.ArrayList;

import db.event_db;
import db.item_db;
import db.upgrades_db;
import helpers.S;

public class game_manger {
	
	public static market m;
	public static player p;
	public static int turn;
	
	
	public static void start_game() 
	{
		//init item db
		item_db.gen_items();
		//init events
		event_db.gen_events();
		//init upgrades
		upgrades_db.gen_upgrades();
		//init market
		m = new market(item_db.db);
		m.supply_and_demend_gen();
		
		//init player
		p = new player();
		p.credits = 1000;

	}//end start_game
	
	
	public static void pass_turn() 
	{
		turn++;
		//add events
		//shufel murket
		m.supply_and_demend_gen();
		//update player
		p.update_taxes();
		//genrate events
		event_manger.random_events();
		
		//tax return
		if(p.taxes_owed<0) 
		{
			game_manger.p.events.add("tax return,"+(p.taxes_owed*-1)+", ");
			game_manger.p.credits += p.taxes_owed*-1;
		}
		
	}//end pass_turn
	
}
