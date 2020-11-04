package screens;

import java.util.Map.Entry;

import db.upgrades_db;
import game_objects.game_manger;
import game_objects.item;
import game_objects.marketing_system;
import game_objects.player;
import game_objects.upgrades_system;
import helpers.C.GUIConsoleIO;
import helpers.IndexableMap;
import helpers.gconsole_menu;
import helpers.gyinput;
import helpers.yvars;

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
				,"marketing"
				,"personal"
				,"station security ships and weapons"

				};
		ui = new gyinput(cio2);
	}//end constructor
	
	

	@Override
	public void menu_show() {
		
		cio.println(game_manger.p.show_stats(),pc);
		
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
		//marketing
		if(next.equals("5")) {marketing_do();}
		//personal
		if(next.equals("6")) {personal_do();}
		//station security ships and weapons
		if(next.equals("7")) {gm.change_menu("station_security_screen");}
		
		//if(messge != "") {messge="";}
		
		
	}//end menu_user_input
	
	private void marketing_do() 
	{
		int choice = mini_menu(
				"advertise goods//"
				+ "advertise rent space//"
				+ "advertise station attractions");
		//advertise goods
		if(choice==1) {messge = marketing_system.market_goods(cio, ui);}
	}//end marketing_do
	
	private void show_upgrades() 
	{
		
		cio.println("select option:(0 to return)");

		int choice = mini_menu(
				"show upgrades//"
				+ "buy upgrades"
			);
		if(choice ==1) {upgrades_list();show_upgrades();}
		if(choice ==2) {buy_upgrades();}
		
		
	}//end show_upgrades
	
	public void upgrades_list() 
	{
		upgrades_system.upgrades_list(cio);
	}
	
	public void buy_upgrades() 
	{
		messge = upgrades_system.buy_upgrades(cio, ui);
		
	}//end buy_upgrades
	
	
	private void pay_taxes() {
		
		player p = game_manger.p;
		
		cio.println("taxes you owe:" +p.taxes_owed);
		
		int amount_to_pay = ui.get_int("how much would you like to pay? (0 to exit)");
		if(amount_to_pay ==0 || amount_to_pay<0) {return;}
		
		p.pay_taxes(amount_to_pay);
		
		messge ="taxes paid:" +amount_to_pay;
		
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
	
	
	public void personal_do() 
	{
		player p = game_manger.p;
		int owed = p.s_personal.payments_owed;
		cio.println("payments you owe to station personal: "+owed);
		
		int choice = mini_menu(
				"hire personal//"
				+ "pay salery//"
				+ "fire personal//"
				+ "hire character//"
				+ "view characters//"
				+ "(0 to return)");
		
		//1)hire personal
		if(choice ==1) 
		{
			//get personal as list
			String plist =p.s_personal.personal_list();
			cio.print(plist);//print it 
			//get player type choice and amount he wants to hire
			int ptype = ui.get_int("select the type of personal you want to hire?");
			int amount = ui.get_int("how meany you want to hire?");
			//
			boolean ydo = p.hire_personal(ptype, amount);
			if(ydo) {messge= "hierd personal";}
			else {messge="failed to hire personal";}
		}//end 1
		
		//2)pay salery
		if(choice ==2) {
			int amount_to_pay = ui.get_int("enter payment:");
			
			//reduce payments owed
			if(p.pay_sallery(amount_to_pay)) 
			{
				messge="you paied "+amount_to_pay+" to station personal";
			}else {messge="payment faield";}
			
		}//end if 2 pay salery
		
		//3)fire personal
		if(choice ==3) 
		{
			//get personal as list
			String plist =game_manger.p.s_personal.personal_list();
			cio.print(plist);//print it 
			//get player type choice and amount he wants to hire
			int ptype = ui.get_int("select the type of personal you want to fire?");
			int amount = ui.get_int("how meany you want to fire?");
			//
			boolean ydo = game_manger.p.fire_personal(ptype, amount);
			if(ydo) {messge= "fierd personal";}
			else {messge="failed to fire personal";}
		}
		
		//4)hire character
		if(choice ==4) 
		{
			cio.print(p.s_personal.charecter_for_hire_list());
			int cselect = ui.get_int("select a charecter (0 to exit)");
			if(cselect ==0) {return;}
		}
		//5) "view characters
		if(choice ==5) 
		{
			
		}
		
		
		
	}//end personal_do

	

}
