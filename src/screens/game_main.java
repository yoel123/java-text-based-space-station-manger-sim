package screens;

import java.util.ArrayList;
import java.util.Arrays;

import db.event_db;
import game_objects.event_manger;
import game_objects.game_manger;
import helpers.C.GUIConsoleIO;
import helpers.S;
import helpers.gconsole_menu;
import helpers.gyinput;

/*
 * thats the main game screen, where the player decides what he wants todo
 * */

public class game_main extends gconsole_menu {
	gyinput ui;
	public game_main(GUIConsoleIO cio2) {
		super(cio2);
		
		menu_items = new String[]{"market"
				,"management"
				,"station information"
				,"events"
				,"pass turn"
				,"save game"
				};
		ui = new gyinput(cio2);
	}//end constructor
	
	@Override
	public void menu_show() {
		//show player stats
		cio.println(game_manger.p.show_stats(),pc);
		super.menu_show();
	}



	@Override
	public void menu_user_input()
	{

		super.menu_user_input();
		
		//market
		if(next.equals("1")) {gm.change_menu("market_screen");}
		
		
		//if(next.equals("1")) {}
		
		//management screen
		if(next.equals("2")) {cio.clear();gm.change_menu("station_mangment_screen");}
		
		if(next.equals("3")) {gm.change_menu("station_info_screen");}
		
		//events show
		if(next.equals("4")) {show_events();}
		
		//pass turn
		if(next.equals("5")) {game_manger.pass_turn(); messge ="passed turn";}
		
		//save game
		if(next.equals("6")) {game_manger.save_game(); messge ="saved game";}
		
		
				
				
	}//end menu_user_input
	
	public void show_events() 
	{
		ArrayList<String> events = game_manger.p.events;
		int i =1;
		String[] er;
		for(String e :events) 
		{
			er=e.split(event_db.ychar);
			S.o(i+") "+er[0]);
			//S.o(Arrays.toString(er));
			i++;
		}
		
		int number_of_options = events.size();
		//sl_e = selected events		
		int sel_e = ui.get_int("resolve event(0 to go back)");
		
		//exit if 0
		if(sel_e ==0) {return;}
		
		sel_e--;//deincrament so i can get the event from the arraylist
		
		if(sel_e >= number_of_options){
			cio.println("Invalid choice, please select another event");
			show_events();
			return;
			}
		
		String se = events.get(sel_e);
		//if event was immidiate (meaning no player choice so dont use do event
		if(se.contains("**")) 
		{
			cio.println("--event already done select another\n");
			show_events();
		}
		else {
			messge = event_manger.do_event(se, cio);
			events.remove(sel_e);
		}
	}//end show_events
}
