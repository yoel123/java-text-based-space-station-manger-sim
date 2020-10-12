package game_objects;

import java.util.Collections;
import java.util.Iterator;
import static javax.swing.JOptionPane.showMessageDialog;

public class damage_system {
	
	public static String[] station_systems= {"coms","dock","generator"
			,"life_support","stracture"};
	
	public static void add_damage(String name,int amount) 
	{
		player p = game_manger.p;
		
		//if damage exists exsit
		if(p.rapairs_prograss.containsKey(name))
		{
			//new value
			int nval = p.rapairs_prograss.get(name).intValue()+amount;
			p.rapairs_prograss.replace(name, nval);
			return;
		}
		
		//add to damage list and rapairs_prograss
		p.damage_list.add(name);
		p.rapairs_prograss.put(name, amount);
		
		//if its an upgrade move to disabled
		if(p.upgrades.indexOf(name)!=-1) 
		{
			//add to disabled
			p.disabled_upgrades.add(name);
			//remove from upgrades
			p.upgrades.remove(name);
		}
	}//end add_damage

	public static void remove_damage(String name) 
	{
		player p = game_manger.p;
		
		//remove from damage list and rapairs_prograss
		int i = p.damage_list.indexOf(name);
		p.damage_list.set(i, "yremove");
		p.rapairs_prograss.remove(name);
	
		
	}//remove_damage

	public static void repair_damage(String name,int amount) 
	{
		player p = game_manger.p;
		//check if exists if not exit
		if(p.rapairs_prograss.getKeyIndex(name)==-1) {return;}
		
		//get damage value
		int nval = p.rapairs_prograss.get(name).intValue()-amount;
		
		//reduce supplies(can only fix if has supplies)
		item supplies = p.get_item("supplies");//get supply item from player items
		if(supplies == null) {return;}//no supplies exit
		if(supplies.amount<amount) {return;}//not enough supplies exit
		
		supplies.remove_amount(amount);
		p.cargo-=amount;//remove from cargo also
		
		//repair done
		if(nval<=0) 
		{
			remove_damage(name);
			p.remove_counter(name);
			//if it was an upgrade make it operational again
			if(p.disabled_upgrades.indexOf(name)!=-1) 
			{
				//return it to upgrades
				p.upgrades.add(name);
				//remove from disabled upgrades
				p.disabled_upgrades.remove(name);
			}
			return;
		}
		//repair part of the damege
		p.rapairs_prograss.replace(name, nval);
	}//repair_damage
	
	
	public static void random_damage() 
	{
		player p = game_manger.p;
		
		int amount = p.rndgen.nextInt(4)+1;
		
		//chose if to damage ststion system or upgrades
		//check if there are upgrades
		if(p.upgrades.size()>0) 
		{
			int do_upgrade_damge = p.rndgen.nextInt(10);
			//do upgrade damage (if bigger then 5 do station system
			if(do_upgrade_damge<5) 
			{
				int rand_upgrade = p.rndgen.nextInt(p.upgrades.size());
				
				String upgrade_to_damege = p.upgrades.get(rand_upgrade);
				//add to disabled
				p.disabled_upgrades.add(upgrade_to_damege);
				//remove from upgrades
				p.upgrades.remove(upgrade_to_damege);
				add_damage(upgrade_to_damege,amount);
				return;//exit
			}
		}//end if(p.upgrades.size()>0)
		
		
		//get random arry pos from systems array
		int randomIndex = p.rndgen.nextInt(station_systems.length);
		String damege = station_systems[randomIndex];
		//add to damages
		add_damage(damege,amount);
		
	}//end random_damage

	public static void update_damege_effects() 
	{
		player p = game_manger.p;
		
		//engineers repair damage///
		
		//number of engineers
		int engineers = p.s_personal.plist.getKeyIndex("engineers");
		//how much they can repair per turn
		int repair_amount = engineers/2;
		
		//Maintain station damage
		if(p.damage_list.size()==0) {return;}//no damege exit
		
		//loop damage list
		
		for(String d :p.damage_list) 
		{
			
			//no repair points? stop repairs
			if(repair_amount<=0) {break;}
			repair_damage(d, 1);//rapair current damage by 1
			repair_amount--;//deincrament the times engineers can repair
		}
		
		//remove unneeded elements
		 p.damage_list.removeAll(Collections.singleton("yremove"));
		//showMessageDialog(null, p.damage_list);
		// showMessageDialog(null, p.upgrades+"upgrades");
		//showMessageDialog(null, p.disabled_upgrades+"disabled_upgrades");

		
		//if life support damage add: life support game_over timer once
		if(p.damage_list.indexOf("life_support")!=-1) 
		{p.add_counter_once("life_support", 10);}
		
	}//end update_damege_effects

	

}
