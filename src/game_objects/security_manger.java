package game_objects;

import java.io.Serializable;
import java.util.ArrayList;

import helpers.S;

public class security_manger implements Serializable{
	
	public ArrayList<ship> ships;
	
	public ArrayList<weapon> weapons;

	public player p;
	
	public security_manger() {
	
		ships = new ArrayList<ship>();
		weapons = new ArrayList<weapon>();
		
		p = game_manger.p;
		
	//	ships.add(new ship(ships_db.db.get()));
		
	}//end constructor
	
	
	///////weapons//////
	
	public String weapons_list() 
	{
		String ret = "";
		
		int i = 0;
		for(weapon w :weapons) 
		{
			ret +=i+") "+w.name+" amount: "+w.amount+" price: "+w.cost;
			i++;
		}
		
		return ret;
	}//end weapons_list
	

	
	/**get weapon by name*/
	public weapon get_weapon(String name) 
	{
		for(weapon w :weapons) 
		{
			if(w.name ==name) {return w;}
		}
		return null;
	} //end get_weapon
	
	public boolean sell_weapon(weapon w,int amount,int sell_price) 
	{
		p = game_manger.p;
		//get weapon by name
		weapon w2 = get_weapon(w.name);
		//check if exists and has amount to sell
		if(w2 ==null || w2.amount<amount) {return false;}
		
		//calculate profit
		int profit = amount *sell_price;
		p.credits += profit;
		
		//handle taxes
		p.add_taxes(profit);

		w2.amount -= amount;//reduce amount from player item
		w.amount += amount;//add amount to market item
		//reduce cargo amount
		p.cargo-=amount;
		
		return true;
	}//end sell_weapon
	
	public boolean buy_weapon(weapon w,int amount) 
	{
		p = game_manger.p;
		int cost = w.cost*amount;

		//if can buy
		if(!p.can_buy(cost,p.credits,amount,w.amount)) {return false;}
		
		//see if station has the weapon
		weapon w2 = get_weapon(w.name);
		
		//if has weapon
		if(w2!=null) 
		{
			w2.amount += amount;//just add amount
		}else 
		{
			//add new weapon to arraylist
			weapon nw = new weapon(w);
			nw.amount = amount;
			weapons.add(nw);
			
		}
		//reduce market amount
		w.amount -= amount;
		//reduce player amount
		p.credits -= cost;
		//add to cargo amount
		p.cargo+=amount;
		
		return true;
	}//end buy_weapon
	
	public String display_all_wapons()
	{
		String txt="";
		int i =1;
		for(weapon it :weapons) 
		{
			txt += i+") "+it.toStringp()+"\n";
			i++;
		}
		return txt;
	}
	

	
	public String display_all_wapons(ArrayList<weapon> weapons2)
	{
		String txt="";
		int i =1;
		for(weapon it :weapons2) 
		{
			txt += i+") "+it.toString()+"\n";
			i++;
		}
		return txt;
	}
	
	///////end weapons//////////
	
	///////ships////////
	
	public String ships_list() 
	{
		String ret = "";
		int i = 1;
		for(ship s :ships) 
		{
			ret +=i+") "+s.name+"("+s.ship_name+") class: "+s.ship_class;
			ret+="\n";
			i++;
		}
		
		return ret;
	}//end ships_list
	
	public String ships_list(ArrayList<ship> ships2) 
	{
		String ret = "";
		int i = 1;
		for(ship s :ships2) 
		{
			ret +=i+") "+s.name+"("+s.ship_name+") class: "+s.ship_class;
			ret+="\n";
			i++;
		}
		
		return ret;
	}//end ships_list
	
	
	
	public boolean sell_ship(ship s) 
	{
		p = game_manger.p;
		ships.remove(s);
		p.credits += s.cost;
		p.add_taxes(s.cost);
		return true;
	}//end sell_ship
	
	public boolean buy_ship(ship s) 
	{
		p = game_manger.p;
		if(!p.can_buy(s.cost)) {return false;}
		ships.add(s);
		p.credits -= s.cost;
		return true;
	}//end buy_ship
	

}
