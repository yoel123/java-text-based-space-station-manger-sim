package screens;

import game_objects.game_manger;
import game_objects.market;
import game_objects.player;
import game_objects.ship;
import game_objects.weapon;
import helpers.C.GUIConsoleIO;
import helpers.S;
import helpers.gconsole_menu;
import helpers.gyinput;

public class security_screen extends gconsole_menu {
	gyinput ui; 
	public security_screen(GUIConsoleIO cio2) {
		super(cio2);
		menu_items = new String[]{
				"go back",
				"buy weapons"
				,"sell weapons"
				,"buy ships"
				,"sell ships"
				,"view ships"
				

				};
		ui = new gyinput(cio2);
	}//end constructor

	@Override
	public void menu_user_input() {
		
		super.menu_user_input();
		
		if(next.equals("1")) {gm.change_menu("game_main");}
		if(next.equals("2")) {buy_weapons();}
		if(next.equals("3")) {sell_weapons();}
		if(next.equals("4")) {buy_ships();}
		if(next.equals("5")) {sell_ships();}
		if(next.equals("6")) {view_ships();}
		
	}//end menu_user_input

	private void view_ships() {
		player p = game_manger.p;
		//show ship list
		S.o(p.sm.ships_list());
		//get the ship id frop player
		int shipid = ui.get_int("select a ship (0 to return)");
	    if(shipid==0) {return;}//exit if 0
	    if(shipid>p.sm.ships.size()) {view_ships();}
	    
		//get ship from security manger
		ship s = p.sm.ships.get(shipid-1);
		S.o(s.toString());
		
		S.o("\n-------------\n");
		
		//what would the player like todo with ship
		int choice = mini_menu(
				"return//"
				+ "view/unequip weapons//"
				+ "equip weapons//"
				+ "fighter bomber bay//"
				+ "upgrades//"
				+ "sell//");
		//unequip weapons
		if(choice ==2) 
		{
			p.sm.ship_unequip_weapon(s,cio,ui);
		}
		//equip weapons
		if(choice ==3) 
		{
			p.sm.ship_equip_weapon(s,cio,ui);
		}
		
		//ui.get_string("\npress enter to continue...");
	}//end view_ships

	private void sell_ships() {
		player p = game_manger.p;
		market m = game_manger.m;
		
		//show ship list
		S.o(p.sm.ships_list());
		//get the ship id frop player
		int shipid = ui.get_int("select a ship (0 to return)");
	    if(shipid==0) {return;}//exit if 0
	   
	    ship s = p.sm.ships.get(shipid-1);
		
		if(p.sm.sell_ship(s)) 
		{
			messge = "succesfully sold ship";
		}else 
		{
			messge = "failed to sell ship";
		}
		
	}//end sell_ships

	private void buy_ships() {
		
		player p = game_manger.p;
		market m = game_manger.m;
		//show ship list
		S.o(p.sm.ships_list(m.ships));
		//get the ship id frop player
		int shipid = ui.get_int("select a ship (0 to return)");
		if(shipid==0) {return;}//exit if 0
		
		ship s = m.ships.get(shipid-1);
		
		if(p.sm.buy_ship(s)) 
		{
			messge = "succesfully bought ship";
		}else 
		{
			messge = "failed to buy ship";
		}
		
	}//end buy_ships

	private void sell_weapons() {
		player p = game_manger.p;
		market m = game_manger.m;
		//update player weapons prices
		m.update_player_item_price(p);
		
		//weapons list
		S.o(p.sm.display_all_wapons());
		//get user choice
		int wid = ui.get_int("select a weapon to sell (0 to return)");
	    if(wid==0) {return;}//exit if 0
		int amount = ui.get_int("select amount to sell");
		
		//get selected weapon 
		weapon w = p.sm.weapons.get(wid-1);
		//get weapon from market
		weapon wm = m.get_weapon(w.name);
		if(p.sm.sell_weapon(wm, amount, wm.cost)) 
		{
			messge = "succesfully sold weapons";
		}else 
		{
			
			messge = "failed to sell weapons";
		}
			
		
	}//end sell_weapons

	private void buy_weapons() {
		
		player p = game_manger.p;
		market m = game_manger.m;
		//weapons list
		S.o(p.sm.display_all_wapons(m.weapons));
		//get user choice
		int wid = ui.get_int("select a weapon to buy (0 to return)");
	    if(wid==0) {return;}//exit if 0
	    int amount = ui.get_int("select amount to buy");
	    
	    //validate
	    if(wid-1>m.weapons.size()) {messge="weapon not found"; return;}
		
	    //get weapon
	    weapon w = m.weapons.get(wid-1);
	    
	    //try to buy
	    if(p.sm.buy_weapon(w, amount)) {
	    	w.you_paid = w.cost;
	    	messge = "succesfully bought weapons";
	    }else {
	    	messge = "failed to buy weapons";
	    }
	    
		
	}//end buy_weapons
	
	

}
