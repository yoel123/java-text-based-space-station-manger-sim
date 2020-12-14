package helpers;//change to your packge name

import java.awt.Color;

import helpers.C.GUIConsoleIO;

public class gyinput {

	private GUIConsoleIO cio;
	public Color pc = Color.WHITE;//print color
	public gyinput(GUIConsoleIO cio2) {
		cio = cio2;
		
	}//end constructor
	
	public Double get_num(String request) 
	{
		Double num = 0.0;
		cio.println(request,pc);
		String next = cio.nextLine();
		if(!is_num(next)) 
		{
			cio.println("please enter a number!",pc);
			num = get_num( request);//recurtion until they give a num
			return num;
		}
		else 
		{
			return Double.parseDouble(next);
		}
		
		
	}//end get_num
	
	public int get_int(String request) 
	{
		int num = 0;
		cio.println(request,pc);
		String next = cio.nextLine();
		try  
		{  
			return Integer.parseInt(next);  
		}  
	    catch(NumberFormatException nfe)  
		{  
	    	cio.println("please enter a number! not a decimel or text"
	    			,pc);
			
			num = get_int( request);//recurtion until they give a num
				return num; 
		}
				
	}//end get_int
	
	public String get_string(String request) 
	{
		cio.println(request,pc);
		return cio.nextLine();
	}//end get_string
	
	public static boolean is_num(String str)
	{
		  try  
		  {  
		    double d = Double.parseDouble(str);  
		  }  
		  catch(NumberFormatException nfe)  
		  {  
		    return false;  
		  }  
		  return true; 
	}//end is_num

	public static boolean is_int(String str)
	{
		try
		{
			int i = Integer.parseInt(str);
		}
		catch(NumberFormatException nfe)
		{
			return false;
		}
		return true;
	}//end is_int
	
}
