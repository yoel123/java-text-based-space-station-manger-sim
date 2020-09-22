package screens;

import java.util.ArrayList;

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
				,"events"
				,"pass turn"
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
		
		//events show
		if(next.equals("3")) {show_events();}
		
		//pass turn
		if(next.equals("4")) {game_manger.pass_turn();}
		
		
	}//end menu_user_input
	
	public void show_events() 
	{
		ArrayList<String> events = game_manger.p.events;
		int i =1;
		for(String e :events) 
		{
			S.o(i+") "+e);
			i++;
		}
		
		int sel_e = ui.get_int("resolve event(0 to go back)");
		
		//exit if 0
		if(sel_e ==0) {return;}
		
		sel_e--;//deincrament so i can get the event from the arraylist
		
		String se = events.get(sel_e);
		//if event was immidiate (meaning no player choice so dont use do event
		if(se.contains("**")) 
		{
			cio.println("event already done select another");
			show_events();
		}
		else {
			event_manger.do_event(se, cio);
			events.remove(sel_e);
		}
	}//end show_events
}
