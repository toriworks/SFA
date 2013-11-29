package fnc.salesforce.android.LIB;

import android.util.Log;

public class DebugLog {
	public static boolean B_DEBUG = true;
	//CHOYS
	public static final String CLASS_TAG			= "[Kolon]";
	
	public static void err(String tag, String msg)
	{
		String tag2;
		
		if (B_DEBUG == false)
			return;
		
		if (tag == null)
			tag2 = "";
		else 
			tag2 = tag;
		//CHOYS
		Log.e(CLASS_TAG, "[" + tag2 + "] " + msg);
	}
	
	public static void out(String tag, String msg)
	{
		String tag2;
		
		if (B_DEBUG == false)
			return;
		
		if (tag == null)
			tag2 = "";
		else 
			tag2 = tag;
		//CHOYS
		Log.d(CLASS_TAG, "[" + tag2 + "] " + msg);
	}
	
	public static void out(String tag, int msg)
	{
		String tag2;
		
		if (B_DEBUG == false)
			return;
		
		if (tag == null)
			tag2 = "NULL";
		else
			tag2 = tag;
		//CHOYS
		Log.d(CLASS_TAG, "[" + tag2 + "] " + msg);
	}
}
