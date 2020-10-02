package screens;

import game_objects.game_manger;
import game_objects.player;
import helpers.C.GUIConsoleIO;
import helpers.gconsole_menu;
import helpers.gyinput;

/*
 * station mangment screen where the player can upgrade,
 * pay taxes, buy insurence and other logestical stuff related to manging
 * the station
 * */
public class mangment_screen extends gconsole_menu {
	gyinput ui;
	public mangment_screen(GUIConsoleIO cio2) {
		super(cio2);
		menu_items = new String[]{
				"back to main screen",
				"upgrade station"
				,"buy insurence"
				,"taxes"
				,"personal"

				};
		ui = new gyinput(cio2);
	}//end constructor
	
	

	@Override
	public void menu_show() {
		
		cio.println("----station management-----");
		super.menu_show();
	}



	@Override
	public void menu_user_input() {
		
		super.menu_user_input();
		
		if(next.equals("1")) {cio.clear();gm.change_menu("game_main");}
		
		//upgrades
		if(next.equals("2")) {show_upgrades() ;}
		
		//insurance
		if(next.equals("3")) {buy_insurence() ;}
		//taxes
		if(next.equals("4")) {pay_taxes() ;}
		
		//if(messge != "") {messge="";}
		
		
	}//end menu_user_input
	
	
	private void show_upgrades() 
	{
		
		cio.println("select option:(0 to return)");
		cio.println("1)show upgrades");
		cio.println("2)buy upgrades");
		int choice = ui.get_int("");
		if(choice ==1) {upgrades_list();show_upgrades();}
		if(choice ==2) {}
		
		
	}//end show_upgrades
	
	public void upgrades_list() 
	{
		cio.println("=======station upgrades======");
		for(String u:game_manger.p.upgrades) 
		{
			cio.println(u);
		}
		cio.println("=============");
	}

	private void pay_taxes() {
		
		player p = game_manger.p;
		
		cio.println("taxes you owe:" +p.taxes_owed);
		
		int amount_to_pay = ui.get_int("how much would you like to pay? (0 to exit)");
		if(amount_to_pay ==0) {return;}
		p.pay_taxes(amount_to_pay);
	}//end pay_taxes



	public void buy_insurence() 
	{
		player p = game_manger.p;
		if(p.insurance) 
		{
			messge="--already has insurance--";
		}
		else 
		{
			String a = ui.get_string("would you like to buy insurence? "
					+ " (costs "+p.insurance_rate+") y/n");
			if(a.equals("n")){return;}
			if(a.equals("y") && p.credits >= p.insurance_rate && !p.insurance) 
			{
				p.credits -= p.insurance_rate;
				p.insurance = true;
				messge = "--insurance bought--";
			}
			else 
			{
				messge = "--couldn't buy insurance--";
			}
		}
	}//end buy_insurence
	
	

}
