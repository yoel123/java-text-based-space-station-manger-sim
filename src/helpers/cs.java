package helpers;

import java.awt.Color;

import helpers.C.GUIConsoleIO;

public class cs {
	
	public static void white(GUIConsoleIO cio) 
	{
		cio.setBackgroundColor(Color.WHITE);
		cio.setCaretColor(Color.BLACK);
		cio.setPromptColor(Color.BLACK);
	}//end white
	
	public static void white(GUIConsoleIO cio,String s) 
	{
		white(cio);
		cio.println(s,Color.BLACK);
	}//white 2
}
