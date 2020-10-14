package game_objects;

import helpers.gyinput;
import helpers.C.GUIConsoleIO;

public class marketing_system {

	
	public static String market_goods(GUIConsoleIO cio,gyinput ui)
	{
		cio.println("select how you want advertise:");
		
		cio.println("1)spacebook news board kragslist");
		cio.println("2)news website");
		cio.println("3)spacetube");
		cio.println("4)commision trade union");
		cio.println("5)hire freelance traders");
		cio.println("6)post product bounty");
		cio.println("7)everything");
		
		
		
		
		String fail_msg = "failed to buy marketing not enogh credits or alrady bought";
		
		int choice = ui.get_int("");
		
		int cost = choice *2000;
		
		//can buy marketing and didnt buy this turn and choice is less then 7
		if(game_manger.p.can_buy(cost) && game_manger.p.good_advertise ==0 && choice<=7 ) 
		{
			//reduce player credits
			game_manger.p.credits = game_manger.p.credits-cost;
			//set goods advertising level
			game_manger.p.good_advertise = choice;
			
			return "bought marketing";
		}
		
		return fail_msg;
	}//end market_goods
	
}
