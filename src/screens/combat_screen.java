package screens;

import helpers.C.GUIConsoleIO;
import helpers.gconsole_menu;
import helpers.gyinput;

public class combat_screen extends gconsole_menu {

	gyinput ui; 
	public combat_screen(GUIConsoleIO cio2) {
		super(cio2);
		
		menu_items = new String[]{
				"view ships","order ships","global order",
				"next turn","retreat"
				};
		ui = new gyinput(cio2);

	}
	@Override
	public void menu_user_input() {
		
		super.menu_user_input();
		
		if(next.equals("1") ) 
		{
			
		}
		
		if(next.equals("2") ) 
		{
			
		}
		
		if(next.equals("3") ) 
		{
			
		}
		
		if(next.equals("4") ) 
		{
			
		}
		
		if(next.equals("5") ) 
		{
			
		}
		
		
	}//end menu_user_input
	
	
	
	

}
