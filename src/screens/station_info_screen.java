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
		dameged_upgrades();
		display_station_inventory();
		display_station_long_events();
		damege_list();
	}//end display_player_data
	
	public void display_station_upgrades() {
		cio.println("\n--Station Upgrades--");
		for(String upgrade:game_manger.p.upgrades) {
			cio.println(upgrade);
		};
	}// end display_station_upgrades
	
	public void display_station_long_events() {
		cio.println("\n--Long Term Events--");
		for(String item:game_manger.p.counters_list) {
			//check if counter exists (if not continue to next iteration of the loop)
			if(game_manger.p.counters.getKeyIndex(item+"_counter")==-1) {continue;}
			int event_turns_remaining = game_manger.p.counters.get(item+"_max")-game_manger.p.counters.get(item+"_counter");
			cio.println(item+"::: turns remaining: "+event_turns_remaining);
		};	
	}//end display_station_long_events
	
	public void display_station_inventory() {
		cio.println("\n--Station Inventory--");
		for(item items:game_manger.p.items) {
			cio.println(items.name.toString()+"::: amount: "+items.amount);
		};
	}//display_station_inventory
	
	public void dameged_upgrades() 
	{
		cio.println("\n--Station disabled upgrades--");
		for(String upgrade:game_manger.p.disabled_upgrades) {
			cio.println(upgrade);
		};
	}
	
	public void damege_list() 
	{
		cio.println("\n--Station damages--");
		for(String upgrade:game_manger.p.damage_list) {
			cio.println(upgrade);
			cio.println("damage amount: "+game_manger.p.rapairs_prograss.get(upgrade));
		};
	}
	

	
}
