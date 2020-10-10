package screens;

import helpers.C.GUIConsoleIO;
import game_objects.game_manger;
import helpers.gconsole_menu;

/*
 * beginning of the game screen
 * where the player can start new game load new game see the credits
 * and exit the game
 * */

public class start_menu extends gconsole_menu {

	public start_menu(GUIConsoleIO cio2) {
		super(cio2);
		menu_items = new String[]{"start"
				,"continue","credits"};
	}//end constructor

	@Override
	public void menu_show() {
		cio.print("                                   _        _   _             \n" + 
				"                                  | |      | | (_)            \n" + 
				" ___ _ __   __ _  ___ ___      ___| |_ __ _| |_ _  ___  _ __  \n" + 
				"/ __| '_ \\ / _` |/ __/ _ \\    / __| __/ _` | __| |/ _ \\| '_ \\ \n" + 
				"\\__ \\ |_) | (_| | (_|  __/    \\__ \\ || (_| | |_| | (_) | | | |\n" + 
				"|___/ .__/ \\__,_|\\___\\___|    |___/\\__\\__,_|\\__|_|\\___/|_| |_|\n" + 
				"    | |                                                       \n" + 
				"    |_|                                                       \n" + 
				"                                                _             \n" + 
				"                                               (_)            \n" + 
				"  _ __ ___   __ _ _ __   __ _  ___ _ __     ___ _ _ __ ___    \n" + 
				" | '_ ` _ \\ / _` | '_ \\ / _` |/ _ \\ '__|   / __| | '_ ` _ \\   \n" + 
				" | | | | | | (_| | | | | (_| |  __/ |      \\__ \\ | | | | | |  \n" + 
				" |_| |_| |_|\\__,_|_| |_|\\__, |\\___|_|      |___/_|_| |_| |_|  \n" + 
				"                         __/ |                                \n" + 
				"                        |___/                                 "
				+ "");
		super.menu_show();
	}

	@Override
	public void menu_user_input() 
	{
		super.menu_user_input();
		if(next.equals("1") || next.equals("start")) 
		{
			
			cio.clear();
			yp("\n-----\nstart game\n-----\n");
			//game menger start game func
			game_manger.start_game();
			//go to main game screen
			gm.change_menu("game_main");
		}//end start
		
		if(next.equals("2") ) 
		{
			cio.clear();
			game_manger.load_game();
			gm.change_menu("game_main");
		}
		if(next.equals("3") || next.equals("credits")) 
		{
			cio.clear();
			yp("\n\n#################");
			yp("created by yoel fisher");
			yp("#################\n");
		}//end credits
		
	}//end menu_user_input
	
	
	
	

}
