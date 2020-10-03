package helpers;

import java.awt.Color;
import java.util.Scanner;

import helpers.C.GUIConsoleIO;

public class gconsole_menu {

	protected GUIConsoleIO cio;
	public boolean is_running;
	public String messge="";
	public String[] menu_items;
	public Color pc = Color.WHITE;
	protected String next="";
	public gmenu_manger gm;
	public gconsole_menu(GUIConsoleIO cio2) {
		cio = cio2;
		is_running = true;
		menu_items = new String[]{"show all","add","edit"};
	}//end constructor
	
	public void run() 
	{
		while(is_running) 
		{
			update();
		}
	}//end run
	
	public void update() 
	{
		cio.clear();
		menu_show() ;
		menu_user_input();
	}//end update
	
	public void menu_show() 
	{
		yp("\n"+messge+"\n");
		int i=1;//iterator
		//foreach menu_items
		for(String item :menu_items) 
		{
			cio.println(i+") "+item,pc);
			i++;
		}
		if(messge!="") {messge="";}
	}//end menu_show
	
	public void menu_user_input() 
	{
		next = cio.nextLine();
		
		//exit program if user writes exit
		if(next.equals("exit")) 
		{
			cio.println("program ended");
			is_running = false;
			if(gm !=null) {gm.is_running = false;}
		}
		
		if(next.equals("cls")) {cio.clear();}
		
	}//end menu_user_input
	
	public void yp(Object o) 
	{
		cio.println(o,pc);
	}//end yp
	
	
}


