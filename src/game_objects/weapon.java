package game_objects;

import java.io.Serializable;

import db.weapons_db;
import helpers.yvars;

public class weapon implements Serializable{

	//name,rarity,dmg,fire_rate,range,ammo,size,bullet_speed
	//bullet_hp,weapon_type,dmg_type,shot_type,cost,min_cost,max_cost
	//spacial
	
	public int dmg,fire_rate,range,ammo,bullet_size,bullet_speed
	,bullet_hp,cost,min_cost,max_cost,amount,you_paid;
	
	public String name,rarity,weapon_type,dmg_type,shot_type
	,spacial,weapon_size;
	
	public weapon(weapon w) 
	{
		name = w.name;
		rarity = w.rarity;
		dmg = w.dmg;
		fire_rate = w.fire_rate;
		range = w.range;
		ammo = w.ammo;
		bullet_size = w.bullet_size;
		bullet_speed = w.bullet_speed;
		bullet_hp = w.bullet_hp;
		weapon_type = w.weapon_type;
		dmg_type = w.dmg_type;
		shot_type = w.shot_type;
		cost = w.cost;
		min_cost = w.min_cost;
		max_cost = w.max_cost;
		spacial= w.spacial;
		weapon_size = w.weapon_size;
	}//end weapon
	
	public weapon(String data) 
	{
		String[] datar = data.split(weapons_db.ychar);
		name = datar[0];
		rarity = datar[1];
		dmg = yvars.ystoint(datar[2]);
		fire_rate = yvars.ystoint(datar[3]);
		range = yvars.ystoint(datar[4]);
		ammo = yvars.ystoint(datar[5]);
		bullet_size = yvars.ystoint(datar[6]);
		bullet_speed = yvars.ystoint(datar[7]);
		bullet_hp = yvars.ystoint(datar[8]);
		weapon_type = datar[9];
		dmg_type = datar[10];
		shot_type = datar[11];
		cost = yvars.ystoint(datar[12]);
		min_cost = yvars.ystoint(datar[13]);
		max_cost = yvars.ystoint(datar[14]);
		spacial = datar[15];
		weapon_size= datar[16];
		
	}//end constructor
	
	
	public String toString() 
	{
		int amount2 = amount;
		if(amount2<0) {amount2=0;}
		return name+" price:"+cost+" amount: "+
		amount+" price range:"+min_cost+" - "+max_cost;	

	}//end to string
	
	public String toStringp()
	{
		return name+" sell price:"+cost+" amount: "+
			amount+" you paid:"+you_paid;
	}//endtoString

}
