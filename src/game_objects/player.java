package game_objects;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;
import static javax.swing.JOptionPane.showMessageDialog;
import helpers.IndexableMap;

public class player implements Serializable{
	
	public int credits;
	public ArrayList<item> items;
	public ArrayList<String> events,upgrades,disabled_upgrades
	,counters_list,damage_list;
	public boolean insurance;
	public int insurance_rate = 500,
			taxes_owed,didnt_pay_taxes_c,didnt_pay_taxes_max=10
			,station_secutity=0,skipd_maintenance = 0,workers_needed=10;
	double tax_rate = 0.05;
	public boolean taxes_problem,did_maintenance,not_enough_workers;
	
	int cargo=0,cargo_limit = 500,cargo_for_rent=0,rented_cargo=0;
	
	public int good_advertise=0;
	
	public  int turn;
	
	//long term events counters
	public IndexableMap<String,Integer> counters,rapairs_prograss ;
	
	//station personal object
	public station_personal s_personal;
	
	//station security
	public security_manger sm;
	
	//randog genrator 
	public Random rndgen;
	
	public player() {
		
		
		rndgen = new Random();
		items = new ArrayList<item>();
		events = new ArrayList<String>();
		disabled_upgrades = new ArrayList<String>();
		upgrades = new ArrayList<String>();
		upgrades.add("tst");
		counters_list = new ArrayList<String>();
		counters = new IndexableMap<String,Integer>();
		rapairs_prograss = new IndexableMap<String,Integer>();
		damage_list = new ArrayList<String>();
		turn=0;
		add_counter("test",3);
		
		s_personal = new station_personal();
		
		sm = new security_manger();
		
	}//end constructor
	
	public void start_turn_reset() 
	{	 //reset insurance
		 insurance=false;
		 //reset advertisements 
		 good_advertise = 0;
		 //reset maintenence
		 did_maintenance = false;
	}//end start_turn_reset
	
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
		if(i==-1) {return;}
		counters_list.set(i, "yremove");
	}//end remove_counter
	
	
	
	public void reset_counter(String name) 
	{
		 counters.replace(name+"_counter",0);
	}//end reset_counter
	
	public void incrament_counter(String name) 
	{
		//counter dosnt exist return
		if(counters.getKeyIndex(name+"_counter")==-1) {return;}
		
		int count = counters.get(name+"_counter").intValue()+1;
		counters.replace(name+"_counter",count);
	}//end reset_counter
	
	
	
	public boolean is_counter_done(String name) 
	{
		
		//counter dosnt exist return
		if(counters.getKeyIndex(name+"_counter")==-1) {return false;}
		
		
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
		//if(cost > credits || amount > it.amount) {return false;}
		
		if(!can_buy(cost ,credits , amount , it.amount)) {return false;}
		
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
	}//end sell
	
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
	
	public boolean can_buy(int cost) 
	{
		if(cost > credits ) {return false;}
		return true;
	}//end can_buy
	
	public boolean can_buy(int cost,int credits,int amount,int max_amount) 
	{
		if(cost > credits || amount > max_amount) {return false;}
		return true;
	}//end can_buy
	
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
		if(taxes_owed>0) 
		{
			//counter is add just so player can see it in station info
			//it dosnt do anything.
			add_counter_once("tax_fine",didnt_pay_taxes_max-1);//add acounter
			didnt_pay_taxes_c++;
		}
		
		
		//if player didn't pay taxes  give him a warning 1 turn before his fined
		if(didnt_pay_taxes_c==didnt_pay_taxes_max-1) 
		{
			events.add("**notice! this is your final notice pay your taxes"
					+ " or your assets would be liquidated");
			
		}
		
		//implament later
		//if player didnt heed warning give him problems
		if(didnt_pay_taxes_c>13) 
		{
			taxes_problem = true;
		}
		
		//fine the player
		if(didnt_pay_taxes_c>didnt_pay_taxes_max) 
		{
			taxes_owed +=2000;
			events.add("**notice!,0,your late on your tax payments and got fined "
					+ " please pay your taxes on time");
			didnt_pay_taxes_c=0;//reset counter
			remove_counter("tax_fine");//remove counter from counter list
		}
	}//end  update_taxes
	
	public boolean pay_taxes(int amount) 
	{
		if(amount > credits) {return false;}
		
		credits -= amount;
		taxes_owed -= amount;
		if(taxes_owed>200 && amount<200) {return true;}//reset only if paid more then 200
		didnt_pay_taxes_c = 0;
		remove_counter("tax_fine");//remove counter from counter list
		
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
		if(s_personal.payments_owed<=0 || n >200) 
		{
			remove_counter("worker_union_fine");
		}
		return true;
	}//end pay_sallery
	

	public boolean hire_personal(int type,int n)
	{
		//IndexableMap when index ==size
		if(type==s_personal.plist.size()) {type = 0;}
		//get key by index
		String stype = s_personal.plist.getKeyAt(type);
		return hire_personal(stype,n);
	}
	public boolean hire_personal(String type,int n)
	{
	
		//get personal you want to buy
		character c = s_personal.plist.get(type);
		//if dosnt exist return falase
		if(c==null) {return false;}
		//cost is the number of personal you want to buy multiplied by their salary
		//per turn, thats the recruitment cost
		int cost = n*c.stats.get("salery").intValue();
		//player dosnt have the credits to pay exit
		if(cost>credits) {return false;}
		//remove credits
		credits -= cost;
		//add the personal to station personal
		s_personal.add_personal( type, n);
		
		return true;
	}//end hire_personal
	
	public boolean fire_personal(int type,int n)
	{
		//IndexableMap when index ==size
		if(type==s_personal.plist.size()) {type = 0;}
		//get key by index
		String stype = s_personal.plist.getKeyAt(type);
		return fire_personal(stype,n);
	}
	public  boolean fire_personal(String type,int n)
	{
		//get personal you want to fire
		character c = s_personal.plist.get(type);
		//if dosnt exist return falase
		if(c==null) {return false;}
		
		//remoove personal
		s_personal.remove_personal( type, n);
		
		return true;
	}//end fire_personal
	
	///////end personal////////
	
	
	///////maintenance//////
	public void do_maintenance() 
	{
		
		//check if thers enough workers
		
		character workers = s_personal.plist.get("general_workers");
		
		if(workers.stats.get("amount")<workers_needed)
		{not_enough_workers=true;}
		
		///check maintenence
		
		//get supplies
		item supply = get_item("supplies");
		
		//if no supplies exit
		if(supply ==null) {return;}
		
		//get engeners
		character engineers = s_personal.plist.get("engineers");
		
		//maintenence supply cost (minmum of 3 plus upgrades)
		int maintenance_cost = upgrades.size()/5+1;
		
		//Chief engineer reduces supply
		if(s_personal.plist.containsKey("chief engineer")) {maintenance_cost--;}
		
		//no engineers, no maintenance
		if(engineers.stats.get("amount").intValue()<=0) {return;}
		
		//reduce maintenance by engineers num (by 2 if you have more then 6
		if(engineers.stats.get("amount").intValue()>6) {maintenance_cost -=2;}
		
		//check if you have the supplies to mentain the station
		if(maintenance_cost>supply.amount) {return;}
		
		//min maintenance_cost is 1
		if(maintenance_cost<0) {maintenance_cost=1;}
		
		//reduce supplies
		supply.amount -= maintenance_cost;
		cargo -= maintenance_cost;
		//set did_maintenance to true
		did_maintenance = true;
		
		//stop no maintenance malfunction counter
		remove_counter("no_maintenance_malfunction");
		
	}//end do_maintenance
	///////maintenance//////
	


	

}
