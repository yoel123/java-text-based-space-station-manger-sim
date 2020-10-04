package screens;

import java.lang.reflect.Array;
import java.util.ArrayList;

import game_objects.game_manger;
import helpers.C.GUIConsoleIO;
import helpers.gconsole_menu;
import helpers.gyinput;
import game_objects.item;

/*station market terminal
 * where the player can buy and sell goods
 * */

public class station_info_screen extends gconsole_menu {

	gyinput ui; 
	public station_info_screen(GUIConsoleIO cio2) {
		super(cio2);
		menu_items = new String[]{
				"back"
				};
		//For array lists of player info add here to loop through
		//could reduce large code section in menu show
		String[] player_info = new String[] {
			"items"
			,"upgrades"
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
		cio.println("--------------------");
		cio.println("Current turn:"+game_manger.p.turn);
		cio.println("\n--Station Upgrades--");
		for(String upgrade:game_manger.p.upgrades) {
			cio.println(upgrade);
		};
		cio.println("\n--Station Inventory--");
		for(item items:game_manger.p.items) {
			cio.println(capitalize(items.name.toString())+"::: amount: "+items.amount);
		};
		cio.println("\n--Long Term Events--");
		for(Object items:game_manger.p.counters_list) {
			int event_turns_remaining = game_manger.p.counters.get(items.toString()+"_max")-game_manger.p.counters.get(items.toString()+"_counter");
			cio.println(capitalize(items.toString())+"::: turns remaining: "+event_turns_remaining);
		};
	}//end display_player_data
	
	
	//capitalize function from https://stackoverflow.com/questions/5725892/how-to-capitalize-the-first-letter-of-word-in-a-string-using-java/45153268
	public String capitalize(String originalString) {
		if(originalString==null || originalString.length()==0) {return originalString;}
		else {
			return originalString.substring(0,1).toUpperCase() + originalString.substring(1);
		}
	}//end capitalize
	
}
