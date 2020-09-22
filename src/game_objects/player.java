package game_objects;

import java.util.ArrayList;

import helpers.S;

public class player {
	
	public int credits;
	public ArrayList<item> items;
	public ArrayList<String> events,upgrades;
	public boolean insurance;
	public int insurance_rate = 500,
			taxes_owed,didnt_pay_taxes_c,didnt_pay_taxes_max=5;
	double tax_rate = 0.05;
	boolean taxes_problem;
	
	public player() {
		
		items = new ArrayList<item>();
		events = new ArrayList<String>();
		upgrades = new ArrayList<String>();
		upgrades.add("tst");

	}//end constructor
	
	//buy item
	public boolean buy(item it,int amount)
	{
		int cost = it.cost;
		
		//check if theres enough credits and the amount is not bigger then
		//Available amount
		if(cost > credits || amount > it.amount) {return false;}
		
		//get item from arraylisr
		item my_it = get_item(it.name);
		
		//if it exists incrament amount
		if(my_it!=null) 
		{
			my_it.amount+=amount;
			
		}else 
		{
			//else add it (copy it) and set it to amount
			item nit =new item(it);
			items.add(nit);
			my_it = get_item(it.name);//dont want to change the refrence
			nit.player_owned =true;
			nit.amount = amount;//reset amount to amount bought
		}
		it.amount -=amount;
		credits -= cost*amount;
		
		return true;
	}//end buy
	
	public boolean sell(item it,int amount,int sell_price)
	{
		String name = it.name;
		//get item by name
		item my_it = get_item(name);
		//check if exists and has amount to sell
		if(my_it ==null || my_it.amount<amount) {return false;}
		
		int profit = amount *sell_price;
		credits += profit;
		
		//handle taxes
		add_taxes(profit);
		
		my_it.amount -= amount;//reduce amount from player item
		it.amount += amount;//add amount to market item
		return true;
	}
	
	//get player item by name
	public item get_item(String name) 
	{
		for(item it:items)
		{
			if(it.name.equals(name)) {return it;}
		}
		return null;
	}//end get_item
	
	
	public String items_list() 
	{
		String ret = "";
		int i=1;
		for(item it:items)
		{
			ret+=i+") "+it.toStringp()+"\n";
			i++;
		}
		return ret;
	}//end items_list
	
	public String show_stats()
	{
	
		return "cerdits: "+credits;
	}//end show_stats
	
	public void add_taxes(int earnings) 
	{
		
		double to_pay = earnings * this.tax_rate;
		taxes_owed += to_pay; 
	}//end add_txes
	
	public void update_taxes() 
	{
		//incrament how long player didnt pay taxes
		if(taxes_owed>0) {didnt_pay_taxes_c++;}
		//if player didn't pay taxes for 12 turns give him a warning
		if(didnt_pay_taxes_c>12) 
		{
			events.add("notice!,0,this is your final notice pay your taxes"
					+ " or your assets would be liquidated");
		}
		//if player didnt heed warning give him problems
		if(didnt_pay_taxes_c>13) 
		{
			taxes_problem = true;
		}
		
		//fine the player
		if(didnt_pay_taxes_c>didnt_pay_taxes_max) 
		{
			taxes_owed +=2000;
			events.add("notice!,0,your late on your tax payments and got fined "
					+ " please pay your taxes on time");
		}
	}//end  update_taxes
	
	public boolean pay_taxes(int amount) 
	{
		if(amount > credits) {return false;}
		
		credits -= amount;
		taxes_owed -= amount;
		didnt_pay_taxes_c = 0;
		
		return true;
		
	}//end pay_taxes
	
	
	public boolean buy_upgrade(String name,int cost)
	{
		//can buy
		if(cost > credits ) {return false;}
		
		credits -=cost;
		upgrades.add(name);
		
		return true;
	}//end buy_upgrade
	


	

}
