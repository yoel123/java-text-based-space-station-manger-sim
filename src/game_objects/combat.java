package game_objects;

import java.util.ArrayList;

import db.ship_db;
import db.weapons_db;

public class combat {

	public  ArrayList<ship> player_fleet,enemy_fleet;
	public boolean resolved,win,lose;
	public void gen_demo_fleet() 
	{
		enemy_fleet = new ArrayList<ship>();
		//3 heavy shutels
		enemy_fleet.add( new ship( ship_db.db.getValueAt(0) ) );
		enemy_fleet.add( new ship( ship_db.db.getValueAt(0) ) );
		enemy_fleet.add( new ship( ship_db.db.getValueAt(0) ) );
		//give them a cannon each
		for(ship s:enemy_fleet) 
		{
			s.add_weapon(new weapon(weapons_db.db.getValueAt(0)));
		}
	}//end gen_demo_fleet
	
}
