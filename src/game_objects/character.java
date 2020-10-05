package game_objects;

import java.io.Serializable;

import helpers.IndexableMap;

public class character implements Serializable{

	String name,title;
	
	public IndexableMap<String,Integer> stats;
	
	public IndexableMap<String,String> traits;

	public character() {
		
		stats = new IndexableMap<String,Integer>();
		
	}//end constructor
	
	
}
