package screens;

import helpers.C.GUIConsoleIO;
import helpers.gconsole_menu;
import helpers.gyinput;

public class combat_screen extends gconsole_menu {

	gyinput ui; 
	public combat_screen(GUIConsoleIO cio2) {
		super(cio2);
		
		menu_items = new String[]{
				"buy","sell","back"
				};
		ui = new gyinput(cio2);

	}
	
	

}
