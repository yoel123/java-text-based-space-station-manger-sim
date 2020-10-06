package game_objects;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import db.event_db;
import helpers.S;
import helpers.gyinput;
import helpers.yvars;
import helpers.C.GUIConsoleIO;
import static javax.swing.JOptionPane.showMessageDialog;

public class event_manger {

	public static void random_events() 
	{
		
		//clear old events
		game_manger.p.events.clear();
		
		//number of events that will happen
		int event_num =(int)(Math.random()*6+1);
		
		//gen events
		for(int i=0; i<= event_num ; i++)
		{
			//random event id
			int rand_event =(int)(Math.random()*event_db.db.size());
			rand_event = 5;//for testing
			
			//get event from db
			String e = event_db.db.getValueAt(rand_event);
			//if event is immediate do_immidiate
			if(e.contains("immediate")) 
			{
				e = " ** "+do_immidiate(e)+" **";
			}
			//add to events arraylist
			game_manger.p.events.add(e);
		}
		
	}//end random events
	
	//do the action of an event
	public static String do_event(String etxt
			,GUIConsoleIO cio) 
	{
		//event text to array
		String[] e = etxt.split("//");
		String action = e[0];//the events logic action that will happen
		//some events hold an amount value
		int amount = yvars.ystoint(e[1]);
		//the events description/flavor text
		String desc = e[2];
		
		//player handale
		player p  = game_manger.p;
		
		//user input helper
		gyinput ui = new gyinput(cio);

		if(action.equals("test")) 
		{
			cio.println("test works");
		}
		
		//smuglers offer to sell you somthing
		if(action.equals("smuglers_sell")) 
		{
			
			//random item get
			int item_num = game_manger.m.items.size();
			int rndi =(int)(Math.random()*item_num);
			item goods_to_sell =  game_manger.m.items.get(rndi);
			goods_to_sell = new item(goods_to_sell);//make copy
			String goods_name = goods_to_sell.name;
			int goods_amount = (int)(Math.random()*10+1);
			int smugler_price_per_item = goods_to_sell.max_price/4;//item max/2
			goods_to_sell.cost = smugler_price_per_item;
			goods_to_sell.amount = goods_amount;
			int total_price = goods_amount*smugler_price_per_item;
			//rand chance of getting caught
			
			//ask player if he wants to buy
			String ask_t_buy = desc+" wants to sell you "+goods_amount
					+" "+goods_name+" for "+total_price+" do you agree? (y/n)";
			String answer = ui.get_string(ask_t_buy);
			
			//is busted "roll"
			int rnd2 =(int)(Math.random()*10+1);
			int fine = 10000;
			
			String messge = "";//messge that will be returned
			
			//if user agrees to the deal
			if(answer.equals("y")) 
			{

				//check if can buy
				if(game_manger.p.buy(goods_to_sell, goods_amount)) 
				{
					cio.println("done deal");
					messge = "done deal";
				}else 
				{
					cio.println("deal failed (you dont have enough credits");
					messge = "deal failed";
				}
				
				//check if busted
				if(rnd2>8) 
				{
					cio.println(" the police catchs on to your illigal deal and fines you for: "+fine);
					game_manger.p.credits -= fine;
					messge += " the police catchs on to your illigal deal and fines you for: " + fine;
				}
			}//end if y
			
			
			return messge;
		}//end smuglers_sell
		
		if(action.equals("smuglers_buy")) 
		{
			
		}//end smuglers_buy

		//a crazy inventor wants to upgrade your station for cheap
		if(action.equals("crazy_inventor")) 
		{
			
			//Upgrade data
			String[] upgrades =  {
				"mtest1"
				, "mtest2"
				, "mtest3"
			};
			int rnd_upgrade = (int)Math.random()*upgrades.length;
			String upgrade = upgrades[rnd_upgrade];
			int upgrade_amount = (int)(Math.random()*10+1); //Not sure what valid ranges should be
			
			//showMessageDialog(null,rnd_upgrade+" "+upgrade_amount);
			
			//Ask player if they agree to the deal
			String upgrades_offer = desc+" would like to make an "
					+ "experimental upgrade on your station do you agree? (y/n)";
			String answer = ui.get_string(upgrades_offer);

			//Is a bad upgrade
			int rnd_damage_multiplier; // Not sure what range for random should be
			int station_damage; // To be calculated when station damage amount ranges are known
			String message = "Your station took a catastrophic blow, the crazy"
					+ " inventor's upgrade fails!";
			
			
			if(answer.equals("y") || answer.equals("Y")) 
			{

				//check if upgrades are a flop and cause damage... TBD
				
				//check if can afford upgrades
				if(game_manger.p.buy_upgrade(upgrade, upgrade_amount)) 
				{
					cio.println("Upgrades Applied");
					message = "Upgrades Applied";
				}else 
				{
					cio.println("The Crazy Inventor scoffs at you with disdain! (you dont have enough credits)");
					message = "No upgrades were applied";
				}
				
			
			}//end if y
			
			 ui.get_string("press enter to continue");
			return message;
		}// end crazy_inventor
		
		cio.println(desc);
		return "";

	}//end do_event
	
	public static String do_immidiate(String etxt) 
	{
		//event text to array
		String[] e = etxt.split("//");
		//the event logic action
		String action = e[0];
		//some events have an amount
		int amount = yvars.ystoint(e[1]);
		//the events description
		String desc = e[2];
		
		//player hendale
		player p  = game_manger.p;
		
		//the string this method returns
		String ret ="";
		
		//events that cause you to loss money
		if(action.equals("money_reduce")) 
		{
			//if you have insurance they cover it
			if(p.insurance)
			{
				desc =" [insurence cancels this event] "+desc;
			}else
			{
				//if you dont have insurance loss money
				game_manger.p.credits -=amount;
				desc = desc+" you lost "+amount+" credits";
			}
			//return the text to show the player
			ret = desc;
		}//end money_reduce
		
		return ret;
	}//end do_immidiate
	
	public static void long_term_events_update() 
	{
		if(game_manger.p.counters_list.size()==0) {return;}
		player p = game_manger.p;
		ArrayList<String> counters = game_manger.p.counters_list;
		Iterator<String> i = counters.iterator();
		for(String e:counters) 
		{
			p.incrament_counter(e);
			if(p.is_counter_done(e)) 
			{
				do_long_term_events(e); 
			}
		}
		
		//remove unneeded elements
		counters.removeAll(Collections.singleton("yremove"));

	}//end do_long_term_events
	
	public static void do_long_term_events(String name) 
	{
		if(name.equals("test")) 
		{
			 game_manger.p.events.add("**test long term event fired");
			 game_manger.p.remove_counter(name);
			 
		}
		
		if(name.equals("worker_union_fine")) 
		{
			 game_manger.p.events.add("**worker union fine!"
			 		+ " pay your workers or else we will close your buisness");
			 game_manger.p.s_personal.union_fine++;
			 //remove credits (1000 multiplied by fines you got)
			 game_manger.p.credits -= 1000*game_manger.p.s_personal.union_fine;
			 game_manger.p.remove_counter(name);
			 //showMessageDialog(null, "pay workers!");
		}
		
	}//end do_long_term_events
	
}//end event_manger
