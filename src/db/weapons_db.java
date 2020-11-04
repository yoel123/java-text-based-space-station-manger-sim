package db;

import helpers.IndexableMap;

public class weapons_db {
	public static  IndexableMap<String,String> db;
	public static String ychar ="//";
	public static void gen_weapons() 
	{
		//name,rarity,dmg,fire_rate,range,ammo,bullet_size,bullet_speed
		//bullet_hp,weapon_type,dmg_type,shot_type,cost,min_cost,max_cost
		//spacial,weapon_size
		db = new IndexableMap<String,String>();
		
		db.put("cannon", "cannon//common//10//2//8//80//3//5//30//balistic"
				+ "//armor_pircing//canon_bolt//300//300//1500//none//small");
		
	}
}
