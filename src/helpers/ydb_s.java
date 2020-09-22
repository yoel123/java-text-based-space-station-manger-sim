package helpers;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

//import blackjack.S;

public class ydb_s 
{
	
	///save and load
	public static boolean write_to_file(String file ,Object obj) 
	{
		try 
		{
			ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(file));
			os.writeObject(obj);
			os.close();
		}	
	    catch(Exception e) 
		{
	         System.out.println("Exception thrown  :" + e);
	         return false;
	    }
		return true;
	}//end write_to_file
	
	
	public static Object read_file(String file ) 
	{
		try 
		{
			ObjectInputStream  oi = new ObjectInputStream( new FileInputStream(file) );
			Object tst = oi.readObject();
			//S.o(tst.db.get(2));
			Object arr = tst;
			oi.close();
			return arr;
			
		}catch(Exception e) 
		{
		         System.out.println("Exception thrown  :" + e);
		}
		return new ArrayList() ;
	}//end read_file
	
}//end ydb
