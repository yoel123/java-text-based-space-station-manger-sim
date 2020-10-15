package game_objects;

import java.io.Serializable;
import java.util.Map.Entry;

import helpers.IndexableMap;

public class station_personal implements Serializable{

	//personal list, holds personal by types like.gurds engineers.
	//the character class can hold any data we need on stats attribute
	public IndexableMap<String,character> plist;
	
	public int payments_owed=0,union_fine=0;
	
	public station_personal() {
		plist = new IndexableMap<String,character>();
		
		create_personal("guards", 2,300);
		create_personal("general_workers", 10,100);
		create_personal("engineers", 1,500);
		
	}//end constructor
	
	public void create_personal(String name,int amount,int salary) 
	{
		character c = new character();
		
		c.stats.put("amount", amount);
		c.stats.put("salery", salary);
		
		plist.put(name,c);
		
	}
	
	public void add_personal(String name,int amount) 
	{
		character c = plist.get(name);
		int namount  = c.stats.get("amount")+amount;
		c.stats.replace("amount",namount);
		
	    plist.replace(name,c);
	
	}//end add_personal
	
	public void remove_personal(String name,int amount) 
	{ 
		character c = plist.get(name);
		int namount  = c.stats.get("amount")-amount;
		if(namount<0) {namount=0;}//make sure its not smaller then 0
		c.stats.replace("amount",namount);
		
	    plist.replace(name,c);
	}//end remove_personal
	
	public character get_personal(String type)
	{
		//if not exist return null
		if(plist.getKeyIndex(type)==-1) {return null;}
		
		return plist.get(type);
	}//end get_personal
	
	
	public String personal_list() 
	{
		String ret = "";//string to return
		
		int salery = 0;
		String pname;//personal name
		character c;
		
		int i = 1;
		//loop all personal
		for (Entry<String, character> m : plist.entrySet()) 
		{
			//get current charecter
			c = m.getValue();
			//add charecter salery to allsalery
			salery = c.stats.get("salery");
			
			pname = m.getKey();//the maps key is personals name
			
			//add to ret
			ret += i+") "+pname+"| salery:"+salery
					+"| current hired:"+c.stats.get("amount")+"\n";
			
			i++;
		}
		
		return ret;
	}//end personal_list
	
	
	
	//per turn salary for all workers
	public int calculate_salery() 
	{
		//everyones salery
		int allsalery = 0;
		character c;
		
		//loop all personal
		for (Entry<String, character> i : plist.entrySet()) 
		{
			//get current charecter
			c = i.getValue();
			//add charecter salery to allsalery
			allsalery += c.stats.get("salery");
		}
		
		return allsalery;
	}//end calculate_salery
	
	

}//end station_personal
