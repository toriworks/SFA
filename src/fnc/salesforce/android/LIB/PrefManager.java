package fnc.salesforce.android.LIB;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefManager {
	private static PrefManager prefInstance = null;

	private SharedPreferences pref;
	
	private PrefManager(Context context, String prefName){
		pref = context.getSharedPreferences(prefName, 0);
	}
	
	public static synchronized PrefManager getInstance(Context context, String prefName){
		if(prefInstance == null){
			prefInstance = new PrefManager(context, prefName);
		}
		return prefInstance;
	}
	
	// load Method
	public String loadPref(String fieldName, String sDefault){
		String result = pref.getString(fieldName, sDefault);
		return result;
	}

	public int loadPref(String fieldName, int iDefault){
		int result = pref.getInt(fieldName, iDefault);
		return result;
	}

	public long loadPref(String fieldName, long lDefault){
		long result = pref.getLong(fieldName, lDefault);
		return result;
	}
	public float loadPref(String fieldName, float fDefault){
		float result = pref.getFloat(fieldName, fDefault);
		return result;
	}
	public boolean loadPref(String fieldName, boolean bDefault){
		boolean result = pref.getBoolean(fieldName, bDefault);
		return result;
	}
	
	// Save Method 
	public boolean savePref(String fieldName, String sData){
		SharedPreferences.Editor e = pref.edit();
		e.putString(fieldName, sData);
		e.commit();		
		return true;
	}

	public boolean savePref(String fieldName, int iData){
		SharedPreferences.Editor e = pref.edit();
		e.putInt(fieldName, iData);
		e.commit();		
		return true;
	}

	public boolean savePref(String fieldName, float fData){
		SharedPreferences.Editor e = pref.edit();
		e.putFloat(fieldName, fData);
		e.commit();		
		return true;
	}

	public boolean savePref(String fieldName, boolean bData){
		SharedPreferences.Editor e = pref.edit();
		e.putBoolean(fieldName, bData);
		e.commit();		
		return true;
	}

	public boolean savePref(String fieldName, long lData){
		SharedPreferences.Editor e = pref.edit();
		e.putLong(fieldName, lData);
		e.commit();		
		return true;
	}
	
}
