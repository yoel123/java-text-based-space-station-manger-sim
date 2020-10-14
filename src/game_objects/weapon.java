package game_objects;

import db.weapons_db;
import helpers.yvars;

public class weapon {

	//name,rarity,dmg,fire_rate,range,ammo,size,bullet_speed
	//bullet_hp,weapon_type,dmg_type,shot_type,cost,min_cost,max_cost
	//spacial
	
	public int dmg,fire_rate,range,ammo,size,bullet_speed
	,bullet_hp,cost,min_cost,max_cost;
	
	public String name,rarity,weapon_type,dmg_type,shot_type
	,spacial;
	
	public weapon(String data) 
	{
		String[] datar = data.split(weapons_db.ychar);
		name = datar[0];
		rarity = datar[1];
		dmg = yvars.ystoint(datar[2]);
		fire_rate = yvars.ystoint(datar[3]);
		range = yvars.ystoint(datar[4]);
		ammo = yvars.ystoint(datar[5]);
		size = yvars.ystoint(datar[6]);
		bullet_speed = yvars.ystoint(datar[7]);
		bullet_hp = yvars.ystoint(datar[8]);
		weapon_type = datar[9];
		dmg_type = datar[10];
		shot_type = datar[11];
		cost = yvars.ystoint(datar[12]);
		min_cost = yvars.ystoint(datar[13]);
		max_cost = yvars.ystoint(datar[14]);
		//spacial
		
	}//end constructor

}
