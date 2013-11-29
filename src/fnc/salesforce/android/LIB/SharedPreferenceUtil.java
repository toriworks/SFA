package fnc.salesforce.android.LIB;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

public class SharedPreferenceUtil {
// 값 저장...
	public static int getIntSharedPreference(Context context, String key) {

		int value = 0;

		try {

			SharedPreferences prefs = PreferenceManager
					.getDefaultSharedPreferences(context);
			value = prefs.getInt(key, 0);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return value;
	}

	public static void putSharedPreference(Context context, String key,
			String value) {

		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);

		Editor editor = prefs.edit();

		editor.putString(key, value);
		editor.commit();
	}

	public static void clearShared(Context context) {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		Editor editor = prefs.edit();
		editor.clear();
		editor.commit();

	}

	public static void putSharedPreference(Context context, String key,
			boolean value) {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		Editor editor = prefs.edit();

		editor.putBoolean(key, value);
		editor.commit();
	}

	public static String getSharedPreference(Context context, String key) {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		String value = "";
		try {
			value = prefs.getString(key, null);
		} catch (Exception e) {
			value = "";
			e.printStackTrace();
		}

		// return prefs.getString(key, null);
		return value;
	}

	public static boolean getBooleanSharedPreference(Context context, String key) {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);

		boolean value = true;
		try {
			value = prefs.getBoolean(key, false);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return value;
	}

	public static void putSharedPreference(Context context, String key,
			int value) {

		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		Editor editor = prefs.edit();

		editor.putInt(key, value);
		editor.commit();
	}

}
