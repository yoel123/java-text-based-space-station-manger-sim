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
		if(next.equals("3") || next.equals("credits")) 
		{
			cio.clear();
			yp("\n\n#################");
			yp("created by yoel fisher");
			yp("#################\n");
		}//end credits
		
	}//end menu_user_input
	
	
	
	

}
