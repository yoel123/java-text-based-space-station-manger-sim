package game_objects;

import static javax.swing.JOptionPane.showMessageDialog;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import helpers.IndexableMap;
import helpers.S;

public class player implements Serializable{
	
	public int credits;
	public ArrayList<item> items;
	public ArrayList<String> events,upgrades,counters_list;
	public boolean insurance;
	public int insurance_rate = 500,
			taxes_owed,didnt_pay_taxes_c,didnt_pay_taxes_max=5
			,station_secutity=0;
	double tax_rate = 0.05;
	boolean taxes_problem;
	
	int cargo=0,cargo_limit = 500;
	
	public  int turn;
	
	//long term events counters
	public IndexableMap<String,Integer> counters ;
	
	//station personal object
	public station_personal s_personal;
	
	public player() {
		
		items = new ArrayList<item>();
		events = new ArrayList<String>();
		upgrades = new ArrayList<String>();
		upgrades.add("tst");
		counters_list = new ArrayList<String>();
		counters = new IndexableMap<String,Integer>();
		turn=0;
		add_counter("test",3);
		
		s_personal = new station_personal();

	}//end constructor
	
	////////////counters////////////
	
	public void add_counter(String name,int max) 
	{
		counters.put(name+"_counter", 0);
		counters.put(name+"_max", max);
		counters_list.add(name);
	}//end add_counter
	
	public void add_counter_once(String name,int max) 
	{
		if(counters_list.indexOf(name)!=-1 ) {return;}
		add_counter(name,max);
	}//end add_counter
	
	public void remove_counter(String name) 
	{
		counters.remove(name+"_counter");
		
		counters.remove(name+"_max");
		int i = counters_list.indexOf(name);
		counters_list.set(i, "yremove");
	}//end remove_counter
	
	
	
	public void reset_counter(String name) 
	{
		 counters.replace(name+"_counter",0);
	}//end reset_counter
	
	public void incrament_counter(String name) 
	{
		int count = counters.get(name+"_counter").intValue()+1;
		counters.replace(name+"_counter",count);
	}//end reset_counter
	
	
	
	public boolean is_counter_done(String name) 
	{
		int count = counters.get(name+"_counter").intValue()
		,max = counters.get(name+"_max").intValue();
		
		if(count> max) {return true;}
		
		return false;
	}//end is_counter_done
	
     ////////////end counters////////////
	
	/////////market/////////
	//buy item
	public boolean buy(item it,int amount)
	{
		int cost = it.cost*amount;
		
		//check if theres enough credits and the amount is not bigger then
		//Available amount
		if(cost > credits || amount > it.amount) {return false;}
		
		//if no room in cargo
		if((amount+cargo)>cargo_limit) {return false;}
		
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
		credits -= cost;
		//add to cargo amount
		cargo+=amount;
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
		//reduce cargo amount
		cargo-=amount;
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
	
	/////////end market/////////
	
	public String show_stats()
	{
	
		return "cerdits: "+credits+" cargo space:"+cargo+"/"+cargo_limit;
	}//end show_stats
	
	/////////taxes////////
	
	//add taxes calculated from your earnings
	public void add_taxes(int earnings) 
	{
		
		double to_pay = earnings * this.tax_rate;
		taxes_owed += to_pay; 
	}//end add_txes
	
	//makes sure you pay your taxes in a few turns or else you get fined
	public void update_taxes() 
	{
		//incrament how long player didnt pay taxes
		if(taxes_owed>0) {didnt_pay_taxes_c++;}
		//if player didn't pay taxes for 12 turns give him a warning
		if(didnt_pay_taxes_c>10) 
		{
			events.add("**notice! this is your final notice pay your taxes"
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
	
	/////////end taxes////////
	
	/////upgrades//////
	public boolean buy_upgrade(String name,int cost)
	{
		//can buy
		if(cost > credits ) {return false;}
		
		//check if upgrade exists
		if(upgrades.indexOf(name)>-1) {return false;}
		
		credits -=cost;
		upgrades.add(name);
		
		return true;
	}//end buy_upgrade
	
	///////personal////////
	
	public void incrament_sallery_owed()
	{
		s_personal.payments_owed += s_personal.calculate_salery();
		
	}//end incrament_sallery_owed
	
	public boolean pay_sallery(int n)
	{
		if(n>credits || n<0) {return false;}
		s_personal.payments_owed -=n;
		credits -= n;
		//return overpay
		if(s_personal.payments_owed<0) 
		{
			credits += s_personal.payments_owed*-1;
		}
		//remove long term event "worker_union_fine"
		if(s_personal.payments_owed<=0 && n >200) 
		{
			remove_counter("worker_union_fine");
		}
		return true;
	}//end pay_sallery
	
	public boolean hire_personal(String type,int n){return false;}//end hire_personal
	
	public boolean fire_personal(String type,int n){return false;}//end fire_personal
	
	
	///////end personal////////
	


	

}
