package game_objects;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map.Entry;

import db.ship_db;
import db.weapons_db;
import helpers.IndexableMap;
import helpers.S;

public class market implements Serializable{
	public ArrayList<item> items;
	public ArrayList<ship> ships;
	public ArrayList<weapon> weapons;
	public IndexableMap<String,Integer> item_supply,item_demannd;

	public market(IndexableMap<String,String> db)
	{
		items = new ArrayList<item>();
		
		item_supply = new IndexableMap<String,Integer>();
		item_demannd = new IndexableMap<String,Integer>();
		ships = new ArrayList<ship>();
		weapons = new ArrayList<weapon>();
		
		pupulate_items(db);
	}//end constructor
	
	
	
	public void pupulate_items(IndexableMap<String,String> db) 
	{
		for (Entry<String, String> me : db.entrySet()) 
		{
			
			items.add(new item(me.getValue()));
		}
		
		pupulate_ships_and_weapons();
		
	}//end pupulate_items
	
	public void pupulate_ships_and_weapons() 
	{
		IndexableMap<String,String> sipsdb = ship_db.db;
		IndexableMap<String,String> weapondb = weapons_db.db;
		weapon w;
		ship s;
		//add weapons
		for (Entry<String, String> me : weapondb.entrySet()) 
		{
			w = new weapon(me.getValue());
	
			//only common weapons in market
			if(w.rarity.equals("common")) 
			{
				weapons.add(w);
			}
			
		}//end add weapons
		
		for (Entry<String, String> me : sipsdb.entrySet()) 
		{
			s= new ship(me.getValue());
			//only civilian or military no spacial or faction only etc
			if(s.ship_type.equals("civilian") || s.ship_type.equals("military")) 
			{
				ships.add(s);
			}
		}
		
	}//end pupulate_ships_and_weapons
	
	//change prices and amount of all items in market
	public void supply_and_demend_gen()
	{
		int rand1,rand2,it_supply=0,it_demmand=0;
		for(item it :items) 
		{
			
			//check if item supply and demand exist
			if(item_supply.containsKey(it.name)){it_supply = item_supply.get(it.name);}
			else{it_supply = 0;}
			if(item_demannd.containsKey(it.name)){it_demmand = item_demannd.get(it.name);}
			else{it_demmand = 0;}
			
			//demmand (random+modifiers)
			rand1=(int)(Math.random()*6+1)+it_demmand;
			//supply (random+modifiers)
			rand2=(int)(Math.random()*5+1)+game_manger.p.good_advertise+it_supply;
			
			if(rand1>10) {rand1=10;}//cant be bigger then 100%
			
			int demand = rand1 * it.min_price;
			if(demand >it.max_price) {demand = it.max_price;}
			if(demand<0) {demand=0;}
	
			it.cost = demand;
			it.amount = rand2*10;
		}//end for items
		
		supply_and_demend_gen_weapons();
	}//end supply_and_demend_gen
	
	public void supply_and_demend_gen_weapons()
	{
		for(weapon w:weapons) 
		{
			//demmand (random+modifiers)
			int rand1=(int)(Math.random()*10);
			
			w.amount = rand1;
		}
	}//end supply_and_demend_gen_weapons
	
	//return a string of all items as a numbered list
	public String display_all()
	{
		String txt="";
		int i =1;
		for(item it :items) 
		{
			txt += i+") "+it.toString()+"\n";
			i++;
		}
		return txt;
	}
	
	//get an item price by id
	public int get_item_price(int id) 
	{
		item it = items.get(id);
		return it.cost;
	}//end get_item_price
	
	//update the prices on player owned items
	public void update_player_item_price(player p) 
	{
		int i=0;
		item temp;
		
		for(item it:p.items) 
		{
			//get market item by current item name
			temp = get_item(it.name);
			//set players item to the same cost as market item
			it.cost = temp.cost;
			i++;
		}
		
		weapon tw;
		for(weapon wp:p.sm.weapons) 
		{
			tw= get_weapon(wp.name);
			wp.cost = tw.cost;
		}
		
		
		
	}//update_player_item_price
	
	//get item by name
	public item get_item(String name) 
	{
		for(item it:items)
		{
			if(it.name.equals(name)) {return it;}
		}
		return null;
	}//end get_item
	
	
	//get weapon by name
	public weapon get_weapon(String name) 
	{
		for(weapon it:weapons)
		{
			if(it.name.equals(name)) {return it;}
		}
		return null;
	}//end get_weapon
	
	
	
}
