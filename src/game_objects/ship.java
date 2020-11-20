package game_objects;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

import db.ship_db;
import helpers.yvars;

public class ship implements Serializable{

	public String ship_name,name,ship_type,ship_class;
	
	public int ship_size,speed,turn_speed,armor_rating,armor_rating_max
	,hull_hp,hull_hp_max,
	shild_rating,shild_rating_max,fighter_bay_size,crew,max_crew,
	crew_moral,basic_maintennce_cost,fuel,max_fuel,fuel_consumption,pilots,
	maintenence_crew_min,energy,cost,cargo,cargo_max;
	
	//weapon ports
	public int small_weapons,medium_weapons,large_weapons;
	
	public ArrayList<weapon> ship_weapons;
	
	public ArrayList<String> upgrades,dameged_systems,debufs,figter_ships;
	
	public static String[] ship_systems= {"coms","dock","generator"
			,"life_support","stracture"};
	
	public character captin;
	
	public String spacial_abilety,manuver,global_command;
	
	public ArrayList<ship> targets;//targets in combat
	
	//ship subsystems and damages
	
	public boolean destroyed;
	
	public ship(String data) {
		ship_weapons =new ArrayList<weapon>(); 
		
		String[] datar = data.split("//");
		name = datar[0];
		ship_class = datar[1];
		ship_type = datar[2];
		ship_size = yvars.ystoint(datar[3]);
		cost = yvars.ystoint(datar[4]);
		speed = yvars.ystoint(datar[5]);
		turn_speed = yvars.ystoint(datar[6]);
		hull_hp = yvars.ystoint(datar[7]);
		hull_hp_max = yvars.ystoint(datar[7]);
		armor_rating = yvars.ystoint(datar[8]);
		armor_rating_max = yvars.ystoint(datar[8]);
		shild_rating = yvars.ystoint(datar[9]);
		shild_rating_max = yvars.ystoint(datar[9]);
		energy = yvars.ystoint(datar[10]);
		large_weapons = yvars.ystoint(datar[11]);
		medium_weapons = yvars.ystoint(datar[12]);
		small_weapons = yvars.ystoint(datar[13]);
		fighter_bay_size = yvars.ystoint(datar[14]);
		fuel = yvars.ystoint(datar[15]);
		max_fuel = yvars.ystoint(datar[15]);
		cargo = yvars.ystoint(datar[16]);
		cargo_max = yvars.ystoint(datar[16]);
		basic_maintennce_cost = yvars.ystoint(datar[17]);
		fuel_consumption = yvars.ystoint(datar[18]);
		crew = yvars.ystoint(datar[19]);
		max_crew = yvars.ystoint(datar[19]);
		spacial_abilety = datar[20];
		
		ship_name = rand_ship_name();
		
		
	}//end constructor

	public void shot(ship e) 
	{
		//check ship type
		//check if it has shields
		//check orders (if disable etc)
		//select a weapon fitting above parameters
		//ship take damage from weapon
		
		//no weapons dont shot
		if(ship_weapons.isEmpty()) {return;}
		
		//mock combat
		//shot first weapon
		e.take_damege(ship_weapons.get(0), e);
		
	}//end shot
	
	public void take_damege(weapon w,ship e)
	{
		//if its a missile try point defence
		//evade if posible (rotation speed vs ship speed vs enemy aim)
		//if has shields damage shields
		//armor check
		//hit hull
		//damage systems
		//check weapon spacial efects
		//check if disabled or destroyed 
		
		//mock combat
		//damage hull for now
		hull_hp--;
		
		//ship hull integrety byond critical, destroy it
		if(hull_hp<=0) {destroyed=true;}
		
	}//end take_damege
	
	public void lunch_fighters() {}//end lunch_fighters
	
	public String rand_ship_name() 
	{
		Random r = game_manger.p.rndgen;
		int i = r.nextInt(ship_db.ship_names.size());
		ship_name = ship_db.ship_names.get(i);
		
		return ship_name;
	}//end rand_ship_name
	
	public String toString() 
	{
		String ret ="";
		
		ret+=" ship name : "+ship_name+
			 "\n ship class : "+ship_class+
			 "\n ship type : "+ship_type+
			 "\n ship size : "+ship_size+
			 "\n ship speed : "+speed+
			 "\n turn speed : "+turn_speed+
			 "\n hull : "+hull_hp+"/"+hull_hp_max+
			 "\n armor : "+armor_rating+"/"+armor_rating_max+
			 "\n shild : "+shild_rating+"/"+shild_rating_max+
			 "\n energy : "+energy+
			 "\n large weapons ports : "+large_weapons+
			 "\n medium weapons ports : "+medium_weapons+
			 "\n small weapons ports : "+small_weapons+
			 "\n fighter bay size : "+fighter_bay_size+
			 "\n fuel : "+fuel+"/"+max_fuel+
			 "\n maintennce_cost : "+basic_maintennce_cost+
			 "";
		
		return ret;
	}//end toString
	
	public boolean add_weapon(weapon w) {
		if(!can_add_weapon(w)) {return false;}
		ship_weapons.add(w);
		return true;
	}//end add_weapon
	
	public void remove_weapon(weapon w) {
		ship_weapons.remove(w);
	}//end add_weapon
	
	
	//checks if weapon of a type can be add
	public boolean can_add_weapon(weapon w) 
	{
		
		//weapon count
		int[] wc = weapon_sizes_count();
		
		//weapon to test size
		String weapon_size = w.weapon_size;
		
		if(w.weapon_size.equals("small") && wc[0]<small_weapons) {return true;}
		if(w.weapon_size.equals("medium") && wc[1]<medium_weapons) {return true;}
		if(w.weapon_size.equals("large") && wc[2]<large_weapons) {return true;}

		return false;
	}//end can_add_weapon
	
	//returns an int array with 3 positions [small weapon count,medium  etc]
	public int[] weapon_sizes_count() 
	{
		//weapon size counter
		int small_weaponsc=0,medium_weaponsc=0,large_weaponsc=0;
		//count weapon sizes already installed
		for(weapon wt :ship_weapons) 
		{
			if(wt.weapon_size.equals("small")) {small_weaponsc++;}
			if(wt.weapon_size.equals("medium")) {medium_weaponsc++;}
			if(wt.weapon_size.equals("large")) {large_weaponsc++;}
			
			
		}
		return new int[] {small_weaponsc,medium_weaponsc,large_weaponsc};
	}//end weapon_sizes_count
	
	
}
