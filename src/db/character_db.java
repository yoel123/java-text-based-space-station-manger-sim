package db;

import java.util.ArrayList;

import helpers.IndexableMap;

public class character_db {
	public static  IndexableMap<String,String> db;
	public static ArrayList<String> ynames, traits,chif_engineer_traits,constable_traits
	,acountent_traits;
	
	public static String ychar ="//";
	
	public static void gen_traits() 
	{
		gen_names();
		traits = new ArrayList<String>();
		chif_engineer_traits = new ArrayList<String>();
		constable_traits = new ArrayList<String>();
		acountent_traits = new ArrayList<String>();
		
		chif_engineer_traits.add("combat engineer");
		chif_engineer_traits.add("mr fix it");
		chif_engineer_traits.add("who needs supply");
		
		constable_traits.add("plasma pistol");
		constable_traits.add("spec ops");
		constable_traits.add("detective");
		
		acountent_traits.add("financial planner");
		acountent_traits.add("bean counter");
		
		
		
	}//end gen_traits
	
	public static void gen_names() 
	{
		ynames =  new ArrayList<String>();
		
		ynames.add("bill");
		ynames.add("bob");
		
	}//end gen names
}
