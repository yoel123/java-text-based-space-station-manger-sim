package screens;

import java.util.Map.Entry;

import db.upgrades_db;
import game_objects.game_manger;
import game_objects.item;
import game_objects.player;
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
				,"personal"
				,"station status report (stats)"

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
		//personal
		if(next.equals("5")) {personal_do();}
		//all stats
		if(next.equals("6")) {}
		
		//if(messge != "") {messge="";}
		
		
	}//end menu_user_input
	
	
	private void show_upgrades() 
	{
		
		cio.println("select option:(0 to return)");
		cio.println("1)show upgrades");
		cio.println("2)buy upgrades");
		int choice = ui.get_int("");
		if(choice ==1) {upgrades_list();show_upgrades();}
		if(choice ==2) {buy_upgrades();}
		
		
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
	
	public void buy_upgrades() 
	{
		//get upgrades db
		IndexableMap<String, String> udb = upgrades_db.db;
		String[] upgrade;//single upgrade
		int i=1;//iteration counter
		for (Entry<String, String> me : udb.entrySet()) 
		{
			
			upgrade = me.getValue().split(upgrades_db.ychar);
			//if player has upgrade dont show it
			if(game_manger.p.upgrades.indexOf(upgrade[0])==-1) 
			{
				cio.println(i+") "+upgrade[0]+" price "+upgrade[1]);
			}
			i++;
		}//end for
		
		//get theupgrade the player wants to buy
		int choice = ui.get_int("select upgrade to buy (enter to return)");
		
		//check for potential errors (validate input)
		if(choice ==0 || choice<0 || udb.getKeyAt(choice-1) ==null)
		{
			messge ="upgrade dosnt exist";
			return;
		}
		
		//get the upgrade player chose
		String[] upgrade_to_buy = udb.getValueAt(choice-1).split(upgrades_db.ychar);
		
		int cost = yvars.ystoint(upgrade_to_buy[1]);
		//check if player can buy (has credits and dosnt already have upgrade
		if(game_manger.p.credits>=cost && game_manger.p.upgrades.indexOf(upgrade_to_buy[0]) ==-1) {
			game_manger.p.upgrades.add(upgrade_to_buy[0]);
			messge ="bought upgrade";
		}else {
			messge ="failed to buy upgrade";
		}
		
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
		int owed = game_manger.p.s_personal.payments_owed;
		cio.println("payments you owe to station personal: "+owed);
		
		cio.println("1)hire personal");
		cio.println("2)pay salery");
		cio.println("3)fire personal");
		
		int choice = ui.get_int("(0 to return)");
		
		//2)pay salery
		if(choice ==2) {
			int amount_to_pay = ui.get_int("enter payment:");
			
			//reduce payments owed
			if(game_manger.p.pay_sallery(amount_to_pay)) 
			{
				messge="you paied "+amount_to_pay+" to station personal";
			}else {messge="payment faield";}
			
		}//end if 2 pay salery
		
		
		
		
	}//end personal_do

	

}
