package helpers;

import java.util.HashMap;

import helpers.C.GUIConsoleIO;

public class gmenu_manger {
	
	public HashMap<String,gconsole_menu> menus;
	public GUIConsoleIO cio;
	public boolean is_running;
	protected gconsole_menu current_menu;
	protected String current_menu_name="start";
	
	public gmenu_manger() 
	{
		menus = new HashMap<String,gconsole_menu>();
		is_running = true;
	}//end constroctor
	
	public void add_menu(String name,gconsole_menu cm) 
	{
		cm.gm = this;
		menus.put(name, cm);
	}//end add_menu
	
	public void run() 
	{
		while(is_running) 
		{
			current_menu.update();
		}
	}//end run
	
	public void change_menu() 
	{
		current_menu = menus.get(current_menu_name);
	}//end change_menu
	
	public void change_menu(String name) 
	{
		current_menu_name = name;
		current_menu = menus.get(current_menu_name);
	}//end change_menu
	
	public gconsole_menu get_menu(String name) 
	{
		return menus.get(name);
	}//end get_menu
}
