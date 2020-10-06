package screens;

import java.lang.reflect.Array;
import java.util.ArrayList;

import game_objects.game_manger;
import helpers.C.GUIConsoleIO;
import helpers.gconsole_menu;
import helpers.gyinput;
import game_objects.item;

/*station information screen
 * a collection of all info about the station for the player to view
 * */

public class station_info_screen extends gconsole_menu {

	gyinput ui; 
	public station_info_screen(GUIConsoleIO cio2) {
		super(cio2);
		menu_items = new String[]{
				"back"
				};
		ui = new gyinput(cio2);
	}//end constructor
	
	

	@Override
	public void menu_show() {
		//show player stats
		cio.println(game_manger.p.show_stats(),pc);
		display_player_data();
		super.menu_show();

	}



	@Override
	public void menu_user_input() 
	{
		super.menu_user_input();

		if(next.equals("1")) 
		{
			gm.change_menu("game_main");
		}
		
		
		
		
	}//end menu_user_input
	
	public void display_player_data() {
		//broke displays into function to easily change to separate
		//options should we decide to change layout later
		cio.println("--------------------");
		cio.println("Current turn:"+game_manger.p.turn);
		display_station_upgrades();
		display_station_inventory();
		display_station_long_events();
	}//end display_player_data
	
	public void display_station_upgrades() {
		cio.println("\n--Station Upgrades--");
		for(String upgrade:game_manger.p.upgrades) {
			cio.println(upgrade);
		};
	}// end display_station_upgrades
	
	public void display_station_long_events() {
		cio.println("\n--Long Term Events--");
		for(Object items:game_manger.p.counters_list) {
			int event_turns_remaining = game_manger.p.counters.get(items.toString()+"_max")-game_manger.p.counters.get(items.toString()+"_counter");
			cio.println(items.toString()+"::: turns remaining: "+event_turns_remaining);
		};	
	}//end display_station_long_events
	
	public void display_station_inventory() {
		cio.println("\n--Station Inventory--");
		for(item items:game_manger.p.items) {
			cio.println(items.name.toString()+"::: amount: "+items.amount);
		};
	}//display_station_inventory

	
}
