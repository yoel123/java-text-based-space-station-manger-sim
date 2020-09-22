package game_objects;

import helpers.yvars;

public class item {

	public String name;
	public int amount,you_paid,cost,min_price,max_price;
	
	boolean player_owned;
	
	public item(item it) {
		name = it.name;
		amount = it.amount;
		you_paid = it.you_paid;
		cost = it.cost;
		min_price = it.min_price;
		max_price = it.max_price;
	
	}//end constructor
	
	
	
	public item(String data)
	{
		String[] datar = data.split(",");
		name = datar[0];
		amount = yvars.ystoint(datar[1]);
		you_paid = yvars.ystoint(datar[2]);
		cost = yvars.ystoint(datar[3]);
		min_price = yvars.ystoint(datar[4]);
		max_price = yvars.ystoint(datar[5]);
	
		
	}//end constructor
	
	
	public String toString()
	{
		return name+" price:"+cost+" amount: "+
			amount+" price range:"+min_price+" - "+max_price;
	}//endtoString
	
	public void add_amount(int val) {amount+=val;}
	public void remove_amount(int val) {amount-=val;}
	
	public String toStringp()
	{
		return name+" sell price:"+cost+" amount: "+
			amount+" you paid:"+you_paid;
	}//endtoString

}
