package main;

import helpers.C;
import helpers.C.GUIConsoleIO;
import helpers.gmenu_manger;
import screens.game_main;
import screens.mangment_screen;
import screens.market_screen;
import screens.security_screen;
import screens.start_menu;
import screens.station_info_screen;

public class ymain {

	public static void main(String[] args) {
		
		C.io.setTitle("space station tycon sim"); // Sets window title
		GUIConsoleIO cio = C.io;//input output of the console
		
		
		//cs.white(cio);
		
		//init menu menger
		gmenu_manger gm = new gmenu_manger();
		
		//init menus
		start_menu tst = new start_menu(cio);

		game_main mainm = new game_main(cio);
		
		market_screen ms = new market_screen(cio);
		
		mangment_screen mng = new mangment_screen(cio);
		
		station_info_screen inf = new station_info_screen(cio);
		
		security_screen ysm = new security_screen(cio);
		
		//add menus to gmenu_manger
		gm.add_menu("start", tst);
		
		gm.add_menu("game_main", mainm);
		
		gm.add_menu("market_screen", ms);
		
		gm.add_menu("station_mangment_screen", mng);
		
		gm.add_menu("station_info_screen", inf);
		
		gm.add_menu("station_security_screen", ysm);
		
		//chane to start menu and run game
		gm.change_menu("start");
		gm.run();

	}//end main

}
