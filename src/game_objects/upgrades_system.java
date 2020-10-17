package game_objects;

import java.util.Map.Entry;

import db.upgrades_db;
import helpers.IndexableMap;
import helpers.gyinput;
import helpers.yvars;
import helpers.C.GUIConsoleIO;

public class upgrades_system {
	
	public static void upgrades_list(GUIConsoleIO cio) 
	{
		cio.println("=======station upgrades======");
		for(String u:game_manger.p.upgrades) 
		{
			cio.println(u);
		}
		cio.println("=============");
	}//end upgrades list
	
	public static String buy_upgrades(GUIConsoleIO cio,gyinput ui) 
	{
		String messge="";
		//get upgrades db
		IndexableMap<String, String> udb = upgrades_db.db;
		String[] upgrade;//single upgrade
		int i=1;//iteration counter
		for (Entry<String, String> me : udb.entrySet()) 
		{
			
			upgrade = me.getValue().split(upgrades_db.ychar);
			//if player has upgrade dont show it
			if(game_manger.p.upgrades.indexOf(upgrade[0])==-1) 
			{
				cio.println(i+") "+upgrade[0]+" price "+upgrade[1]);
			}
			i++;
		}//end for
		
		//get the upgrade the player wants to buy
		int choice = ui.get_int("select upgrade to buy (0 to return)");
		
		//check for potential errors (validate input)
		if(choice ==0 || choice<0 || udb.getKeyAt(choice-1) ==null)
		{
			messge ="upgrade dosnt exist";
			return messge;
		}
		
		//get the upgrade player chose
		String[] upgrade_to_buy = udb.getValueAt(choice-1).split(upgrades_db.ychar);
		
		//check if upgrade requirments meet
		if(upgrade_to_buy.length>3 && !upgrade_requirement(upgrade_to_buy[3]))
		{messge ="failed to meet upgrade requirments:\n"
				+ upgrade_to_buy[3];return messge;}
		
		int cost = yvars.ystoint(upgrade_to_buy[1]);
		//check if player can buy (has credits and dosnt already have upgrade
		if(game_manger.p.credits>=cost && game_manger.p.upgrades.indexOf(upgrade_to_buy[0]) ==-1) {
			game_manger.p.upgrades.add(upgrade_to_buy[0]);
			messge ="bought upgrade";
		}else {
			messge ="failed to buy upgrade";
		}
		return messge;
	}//end buy_upgrades
	
	public static boolean upgrade_requirement(String recs) 
	{
		//get upgrades to check array
		String[] recsr = recs.split(",");
		//get player
		player p = game_manger.p;
		
		//loop upgrades to check
		for(String u:recsr) 
		{
			//if upgrade dosnt exist in player upgrades, failed to meet requirments
			if(p.upgrades.indexOf(u)==-1) {return false;}
		}
		
		//else return true
		return true;
	}//end upgrade_requirement
	
	public static boolean have_upgrade(String name )
	{
		player p = game_manger.p;
		//chack if damage effects
		
		//if have upgrade return true
		if(p.upgrades.indexOf(name)==1) {return true;}
		return false;
	}//end have_upgrade
	

}
