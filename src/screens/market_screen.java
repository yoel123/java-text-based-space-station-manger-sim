package screens;

import game_objects.game_manger;
import game_objects.item;
import helpers.C.GUIConsoleIO;
import helpers.S;
import helpers.gconsole_menu;
import helpers.gyinput;

/*station market terminal
 * where the player can buy and sell goods
 * */

public class market_screen extends gconsole_menu {

	gyinput ui; 
	public market_screen(GUIConsoleIO cio2) {
		super(cio2);
		menu_items = new String[]{
				"back","buy","sell"
				};
		ui = new gyinput(cio2);
	}//end constructor
	
	

	@Override
	public void menu_show() {
		//show player stats
		cio.println(game_manger.p.show_stats(),pc);
		super.menu_show();
	}



	@Override
	public void menu_user_input() 
	{
		super.menu_user_input();

		
		//back
		if(next.equals("1")) 
		{
			gm.change_menu("game_main");
		}
		
		//buy
		if(next.equals("2")) 
		{
			do_buy(); 
		}

		//sell
		if(next.equals("3")) 
		{
			do_sell(); 
		}
		
	
		
		
		
		
	}//end menu_user_input
	
	
	public void do_sell() {
		game_manger.m.update_player_item_price(game_manger.p);
		S.o(game_manger.p.items_list());
		
		int inum = ui.get_int("select item num(0 to exit)");
		if(inum==0) {return;}
		int amount = ui.get_int("select amount to sell");
		
		if(inum-1>game_manger.p.items.size()) {S.o("item not found"); return;}
		
		item it = game_manger.p.items.get(inum-1);
		item itp = game_manger.m.get_item(it.name);
		if(game_manger.p.sell(itp, amount, itp.cost))
		{
			//itp.amount+=amount;//return items to market 
			S.o("sucesffuly sold items");
			messge = "succesfully sold items";
		}else 
		{
			S.o("failed to sell items");
			messge = "failed to sell items";
		}
	}

	public void do_buy() 
	{
		S.o(game_manger.m.display_all());
		//item num get
		int inum = ui.get_int("select item num(0 to exit)");
		if(inum==0) {return;}
		int amount = ui.get_int("select amount to buy");
		
		if(inum-1>game_manger.m.items.size()) {S.o("item not found"); return;}
		
		item it = game_manger.m.items.get(inum-1);
		it.you_paid = it.cost;
		if(game_manger.p.buy(it, amount)) 
		{
		
			S.o("sucesffuly purchesed items");
			messge = "succesfully bought items";
		}else 
		{
			S.o("failed to purchese items");
			messge = "failed to buy items";
		}
	}//end do_buy
	
	
	

}
