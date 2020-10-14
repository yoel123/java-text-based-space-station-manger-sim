package game_objects;

import java.util.ArrayList;

public class ship {

	public String name,ship_type,ship_size;
	
	public int speed,armor_rating,armor_rating_max,hull_hp,hull_hp_max,
	shild_rating,shild_rating_max,fighter_bay_size,crew,max_crew,
	crew_moral,basic_maintennce_cost,fuel,max_fuel,pilots,max_pilots,
	maintenence_crew_min;
	
	//weapon ports
	public int small_weapons,medium_weapons,large_weapons;
	
	public ArrayList<weapon> ship_weapons;
	
	public ArrayList<String> upgrades,dameged_systems,debufs,figter_ships;
	
	public character captin;
	
	public String spacial_abilety,manuver;
	
	public ArrayList<ship> targets;//targets in combat
	
	
	public ship(String data) {

	}//end constructor

	public void shot(ship e) {}//end shot
	
	public void take_damege(weapon w,ship e) {}//end take_damege
	
	public void lunch_fighters() {}//end lunch_fighters
	
	
	
}
