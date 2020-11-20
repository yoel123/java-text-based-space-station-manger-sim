package game_objects;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;
import java.util.Map.Entry;

import db.character_db;
import db.ship_db;
import helpers.IndexableMap;
import helpers.S;

public class character implements Serializable{

	public String name,title;
	
	public IndexableMap<String,Integer> stats;
	
	public IndexableMap<String,String> traits;

	public character() {
		
		stats = new IndexableMap<String,Integer>();
		traits = new IndexableMap<String,String>();
		
	}//end constructor
	
	public void random_name() 
	{
		player p = game_manger.p;
		Random r = game_manger.p.rndgen;
		//random name index
		int i = r.nextInt(character_db.ynames.size());
		name = character_db.ynames.get(i);
	}//end random_name
	
	//genrate basic charecter
	public void gen_base_char()
	{

		player p = game_manger.p;
		Random r = game_manger.p.rndgen;
		//random name
		random_name();
		//st salary and rank
		int rank = r.nextInt(10)+1;
		int salary = (rank*500)+1500;

		stats.put("salery", salary);
		stats.put("rank", rank);
		stats.put("xp", (rank-1)*1000);
		
		stats.put("next_level_xp", rank*1000);
		
	}//end gen_base_char
	
	public void gen_base_char(String ytype)
	{
		player p = game_manger.p;
		Random r = game_manger.p.rndgen;
		
		gen_base_char();
		ArrayList<String> traits_db = new ArrayList<String>();
		if(ytype.equals("chief engineer")) 
		{traits_db = character_db.chif_engineer_traits;}
		if(ytype.equals("constable")) {traits_db = character_db.constable_traits;}
		if(ytype.equals("acountent")) {traits_db = character_db.acountent_traits;}
		
		int rank = stats.get("rank");
		title = ytype;
	
		
		//random traits gen
		for(int i= 0; i<= rank;i++) 
		{
			
			traits.put(get_rand_trait(traits_db), "1");
		}
		
		
		
	}//end gen_base_char
	
	public String get_rand_trait(ArrayList<String> db)
	{
		player p = game_manger.p;
		Random r = game_manger.p.rndgen;
		
		//just incase we get nothing
		if(db.size() ==0||db.size()==-1) {return"";}
		
		//get random trait index
		int trait_i = r.nextInt(db.size());
		
		return db.get(trait_i);
		
	}//end get_rand_trait
	
	
	public boolean has_trait(String name) 
	{
		
		return traits.containsKey(name);
	}
	
	
	public String show_stats() 
	{
		String ret="";
		
		ret+="name: "+name+"\n";
		ret+="title: "+title+"\n";
		ret+="salery: "+stats.get("salery")+"\n";
		ret+="rank: "+stats.get("rank")+"\n";
		ret+="xp: "+stats.get("xp")+"\n";
		ret+="next_level_xp: "+stats.get("next_level_xp")+"\n";
		//traits
		ret+="traits:";
		int i=0;
		for (Entry<String, String> me : traits.entrySet()) 
		{
			ret+=" ,"+me.getKey();
			if(i==3) {i=0;ret+="\n";}//3 traits per row
			i++;
		}
		ret+="\n";
		
		
		
		return ret;
	}//show_stats
	

	
	
}
